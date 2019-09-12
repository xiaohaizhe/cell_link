import AddDevGroupComponent from 'components/dialogs/addDevGroup'

const AddDevGroup = {}
// 注册Loading
AddDevGroup.install = function (Vue) {
    // 生成一个Vue的子类
    // 同时这个子类也就是组件
    Vue.prototype.$addDevGroup = {}
    Vue.prototype.$addDevGroup.show = (config) => {
        const AddDevGroupConstructor = Vue.extend(AddDevGroupComponent)
        // 生成一个该子类的实例
        const instance = new AddDevGroupConstructor()
        // 并将此div加入全局挂载点内部
        document.body.appendChild(instance.$mount().$el)
        instance.userId = config.userId
        if(config.scenarioId){
            instance.ruleForm.scenario.scenarioId = config.scenarioId
        }
        instance.visible = true
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
    Vue.prototype.$addDevGroup.hide = () => {
        // document.body.removeChild(document.querySelector('#custom-loading'))
    }
}

export default AddDevGroup