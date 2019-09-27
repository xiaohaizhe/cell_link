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

//用户修改设备组
export function updateDevGroup(data) {
  return request({
    url: '/api/dg/update',
    method: 'POST',
    data
  })
}

//用户删除设备组
export function deleteDevGroup(dgId) {
  return request({
    url: '/api/dg/delete',
    method: 'DELETE',
    params:{
      dgId
    }
  })
}

//根据id查询设备组详情
export function findDgById(dgId) {
  return request({
    url: '/api/dg/findById',
    method: 'GET',
    params: {
      dgId
    }
  })
}
//根据场景查询设备组列表
export function findListByScenario(scenarioId) {
  return request({
    url: '/api/dg/findListByScenario',
    method: 'GET',
    params: {
      scenarioId
    }
  })
}

//根据场景查询设备组分页
export function findByScenario({scenarioId,page,number,sorts,deviceGroupName}) {
  return request({
    url: '/api/dg/findByScenario',
    method: 'GET',
    params: {
      scenarioId,page,number,sorts,deviceGroupName
    }
  })
}
