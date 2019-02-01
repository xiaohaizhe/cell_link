import fetch from '../config/fetch'
const prodEnv = require('../../config/dev.env')
const SERVER_URL = prodEnv.LOGIN_SERVER_URL;//映射地址

//用户登录
export const getUser = (name,password) => fetch( SERVER_URL + '/api/user/login', {
    name: name,
    password: password
});

//用户登出
export const logout = (id) => fetch( SERVER_URL + '/api/user/logout', {
    id
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

//获取首页全站统计数据
export const getGlobalData = () => fetch( SERVER_URL + '/api/user/get_global_statistics', {});

//获取首页产品数据
export const getProductQuantity = (user_id) => fetch( SERVER_URL + '/api/user/get_product_quantity', {
    user_id:user_id
});

//获取首页散点图数据
export const getHeatmap = () => fetch( SERVER_URL + '/api/product/get_heatmap', {});

//获取我的产品数据
export const queryProduct = (page,number,user_id,sort=0,name) => fetch( SERVER_URL + '/api/product/query', {
    page: page,
    number: number,
    user_id: user_id,
    sort: sort,
    name: name
});

//全部删除产品
export const deleteByUserId = (user_id) => fetch( SERVER_URL + '/api/product/deleteByUserId', {
    user_id: user_id
});

//一个或者多个删除产品
export const deleteProducts = (product_ids) => fetch( SERVER_URL + '/api/product/delete', {
    product_ids: product_ids
});

//添加产品
export const addProduct = (name,description,protocolId,longitude,altitude,userId,cityCode) => fetch( SERVER_URL + '/api/product/add', {
    name,
    description,
    protocolId,
    longitude,
    altitude,
    userId,
    cityCode
},'POST');

//编辑产品
export const modifyProduct = (name,description,id,longitude,altitude,userId,cityCode) => fetch( SERVER_URL + '/api/product/modify', {
    name,
    description,
    id,
    longitude,
    altitude,
    userId,
    cityCode
},'POST');

//获取接入协议
export const getProtocols = () => fetch( SERVER_URL + '/api/product/get_protocols', {});

//获取产品详情
export const getDetail = (product_id) => fetch( SERVER_URL + '/api/product/get_detail', {product_id});