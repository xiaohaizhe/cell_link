import { login , logout , modify} from '@/api/user'
import { getStore, getStoreObj,setStore, removeStore } from '@/utils/mUtils'
import router , {resetRouter} from '@/router'
import axios from 'axios'

const state = {
  token: getStore('token'),
  user: getStoreObj('user'),
  roles: [],
  data:{
        "msg":"成功",
        "code":0,
        "data":{
            "user":{
                "isPwdModified":0,
                "isVertifyPhone":1,
                "phone":"18206295380",
                "name":"dev1",
                "isVertifyEmail":0,
                "type":1,
                "userId":1566784167729
            },
            "token":"eyJhbGciOiJIUzI1NiJ9.eyJjcmVhdGVkIjoxNTY2Nzg0MTY4MDAwLCJuYW1lIjoiZGV2MSIsInB3ZCI6ImUxMGFkYzM5NDliYTU5YWJiZTU2ZTA1N2YyMGY4ODNlIiwiZXhwIjoxNTY3MTU5MjYzLCJ1c2VySWQiOjE1NjY3ODQxNjc3MjksImlhdCI6MTU2NzE1MjA2MywianRpIjoiMTU2Njc4NDE2NzcyOSJ9.6X6i5oVSgG0M6Unv07mxTP77wTCmJXzmB_GWSYn5mfQ"
        }
    }
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_USER: (state, user) => {
    state.user = user
  },
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    const { username, password,isRemember } = userInfo
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: password,isRemember:isRemember }).then(response => {
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
    const { username, password,isRemember } = userInfo
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: password,isRemember:isRemember }).then(response => {
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

  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      logout({user_id:state.userId}).then(() => {
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
