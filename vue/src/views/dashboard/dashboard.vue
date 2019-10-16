<template>
    <div class="dashboard">
        <cl-card class="mgbot-15" :totalData="totalData"></cl-card>
        <div class="mgbot-15">
            <p class="mgR-20 mgbot-15  fullWidth">设备趋势分析</p>
            <div class="mgR-20 fullWidth bgWhite fullHeight">
                <div class="cl-flex mgbot-15" >
                    <el-radio-group v-model="devRadio" class="mgR-20" @change="devChange">
                        <el-radio-button label="0">本月</el-radio-button>
                        <el-radio-button label="1">本周</el-radio-button>
                    </el-radio-group>
                    <el-date-picker v-model="devTime" type="daterange" range-separator="至"
                        start-placeholder="开始日期" @change='dateChange($event)'
                        end-placeholder="结束日期"> 
                    </el-date-picker>
                </div>
                <line-chart chartId="devTrend" ref="devTrend"></line-chart>
            </div>
        </div>
        <div>
            <p class="fullWidth mgbot-15 ">设备异常总览</p>
            <div class="bgWhite fullWidth ">
                <pie-chart chartId="devAbnormal" ref="devAbnormal"></pie-chart>
            </div>
        </div>
        <!-- <p class="mgbot-15">事件触发量</p>
        <div class="fullWidth bgWhite" style="flex-basis: 31%;">
            <div class="cl-flex mgbot-15" >
                <el-radio-group v-model="devRadio" class="mgR-20">
                    <el-radio-button label="0">本月</el-radio-button>
                    <el-radio-button label="1">本周</el-radio-button>
                </el-radio-group>
                <el-date-picker v-model="devTime" type="daterange" range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"> 
                </el-date-picker>
            </div>
            <line-chart chartId="trigger" ref="trigger"></line-chart>
              
        </div> -->
    </div>    
</template>

<script>
    import clCard from 'components/card/card'
    import lineChart from './children/lineChart'
    import pieChart from './children/pieChart'
    import {getOverview,getDevIncrement} from 'api/dev'
    import { mapGetters } from 'vuex'
    import { dateFormat,showMonthFirstDay,showMonthLastDay,getDay } from '@/utils/mUtils'
    export default {
        name: 'dashboard',
        data () {
        return {
                devRadio:'0',
                devTime:'',
                start:'',
                end:'',
                height:'',
                totalData:[{
                        name:'设备组数量',
                        value:0,
                        class:'productNum'
                    },{
                        name:'应用数量',
                        value:0,
                        class:"appNum"
                    }
                ],
                thisMonth:[],
                thisWeek:[]
            }
        },
        components:{
            clCard,
            lineChart,
            pieChart
        },
        computed: {
            ...mapGetters([
                'user'
            ])
        },
        mounted(){
            this.initTime()
            this.getOverview()
            this.getDevIncrement()
        },
        methods:{
            async getOverview(){
                let resp = await getOverview(this.user.userId);
                this.totalData[0].value = resp.data.dgSum;
                this.totalData[1].value = resp.data.appSum;
                this.$refs.devAbnormal.drawChart(resp.data.device);
            },
            async getDevIncrement(){
                let resp = await getDevIncrement(this.user.userId,this.start,this.end);
                this.$refs.devTrend.drawChart(resp.data,'#3BBAF0');
            },
            initTime(){
                this.thisMonth[0] = showMonthFirstDay();
                this.thisMonth[1] = showMonthLastDay();
                let d = new Date().getDay()||7;
                this.thisWeek[0] = getDay(1-d);
                this.thisWeek[1] = getDay(7-d);
                this.start = this.thisMonth[0];
                this.end = this.thisMonth[1];
            },
            devChange(val){
                if(val=="1"){
                    this.start = this.thisWeek[0];
                    this.end = this.thisWeek[1];
                    this.getDevIncrement()
                }else{
                    //本月
                    this.start = this.thisMonth[0];
                    this.end = this.thisMonth[1];
                    this.getDevIncrement()
                }
                this.devTime='';
            },
            dateChange(date){
                this.start = dateFormat(date[0]);
                this.end  = dateFormat(date[1]);
                this.getDevIncrement();
                this.devRadio='';
            },
        }
    }
</script>
<style>
    
    .dashboard .bgWhite{
        padding: 20px;
    }
</style>