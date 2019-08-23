<template>
    <div class="dashboard">
        <cl-card class="mgbot-30"></cl-card>
        <el-row :gutter="20">
            <el-col :span="12">
                <p class="title">设备趋势分析</p>
                <!-- <line-chart></line-chart> -->
            </el-col>
            <el-col :span="12">
                <p class="title">设备异常总览</p>
            </el-col>
        </el-row>
    </div>    
</template>

<script>
    import clCard from 'components/card/card'

    export default {
        name: 'dashboard',
        data () {
        return {
            
            }
        },
        components:{
            clCard
        },
    }
</script>
<style>
    .dashboard .title{
        margin-bottom:18px;
    }
    .dashboard .el-col{
        padding-left:0 !important;
    }
</style>