import request from '@/utils/request'

//日志

//根据用户、场景、设备（或下发内容模糊）查询下发数据分页
export function cmdLog(userId,scenarioId,deviceId,page,number,sort,cmd) {
  return request({
    url: '/api/log/cmd',
    method: 'GET',
    params:{
        userId,scenarioId,deviceId,page,number,sort,cmd
    }
  })
}

//操作日志
export function opLog(userId) {
    return request({
      url: '/api/log/opList',
      method: 'GET',
      params:{
          userId
      }
    })
  }