import { Loading,MessageBox } from 'element-ui';
var msgFlag = false;

export default async(url = '', data = {}, type = 'GET', method = 'fetch') => {
	type = type.toUpperCase();
	// url = url;
	// debugger
	if (type == 'GET') {
		let dataStr = ''; //数据拼接字符串
		Object.keys(data).forEach(key => {
			dataStr += key + '=' + data[key] + '&';
		})

		if (dataStr !== '') {
			dataStr = dataStr.substr(0, dataStr.lastIndexOf('&'));
			url = url + '?' + dataStr;
		}
	}

	if (window.fetch && method == 'fetch') {
		let requestConfig = {
			credentials: 'include',
			method: type,
			headers: {
				'Accept': 'application/json',
				'Content-Type': 'application/json'
			},
			mode: "cors",
			// cache: "force-cache"
		}

		if (type == 'POST') {
			Object.defineProperty(requestConfig, 'body', {
				value: JSON.stringify(data)
			})
		}
		
		// 超时版的fetch
		function _fetch(fetch, timeout) {
			return Promise.race([
				fetch,
				new Promise(function (resolve, reject) {
					setTimeout(() => {
						reject(new Error('request timeout'));
					}, timeout);
				},)
			]);
		}
		try {
			//loading
			var loadingInstance = Loading.service({
				lock: true,
				text: 'Loading',
				spinner: 'el-icon-loading',
				background: 'rgba(0, 0, 0, 0.7)'
			});
			const response = await _fetch(fetch(url, requestConfig),5000);
			if(!response.ok){
				loadingInstance.close();
				if(!msgFlag){
					setTimeout(() => {
						MessageBox.alert("服务器异常，请稍后再试！");
					}, 500);
					msgFlag=true;
				};
				return {code:"error"};
			}else{
				const responseJson = await response.json();
				loadingInstance.close();
				return responseJson;
			}	
		} catch (error) {
			throw new Error(error)
		}
	} else {
		return new Promise((resolve, reject) => {
			let requestObj;
			if (window.XMLHttpRequest) {
				requestObj = new XMLHttpRequest();
			} else {
				requestObj = new ActiveXObject;
			}

			let sendData = '';
			if (type == 'POST') {
				sendData = JSON.stringify(data);
			}

			requestObj.open(type, url, true);
			requestObj.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
			requestObj.send(sendData);

			requestObj.onreadystatechange = () => {``
				if (requestObj.readyState == 4) {
					if (requestObj.status == 200) {
						let obj = requestObj.response
						if (typeof obj !== 'object') {
							obj = JSON.parse(obj);
						}
						resolve(obj)
					} else {
						reject(requestObj)
					}
				}
			}
		})
	}
}
