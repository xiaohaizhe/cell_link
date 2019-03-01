//mutation.js

import {setStore, getStore,removeStore} from '../config/mUtils'

const HANDLE_USER = 'HANDLE_USER'
const REMOVE_USER = 'REMOVE_USER'
const HANDLE_ADMIN = 'HANDLE_ADMIN';
const SAVE_PRODUCT = 'SAVE_PRODUCT';


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
    [HANDLE_USER](state, {autoLogin,userData}){
      state.autoLogin = autoLogin;
      setStore('autoLogin', autoLogin);
      for(let key in userData){
        if(key=='name'){
          state.userName = userData[key];
          setStore('userName', userData[key]);
        }else if(key=='id'){
          state.userId = userData[key];
          setStore('userId', userData[key]);
        }else{
          setStore(key,userData[key]);
          state.key = userData[key];
        }
        
      }
    },
    [HANDLE_ADMIN](state, {autoLogin,adminName}){
      state.autoLogin = autoLogin;
      state.adminName = adminName;
      setStore('autoLogin',autoLogin);
      setStore('adminName',adminName);
    },
    [REMOVE_USER](state){
      for(let key in state){
        removeStore(key);
        state.key = null;
      }
    },
    [SAVE_PRODUCT](state, product){
      state.product=product;
      console.log(state);
    },
}