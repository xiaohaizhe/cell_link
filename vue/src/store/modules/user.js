import { login , logout , modifyUser} from '@/api/user'
import { findListByUser } from '@/api/scene'
import { getStore, getStoreObj,setStore, removeStore } from '@/utils/mUtils'
import router , {resetRouter} from '@/router'
import axios from 'axios'
import md5 from 'js-md5';

const state = {
  token: getStore('token'),
  // user: getStoreObj('user'),
  scenes:[],
  activeScene:getStoreObj('activeScene'),
  user:{
    "isPwdModified":1,
    "isVertifyPhone":1,
    "phone":"15605162862",
    "name":"dev2",
    "isVertifyEmail":0,
    "type":1,
    "userId":1566784252992,
    "email":""
    }
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_USER: (state, user) => {
    state.user = user
  },
  SET_SCENES:(state,scene)=>{
    state.scenes = scene
  },
  SET_SCENE:(state,scene)=>{
    state.activeScene = scene
  }
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    const { username, password,isRemember } = userInfo
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password:password ,isRemember:isRemember }).then(response => {//md5(password)
        const { data } = response
        setStore('token',data.token)
        setStore('user',data.user)
        commit('SET_TOKEN', data.token)
        commit('SET_USER', data.user)
        resolve(data.user)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user modify
  modify({ commit }, userInfo) {
    const { userId , pwd , phone , email } = userInfo
    return new Promise((resolve, reject) => {
      modifyUser({ userId: userId, pwd:pwd ,phone:phone,name:'haha',email:email }).then(response => {//md5(pwd)
        const { data } = response
        setStore('user',data)
        commit('SET_USER', data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      logout({userId:state.user.userId}).then(() => {
        commit('SET_TOKEN', '')
        removeStore()
        resetRouter()
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      removeStore()
      resolve()
    })
  },

  getAside({ commit,state }) {
    return new Promise((resolve, reject) => {
        findListByUser(state.user.userId).then(response => {
        const { data } = response
        commit('SET_SCENES',data)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },
  setScene({ commit },data){
    setStore('activeScene',data)
    commit('SET_SCENE', data)
  }

}
//   // dynamically modify permissions
//   changeRoles({ commit, dispatch }, role) {
//     return new Promise(async resolve => {
//       const token = role + '-token'

//       commit('SET_TOKEN', token)
//       setToken(token)

//       const { roles } = await dispatch('getInfo')

//       resetRouter()

//       // generate accessible routes map based on roles
//       const accessRoutes = await dispatch('permission/generateRoutes', roles, { root: true })

//       // dynamically add accessible routes
//       router.addRoutes(accessRoutes)

//       // reset visited views and cached views
//       dispatch('tagsView/delAllViews', null, { root: true })

//       resolve()
//     })
//   }
// }

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
