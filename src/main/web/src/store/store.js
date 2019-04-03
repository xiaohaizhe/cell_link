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
	lineData:[{
		create_time:'周一',
		value:820
	},{
		create_time:'周二',
		value:932
	},{
		create_time:'周三',
		value:901
	},{
		create_time:'周四',
		value:934
	},{
		create_time:'周五',
		value:1290
	},{
		create_time:'周六',
		value:1330
	},{
		create_time:'周日',
		value:1320
	}],
	barData:[{
		create_time:'周一',
		value:820
	},{
		create_time:'周二',
		value:932
	},{
		create_time:'周三',
		value:901
	},{
		create_time:'周四',
		value:934
	},{
		create_time:'周五',
		value:1290
	},{
		create_time:'周六',
		value:1330
	},{
		create_time:'周日',
		value:1320
	}]
}

export default new Vuex.Store({
    state,
	getters,
	actions,
	mutations,
});