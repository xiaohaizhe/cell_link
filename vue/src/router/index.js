import Vue from 'vue'
import Router from 'vue-router'

const login = () => import('views/login/login.vue')
const overview = () => import('views/overview/overview.vue')
const index = () => import('views/index/index.vue')
const dashboard = () => import('views/index/index.vue')
const devList = () => import('views/devList/devList.vue')
const log = () => import('views/log/log.vue')
const userIndex = () => import('views/user/index.vue')
const user = () => import('views/user/index.vue')
const editPwd = () => import('views/user/children/editPwd.vue')
const bindtel = () => import('views/user/children/bindtel.vue')
const bindEmail = () => import('views/user/children/bindEmail.vue')
const editPwdIndex = () => import('views/firstLand/index.vue')
const scene = () => import('views/scene/scene.vue')
const dgTable = () => import('views/scene/children/dgTable.vue')
const appTable = () => import('views/scene/children/application.vue')
const triggerTable = () => import('views/scene/children/trigger.vue')
const devDetail = () => import('views/device/devDetail.vue')
const devGroup =  () => import('views/devGroup/devGroup.vue')
const dataStream = () => import('views/device/children/dataStream.vue')
const orderLog = () => import('views/log/children/orderLog.vue')
const triggerLog = () => import('views/log/children/triggerLog.vue')
const opLog = () => import('views/log/children/opLog.vue')
const help= () => import('views/help/help.vue')
const introduction = () => import('views/help/children/introduction.vue')
const devFlow = () => import('views/help/children/devFlow.vue')
const guideAcc =  () => import('views/help/children/guideAcc.vue')
const guidePro =  () => import('views/help/children/guidePro.vue')
const guideDev =  () => import('views/help/children/guideDev.vue')
const guideDs =  () => import('views/help/children/guideDs.vue')
const guideApp =  () => import('views/help/children/guideApp.vue')
const guideSec =  () => import('views/help/children/guideSec.vue')
const admin = () => import('views/admin/index.vue')
const application = () => import('views/application/index.vue')
const publish = () => import('views/application/publish.vue')
const redirect = () => import('views/redirect.vue')
const devOrderLog = () => import('views/device/children/devOrderLog.vue')
Vue.use(Router)

export const constantRoutes = [
    {
      path: '/redirect',
      name: 'redirect',
      component: redirect
    },
    {
      path: '/login',
      name: 'login',
      component: login
    },
    {
      path: '/overview',
      name: 'overview',
      component: overview,
      meta: { required: true}
    },{
      path: '/admin',
      component: admin,
      name:'admin',
      meta: { title:'概况',name:'admin',required: true}
    },
    {
      path: '/',
      component: index,
      redirect: '/dashboard',
      meta: {required: true},
      children: [
        {
          path: 'dashboard',
          component:dashboard,
          name: 'dashboard',
          meta: { title:'设备概况',name:'dashboard',required: true}
        },
        {
          path: 'devList',
          component:devList,
          name: 'devList',
          meta: { title:'设备列表',name:'devList'}
        },
        {
          path: 'log',
          component:log,
          redirect:'/log/orderLog',
          name: 'log',
          meta: { title:'日志信息',name:'log'},
          children:[{
            path: 'orderLog', 
            component: orderLog,
            meta: { title:'下发日志',name:'orderLog'}
          },{
            path: 'triggerLog', 
            component: triggerLog,
            meta: { title:'触发日志',name:'triggerLog'}
          },{
            path: 'opLog', 
            component: opLog,
            meta: { title:'操作日志',name:'opLog'}
          }]
        },
        {
          path: 'scene/:scenarioId/',
          component:scene,
          redirect: 'noRedirect',
          meta: { title:'我的场景' , sceneFlag:true,name:'scene'},
          children:[
            { 
              path: 'devGroup', 
              component: dgTable,
              meta: { title:'设备组',clMatch:'scene',name:'devGroup'},
            },
            { 
              path: 'trigger', 
              component: triggerTable,
              meta: { title:'触发器',clMatch:'scene',name:'trigger'},
            },
            { 
              path: 'application', 
              component: appTable,
              meta: { title:'应用' ,clMatch:'scene',name:'application'}
            }
          ]
        },
        { 
          path: 'devGroup/:dgId', 
          component: devGroup,
          meta: { clMatch:'scene' , title:'设备组详情', dgFlag:true}
        },
        { 
          path: 'application/:appId', 
          component: application,
          meta: { clMatch:'scene' , title:'应用详情', appFlag:true}
        },
        {
          path: 'device/:deviceId', 
          component: devDetail,
          meta: { clMatch:'scene' , title:'设备详情', devFlag:true},
          children:[
            {
              path: 'dataStream', 
              component: dataStream,
              meta: { clMatch:'scene' , name:'dataStream',title:'数据流展示'},
            },
            {
              path: 'devOrderLog', 
              component: devOrderLog,
              meta: { clMatch:'scene' , name:'devOrderLog', title:'下发日志'},
            }
          ]
        }
      ]
    },
    { 
      path: '/publish/:appId', 
      component: publish,
    },
    {
      path: '/help',
      name: 'help',
      redirect: '/help/introduction',
      component: help,
      meta: { title:'帮助中心'},
      children: [
        {
          path:'introduction',
          name:'introduction',
          component:introduction
        },{
          path:'devFlow',
          name:'devFlow',
          component:devFlow
        },{
          path:'guideAcc',
          name:'guideAcc',
          component:guideAcc
        },{
          path:'guidePro',
          name:'guidePro',
          component:guidePro
        },{
          path:'guideDev',
          name:'guideDev',
          component:guideDev
        },{
          path:'guideDs',
          name:'guideDs',
          component:guideDs
        },{
          path:'guideApp',
          name:'guideApp',
          component:guideApp
        },{
          path:'guideSec',
          name:'guideSec',
          component:guideSec
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