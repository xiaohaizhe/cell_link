<template>
       <div class="bg-fff flexAround" style="padding: 4%;width: 100%;height:100%">
            <div v-for="item in appDatas" :key="item.id" >
                <div v-for="(chart,index) in item.applicationChartDatastreamList" :key="chart.id" class="flexAround" >
                    <bar-chart :chartId="`chart1${chart.chart_id+'-'+index}`" :data="chart.dd_data" v-if="item.chartId==2" class="chart"></bar-chart>
                    <line-chart :chartId="`chart${chart.chart_id+'-'+index}`" :data="chart.dd_data" v-if="item.chartId==1" class="chart"></line-chart>
                </div>
            </div>
        </div>
</template>

<script>
    import lineChart from 'components/charts/lineChart'
    import barChart from 'components/charts/barChart'
    import {getAppChart,delApp} from 'service/getData'

    export default {
        name: 'infoBank',
        data () {
            return {
                appId:0,
                appDatas:[],
            }
        },
        components:{
            'line-chart':lineChart,
            'bar-chart':barChart
        },
        created(){
            this.appId = this.$route.params.appId;
        },
        mounted(){
            this.getAppChart();
        },
        methods:{
            //折线图1，柱状图2
            async getAppChart(){
                let resp = await getAppChart(this.appId);
                if(resp.code==0){
                    this.appDatas = resp.data.applicationChartList;
                }else if(resp.code=="error"){
                    return;
                }else{
                    this.$message({
                        message: "获取数据失败！",
                        type: 'error'
                    });
                }
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