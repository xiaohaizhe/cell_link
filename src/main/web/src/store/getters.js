let user = (state) => state.user
let product = (state) => state.product
let prodTab = (state) => state.prodTab
let lineData = (state) => state.lineData
let barData = (state) => state.barData

export default {
	// user: function (state) {
	// 	debugger
	// 	let user = JSON.parse(window.localStorage.getItem('user'));
	// 	if(user.autoLogin){
	// 		return user;
	// 	}else{
	// 		let startTime =user.startTime;
	// 		if(Date.now()-startTime>state.expire){
	// 			localStorage.removeItem(name);
	// 			return null;
	// 		}
	// 		return user;
	// 	}
	//   },
	user,
	product,
	prodTab,
	lineData,
	barData
}