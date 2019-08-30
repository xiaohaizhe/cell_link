import request from '@/utils/request'

export function login(query) {
  return request({
    url: '/user/login',
    method: 'get',
    params: query
  })
}