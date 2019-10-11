import request from '@/utils/request'

//数据流

//根据设备id(和数据流名称模糊)查询数据流分页数据和数据流总览数据
export function findByDevice({deviceId,datastreamName,page,number,sorts}) {
    return request({
      url: '/api/datastream/findByDevice',
      method: 'GET',
      params:{
        deviceId,datastreamName,page,number,sorts
      }
    })
  }

//根据数据流id获取具体时间范围内的数据点数据
export function findByDatastream(datastreamId,start,end) {
  return request({
    url: '/api/datastream/findByDatastream',
    method: 'GET',
    params:{
      datastreamId,start,end
    }
  })
}

//根据id查询数据流详情
export function findByDeviceId(deviceId) {
  return request({
    url: '/api/datastream/findByDeviceId',
    method: 'GET',
    params: {
      deviceId
    }
  })
}

//数据流异常
export function getStatus(ds_id) {
  return request({
    url: '/api/datastream/getStatus',
    method: 'GET',
    params: {
      ds_id
    }
  })
}
//数据流异常日志
export function getStatusLog(ds_id) {
  return request({
    url: '/api/datastream/getStatusLog',
    method: 'GET',
    params: {
      ds_id
    }
  })
}