<template>
    <div>
      <el-form :model="ruleForm2" status-icon :rules="rules2" ref="ruleForm2" label-width="100px" class="demo-ruleForm">
        <el-form-item label="原密码" prop="oldPass">
          <el-input type="password" v-model="ruleForm2.oldPass" autocomplete="off" placeholder="请输入原密码" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="pass">
          <el-input type="password" v-model="ruleForm2.pass" autocomplete="off" placeholder="请输入新密码" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="checkPass">
          <el-input type="password" v-model="ruleForm2.checkPass" autocomplete="off" placeholder="请确认密码" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('ruleForm2')">提交</el-button>
          <el-button @click="resetForm('ruleForm2')">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </template>
<script>
export default {
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入新密码'));
      } else {
        if (this.ruleForm2.checkPass !== '') {
          this.$refs.ruleForm2.validateField('checkPass');
        }
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入新密码'));
      } else if (value !== this.ruleForm2.pass) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      ruleForm2: {
        pass: '',
        checkPass: '',
        oldPass: '',
      },
      rules2: {
        pass: [
          { required: true,validator: validatePass, trigger: 'blur' }
        ],
        checkPass: [
          {required: true, validator: validatePass2, trigger: 'blur' }
        ],
        oldPass: [
        {required: true, message: '请输入原密码', trigger: 'blur'}
        ]
      }
    };
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const param = {
            oldPass:this.ruleForm2.oldPass,
            newPass:this.ruleForm2.pass,
          };
          this.$http
            .post('/user/resetPassword', param)
            .then((res) => {
              if(res.data.result == "success") {
                this.$message({
                  message: '修改密码成功',
                  type: 'success'
                });
              } else {
                this.$message.error(res.data.message);
              }
            })
        } else {
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
  }
}
</script>
  <!-- Add "scoped" attribute to limit CSS to this component only -->
  <style scoped>
  </style>
  