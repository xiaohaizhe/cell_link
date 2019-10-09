<template>
    <el-container class="fullHeight is-vertical">
        <el-header height="70px">
            <div class="cl-flex head">
                <div class="cl-flex alignEnd">
                    <img :src="logo" style="width:40px;height:40px;"/>
                    <span class="font-28 colorWhite" style="margin-left:15px;">智能感知平台</span>
                </div>
            </div>
        </el-header>
        <el-container class="fullHeight">
            <el-main class="fullHeigh">
                <div class="bgWhite topCard cl-flex justifyBet mgbot-10">
                    <div class="detail">
                        <p class="font-24 mgbot-20"><span class="mgR-10">应用名称：{{activeApp.appName}}</span></p>
                        <p class="colorGray font-14 mgbot-20">应用描述：{{activeApp.description}}</p>
                        <p class="colorGray font-14">发布链接：<a :href="url">{{url}} </a></p>
                    </div>
                </div>
                <div class="bgWhite cl-flex flexWrap justifyBet" style="padding:10px" >
                    <li v-for="(item, index) in appChartList" class="appChart"
                        :key="index">
                        <line-chart :chartId="`appLineChart${item.acId}`" :ref="`appLineChart${item.acId}`" :data="item.appDatastreamList" v-if="item.chart==2"></line-chart>
                        <bar-chart :chartId="`appBarChart${item.acId}`" :ref="`appBarChart${item.acId}`" :data="item.appDatastreamList"  v-if="item.chart==1"></bar-chart>
                    </li>
                </div>
            </el-main>
        </el-container>
    </el-container>
</template>
<script>
    import lineChart from './children/lineChart'
    import barChart from './children/barChart'
    import { getChartDetail ,findAppById} from 'api/application'
    export default {
        name: 'publish',
        data () {
        return {
            appChartList:[],
            logo:require('assets/celllink.svg'),
            url:window.location.href,
            activeApp:{}
        }
        },
        components:{
            lineChart,barChart
        },
        mounted(){
            this.findAppById()
            this.getChartDetail()
        },
        methods:{
            async findAppById(){
                let resp = await findAppById(this.$route.params.appId)
                this.activeApp = resp.data;
            },
            async getChartDetail(){
                let resp = await getChartDetail(this.$route.params.appId)
                this.appChartList = resp.data.sort(function(a,b){
                    return a.sequenceNumber - b.sequenceNumber
                })
            },
        }
    }
</script>
<style lang="scss" scoped>
    .el-header{
        padding: 0;
    }
    .head{
        background: #00253B;
        padding: 15px 50px;
        position: fixed;
        justify-content: space-between;
        z-index: 999;
        width: 100%;
    }
    .appChart{
        flex-basis: 49%;
        height: 400px;
        border: 1px solid #f2f2f2;
        margin-bottom: 10px
    }
</style>