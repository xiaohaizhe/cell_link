import request from '@/utils/request'

//应用

//根据场景获取应用分页数据
export function getAppList({scenarioId,page,number,sort,appName}) {
  return request({
    url: '/api/app/findByScenario',
    method: 'GET',
    params:{
        scenarioId,page,number,sort,appName
    }
  })
}

//用户新增应用
export function addApp(data) {
    return request({
      url: '/api/app/add_app',
      method: 'POST',
      data
    })
  }