import Vue from 'vue'
import Vuex from 'vuex'
import {getStore} from '../config/mUtils'
import mutations from './mutations'
import getters from './getters'
import actions from './actions'

Vue.use(Vuex);

const state = {
	userId: null || getStore('userId'),
	userName: null || getStore('userName'),
	autoLogin: false || getStore('autoLogin')
}

export default new Vuex.Store({
    state,
	getters,
	actions,
	mutations,
});