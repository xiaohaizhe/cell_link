import Vue from 'vue'
import Router from 'vue-router'

const login = r => require.ensure([], () => r(require('../page/Login/Login')), 'login')
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
const streamShow = r => require.ensure([], () => r(require('../page/datastream/streamShow')), 'streamShow')
const devDetail = r => require.ensure([], () => r(require('../page/devDetail/devDetail')), 'devDetail')
const myProduct = r => require.ensure([], () => r(require('../page/product/myProduct')), 'myProduct')
const prodOverview = r => require.ensure([], () => r(require('../page/product/children/prodOverview')), 'prodOverview')
const devManage = r => require.ensure([], () => r(require('../page/product/children/devManage')), 'devManage')
const dsManage = r => require.ensure([], () => r(require('../page/product/children/dsManage')), 'dsManage')
const appManage = r => require.ensure([], () => r(require('../page/product/children/appManage')), 'appManage')
const intellAna = r => require.ensure([], () => r(require('../page/product/children/intellAna')), 'intellAna')
const triggerManage = r => require.ensure([], () => r(require('../page/product/children/triggerManage')), 'triggerManage')
const trigger = r => require.ensure([], () => r(require('../page/trigger/trigger')), 'trigger')
const cmdLogs = r => require.ensure([], () => r(require('../page/cmdLogs/cmdLogs')), 'cmdLogs')
const triggerDetail = r => require.ensure([], () => r(require('../page/trigger/triggerDetail')), 'triggerDetail')
const associatedDev = r => require.ensure([], () => r(require('../page/trigger/associatedDev')), 'associatedDev')
const publish = r => require.ensure([], () => r(require('../page/application/publish')), 'publish')
const linear = r => require.ensure([], () => r(require('../page/intellAnalysis/linear')), 'linear')
const heatmap = r => require.ensure([], () => r(require('../page/intellAnalysis/heatmap')), 'heatmap')

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '',
      redirect: '/overview'
    },{
      path: '/login',
      name: 'login',
      component: login,        //登录页
      meta: { keepAlive: false }
    },{
      path: '/overview',
      name: 'overview',
      component: overview,        //未登录首页
      meta: { keepAlive: false }
    },{
      path: '/index',
      name: 'index',
      component: index,        //管理员首页
      meta: { keepAlive: false,requireAuth: true }
    },{
      path: '/home',
      name: 'home',
      component: home,        //已登录首页
      meta: { keepAlive: false,requireAuth: true }
      // meta: {
      //   requireAuth: true,  // 添加该字段，表示进入这个路由是需要登录的
      // }
    },{
      path: '/myProduct/:productId/',      //我的产品
      name: 'myProduct',
      component: myProduct,
      meta:{requireAuth: true},
      children:[{
            path: "",
            component: prodOverview   // 当进入到home时，下面的组件显示
        },{
            path:'overview',
            name:'prodOverview',
            component:prodOverview
        },{
            path:'devManage',
            name:'devManage',
            component:devManage
        },{
          path:'dsManage',
          name:'dsManage',
          component:dsManage, 
        },{
          path:'appManage',
          name:'appManage',
          component:appManage, 
        },{
          path:'intellAna',
          name:'intellAna',
          component:intellAna, 
        },{
          path:'triggerManage',
          name:'triggerManage',
          component:triggerManage, 
          meta: { keepAlive: false }
        }
      ]
    },{
      path: '/addProduct',      //添加产品
      name: 'addProduct',
      component: addProduct,
      meta: { requireAuth: true }
    },{
      path: '/editProduct/:productId/',     //编辑产品
      name: 'editProduct',
      component: editProduct,
      meta: { requireAuth: true }
    },{
      path: '/user',     //个人中心
      name: 'user',
      component: user,
      meta: { keepAlive: false,requireAuth: true }
    },{
      path: '/editpsw',     //修改密码
      name: 'editpsw',
      component: editpsw,
      meta: { requireAuth: true }
    },{
      path: '/bindtel',     //手机换绑
      name: 'bindtel',
      component: bindtel,
      meta: { requireAuth: true }
    },{
      path: '/bindEmail',     //绑定邮箱
      name: 'bindEmail',
      component: bindEmail,
      meta: { requireAuth: true }
    },{
      path:'/addUser',        //添加用户
      name: 'addUser',
      component: addUser,
      meta: { requireAuth: true }
    },{
      path:'/editUser/:userData',        //编辑用户
      name: 'editUser',
      component: editUser,
      meta: { requireAuth: true }
    },{
      path: '/userManage/:userData',     //管理员-用户管理
      name: 'userManage',
      component: userManage,
      meta: { requireAuth: true }
    },{
      path: '/userDetail/:productData',     //管理员-用户详情
      name: 'userDetail',
      component: userDetail,
      meta: { requireAuth: true }
    },{
      path: '/streamShow/:data',     //数据流展示
      name: 'streamShow',
      component: streamShow,
      meta: { requireAuth: true }
    },{
      path: '/devDetail/:devData',     //管理员-设备详情
      name: 'devDetail',
      component: devDetail,
      meta: { requireAuth: true }
    },{
      path: '/trigger/:data',     //触发器展示
      name: 'trigger',
      component: trigger,
      meta: { requireAuth: true }
    },{
      path: '/cmdLogs/:data',     //下发日志
      name: 'cmdLogs',
      component: cmdLogs,
      meta: { requireAuth: true }
    },{
      path: '/triggerDetail/:trigger/',     //触发器详情
      name: 'triggerDetail',
      component: triggerDetail,
      meta: { requireAuth: true }
    },{
      path: '/associatedDev/:trigger/',     //触发器关联
      name: 'associatedDev',
      component: associatedDev,
      meta: { requireAuth: true }
    },{
      path: '/publish/:appId',     //应用详情-发布
      name: 'publish',
      component: publish,
      meta: { requireAuth: true }
    },{
      path: '/linear/:productId/',     //智能分析-线性回归
      name: 'linear',
      component: linear,
      meta: { requireAuth: true }
    },{
      path: '/heatmap/:productId/',     //智能分析-热力图
      name: 'heatmap',
      component: heatmap,
      meta: { requireAuth: true }
    }
    
  ]
})

