import Vue from 'vue'
import Router from 'vue-router'
const layout = () => import('@/components/layout')
// 登录页
const login = () => import('@/components/Login')
const changePassword = () => import('@/views/centor/changePassword')
const Approver = () => import('@/views/userManage/Approver')
const teacher = () => import('@/views/userManage/teacher')
const student = () => import('@/views/userManage/student')
const info = () => import('@/views/centor/info')
const role = () => import('@/views/dictionary/role')
const academy = () => import('@/views/dictionary/academy')
const riskPlace = () => import('@/views/riskPlace/riskPlace')
const main = () => import('@/views/index')
const arroundNum = () => import('@/views/approvalCenter/arroundNum')
const studentAproval = () => import('@/views/approvalCenter/studentAproval')
const teacherAproval = () => import('@/views/approvalCenter/teacherAproval')
const applyResult = () => import('@/views/applyCentor/applyResult')
const studentApply = () => import('@/views/applyCentor/studentApply')
const teacherApply = () => import('@/views/applyCentor/teacherApply')
Vue.use(Router)
// 固定的路由表
export const fixedRouter = [{
    path: '',
    component: login,
    hidden: true
  },
  {
    path: '',
    component: layout, //整体页面的布局(包含左侧菜单跟主内容区域)
    children: [{
      path: 'main',
      component: main,
      meta: {
        title: '首页', //菜单名称
        roles: ['审批人员', '管理员', '教师职工', '学生'], //当前菜单哪些角色可以看到
        icon: 'el-icon-info' //菜单左侧的icon图标
      }
    }]
  },
]
// 需要权限判断展示的路由
export const permissionRouter = [
  {
    path: "/centor",
    name: "centor",
    component: layout,
    meta: {
      title: "个人中心",
      icon: "el-icon-setting",
      roles: ['审批人员', '管理员', '教师职工', '学生']
    },
    children: [{
      path: "changePassword",
      name: "changePassword",
      component: changePassword,
      meta: {
        title: "修改密码",
        roles: ['审批人员', '管理员', '教师职工', '学生']
      }
    },
    {
      path: "changeInfo",
      name: "info",
      component: info,
      meta: {
        title: "个人信息",
        roles: ['审批人员', '管理员', '教师职工', '学生']
      }
    }
    ]
  },
  {
    path: "/userManage",
    name: "userManage",
    component: layout,
    meta: {
      title: "组织机构管理",
      icon: "el-icon-edit-outline",
      roles: ['审批人员', '管理员', '教师职工']
    },
    children: [{
      path: "Approver",
      name: "Approver",
      component: Approver,
      meta: {
        title: "审批人员管理",
        roles: ['管理员']
      }
    },
    {
      path: "teacher",
      name: "teacher",
      component: teacher,
      meta: {
        title: "教师员工管理",
        roles: ['审批人员', '管理员']
      }
    },
    {
      path: "student",
      name: "student",
      component: student,
      meta: {
        title: "学生管理",
        roles: ['审批人员', '管理员', '教师职工']
      }
    }
    ]
  },
  {
    path: "/dictionary",
    name: "dictionary",
    component: layout,
    meta: {
      title: "数据字典管理",
      icon: "el-icon-document",
      roles: ['管理员']
    },
    children: [{
      path: "academy",
      name: "academy",
      component: academy,
      meta: {
        title: "学院管理",
        roles: ['管理员']
      }
    },
    {
      path: "role",
      name: "role",
      component: role,
      meta: {
        title: "角色管理",
        roles: ['管理员']
      }
    }
    ]
  },
  {
    path: "/riskPlace",
    name: "riskPlace",
    component: layout,
    meta: {
      title: "风险地区管理",
      icon: "el-icon-location",
      roles: ['管理员']
    },
    children: [
      {
        path: "riskPlace",
        name: "riskPlace",
        component: riskPlace,
        meta: {
          title: "风险地区管理",
          icon: "el-icon-location",
          roles: ['管理员']
        }
      }
    ]
  },
  {
    path: "/approvalCenter",
    name: "approvalCenter",
    component: layout,
    meta: {
      title: "审批中心",
      icon: "el-icon-bell",
      roles: ['审批人员', '管理员', '教师职工']
    },
    children: [{
      path: "teacherAproval",
      name: "teacherAproval",
      component: teacherAproval,
      meta: {
        title: "教职工审批",
        roles: ['审批人员', '管理员']
      }
    },
    {
      path: "studentAproval",
      name: "studentAproval",
      component: studentAproval,
      meta: {
        title: "学生审批",
        roles: ['管理员', '教师职工']
      }
    },
    {
      path: "arroundNum",
      name: "arroundNum",
      component: arroundNum,
      meta: {
        title: "范围查询",
        roles: ['审批人员', '管理员']
      }
    },
    ]
  },
  {
    path: "/applyCentor",
    name: "applyCentor",
    component: layout,
    meta: {
      title: "申请中心",
      icon: "el-icon-edit",
      roles: ['管理员', '教师职工', '学生']
    },
    children: [
      {
        path: "studentApply",
        name: "studentApply",
        component: studentApply,
        meta: {
          title: "学生申请",
          roles: ['管理员', '学生']
        }
      },
      {
        path: "teacherApply",
        name: "teacherApply",
        component: teacherApply,
        meta: {
          title: "教师申请",
          roles: ['管理员', '教师职工']
        }
      },
      {
        path: "applyResult",
        name: "applyResult",
        component: applyResult,
        meta: {
          title: "申请反馈",
          roles: ['管理员', '教师职工', '学生']
        }
      }
    ]
  }
]


export default new Router({
  routes: fixedRouter

})
