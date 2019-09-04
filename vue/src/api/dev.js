import request from '@/utils/request'

//设备

//用户新增设备
export function addDev(data) {
  return request({
    url: '/api/device/add',
    method: 'POST',
    data
  })
}
