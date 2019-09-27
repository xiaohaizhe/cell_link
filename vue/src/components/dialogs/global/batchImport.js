import batchImportComponent from 'components/dialogs/batchImport'

const batchImport = {}
// 注册Loading
batchImport.install = function (Vue) {
    // 生成一个Vue的子类
    // 同时这个子类也就是组件
    Vue.prototype.$batchImport = {}
    Vue.prototype.$batchImport.show = (config) => {
        const batchImportConstructor = Vue.extend(batchImportComponent)
        // 生成一个该子类的实例
        const instance = new batchImportConstructor()
        // 并将此div加入全局挂载点内部
        document.body.appendChild(instance.$mount().$el)
        instance.userId = config.userId
        instance.token = config.token
        instance.visible = true
        instance.findListByUser();
        if(config.scenarioId){
            instance.ruleForm.scenarioId = config.scenarioId
            instance.ruleForm.dgId = config.dgId
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
    Vue.prototype.$batchImport.hide = () => {
        // document.body.removeChild(document.querySelector('#custom-loading'))
    }
}

export default batchImport