<template>
    <div>
        <cl-header headColor="#181818"></cl-header>
        <sub-header title="应用管理" :subtitle="`${appData.name}`" detail="发布" v-on:direct="navDirect"></sub-header>
        <div class="mainContent">
            <div class="flexBtw">
                <p class="font-16 mgbot-20">发布链接：{{outerUrl}}</p>
                <div class="">
                    <el-tooltip class="item" effect="dark" content="编辑" placement="bottom">
                        <i class="editIcon cl-icon" @click="goAddress(false)"></i>
                    </el-tooltip>
                    <el-tooltip class="item" effect="dark" content="删除" placement="bottom">
                        <i class="delete cl-icon" @click="deleteItem(appData.id)"></i>
                    </el-tooltip>
                </div>
            </div>
            <div class="bg-fff flexAround" style="padding: 4%;flex-wrap: wrap;">
                <div v-for="item in appDatas" :key="item.id" >
                    <div v-for="(chart,index) in item.applicationChartDatastreamList" :key="chart.id" class="flexAround">
                        <bar-chart :chartId="`chart1${chart.chart_id+'-'+index}`" :data="chart.dd_data" v-if="item.chartId==2" class="chart"></bar-chart>
                        <line-chart :chartId="`chart${chart.chart_id+'-'+index}`" :data="chart.dd_data" v-if="item.chartId==1" class="chart"></line-chart>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import headTop from 'components/header/head'
    import subHead from 'components/subHeader/subHeader'
    import lineChart from 'components/charts/lineChart'
    import barChart from 'components/charts/barChart'
    import {getAppChart,delApp} from 'service/getData'

    export default {
        name: 'publish',
        data () {
            return {
                direct:"",
                appData:{},
                appDatas:[],
                appId:"0",
                outerUrl:''
            }
        },
        components:{
            'cl-header':headTop,
            'sub-header':subHead,
            'line-chart':lineChart,
            'bar-chart':barChart
        },
        computed:{
        },
        created(){
            this.appId = this.$route.params.appId;
        },
        mounted(){
            this.getAppChart();
            this.outerUrl = window.location.origin + "/#/appOuter/" + this.appId;
        },
        methods: {
            //折线图1，柱状图2
            async getAppChart(){
                let resp = await getAppChart(this.appId);
                if(resp.code==0){
                    this.appData = resp.data;
                    this.name = resp.data.name;
                    this.appDatas = resp.data.applicationChartList;
                }else if(resp.code=="error"){
                    return;
                }else{
                    this.$message({
                        message: "获取数据失败！",
                        type: 'error'
                    });
                }
            },
            //删除app
            deleteItem(id){
                this.$confirm('删除应用后，相关数据流等资源将会被全部删除，且无法恢复。确定要删除设备吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteApp(id);
                })
            },
            async deleteApp(id){
                let resp = await delApp(id);
                if(resp.code==0){
                    this.$message({
                        type: 'success',
                        message: '删除成功!'
                    });
                    this.goAddress(true);
                }else if(resp.code=="error"){
                    return;
                }else{
                    this.$message({
                        type: 'error',
                        message: '删除失败!'+resp.msg
                    });
                }
            },
            navDirect(){
                //加密
                let b = new Buffer(this.appData.productId);
                let s = b.toString('base64');
                let data = encodeURIComponent(s);
                this.$router.push('/myProduct/'+data+'/appManage')
            },
            goAddress(flag){
                //加密
                let b = new Buffer(this.appData.productId);
                let s = b.toString('base64');
                let data = encodeURIComponent(s);
                if(flag){
                    this.$router.push({name:'appManage',params:{productId:data,data:this.appData}})
                }else
                    this.$router.push({name:'appManage',params:{productId:data,data:this.appData,editVisible:true}})
            }                                       
        }

    }
</script>

<style>
    .chart{
        width: 450px;
        margin: 10px;
    }
</style>
