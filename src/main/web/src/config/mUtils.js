/**
 * 存储localStorage
 */
export const setStore = (name, content) => {
	if (!name) return;
	if (typeof content !== 'string') {
		content = JSON.stringify(content);
	}
	window.localStorage.setItem(name, content);
}

/**
 * 获取localStorage
 */
export const getStore = name => {
	if (!name) return;
	return window.localStorage.getItem(name);
}

/**
 * 删除localStorage
 */
export const removeStore = name => {
	if (!name) return;
	window.localStorage.removeItem(name);
}

/**
 * 获取某天日期
 */
export const getDay = day => {
	var today = new Date();  
	  
	var targetday_milliseconds=today.getTime() + 1000*60*60*24*day;          

	today.setTime(targetday_milliseconds); 
	  
	var tYear = today.getFullYear();  
	var tMonth = today.getMonth();  
	var tDate = today.getDate();  
	tMonth = doHandleMonth(tMonth + 1);  
	tDate = doHandleMonth(tDate);  
	return tYear+"-"+tMonth+"-"+tDate +' 00:00:00';
}
//时间格式化函数，此处仅针对yyyy-MM-dd hh:mm:ss 的格式进行格式化
export const dateFormat = time =>{
    var date=new Date(time);
    var year=date.getFullYear();
    /* 在日期格式中，月份是从0开始的，因此要加0
     * 使用三元表达式在小于10的前面加0，以达到格式统一  如 09:11:05
     * */
    var month= date.getMonth()+1<10 ? "0"+(date.getMonth()+1) : date.getMonth()+1;
    var day=date.getDate()<10 ? "0"+date.getDate() : date.getDate();
    var hours=date.getHours()<10 ? "0"+date.getHours() : date.getHours();
    var minutes=date.getMinutes()<10 ? "0"+date.getMinutes() : date.getMinutes();
    var seconds=date.getSeconds()<10 ? "0"+date.getSeconds() : date.getSeconds();
    // 拼接
    return year+"-"+month+"-"+day+" "+hours+":"+minutes+":"+seconds;
}
//获取当前日期前N个月的日期
export const getPreMonthDay = (date = new Date(),monthNum=3) => {
	var year = date.getFullYear();  //获取当前日期的年份
	var month = date.getMonth()+1;   //获取当前日期的月份
	var day = date.getDate();  //获取当前日期的日
	var days = new Date(year, month, 0);
	days = days.getDate(); //获取当前日期中月的天数
	var year2 = year;
	var month2 = parseInt(month) - monthNum;
	if (month2 <=0) {
		year2 = parseInt(year2) - parseInt(month2 / 12 == 0 ? 1 : Math.abs(parseInt(month2 / 12)) + 1)
		month2 = 12 - (Math.abs(month2) % 12);
	}
	var day2 = day;
	var days2 = new Date(year2, month2, 0);
	days2 = days2.getDate();
	if (day2 > days2) {
		day2 = days2;
	}
	if (month2 < 10) {
		month2 = '0' + month2;
	}
	var t2 = year2 + '-' + month2 + '-' + day2 +' 00:00:00';
	return t2;
}
function doHandleMonth(month){  
	var m = month;  
	if(month.toString().length == 1){  
	   m = "0" + month;  
	}  
	return m;  
}