import AddDeviceComponent from 'components/dialogs/addDevice'

const AddDevice = {}
// 注册Loading
AddDevice.install = function (Vue) {
    // 生成一个Vue的子类
    // 同时这个子类也就是组件
    Vue.prototype.$addDevice = {}
    Vue.prototype.$addDevice.show = (config) => {
        const AddDeviceConstructor = Vue.extend(AddDeviceComponent)
        // 生成一个该子类的实例
        const instance = new AddDeviceConstructor()
        // 并将此div加入全局挂载点内部
        document.body.appendChild(instance.$mount().$el)
        instance.userId = config.userId
        instance.visible = true
        instance.findListByUser();
        if(config.scenarioId){
            instance.ruleForm.scenarioId = config.scenarioId
            instance.ruleForm.deviceGroup.dgId = config.dgId
            instance.findListByScenario(config.scenarioId);
        }
        instance.okClick = config.onOk ? function (dgId) {
            instance.visible = false
            config.onOk(dgId)
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
    Vue.prototype.$addDevice.hide = () => {
        // document.body.removeChild(document.querySelector('#custom-loading'))
    }
}

export default AddDevice