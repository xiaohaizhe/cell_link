// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import 'babel-polyfill'  //ie打不开问题
import router from './router'
import { Input , Button , Checkbox , MessageBox , Message , Tabs , TabPane , Dropdown, 
    DropdownMenu , DropdownItem , Select , Pagination , Icon  } from 'element-ui';
import './style/main.css'
import store from './store/store'
// import echarts from 'echarts' //引入echarts

// Vue.prototype.$echarts = echarts //引入组件
Vue.config.productionTip = false
Vue.use(Dropdown);
Vue.use(DropdownMenu);
Vue.use(DropdownItem);
Vue.use(Icon);
Vue.use(Tabs);
Vue.use(Input);
Vue.use(Button);
Vue.use(Select); 
Vue.use(TabPane);
Vue.use(Checkbox);
Vue.use(Pagination);

Vue.prototype.$message = Message;
Vue.prototype.$alert = MessageBox.alert;
Vue.prototype.$confirm = MessageBox.confirm;


router.beforeEach((to, from, next) => {
  if (to.meta.requireAuth) {  // 判断该路由是否需要登录权限
      if (store.state.autoLogin) {  // 通过vuex state获取当前的token是否存在
          next();
      }
      else {
          next({
              path: '/login',
              query: {redirect: to.fullPath}  // 将跳转的路由path作为参数，登录成功后跳转到该路由
          })
      }
  }
  else {
      next();
  }
})

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})
