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