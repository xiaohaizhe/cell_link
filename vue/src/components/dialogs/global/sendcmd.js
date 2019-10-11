import SendCmdComponent from 'components/dialogs/sendcmd'

const SendCmd = {}
// 注册Loading
SendCmd.install = function (Vue) {
    // 生成一个Vue的子类
    // 同时这个子类也就是组件
    Vue.prototype.$sendcmd = {}
    Vue.prototype.$sendcmd.show = (config) => {
        const SendCmdConstructor = Vue.extend(SendCmdComponent)
        // 生成一个该子类的实例
        const instance = new SendCmdConstructor()
        // 并将此div加入全局挂载点内部
        document.body.appendChild(instance.$mount().$el)
        instance.visible = true
        instance.deviceId = config.deviceId
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
    Vue.prototype.$sendcmd.hide = () => {
        // document.body.removeChild(document.querySelector('#custom-loading'))
    }
}

export default SendCmd