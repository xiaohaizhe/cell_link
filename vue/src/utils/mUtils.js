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
	let content = window.localStorage.getItem(name);
	if (!content) return;
    return content;
}
/**
 * 获取localStorage obj
 */
export const getStoreObj = name => {
	let content = JSON.parse(window.localStorage.getItem(name));
	if (!content) return;
    return content;
}
/**
 * 删除localStorage
 */
export const removeStore = () => {
	window.localStorage.clear()
}

/**
 * 格式化时间
 */
export const dateFormat = (date) => {
	let fmt = "YYYY-mm-dd HH:MM:SS";
    let ret;
    let opt = {
        "Y+": date.getFullYear().toString(),        // 年
        "m+": (date.getMonth() + 1).toString(),     // 月
        "d+": date.getDate().toString(),            // 日
        "H+": date.getHours().toString(),           // 时
        "M+": date.getMinutes().toString(),         // 分
        "S+": date.getSeconds().toString()          // 秒
        // 有其他格式化字符需求可以继续添加，必须转化成字符串
    };
    for (let k in opt) {
        ret = new RegExp("(" + k + ")").exec(fmt);
        if (ret) {
            fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")))
        };
    };
    return fmt;
}

//本月第一天
export const showMonthFirstDay = () =>
{     
    var nowdate=new Date();     
    var monthStartDate = new Date(nowdate.getFullYear(), nowdate.getMonth(), 1); 
	return dateFormat(monthStartDate); 
}

//本月最后一天
export const showMonthLastDay = () =>
{     
	var nowdate=new Date();   
    var monthEndDate = new Date(nowdate.getFullYear(), nowdate.getMonth(), getMonthDays(nowdate.getFullYear(),nowdate.getMonth())); 
	return dateFormat(monthEndDate);
}
//获得某月的天数 
function getMonthDays(nowYear,myMonth){ 
	var monthStartDate = new Date(nowYear, myMonth, 1); 
	var monthEndDate = new Date(nowYear, myMonth + 1, 1); 
	var days = (monthEndDate - monthStartDate)/(1000 * 60 * 60 * 24); 
	return days; 
}

/**
 * 获取某天日期
 */
export const getDay = (day,time=' 00:00:00') => {
	var today = new Date();  
	var targetday_milliseconds=today.getTime() + 1000*60*60*24*day;          
	today.setTime(targetday_milliseconds); 
	var tYear = today.getFullYear();  
	var tMonth = today.getMonth();  
	var tDate = today.getDate();  
	tMonth = doHandleMonth(tMonth + 1);  
	tDate = doHandleMonth(tDate);  
	return tYear+"-"+tMonth+"-"+tDate +time;
}

function doHandleMonth(month){  
	var m = month;  
	if(month.toString().length == 1){  
	   m = "0" + month;  
	}  
	return m;  
}