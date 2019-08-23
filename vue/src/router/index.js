import Vue from 'vue'
import Router from 'vue-router'

const login = r => require.ensure([], () => r(require('views/login/login')), 'login')
const overview = r => require.ensure([], () => r(require('views/overview/overview')), 'overview')
const index = r => require.ensure([], () => r(require('views/index/index')), 'index')
const dashboard = r => require.ensure([], () => r(require('views/dashboard/dashboard')), 'dashboard')

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
    // {
    //   path: '/index',
    //   name: 'index',
    //   component: index
    // },
    {
      path: '/',
      component: index,
      redirect: '/dashboard',
      children: [
        {
          path: 'dashboard',
          component:dashboard,
          name: 'Dashboard',
          meta: { title: 'Dashboard', icon: 'dashboard', affix: true }
        }
      ]
    },
  ]
})
