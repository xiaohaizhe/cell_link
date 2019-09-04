import request from '@/utils/request'

//设备组

//用户新增设备组
export function addDevGroup(data) {
  return request({
    url: '/api/dg/add',
    method: 'POST',
    data
  })
}

