import EditAppChartComponent from 'components/dialogs/editAppChart'

const EditAppChart = {}
// 注册Loading
EditAppChart.install = function (Vue) {
    // 生成一个Vue的子类
    // 同时这个子类也就是组件
    Vue.prototype.$editAppChart = {}
    Vue.prototype.$editAppChart.show = (config) => {
        const EditAppChartConstructor = Vue.extend(EditAppChartComponent)
        // 生成一个该子类的实例
        const instance = new EditAppChartConstructor()
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
    Vue.prototype.$editAppChart.hide = () => {
        // document.body.removeChild(document.querySelector('#custom-loading'))
    }
}

export default EditAppChart