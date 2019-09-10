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
const bindtel = r => require.ensure([], () => r(require('views/user/children/bindtel')), 'bindtel')
const bindEmail = r => require.ensure([], () => r(require('views/user/children/bindEmail')), 'bindEmail')
const editPwdIndex = r => require.ensure([], () => r(require('views/firstLand/index')), 'index')
const scene = r => require.ensure([], () => r(require('views/scene/scene')), 'scene')
const dgTable = r => require.ensure([], () => r(require('views/scene/children/dgTable')), 'dgTable')
const appTable = r => require.ensure([], () => r(require('views/scene/children/application')), 'application')
const triggerTable = r => require.ensure([], () => r(require('views/scene/children/trigger')), 'trigger')
const devDetail = r => require.ensure([], () => r(require('views/device/devDetail')), 'devDetail')
const devGroup =  r => require.ensure([], () => r(require('views/devGroup/devGroup')), 'devGroup')
const dataStream = r => require.ensure([], () => r(require('views/device/children/dataStream')), 'dataStream')

Vue.use(Router)

export const constantRoutes = [
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
          name: 'dashboard',
          meta: { title:'设备概况'}
        },
        {
          path: 'devList',
          component:devList,
          name: 'devList',
          meta: { title:'设备列表'}
        },
        {
          path: 'log',
          component:log,
          name: 'log',
          meta: { title:'日志信息'}
        },
        {
          path: 'scene/:scenarioId/',
          component:scene,
          redirect: 'noRedirect',
          meta: { title:'我的场景' , sceneFlag:true},
          children:[
            { 
              path: 'devGroup', 
              component: dgTable,
              meta: { title:'设备组',clMatch:'scene'},
            },
            { 
              path: 'trigger', 
              component: triggerTable,
              meta: { title:'触发器',clMatch:'scene'},
            },
            { 
              path: 'application', 
              component: appTable,
              meta: { title:'应用' ,clMatch:'scene'},
            }
          ]
        },
        { 
          path: 'devGroup/:dgId', 
          component: devGroup,
          meta: { clMatch:'scene' , title:'设备组详情', dgFlag:true}
        },
        {
          path: 'device/:deviceId', 
          component: devDetail,
          meta: { clMatch:'scene' , title:'设备详情', devFlag:true},
          children:[
            {
              path: 'dataStream', 
              component: dataStream,
              meta: { clMatch:'scene' , title:'数据流'},
            }
          ]
        }
      ]
    },
    {
      path: '/user',
      name: 'userIndex',
      redirect: '/user/index',
      component: userIndex,
      meta: { title:'个人中心'},
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
        },
        {
          path: 'bindtel',
          component:bindtel,
          name: 'bindtel',
          meta: { title: '手机换绑' }
        },
        {
          path: 'bindEmail/:data',
          component:bindEmail,
          name: 'bindEmail',
          meta: { title: '绑定邮箱' }
        }
      ]
    },{
      path: '/firstLand',
      name: 'firstLand',
      redirect: '/firstLand',
      component: editPwdIndex,
      children: [
        {
          path: '',
          component:editPwd
        }
      ]
    }
  ]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}


export default router