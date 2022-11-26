<template>
  <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
      <el-form-item label="名字" prop="name">
        <el-input type="text" v-model="ruleForm.name" autocomplete="off" placeholder="请输入名字" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="用户名" prop="username">
        <el-input type="text" v-model="ruleForm.username" autocomplete="off" placeholder="请输入用户名" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="所属学院"  prop="academyId">
      <el-select v-model="ruleForm.academyId"  filterable clearable  placeholder="请选择">
        <el-option
          v-for="item in academys"
          :key="item.id"
          :label="item.academyName"
          :value="item.id">
        </el-option>
      </el-select>  
    </el-form-item>
    <el-form-item label="邮箱" prop="email">
        <el-input type="text" v-model="ruleForm.email" autocomplete="off" placeholder="请输入邮箱" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input type="text" v-model="ruleForm.phone" autocomplete="off" placeholder="请输入手机号" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="出访地">
        <el-cascader
        ref="cascaderAddr"
          size="large"
          :options="places"
          v-model="selectedOptions"
          @change="handleChange">
        </el-cascader>
    </el-form-item>
    <el-form-item label="出入类型" prop="type">
      <el-switch
        style="display: block"
        v-model="ruleForm.type"
        active-color="#13ce66"
        @change="value()"
        inactive-color="#ff4949"
        active-text="出校"
        inactive-text="入校">
      </el-switch>
    </el-form-item>
    <el-form-item label="审批人" prop="approvalUserId">
      <el-select v-model="ruleForm.approvalUserId"  filterable clearable  placeholder="请选择">
        <el-option
          v-for="item in approvalUserIds"
          :key="item.id"
          :label="item.name"
          :value="item.id">
        </el-option>
      </el-select>  
    </el-form-item>
    <el-form-item label="出入校时间" prop="date">
      <el-date-picker
        v-model="ruleForm.date"
        type="datetimerange"
        align="right"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        @change="changeDate()"
        :default-time="['8:00:00', '22:00:00']">
      </el-date-picker>
    </el-form-item>
    <el-form-item label="出入校原因" prop="reason">
      <el-autocomplete
        class="inline-input"
        v-model="ruleForm.reason"
        :fetch-suggestions="querySearch"
        placeholder="请输入原因"
        @select="handleSelect"
      ></el-autocomplete>
    </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
        <el-button @click="resetForm('ruleForm')">重置</el-button>
      </el-form-item>
      
    </el-form>
</template>
<script>
import { regionData } from 'element-china-area-data'
export default {
  inject: ['reload'],
  data() {
    return {
      academys: [],
      restaurants: [],
      approvalUserIds: [],
      places: regionData,
      selectedOptions: [],
      ruleForm: {
      name: '',
      approvalUserId: '',
      username: '',
      academyId: '',
      academyName: '',
      email: '',
      phone: '',
      type: false,
      date: '',
      reason: '',
      toPlace: '',
    },
    rules: {
      name: [
      {required: true, message: '请输入原密码', trigger: 'blur'}
      ],
      username: [
      {required: true, message: '请输入原密码', trigger: 'blur'}
      ],
      academyId: [
      {required: true, message: '请输入原密码', trigger: 'blur'}
      ],
      email: [
      {required: true, message: '请输入原密码', trigger: 'blur'}
      ],
      phone: [
      {required: true, message: '请输入原密码', trigger: 'blur'}
      ],
      date: [
      {required: true, message: '请输入原密码', trigger: 'blur'}
      ],
      reason: [
      {required: true, message: '请输入原密码', trigger: 'blur'}
      ],
      approvalUserId: [
      {required: true, message: '请输入原密码', trigger: 'blur'}
      ],
      place:[
      {required: true, message: '请输入原密码', trigger: 'blur'}
      ],
      type:[
      {required: true, message: '请输入原密码', trigger: 'blur'}
      ],
    }
    }

  },
  methods: {
    getList() {
      this.$http
      .post('/user/getAllTeacherList')
      .then((res) => {
        this.approvalUserIds = res.data.approverList
      })
      this.$http
      .get('/academy/getAllList')
      .then((res) => {
        this.academys = res.data.academyList
      })
      let ticket = localStorage.getItem("ticket")
      if(ticket !== '') {
        this.$http
            .post('/user/getRoleByTicket', ticket,{
              headers: {
                'Content-Type': 'text/plain'
              }
          })
            .then((res) => {
              if(res.data.result == "success") {
                this.ruleForm = res.data.user
                this.ruleForm.academyId = Number(res.data.user.academyId)
                this.ruleForm.type = true
              }
            })
      }
      this.ruleForm.type = false
    },
    submitForm(formName) {
      if(this.ruleForm.toPlace === undefined) {
        this.$message({
        message: '请填写出访地',
        type: 'warning'
      });
      return;
      }
      this.$refs[formName].validate((valid) => {
        if(valid) {
          const param = {
            academyId: this.ruleForm.academyId,
            email: this.ruleForm.email,
            phone: this.ruleForm.phone,
            toPlace: this.ruleForm.toPlace,
            type: this.ruleForm.type === true ? 0 : 1,
            approvalUserId: this.ruleForm.approvalUserId,
            startDate: this.ruleForm.date[0],
            endDate: this.ruleForm.date[1],
            reason: this.ruleForm.reason,
            status: 0
          };
          this.$http
          .post('/apply/addApplyInfo', param)
          .then((res) => {
              this.$message({
                message: res.data.message,
                type: 'success'
              });
            this.reload()
          })
        } 
      });
    },
    resetForm(formName) {
      this.reload()
    },
    handleChange (e) {
      var thsAreaCode = this.$refs['cascaderAddr'].currentLabels    // 获取label值
      this.ruleForm.toPlace = thsAreaCode[0] + ' ' + thsAreaCode[1] + ' ' + thsAreaCode[2] //  最终结果是个一维数组对象

    },
    querySearch(queryString, cb) {
      var restaurants = this.restaurants;
      var results = queryString ? restaurants.filter(this.createFilter(queryString)) : restaurants;
      // 调用 callback 返回建议列表的数据
      cb(results);
    },
    createFilter(queryString) {
      return (restaurant) => {
        return (restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
      };
    },
    loadAll() {
      return [
        { "value": "因实习" },
        { "value": "因求职" },
        { "value": "因探亲" },
        { "value": "因就医" },
        { "value": "考研辅导课" }
      ];
    },
    handleSelect(item) {
      
    },
    value() {

    },
    changeDate() {
      console.log(this.ruleForm.date[0])
    }
  },
  mounted() {
    this.getList()
    this.restaurants = this.loadAll();
  }
}

</script>
<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
