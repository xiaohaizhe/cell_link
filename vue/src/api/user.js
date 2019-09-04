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