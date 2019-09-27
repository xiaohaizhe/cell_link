import EditAppComponent from 'components/dialogs/editApp'

const EditApp = {}
// 注册Loading
EditApp.install = function (Vue) {
    // 生成一个Vue的子类
    // 同时这个子类也就是组件
    Vue.prototype.$editApp = {}
    Vue.prototype.$editApp.show = (config) => {
        const EditAppConstructor = Vue.extend(EditAppComponent)
        // 生成一个该子类的实例
        const instance = new EditAppConstructor()
        // 并将此div加入全局挂载点内部
        document.body.appendChild(instance.$mount().$el)
        instance.appData = config.appData
        instance.ruleForm.appName = config.appData.appName
        instance.ruleForm.description = config.appData.description
        instance.visible = true
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
    Vue.prototype.$editApp.hide = () => {
        // document.body.removeChild(document.querySelector('#custom-loading'))
    }
}

export default EditApp