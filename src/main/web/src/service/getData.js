import fetch from '../config/fetch'
const prodEnv = require('../../config/dev.env')
const SERVER_URL = prodEnv.LOGIN_SERVER_URL;//映射地址

//普通用户登录
export const getUser = (name,password) => fetch( SERVER_URL + '/api/user/login', {
    name: name,
    password: password
});

//管理员登录
export const getAdmin = (name,pwd) => fetch( SERVER_URL + '/api/admin/login', {
    name,
    pwd
});

//普通用户登出
export const logout = (id) => fetch( SERVER_URL + '/api/user/logout', {
    id
});

//管理员登出
export const adminLogout = (name) => fetch( SERVER_URL + '/api/admin/logout', {
    name
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

//普通用户获取我的产品数据
export const queryProduct = (page,number,user_id,sort=0,name) => fetch( SERVER_URL + '/api/product/query', {
    page: page,
    number: number,
    user_id: user_id,     //管理员传0
    sort: sort,            
    name: name
});

//管理员获取用户数据
export const queryUser = (page,number,user_name,isValid=2) => fetch( SERVER_URL + '/api/admin/query_by_uname', {
    page,number,user_name,isValid//2全部，1非禁用，0禁用
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

//我的产品-查看日志
export const getOperationLogs = (user_id,key_word) => fetch( SERVER_URL + '/api/user/get_operation_logs', {user_id,key_word});

//获取接入协议
export const getProtocols = () => fetch( SERVER_URL + '/api/product/get_protocols', {});

//获取产品详情
export const getDetail = (product_id) => fetch( SERVER_URL + '/api/product/get_detail', {product_id});

//个人中心-修改密码-获取手机验证码
export const sendCode = (user_id,phone) => fetch( SERVER_URL + '/api/verification/send_code', {user_id,phone});

//个人中心-修改密码-获取邮箱验证码
export const sendEmail = (user_id,email) => fetch( SERVER_URL + '/api/verification/send_email', {user_id,email});

//个人中心-修改密码-验证手机验证码
export const vertifyCode = (user_id,phone,code) => fetch( SERVER_URL + '/api/verification/vertify_code', {user_id,phone,code});

//个人中心-修改密码-验证邮箱验证码
export const vertifyEmail = (user_id,email,code) => fetch( SERVER_URL + '/api/verification/vertify_email', {user_id,email,code});

//个人中心-修改密码-修改密码
export const modifyPwd = (id,pwd,phone) => fetch( SERVER_URL + '/api/user/modify', {id,pwd,phone},'POST');

//管理员添加用户
export const addUser = ({name,pwd,phone,email}) => fetch( SERVER_URL + '/api/admin/add', {
    name,pwd,phone,email
},'POST');

//管理员编辑用户
export const modifyUser = ({id,name,pwd,phone,email}) => fetch( SERVER_URL + '/api/admin/modify', {
    id,name,pwd,phone,email
},'POST');

//管理员-用户详情-列表查看详情
export const getProductOverview = (product_id) => fetch( SERVER_URL + '/api/product/get_product_overview', {product_id});

//管理员更改用户账号有效性
export const changeValid = (user_id,admin_name) => fetch( SERVER_URL + '/api/admin/change_effectiveness', {user_id,admin_name});

