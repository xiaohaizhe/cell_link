<template>
    <div class="dashboard cl-flex directColumn">
        <cl-card class="mgbot-15"></cl-card>
        <div class="cl-flex mgbot-15">
            <p class="mgR-20 fullWidth">设备趋势分析</p>
            <p class="fullWidth">设备异常总览</p>
        </div>
        <div class="cl-flex mgbot-15 fullHeight" style="flex-basis: 34%;">
            <div class="mgR-20 fullWidth bgWhite fullHeight">
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
                <line-chart chartId="devTrend" ref="devTrend"></line-chart>
                </div>
            <div class="bgWhite fullWidth fullHeight">
                <pie-chart chartId="devAbnormal" ref="devAbnormal"></pie-chart>
            </div>
        </div>
        <p class="mgbot-15">事件触发量</p>
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
              
        </div>
    </div>    
</template>

<script>
    import clCard from 'components/card/card'
    import lineChart from './children/lineChart'
    import pieChart from './children/pieChart'

    export default {
        name: 'dashboard',
        data () {
        return {
                devRadio:'0',
                devTime:'',
                height:'',
            }
        },
        components:{
            clCard,
            lineChart,
            pieChart
        },
        mounted(){
            // this.height = document.getElementById("clCard").offsetHeight;
            // debugger
            this.$refs.devTrend.drawChart([],'#3BBAF0');
            this.$refs.devAbnormal.drawChart([]);
            this.$refs.trigger.drawChart([],'#A3E26B');
        },
        methods:{
        }
    }
</script>
<style>
    
    .dashboard .bgWhite{
        padding: 20px;
    }
</style>