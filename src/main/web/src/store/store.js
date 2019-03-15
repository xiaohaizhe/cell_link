import Vue from 'vue'
import Vuex from 'vuex'
import {getStore} from 'config/mUtils'
import mutations from './mutations'
import getters from './getters'
import actions from './actions'

Vue.use(Vuex);

const state = {
	userId: null || getStore('userId'),
	userName: null || getStore('userName'),
	adminName: null || getStore('adminName'),
	autoLogin: false || getStore('autoLogin'),
	isvalid: null || getStore('isvalid'),
	isvertifyemail: null || getStore('isvertifyemail'),
	isvertifyphone: null || getStore('isvertifyphone'),
	phone: null || getStore('phone'),
	pwd: null || getStore('pwd'),
	product: {},
	prodTab:'prodOverview'
}

export default new Vuex.Store({
    state,
	getters,
	actions,
	mutations,
});