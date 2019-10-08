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

//用户修改应用
export function editApp(data) {
  return request({
    url: '/api/app/update_app',
    method: 'POST',
    data
  })
}

//用户删除应用
export function deleteApp(appId) {
  return request({
    url: '/api/app/delete_app',
    method: 'DELETE',
    params:{
      appId
    }
  })
}


//根据id获取应用详情
export function findAppById(appId) {
  return request({
    url: '/api/app/findById',
    method: 'GET',
    params:{
      appId
    }
  })
}

//添加应用图表
export function addAppChart(data) {
  return request({
    url: '/api/app/add_chart',
    method: 'POST',
    data
  })
}

//获取图表类型
export function getChart() {
  return request({
    url: '/api/app/getChart',
    method: 'GET',
    params:{
      
    }
  })
}

//获取图表类型
export function getChartDetail(appId) {
  return request({
    url: '/api/app/findDetailById',
    method: 'GET',
    params:{
      appId
    }
  })
}