<template>
  <div class="app_wapper">
    <el-container>
      <!-- 左侧菜单 -->
      <el-aside class="slider_container">
        <el-menu class="sub_meuns_wapper" mode="vertical" unique-opened :default-active="$route.path" background-color="#304156" text-color="#fff" active-text-color="#409EFF">
          <!-- 菜单组件 -->
          <side-Meuns :routes="getRoutes"></side-Meuns>
        </el-menu>
      </el-aside>
      <!-- 右侧内容区域 -->
      <el-container class="app_content">
        <el-header style="text-align: right; font-size: 12px">
          <div class="el-icon-view" style="margin-right: 15px">{{ name }}</div>
          <el-button @click="outSystem" size="mini" plain type="primary">退出</el-button>
        </el-header>
        <!-- 内容区域 -->
        <el-main>
          <!-- 二级路由跳转 -->
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>
<script>
// 左侧菜单组件
import sideMeuns from '@/components/sideMeuns'
import { permissionRouter } from '@/router'
export default {
  computed: {
    getRoutes() {
      return global.antRouter
    }

  },
  components: {
    sideMeuns
  },
  data() {
    return {
      userRole: 'Topest',
      name: ''
    }
  },
  methods: {
    outSystem() { //退出系统
      localStorage.setItem("userRole", 'unload')
      //从cookie中获取登录凭证
      let ticket = ''
      var arr = document.cookie.split(";");
      for (var i = 0; i < arr.length; i++) {
        var arr2 = arr[i].split("=");
        if (arr2[0] == "ticket") {
          ticket = arr2[1];
        }
      }
      if(ticket === '') return;
      this.$http
        .post('/user/logout', ticket,{
          headers: {
            'Content-Type': 'text/plain'
          }
      })
        .then((res) => {
          this.$message({
            message: '退出登录成功',
            type: 'success'
          });
          this.$router.push({
            path: '/'
          })
        })
      // 跳转到登录页的时候顺便刷新
      window.location.href = window.location.origin + window.location.pathname
    },
  },
  mounted() {
    // console.log("菜单的展开跟路由有关系，查看elementui--el-menu  的default-active 属性")
    // console.log(this.$route.path)
    let userRole = localStorage.getItem("userRole")
    let userName = localStorage.getItem("userName")
    if(userRole === '管理员') {
      this.name = ' 你好，管理员'
    } else if(userRole === '审批人员') {
      this.name = ' 你好，' + userName + ' 老师'
    } else if(userRole === '教师职工') {
      this.name = ' 你好，' + userName + ' 老师'
    } else if(userRole === '学生') {
      this.name = ' 你好，' + userName + ' 同学'
    } else {
      this.name = ' 系统出错，请联系管理人员(1309549123@qq.com)'
    }
  }
}

</script>
<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
/*wapper*/
.app_wapper {
  position: relative;
  height: 100%;
  width: 100%;
}

.app_content {
  min-height: 500px;
  margin-left: 180px;
  border: 1px solid #eee;
  box-sizing: border-box;
}

.el-header {
  background-color: #B3C0D1;
  color: #333;
  line-height: 60px;
}

.app_wapper .el-aside,
.slider_container .el-menu {
  color: #333;
  transition: width .28s;
  width: 180px !important;
  height: 100%;
  position: fixed;
  top: 0;
  bottom: 0;
  left: 0;
  z-index: 1001;
  background-color: rgb(48, 65, 86);
}

</style>
