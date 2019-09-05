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
import addScene from 'components/dialogs/addScene'

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
