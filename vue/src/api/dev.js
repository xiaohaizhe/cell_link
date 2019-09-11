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

//用户修改单个设备
export function updateDev(data) {
  return request({
    url: '/api/device/update',
    method: 'POST',
    data
  })
}

//用户删除设备
export function deleteDev(deviceId) {
  return request({
    url: '/api/device/delete',
    method: 'DELETE',
    params:{
      deviceId
    }
  })
}

//根据id查询设备详情
export function findDevById(deviceId) {
  return request({
    url: '/api/device/findById',
    method: 'GET',
    params: {
      deviceId
    }
  })
}

//根据设备名模糊查询或者场景id或者设备组id查询设备分页数据
export function findByDeviceName({deviceName,userId,scenarioId,dgId,status,start,end,page,number,sorts}) {
  return request({
    url: '/api/device/findByDeviceName',
    method: 'GET',
    params:{
      deviceName,userId,scenarioId,dgId,status,start,end,page,number,sorts
    }
  })
}
