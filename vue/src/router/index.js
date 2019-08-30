import Vue from 'vue'
import Router from 'vue-router'

const login = r => require.ensure([], () => r(require('views/login/login')), 'login')
const overview = r => require.ensure([], () => r(require('views/overview/overview')), 'overview')
const index = r => require.ensure([], () => r(require('views/index/index')), 'index')
const dashboard = r => require.ensure([], () => r(require('views/dashboard/dashboard')), 'dashboard')
const devList = r => require.ensure([], () => r(require('views/devList/devList')), 'devList')
const log = r => require.ensure([], () => r(require('views/log/log')), 'log')
const userIndex = r => require.ensure([], () => r(require('views/user/index')), 'index')
const user = r => require.ensure([], () => r(require('views/user/children/user')), 'user')
const editPwd = r => require.ensure([], () => r(require('views/user/children/editPwd')), 'editPwd')
Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/login',
      name: 'login',
      component: login
    },
    {
      path: '/overview',
      name: 'overview',
      component: overview
    },
    {
      path: '/',
      component: index,
      redirect: '/dashboard',
      children: [
        {
          path: 'dashboard',
          component:dashboard,
          name: 'Dashboard',
          meta: { title:'设备概况', aside:true }
        },
        {
          path: 'devList',
          component:devList,
          name: 'DevList',
          meta: { title:'设备列表', aside:true }
        },
        {
          path: 'log',
          component:log,
          name: 'Log',
          meta: { title:'日志信息', aside:true }
        },
        {
          path: '/user',
          name: 'userIndex',
          redirect: '/user/index',
          component: userIndex,
          meta: { title:'个人中心', aside:false },
          children: [
            {
              path: 'index',
              component:user,
              name: 'user',
              meta: { title: '首页' }
            },
            {
              path: 'editPwd',
              component:editPwd,
              name: 'editPwd',
              meta: { title: '修改密码' }
            }
          ]
        }
      ]
    }
  ]
})
