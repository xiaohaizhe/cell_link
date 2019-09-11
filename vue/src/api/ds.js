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