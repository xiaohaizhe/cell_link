// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import store from './store'
import router from './router'
import echarts from 'echarts' //引入echarts
import 'babel-polyfill'       //ie打不开问题
import ElementUI from 'element-ui';
import '@/styles/index.css';
import '@/styles/main.css'
import axios from 'axios'
import 'default-passive-events'
//新建组件
import addScene from 'components/dialogs/global/addScene.js' 
import addDevGroup from 'components/dialogs/global/addDevGroup.js' 
import addDevice from 'components/dialogs/global/addDevice.js' 
import addApp from 'components/dialogs/global/addApp.js' 
Vue.use(addScene)
Vue.use(addDevGroup)
Vue.use(addDevice)
Vue.use(addApp)
//新建组件

Vue.use(ElementUI);

Vue.prototype.$http=axios
Vue.config.productionTip = false
Vue.prototype.$echarts = echarts //引入组件

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})
