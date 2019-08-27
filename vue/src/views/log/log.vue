<template>
    <div>
        <el-tabs v-model="activeName" >
            <el-tab-pane label="下发日志" name="orderLog">
                <order-log></order-log>
            </el-tab-pane>
            <el-tab-pane label="触发日志" name="triggerLog">
                <trigger-log></trigger-log>
            </el-tab-pane>
            <el-tab-pane label="操作日志" name="opLog">
                <op-log></op-log>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script>
    import orderLog from './children/orderLog'
    import triggerLog from './children/triggerLog'
    import opLog from './children/opLog'

    export default {
        name: 'log',
        data () {
        return {
            activeName: 'orderLog'
            }
        },
        components:{
            orderLog,
            triggerLog,
            opLog
        },
        mounted(){
        },
        methods:{
        }
    }
</script>
<style>
</style>