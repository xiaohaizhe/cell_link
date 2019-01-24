import fetch from '../config/fetch'
const prodEnv = require('../../config/dev.env')
const SERVER_URL = prodEnv.LOGIN_PYT_URL;//映射地址

//用户登录
export const getUser = (name,password) => fetch( SERVER_URL + '/api/user/login', {
    name: name,
    password: password
});

//用户登录(验证验证码)
export const getUserVertified = (code,user_id) => fetch( SERVER_URL + '/api/verification/user_vertify_sms', {
    code: code,
    user_id: user_id
});

//发送验证码
export const verification = (user_id) => fetch( SERVER_URL + '/api/verification/verification', {
    user_id: user_id
});

//验证验证码
export const vertifySMS = (user_id,code) => fetch( SERVER_URL + '/api/verification/vertify_sms', {
    user_id: user_id,
    code: code
});

//获取首页统计数据
export const getGlobalData = () => fetch( SERVER_URL + '/api/user/get_global_statistics', {});

//获取首页统计数据
export const getProductOverview = () => fetch( SERVER_URL + '/api/product/get_product_overview', {});

//获取首页散点图数据
export const getHeatmap = () => fetch( SERVER_URL + '/api/product/get_heatmap', {});