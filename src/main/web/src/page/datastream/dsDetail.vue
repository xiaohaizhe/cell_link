<template>
    <div>
        <cl-header headColor="#181818"></cl-header>
        <sub-header title="数据流管理" :subtitle="`${dsData.name}-详情`" v-on:direct="navDirect"></sub-header>
        <div class="mainContent">
            <div class="notice cl-flex bg-fff">
                <div class="wid50 devOv">
                    <div class="cl-progress">
                        <v-progress-linear
                        background-color="#1E94A0"
                        color="#004E6D"
                        height="50"
                        :value="sumData.value<15 && sumData.value!=0?85:100-sumData.value"
                        >
                        </v-progress-linear>
                        <div class="cl-flex">
                            <div :style="{width: (sumData.value>80 || sumData.value<20) ? '80%' :`${100-sumData.value}%`}">
                                <p>总数据点</p>
                                <p><span class="font-24">{{sumData.sum}}</span>个</p>
                            </div>
                            <div :style="{width:(sumData.value>80 || sumData.value<20) ?  '20%' : `${sumData.value}%`}">
                                <p>新增数据点</p>
                                <p><span class="font-24">{{sumData.sum_new}}</span>个</p>
                            </div>
                        </div>
                    </div>
                    <p class="center">新增数据流情况</p>
                </div>
                <div class="wid50 center" style="padding:1rem 3rem;">
                    <pie-chart ref="pieChart"></pie-chart>
                    <p>最近100个数据流情况</p>
                </div>
            </div>
            <p class="font-16">基本信息</p>
            <div class="bg-fff cl-card flexBtw mgbot-30" style="margin-top:1.43rem;align-items: center;">
                <div class="cl-flex">
                    <div class="report cl-cardIcon"></div>
                    <div>
                        <p class="font-18 colorBlack mgbot-20">{{dsData.name}}</p>
                        <div>
                            <p class="colorGray">最新更新时间：{{dsData.date}}</p>
                            <p class="colorGray">所属设备：{{dsData.device_name}}</p>
                        </div>
                    </div>
                </div>
                <el-button type="primary" @click="goAddress('streamShow')">查看设备</el-button>
            </div>
            <p class="font-16">数据点图</p>
            <div class=" notice bg-fff" style="padding:2.14rem 40px;margin-top:1.43rem;">
                <div class="cl-flex" style="margin-bottom:40px">
                    <el-date-picker v-model="time" type="daterange" range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期" @change='dateChange'> 
                    </el-date-picker>
                    <el-button @click="getDeviceDS()" style="margin-left:2rem">查看最新</el-button>
                </div>
                <div>
                    <dsChart ref="dsChart" chartId="dsChart"></dsChart>
                </div>
            </div>
            <div class="flexBtw">
                <p class="font-16">最近100个数据点异常情况</p>
                <el-button type="primary"  @click="showDialog">异常日志</el-button>
            </div>
            <div class=" notice bg-fff" style="padding:2.14rem 40px;margin-top:1.43rem;">
                <div>
                    <dsChartAb ref="dsChartAb" chartId="dsChartAb"></dsChartAb>
                </div>
            </div>
        </div>
        <logs :dialogVisible="dialogVisible" :userId="dsData.dd_id" :flag="false" @getDialogVisible="setDialogVisible" v-if='dialogVisible'></logs>
    </div>
</template>

<script>
    import headTop from 'components/header/head'
    import subHead from 'components/subHeader/subHeader'
    import pieChart from 'components/charts/pieChart'
    import dsChart from 'components/charts/dsChart'
    import dsChartAbnormal from 'components/charts/dsChartAbnormal'
    import {getDsStatus,getDeviceDS,getDsStatusLogs} from 'service/getData'
    import {dateFormat} from 'config/mUtils'
    import logs from 'components/dialogs/logs'

    export default {
        name: 'dsDetail',
        data () {
        return {
                logData:[],
                direct:'',
                dsData:{},
                time:'',
                sumData:{value:0},
                dialogVisible: false
            }
        },
        components:{
            'cl-header':headTop,
            'sub-header':subHead,
            'pie-chart':pieChart,
            'dsChart':dsChart,
            'dsChartAb':dsChartAbnormal,
            'logs':logs
        },
        computed:{
        },
        mounted(){
            //解密
            var x = new Buffer(decodeURIComponent(this.$route.params.data), 'base64')
            var y = x.toString('utf8');
            this.dsData = JSON.parse(y);
            //加密
            let b = new Buffer(JSON.stringify(this.dsData.productId));
            let s = b.toString('base64');
            let data = encodeURIComponent(s);
            this.direct= '/myProduct/'+data+'/dsManage';

            this.getDsStatus();
            this.getDeviceDS();
        },
        methods: {
            //获取数据流统计数据
            async getDsStatus(){
                let resp = await getDsStatus(this.dsData.dd_id);
                if(resp.code==0){
                    this.sumData = resp.data;
                    this.sumData.value = resp.data.sum_new/resp.data.sum*100;
                    this.$refs.pieChart.drawChart(resp.data);
                    this.$refs.dsChartAb.drawChart(resp.data.six_hours_data);
                }else if(resp.code=="error"){
                    return;
                }else{
                    this.$message({
                        message: "获取统计数据失败！",
                        type: 'error'
                    });
                }
            },
            //获取数据流数据
            async getDeviceDS(start=dateFormat(new Date(),' 00:00:00'),end = dateFormat(new Date(),' 23:59:59')){
                this.time = [start,end];
                let resp = await getDeviceDS(this.dsData.dd_id,start,end);
                if(resp.code==0){
                    this.$refs.dsChart.drawChart(resp.data);
                }else if(resp.code=="error"){
                    return;
                }else{
                    this.$alert('获取设备数据流失败', '提示', {
                        confirmButtonText: '确定',
                        callback: action => {
                        }
                    });
                }
            },
            //时间改变时间
            dateChange(date){
                let start = dateFormat(date[0],' 00:00:00');
                let end  = dateFormat(date[1],' 23:59:59');
                this.getDeviceDS(start,end);
            },
            //导航定向
            navDirect(){
                this.$router.push(this.direct)
            },
            showDialog(){
                this.dialogVisible = true;  //点击button时，设值为true，触发动态绑定的:isDialogVisible
            },
            setDialogVisible(val){
                this.dialogVisible = val;
            },
            //路由跳转
            goAddress(url){
                let temp = {...this.dsData};
                temp.id = temp.device_id;
                temp.name = temp.device_name;
                //加密
                let b = new Buffer(JSON.stringify(temp));
                let s = b.toString('base64');
                let data = encodeURIComponent(s);
                this.$router.push({path:'/'+url+'/'+data})
            }
        }

    }
</script>

<style>
</style>
