import request from '@/utils/request'

//场景

//用户新增场景
export function addScene(data) {
  return request({
    url: '/api/scenario/add',
    method: 'POST',
    data
  })
}
//用户修改场景
export function updateScene(data) {
  return request({
    url: '/api/scenario/update',
    method: 'POST',
    data
  })
}

//根据用户id查询场景列表
export function findListByUser(userId) {
  return request({
    url: '/api/scenario/findListByUser',
    method: 'GET',
    params: {
      userId
    }
  })
}
