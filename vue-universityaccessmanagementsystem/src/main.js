// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import '@/utils/global'//全局
import VueParticles from 'vue-particles'
import axios from 'axios'
// 控制路由表的js文件
import '@/permission.js'
Vue.config.productionTip = false
Vue.use(ElementUI);
Vue.use(VueParticles)
Vue.prototype.$http = axios
axios.defaults.baseURL = '/api'
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})

function formatDate(date, fmt) {
  date = new Date(date);
  if (typeof(fmt) === "undefined") {
      fmt = "yyyy-MM-dd HH:mm:ss";
  }
  if (/(y+)/.test(fmt)) {
      fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
  }
  let o = {
      'M+': date.getMonth() + 1,
      'd+': date.getDate(),
      'H+': date.getHours(),
      'm+': date.getMinutes(),
      's+': date.getSeconds()
  }
  for (let k in o) {
      if (new RegExp(`(${k})`).test(fmt)) {
          let str = o[k] + ''
          fmt = fmt.replace(RegExp.$1, RegExp.$1.length === 1 ? str : ('00' + str).substr(str.length));
      }
  }
  return fmt
};
  
//定义一个时间过滤器
Vue.filter("FormatDate", function(date, fmt) {
  return formatDate(date, fmt);
});