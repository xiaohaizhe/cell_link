import AddAppChartComponent from 'components/dialogs/addAppChart'

const AddAppChart = {}
// 注册Loading
AddAppChart.install = function (Vue) {
    // 生成一个Vue的子类
    // 同时这个子类也就是组件
    Vue.prototype.$addAppChart = {}
    Vue.prototype.$addAppChart.show = (config) => {
        const AddAppChartConstructor = Vue.extend(AddAppChartComponent)
        // 生成一个该子类的实例
        const instance = new AddAppChartConstructor()
        // 并将此div加入全局挂载点内部
        document.body.appendChild(instance.$mount().$el)
        instance.appData = config.appData
        instance.visible = true
        instance.getChart()
        instance.findListByScenario()
        instance.okClick = config.onOk ? function () {
            instance.visible = false
            config.onOk()
        } : function () {
            instance.visible = false
        }
        instance.cancelClick = config.onCancel ? function () {
            instance.visible = false
            config.onCancel()
        } : function () {
            instance.visible = false
        }
    }
    Vue.prototype.$addAppChart.hide = () => {
        // document.body.removeChild(document.querySelector('#custom-loading'))
    }
}

export default AddAppChart