import addUserComponent from 'components/dialogs/addUser'

const addUser = {}
// 注册Loading
addUser.install = function (Vue) {
    // 生成一个Vue的子类
    // 同时这个子类也就是组件
    Vue.prototype.$addUser = {}
    Vue.prototype.$addUser.show = (config) => {
        const addUserConstructor = Vue.extend(addUserComponent)
        // 生成一个该子类的实例
        const instance = new addUserConstructor()
        // 并将此div加入全局挂载点内部
        document.body.appendChild(instance.$mount().$el)
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
    Vue.prototype.$addUser.hide = () => {
        // document.body.removeChild(document.querySelector('#custom-loading'))
    }
}

export default addUser