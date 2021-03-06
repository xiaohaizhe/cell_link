// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import Vuetify from 'vuetify'
import 'babel-polyfill'  //ie打不开问题
import router from './router'
import { Input , Button , Checkbox , MessageBox , Message , Tabs , TabPane , Dropdown, Dialog,Table,TableColumn,Switch,Scrollbar,
    DropdownMenu , DropdownItem , Select , Pagination , Icon , Option ,Row ,Form,FormItem,Step,Steps,DatePicker,Radio,
    RadioGroup,RadioButton ,Tooltip,InputNumber,Loading,Menu,Submenu,MenuItem,MenuItemGroup,Col} from 'element-ui';
import VueClipboard from 'vue-clipboard2'
// import 'vuetify/dist/vuetify.min.css'
import './style/vuetify.css'
import './style/main.css'
import store from './store/store'
import echarts from 'echarts' //引入echarts
import {getStore} from 'config/mUtils'
import 'material-design-icons-iconfont/dist/material-design-icons.css'

Vue.use(Vuetify);

Vue.prototype.$echarts = echarts //引入组件
Vue.config.productionTip = false
Vue.use(VueClipboard);
Vue.use(Dropdown);
Vue.use(Scrollbar);
Vue.use(Radio);
Vue.use(RadioGroup);
Vue.use(RadioButton);
Vue.use(DropdownMenu);
Vue.use(DropdownItem);
Vue.use(Icon);
Vue.use(Row);
Vue.use(Steps);
Vue.use(Dialog);
Vue.use(Step);
Vue.use(Tabs);
Vue.use(Form);
Vue.use(Input);
Vue.use(Option);
Vue.use(Button);
Vue.use(Select); 
Vue.use(TabPane);
Vue.use(Switch);
Vue.use(Tooltip);
Vue.use(FormItem);
Vue.use(Checkbox);
Vue.use(Pagination);
Vue.use(Table);
Vue.use(TableColumn);
Vue.use(DatePicker);
Vue.use(InputNumber);
Vue.use(Loading.directive);
Vue.use(Menu);
Vue.use(Submenu);
Vue.use(MenuItem);
Vue.use(MenuItemGroup);
Vue.use(Col);

Vue.prototype.$loading = Loading.service;
Vue.prototype.$message = Message;
Vue.prototype.$msgbox = MessageBox;
Vue.prototype.$alert = MessageBox.alert;
Vue.prototype.$confirm = MessageBox.confirm;


router.beforeEach((to, from, next) => {
  if (to.meta.required) {
    let user = getStore('user');
    if(user){
      if(to.name!="home" && user.userName){
        next({
            path: '/home',
            // query: {redirect: to.fullPath}  // 将跳转的路由path作为参数，登录成功后跳转到该路由
        })
      }else if(to.name!="index" && !user.userName){
        next({
          path: '/index',
          // query: {redirect: to.fullPath}  // 将跳转的路由path作为参数，登录成功后跳转到该路由
      })
      }else{
        next();
      }
    }else if(to.name!="overview"){
      next({
          path: '/overview',
          // query: {redirect: to.fullPath}  // 将跳转的路由path作为参数，登录成功后跳转到该路由
      })
    }else{
      next();
    }
  }
  else if (to.meta.requireAuth) {  // 判断该路由是否需要登录权限
    //获取用户信息,判断过期
      let user = getStore('user');
      if(user){
        if(user.autoLogin){
          next();
        }else{
          let startTime =user.startTime;
          let expire = user.expire;
          if(Date.now()-startTime>expire){
              window.localStorage.removeItem(name);
              MessageBox.alert("登陆已过期，请重新登陆！");
              next({
                  path: '/login',
                  query: {redirect: to.fullPath}  // 将跳转的路由path作为参数，登录成功后跳转到该路由
              })
          }else{
            next();
          }
        }
      }else{
        MessageBox.alert("请登陆！");
        next({
            path: '/login',
            query: {redirect: to.fullPath}  // 将跳转的路由path作为参数，登录成功后跳转到该路由
        })
      }
  }
  else {
      next();
  }
});

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})
