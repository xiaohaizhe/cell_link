import request from '@/utils/request'

//用户

//登陆
export function login(query) {
  return request({
    url: '/user/login',
    method: 'GET',
    params: query
  })
}
//用户或管理员登出系统
export function logout(query) {
  return request({
    url: '/api/user/logout',
    method: 'GET',
    params: query
  })
}
//发送验证码
export function sendCode(userId,phone) {
  return request({
    url: '/api/user/sms_phone',
    method: 'GET',
    params: {
      userId,phone
    }
  })
}

//根据验证码验证用户手机号
export function vertifyCode(userId,phone,code) {
  return request({
    url: '/api/user/vertify_phone',
    method: 'GET',
    params: {
      userId,phone,code
    }
  })
}

//用户个人中心修改用户：密码、手机号、邮箱
export function modifyUser(data) {
  return request({
    url: '/api/user/modify',
    method: 'POST',
    data
  })
}

//向邮箱发送激活链接
export function sendEmail(userId,email) {
  return request({
    url: '/api/user/sms_email',
    method: 'GET',
    params: {
      userId,email
    }
  })
}

//管理员用户分页查询
export function findByPage({page,number,sort}) {
  return request({
    url: '/api/user/findByPage',
    method: 'GET',
    params: {
      page,number,sort
    }
  })
}

//管理员重置用户密码：000000
export function reset(userId) {
  return request({
    url: '/api/user/reset',
    method: 'GET',
    params: {
      userId
    }
  })
}

//管理员禁用与授权用户
export function changeValid(userId) {
  return request({
    url: '/api/user/change_effectiveness',
    method: 'GET',
    params: {
      userId
    }
  })
}

//用户数量、设备组、连接数据流、应用数量总览
export function getOverview() {
  return request({
    url: '/user/getOverview',
    method: 'GET',
    params: {
      
    }
  })
}

//管理员新建用户
export function addUser(data) {
  return request({
    url: '/api/user/add',
    method: 'POST',
    data
  })
}

//管理员编辑用户：用户名、手机号、邮箱
export function updateUser(data) {
  return request({
    url: '/api/user/update',
    method: 'POST',
    data
  })
}