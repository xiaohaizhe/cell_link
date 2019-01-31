import Vue from 'vue'
import Router from 'vue-router'

const login = r => require.ensure([], () => r(require('../page/login/login')), 'login')
const index = r => require.ensure([], () => r(require('../page/home/index')), 'index')
const user = r => require.ensure([], () => r(require('../page/user/user')), 'user')
const overview = r => require.ensure([], () => r(require('../page/home/children/overview')), 'overview')
const prodOverview = r => require.ensure([], () => r(require('../page/home/children/prodOverview')), 'prodOverview')
const addProduct = r => require.ensure([], () => r(require('../page/product/addProduct')), 'addProduct')
const editProduct = r => require.ensure([], () => r(require('../page/product/editProduct')), 'editProduct')




Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'index',
      component: index
    },{
      path: '/login',
      name: 'login',
      component: login        //登录页
    },{
      path: '/index',
      name: 'index',
      component: index,
      children: [
        {
          path: '',
          name: 'overview',
          component: overview     //未登录首页
        },{
          path: 'products',
          name: 'prodOverview',
          component: prodOverview,     //已登录首页
          meta: {
            requireAuth: true,  // 添加该字段，表示进入这个路由是需要登录的
          },
        }]
    },{
      path: '/addProduct',      //添加产品
      name: 'addProduct',
      component: addProduct
    },{
      path: '/editProduct',     //编辑产品
      name: 'editProduct',
      component: editProduct
    },{
      path: '/user',     //编辑产品
      name: 'user',
      component: user
    }
    
  ]
})

