import { login , logout , modifyUser} from '@/api/user'
import { findListByUser ,findSceneById} from '@/api/scene'
import { findDgById } from '@/api/devGroup'
import { findDevById } from '@/api/dev'
import { getStore, getStoreObj,setStore, removeStore } from '@/utils/mUtils'
import $route , {resetRouter} from '@/router'
import axios from 'axios'
import md5 from 'js-md5';

const state = {
  token: getStore('token'),
  // user: getStoreObj('user'),
  scenes:[],
  activeScene:{},
  user:{
    "isPwdModified":1,
    "isVertifyPhone":1,
    "phone":"15605162862",
    "name":"dev2",
    "isVertifyEmail":0,
    "type":1,
    "userId":1566784252992,
    "email":""
    },
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
  SET_SCENE:(state,data)=>{
    state.activeScene = data
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

  getAside({ commit,state,dispatch},params) {
    if(params){
      dispatch('setScene',params);
    }
    
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
  getScene({ commit,state},scenarioId){
    return new Promise((resolve, reject) => {
        findSceneById(scenarioId).then(response => {
        const { data } = response
        commit('SET_SCENE',data)
        resolve(data.scenarioId)
      }).catch(error => {
        reject(error)
      })
    })
  },
  getDg({ commit,state},dgId){
    return new Promise((resolve, reject) => {
      findDgById(dgId).then(response => {
        const { data } = response
        commit('SET_SCENE',data)
        resolve(data.scenarioId)
      }).catch(error => {
        reject(error)
      })
    })
  },
  getDev({ commit,state},devId){
    return new Promise((resolve, reject) => {
      findDevById(devId).then(response => {
        const { data } = response
        commit('SET_SCENE',data)
        resolve(data.scenarioId)
      }).catch(error => {
        reject(error)
      })
    })
  },
  setScene({ dispatch },params){
    if(params.scenarioId){
      dispatch('getScene',params.scenarioId)
    }else if(params.dgId){
      dispatch('getDg',params.dgId)
    }else if(params.deviceId){
      dispatch('getDev',params.deviceId)
    }
  },
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
