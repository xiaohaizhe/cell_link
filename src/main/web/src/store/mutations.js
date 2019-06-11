//mutation.js

import {setStore, getStore,removeStore} from '../config/mUtils'

const HANDLE_USER = 'HANDLE_USER'
const EDIT_USER = 'EDIT_USER'
const REMOVE_USER = 'REMOVE_USER'
const HANDLE_ADMIN = 'HANDLE_ADMIN';
const SAVE_PRODUCT = 'SAVE_PRODUCT';
const SAVE_TAB = 'SAVE_TAB';

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
    [HANDLE_USER](state, userData){
      removeStore('user');
      state.user = {};
      for(let key in userData){
        if(key=='name'){
          state.user.userName = userData[key];
        }else if(key=='id'){
          state.user.userId = userData[key];
        }else{
          state.user[key] = userData[key];
        }
      }
      if(!state.user.expire){
        state.user.expire= 5 * 60 * 60 * 1000;//5小时
      }
      setStore('user', state.user);
    },
    [HANDLE_ADMIN](state, userData){
      removeStore('user');
      state.user = {};
      for(let key in userData){
          state.user[key] = userData[key];
      }
      state.user.expire= 5 * 60 * 60 * 1000;
      setStore('user', state.user);
    },
    [REMOVE_USER](state){
        removeStore('user');
        state.user = {};
    },
    [SAVE_PRODUCT](state, product){
      state.product=product;
      console.log(state);
    },
    [SAVE_TAB](state,id){
      state.prodTab = id;
    },
    [EDIT_USER](state,data){  
      for(let key in data){
          state.user[key] = data[key];
      }
      setStore('user', state.user);
    }
}