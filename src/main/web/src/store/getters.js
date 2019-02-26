
let userName = (state) => state.userName
let userId = (state) => state.userId
let autoLogin = (state) => state.autoLogin
let phone = (state) => state.phone
let isvalid = (state) => state.isvalid
let isvertifyemail = (state) => state.isvertifyemail
let isvertifyphone = (state) => state.isvertifyphone
let adminName = (state) => state.adminName


export default {
	userName,
	adminName,
	userId,
	autoLogin,
	phone,
	isvalid,
	isvertifyemail,
	isvertifyphone
}