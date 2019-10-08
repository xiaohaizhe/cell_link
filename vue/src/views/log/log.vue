<template>
    <div>
        <el-tabs v-model="activeName" @tab-click="onTabClick">
            <el-tab-pane label="下发日志" name="orderLog">
            </el-tab-pane>
            <!-- <el-tab-pane label="触发日志" name="triggerLog">
            </!--> 
            <el-tab-pane label="操作日志" name="opLog">
            </el-tab-pane>
        </el-tabs>
        <router-view></router-view>
    </div>
</template>

<script>
    // import orderLog from './children/orderLog'
    // import triggerLog from './children/triggerLog'
    // import opLog from './children/opLog'

    export default {
        name: 'log',
        data () {
        return {
            activeName: 'orderLog'
            }
        },
        mounted(){
            this.activeName = this.$route.meta.name;
        },
        updated(){
            this.activeName = this.$route.meta.name;
        },
        methods:{
            onTabClick(data){
                this.$router.push('/log/'+data.name)
            },
        }
    }
</script>
<style>
</style>