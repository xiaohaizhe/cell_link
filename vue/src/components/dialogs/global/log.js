import LogComponent from 'components/dialogs/logs'

const Log = {}
// 注册Loading
Log.install = function (Vue) {
    // 生成一个Vue的子类
    // 同时这个子类也就是组件
    Vue.prototype.$log = {}
    Vue.prototype.$log.show = (config) => {
        const LogConstructor = Vue.extend(LogComponent)
        // 生成一个该子类的实例
        const instance = new LogConstructor()
        // 并将此div加入全局挂载点内部
        document.body.appendChild(instance.$mount().$el)
        instance.datastreamId = config.datastreamId
        instance.visible = true
        instance.getStatusLog()
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
    Vue.prototype.$log.hide = () => {
        // document.body.removeChild(document.querySelector('#custom-loading'))
    }
}

export default Log