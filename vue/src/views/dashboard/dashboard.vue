<template>
    <div class="dashboard cl-flex directColumn fullHeight">
        <cl-card class="mgbot-20"></cl-card>
        <div class="cl-flex">
            <div class="mgR-20 fullWidth">
                <p class="title">设备趋势分析</p>
            </div>
            <div class="fullWidth">
                <p class="title">设备异常总览</p>
            </div>
        </div>
        <div class="cl-flex mgbot-20" style="flex-basis: 31%;"  id="clCard">
            <div class="mgR-20 fullHeight fullWidth">       
                <el-card class="fullHeight">
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
                </el-card>
            </div>
            <div class="fullHeight fullWidth">
                <el-card class="fullHeight">
                    <pie-chart chartId="devAbnormal" ref="devAbnormal"></pie-chart>
                </el-card>
            </div>
        </div>
        <p class="title">事件触发量</p>
        <div class="cl-flex mgbot-10 fullWidth"  style="flex-basis: 31%;">
            <el-card class="fullHeight fullWidth">
                <div class="cl-flex mgbot-15 ">
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
            </el-card>
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
            this.height = document.getElementById("clCard").offsetHeight;
            // debugger
            this.$refs.devTrend.drawChart([],'#3BBAF0',this.height);
            this.$refs.devAbnormal.drawChart([],this.height);
            this.$refs.trigger.drawChart([],'#A3E26B',this.height);
        },
        methods:{
        }
    }
</script>
<style>
    .dashboard .title{
        margin-bottom:0.9375rem;
    }
    .dashboard .el-radio-button__inner{
        padding: 0.437rem 0.937rem;
    }
    .dashboard .el-date-editor{
        height: 1.875rem;
    }
    .dashboard .el-date-editor .el-range__icon,.dashboard .el-date-editor .el-range-separator{
        line-height: 21px;
    }
</style>