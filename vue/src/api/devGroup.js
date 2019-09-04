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