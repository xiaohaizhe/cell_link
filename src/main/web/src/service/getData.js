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
export const queryProduct = (page,number,user_id,name,sort=0) => fetch( SERVER_URL + '/api/product/query', {
    page: page,
    number: number,
    user_id: user_id,     //管理员传0
    name: name,
    sort: sort,            
    
});

//管理员获取用户数据
export const queryUser = (page,number,user_name,isValid=2,create_sort=0,modify_sort=0) => fetch( SERVER_URL + '/api/admin/query_by_uname', {
    page,number,user_name,isValid//2全部，1非禁用，0禁用
    ,create_sort,modify_sort
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
export const addProduct = (name,description,protocolId,lontitude,latitude,userId,cityCode) => fetch( SERVER_URL + '/api/product/add', {
    name,
    description,
    protocolId,
    lontitude,
    latitude,
    userId,
    cityCode
},'POST');

//编辑产品
export const modifyProduct = (name,description,id,lontitude,latitude,userId,cityCode) => fetch( SERVER_URL + '/api/product/modify', {
    name,
    description,
    id,
    lontitude,
    latitude,
    userId,
    cityCode
},'POST');

//我的产品-查看日志
export const getOperationLogs = (user_id,key_word) => fetch( SERVER_URL + '/api/user/get_operation_logs', {user_id,key_word});

//获取接入协议
export const getProtocols = () => fetch( SERVER_URL + '/api/product/get_protocols', {});

//获取产品详情：设备/数据流/触发信息
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
export const modifyPwd = (id,pwd,phone,name,email) => fetch( SERVER_URL + '/api/user/modify', {id,pwd,phone,name,email},'POST');

//管理员添加用户
export const addUser = ({name,phone,email}) => fetch( SERVER_URL + '/api/admin/add', {
    name,phone,email
},'POST');

//管理员编辑用户
export const modifyUser = ({id,name,phone,email}) => fetch( SERVER_URL + '/api/admin/modify', {
    id,name,phone,email
},'POST');

//管理员-用户详情-列表查看详情
export const getProductOverview = (product_id) => fetch( SERVER_URL + '/api/product/get_product_overview', {product_id});

//管理员更改用户账号有效性
export const changeValid = (user_id,admin_name) => fetch( SERVER_URL + '/api/admin/change_effectiveness', {user_id,admin_name});

//管理员-用户详情-（根据设备id或名称模糊）查询产品下设备
export const queryDevice = (page,number,device_snOrName,product_id,start,end,status=2) => fetch( SERVER_URL + '/api/device/query_by_sn_or_name', {page,number,device_snOrName,product_id,start,end,status});

//获取设备下数据流列表
export const getDevicedslist = (id,page,number) => fetch( SERVER_URL + '/api/device/get_devicedslist', {id,page,number});

//获取设备数据流的数据列表
export const getDeviceDS = (dd_id,start,end) => fetch( SERVER_URL + '/api/device/get_device_ds_data', {dd_id,start,end});

//获取应用详情
export const getAppDetail = (app_id) => fetch( SERVER_URL + '/api/application/get_chart_app_detail', {app_id});

//产品概括——设备趋势分析
export const getDevIncrement = (product_id,start,end) => fetch( SERVER_URL + '/api/device/get_increment', {product_id,start,end});

//产品概括——数据流趋势分析
export const getDsmIncrement = (product_id,start,end) => fetch( SERVER_URL + '/api/dsm/get_increment', {product_id,start,end});

//产品概括——触发器趋势分析
export const getTriggerIncrement = (product_id,start,end) => fetch( SERVER_URL + '/api/trigger/get_totalincrement', {product_id,start,end});

//添加设备
export const addDev = (name,product_id,device_sn) => fetch( SERVER_URL + '/api/device/add', {name,product_id,device_sn},'POST');

//修改设备
export const modifyDev = ({name,device_sn,id}) => fetch( SERVER_URL + '/api/device/modify', {name,device_sn,id},'POST');

//删除设备
export const deleteDev = (device_id) => fetch( SERVER_URL + '/api/device/delete', {device_id});

//获取设备的触发器
export const getAssociatedTriggers = (device_id,page,number) => fetch( SERVER_URL + '/api/trigger/get_associated_triggers', {device_id,page,number});

//获取产品下全部触发器
export const getByName = (product_id,page,number,name) => fetch( SERVER_URL + '/api/trigger/get_by_name', {product_id,page,number,name});

//获取设备下触发器概览
export const getTriggersOv = (device_id) => fetch( SERVER_URL + '/api/trigger/get_associated_triggers_overview', {device_id});

//向设备下发命令
export const sendCmd = ({topic,content,type,userid}) => fetch( SERVER_URL + '/api/command/sendcmd', {topic,content,type,userid});

//获取设备下发命令日志
export const getCmdLogs = (page,number,device_id) => fetch( SERVER_URL + '/api/device/get_cmd_logs', {page,number,device_id});

//新增数据流
export const addDs = ({product_id,name,unit_name,unit_symbol}) => fetch( SERVER_URL + '/api/dsm/add', {product_id,name,unit_name,unit_symbol},'POST');

//编辑数据流
export const modifyDs = ({id,product_id,name,unit_name,unit_symbol}) => fetch( SERVER_URL + '/api/dsm/modify', {id,product_id,name,unit_name,unit_symbol},'POST');

//删除数据流
export const deleteDs = (id) => fetch( SERVER_URL + '/api/dsm/delete', {id});

//（根据应用名模糊）查询产品下图表应用
export const getApp = (product_id,app_name) => fetch( SERVER_URL + '/api/application/get_by_name', {product_id,app_name});

//模糊查询产品下数据流
export const getDsData = (page,number,dsmName,productId) => fetch( SERVER_URL + '/api/dsm/find_by_name', {page,number,dsmName,productId});

//验证触发器给定邮箱
export const vertifyForTrigger = (id,code) => fetch( SERVER_URL + '/api/verification/vertify_for_trigger', {id,code});

//添加触发器（关联一条设备数据流作为触发模板）
export const addTrigger = (name,productId,triggerTypeId,criticalValue,triggerMode,modeValue,deviceId,datastreamId) => fetch( SERVER_URL + '/api/trigger/add', {name,productId,triggerTypeId,criticalValue,triggerMode,modeValue,deviceId,datastreamId},'POST');

//修改触发器
export const modifyTrigger = (id,name,triggerTypeId,criticalValue,triggerMode,modeValue,deviceId,
    datastreamId,productId) => fetch( SERVER_URL + '/api/trigger/modify', 
    {id,name,triggerTypeId,criticalValue,triggerMode,modeValue,deviceId,datastreamId,productId},'POST');

//删除触发器
export const deleteTrigger = (id) => fetch( SERVER_URL + '/api/trigger/delete', {id});

//获取产品下全部设备列表
export const getDevicelist = (productId) => fetch( SERVER_URL + '/api/device/get_devicelist', {productId});

//获取产品下全部数据流列表
export const getDslist = (id) => fetch( SERVER_URL + '/api/device/get_deviceds_by_deviceid', {id});

//获取图表类型
export const getChartTypes = () => fetch( SERVER_URL + '/api/application/get_chart_types', {});

//添加图表应用
export const addChartApp = (productId,name,applicationChartList) => fetch( SERVER_URL + '/api/application/add_chart_app', {productId,name,applicationChartList},'POST');

//编辑图表应用
export const modifyChartApp = (id,name,applicationChartList) => fetch( SERVER_URL + '/api/application/modify_chart_app', {id,name,applicationChartList},'POST');

//获取触发器关联设备
export const getAssociatedDevices = (trigger_id,page,number,sort,name) => fetch( SERVER_URL + '/api/trigger/get_associated_devices', {trigger_id,page,number,sort,name});

//获取触发器未关联设备
export const getNotAssociatedDevices = (trigger_id,page,number,product_id,sort,name) => fetch( SERVER_URL + '/api/trigger/get_not_associated_devices', {trigger_id,page,number,product_id,sort,name});

//删除应用
export const delApp = (id) => fetch( SERVER_URL + '/api/application/del_app', {id});

//获取应用详情
export const getAppChart = (app_id) => fetch( SERVER_URL + '/api/application/get_app_detail', {app_id});

//触发器关联产品下所有未关联设备
export const associateAll = (trigger_id) => fetch( SERVER_URL + '/api/trigger/trigger_associated_all_device', {trigger_id});

//触发器取消关联所有已关联设备
export const disassociateAll = (trigger_id) => fetch( SERVER_URL + '/api/trigger/trigger_disconnected_all_device', {trigger_id});

//触发器与设备取消关联
export const disassociate = (trigger_id,device_id) => fetch( SERVER_URL + '/api/trigger/trigger_disconnected_device', {trigger_id,device_id});

//触发器与设备关联
export const associate = (trigger_id,device_id) => fetch( SERVER_URL + '/api/trigger/trigger_associated_device', {trigger_id,device_id});

//添加智能分析应用
export const addApp = (productId,name,applicationType,analysisDatastreams) => fetch( SERVER_URL + '/api/application/add_analysis_app', {productId,name,applicationType,analysisDatastreams},'POST');

//获取触发器图表数据
export const getTriggerChart = (trigger_id,start,end) => fetch( SERVER_URL + '/api/trigger/get_increment', {trigger_id,start,end});

//获取设备统计
export const getDevStatus = (productId) => fetch( SERVER_URL + '/api/device/get_device_status', {productId});

//获取产品下的数据流列表
export const queryDs = (product_id,start,end,type,dsNameOrDeviceName,page,number) => fetch( SERVER_URL + '/api/dsm/query_by_dsname_or_devicename', {product_id,start,end,type,dsNameOrDeviceName,page,number});

//获取数据流统计
export const getDsStatus = (dd_id) => fetch( SERVER_URL + '/api/dsm/get_status', {dd_id});

//获取数据流日志
export const getDsStatusLogs = (dd_id) => fetch( SERVER_URL + '/api/device/get_ds_status_logs', {dd_id});

//验证用户名
export const vertifyName = (name) => fetch( SERVER_URL + '/api/user/vertify_name', {name});

//重置用户密码
export const resetPwd = (user_id) => fetch( SERVER_URL + '/api/user/reset_pwd', {user_id});