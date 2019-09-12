import AddSceneComponent from 'components/dialogs/addScene'

const AddScene = {}
// 注册Loading
AddScene.install = function (Vue) {
    // 生成一个Vue的子类
    // 同时这个子类也就是组件
    Vue.prototype.$addScene = {}
    Vue.prototype.$addScene.show = (config) => {
        const AddSceneConstructor = Vue.extend(AddSceneComponent)
        // 生成一个该子类的实例
        const instance = new AddSceneConstructor()
        // 并将此div加入全局挂载点内部
        document.body.appendChild(instance.$mount().$el)
        instance.visible = true
        instance.userId = config.userId
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
    Vue.prototype.$addScene.hide = () => {
        // document.body.removeChild(document.querySelector('#custom-loading'))
    }
}

export default AddScene