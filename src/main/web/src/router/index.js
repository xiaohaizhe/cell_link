import Vue from 'vue'
import Router from 'vue-router'

const login = r => require.ensure([], () => r(require('../page/login/login')), 'login')
const user = r => require.ensure([], () => r(require('../page/userCenter/user')), 'user')
const overview = r => require.ensure([], () => r(require('../page/overview/overview')), 'overview')
const home = r => require.ensure([], () => r(require('../page/home/home')), 'home')
const addProduct = r => require.ensure([], () => r(require('../page/product/addProduct')), 'addProduct')
const editProduct = r => require.ensure([], () => r(require('../page/product/editProduct')), 'editProduct')
const editpsw = r => require.ensure([], () => r(require('../page/userCenter/editpsw')), 'editpsw')
const bindtel = r => require.ensure([], () => r(require('../page/userCenter/bindtel')), 'bindtel')
const bindEmail = r => require.ensure([], () => r(require('../page/userCenter/bindEmail')), 'bindEmail')
const index = r => require.ensure([], () => r(require('../page/index/index')), 'index')
const addUser = r => require.ensure([], () => r(require('../page/users/addUser')), 'addUser')
const editUser = r => require.ensure([], () => r(require('../page/users/editUser')), 'editUser')
const userManage = r => require.ensure([], () => r(require('../page/users/userManage')), 'userManage')
const userDetail = r => require.ensure([], () => r(require('../page/users/userDetail')), 'userDetail')


Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '',
      redirect: '/overview'
    },{
      path: '/login',
      name: 'login',
      component: login        //登录页
    },{
      path: '/overview',
      name: 'overview',
      component: overview        //未登录首页
    },{
      path: '/index',
      name: 'index',
      component: index        //管理员首页
    },{
      path: '/home',
      name: 'home',
      component: home,        //已登录首页
      // meta: {
      //   requireAuth: true,  // 添加该字段，表示进入这个路由是需要登录的
      // }
    },{
      path: '/addProduct',      //添加产品
      name: 'addProduct',
      component: addProduct
    },{
      path: '/editProduct',     //编辑产品
      name: 'editProduct',
      component: editProduct
    },{
      path: '/user',     //个人中心
      name: 'user',
      component: user
    },{
      path: '/editpsw',     //修改密码
      name: 'editpsw',
      component: editpsw
    },{
      path: '/bindtel',     //手机换绑
      name: 'bindtel',
      component: bindtel
    },{
      path: '/bindEmail',     //绑定邮箱
      name: 'bindEmail',
      component: bindEmail
    },{
      path:'/addUser',        //添加用户
      name: 'addUser',
      component: addUser
    },{
      path:'/editUser',        //编辑用户
      name: 'editUser',
      component: editUser
    },{
      path: '/userManage',     //管理员-用户管理
      name: 'userManage',
      component: userManage
    },{
      path: '/userDetail',     //管理员-用户详情
      name: 'userDetail',
      component: userDetail
    }
    
  ]
})

