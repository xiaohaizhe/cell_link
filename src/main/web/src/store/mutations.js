//mutation.js

import {setStore, getStore} from '../config/mUtils'

const HANDLE_USERNAME = 'HANDLE_USERNAME'

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
    [HANDLE_USERNAME](state, userName, autoLogin){
      state.userName = userName;
      // 把登录的用户的名保存到localStorage中，防止页面刷新，导致vuex重新启动，用户名就成为初始值（初始值为空）的情况
      setStore('userName', userName);
      setStore('autoLogin', autoLogin);
    }
}