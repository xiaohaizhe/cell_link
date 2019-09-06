import { login , logout , modifyUser} from '@/api/user'
import { findListByUser } from '@/api/scene'
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
  SET_SCENE:(state,scenarioId)=>{
    let temp = state.scenes.filter(item => {
      if(item.scenarioId==scenarioId){
        return true
      }else{
        return false
      }
    })
    state.activeScene = temp[0]
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

  getAside({ commit,state},scenarioId) {
    return new Promise((resolve, reject) => {
        findListByUser(state.user.userId).then(response => {
        const { data } = response
        commit('SET_SCENES',data)
        if(scenarioId){
          commit('SET_SCENE',scenarioId)
        }
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  setScene({ commit },scenarioId){
    commit('SET_SCENE',scenarioId)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
