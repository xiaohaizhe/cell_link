//mutation.js

import {setStore, getStore,removeStore} from '../config/mUtils'

const HANDLE_USER = 'HANDLE_USER'
const REMOVE_USER = 'REMOVE_USER'


// const SOME_MUTATION = 'SOME_MUTATION'

// 第一个参数默认接收state对象
// let increment = (state) => {
//     state.count++
//   }
// let decrement = (state) => {
//   state.count--
// }
//   export default{
//       increment, 
//       decrement
//     }  
export default{
    [HANDLE_USER](state, {userName,userId,autoLogin}){
      state.userName = userName;
      state.userId = userId;
      state.autoLogin = autoLogin;
      // 把登录的用户的名保存到localStorage中，防止页面刷新，导致vuex重新启动，用户名就成为初始值（初始值为空）的情况
      setStore('userName', userName);
      setStore('userId', userId);
      setStore('autoLogin', autoLogin);
    },
    [REMOVE_USER](state){
      state.userName = null;
      state.userId = null;
      state.autoLogin = null;
      removeStore('userName');
      removeStore('userId');
      removeStore('autoLogin');
    }
}