import request from '@/utils/request'

//用户

//登陆
export function login(query) {
  return request({
    url: '/user/login',
    method: 'get',
    params: query
  })
}
//用户或管理员登出系统
export function logout(query) {
  return request({
    url: '/api/user/logout',
    method: 'get',
    params: query
  })
}
//发送验证码
export function sendCode(user_id,phone) {
  return request({
    url: '/api/user/sms_phone',
    method: 'get',
    params: {
      user_id,phone
    }
  })
}

//根据验证码验证用户手机号
export function vertifyCode(user_id,phone,code) {
  return request({
    url: '/api/user/vertify_phone',
    method: 'get',
    params: {
      user_id,phone,code
    }
  })
}

//用户个人中心修改用户：密码、手机号、邮箱
export function modifyUser(data) {
  return request({
    url: '/api/user/modify',
    method: 'POST',
    headers:{
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    data
  })
}
