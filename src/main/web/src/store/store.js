import Vue from 'vue'
import Vuex from 'vuex'
import {getStore} from 'config/mUtils'
import mutations from './mutations'
import getters from './getters'
import actions from './actions'

Vue.use(Vuex);

const state = {
	user:getStore('user') || {},
	product: {},
	prodTab:'prodOverview',
	lineData:[],
	barData:[]
}

export default new Vuex.Store({
    state,
	getters,
	actions,
	mutations,
});