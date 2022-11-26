<template>
    <el-form ref="form" :model="form" label-width="80px">
      <el-form-item label="名字">
        <el-input v-model="form.name"></el-input>
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="form.email"></el-input>
      </el-form-item>
      <el-form-item label="电话">
        <el-input v-model="form.phone"></el-input>
      </el-form-item>
      <el-form-item label="备注">
        <el-input type="textarea" v-model="form.beizhu"></el-input>
      </el-form-item>
      <el-form-item>
          <el-button type="primary" @click="submitForm('form')">提交</el-button>
        </el-form-item>
    </el-form>
  </template>
<script>
export default {
  inject: ['reload'],
  data() {
    return {
      form: {
        name: '',
        email: '',
        phone: '',
        beizhu: ''
      }
    }
  },
  methods: {
    submitForm(formName) {
      const param = {
        name:this.form.name,
        email:this.form.email,
        phone: this.form.phone,
        beizhu: this.form.beizhu,
      };
      this.$http
        .post('/user/updateUser', param)
        .then((res) => {
          if(res.data.result == "success") {
            this.$message({
              message: '修改信息成功',
              type: 'success'
            });
            this.reload()
          } else {
            this.$message.error(res.data.message);
          }
        })
    },
    getList() {
      //从cookie中获取登录凭证
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
                  this.form = res.data.user
                }
              })
      }
    }
  },
  mounted() {
    this.getList()
  }
}
</script>
  <!-- Add "scoped" attribute to limit CSS to this component only -->
  <style scoped>
  </style>
  