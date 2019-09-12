import AddAppComponent from 'components/dialogs/addApp'

const AddApp = {}
// 注册Loading
AddApp.install = function (Vue) {
    // 生成一个Vue的子类
    // 同时这个子类也就是组件
    Vue.prototype.$addApp = {}
    Vue.prototype.$addApp.show = (config) => {
        const AddAppConstructor = Vue.extend(AddAppComponent)
        // 生成一个该子类的实例
        const instance = new AddAppConstructor()
        // 并将此div加入全局挂载点内部
        document.body.appendChild(instance.$mount().$el)
        instance.userId = config.userId
        instance.visible = true
        if(config.scenarioId){
            instance.ruleForm.scenario.scenarioId = config.scenarioId
        }
        instance.findListByUser();
        instance.okClick = config.onOk ? function (scenarioId) {
            instance.visible = false
            config.onOk(scenarioId)
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
    Vue.prototype.$addApp.hide = () => {
        // document.body.removeChild(document.querySelector('#custom-loading'))
    }
}

export default AddApp