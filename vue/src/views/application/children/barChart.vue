<template>
    <div>
        <!-- <p class="center" v-show="!hasData" style="height:50px;line-height:50px;">暂无数据</p> -->
        <div :id="chartId" style="height:330px;"></div>
    </div>
    
</template>

<script>
    // let echarts = require('echarts/lib/echarts')
    // // 引入柱状图
    // require('echarts/lib/chart/bar');
    // // 提示框
    // require('echarts/lib/component/tooltip')

    export default {
        name: 'barChart',
        data () {
            return {
                // hasData:false
            }
        },
        props: {
            chartId:{
                type:String
            },
            data:{
                type:Array
            }
        },
        watch:{
            data(){
                this.drawChart(this.data);
            }
        },
        mounted(){
            this.drawChart(this.data);
        },
        methods: {
            async drawChart(){
                if(this.data.length==0){
                    this.$message({
                        message: "暂无统计数据",
                        type: 'warning'
                    });
                    return false
                }
                let legend = [];
                let series = [];
                for(let v of this.data){
                    // if(v.datapointList.length>0){
                        legend.push(v.datastreamName)
                        series.push({
                                name: v.datastreamName,
                                type: 'bar',
                                data: v.datapointList,
                                barWidth:'20'
                            },)
                    // }
                }
                let barChart = this.$echarts.init(document.getElementById(this.chartId));
                let option = {
                        color:['#3BBAF0','#FBB02F','#A3E26B','#736BE2','#E26BBE'],
                        tooltip : {
                            trigger: 'axis',
                            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                            }
                        },
                        legend:{
                            top:'10',
                            right:'30',
                            data:legend
                        },
                        xAxis: {
                            type: 'time',
                            // boundaryGap: false,
                            axisLine :{
                                lineStyle:{
                                    color:"#D5DDE5"
                                }
                                
                            },
                            axisTick:{
                                show:false,
                            },
                        },
                        dataZoom: [
                            {
                                type: 'inside',
                            },
                            {
                                type: 'slider',
                            }
                        ],
                        grid:{
                            bottom:"80px"
                        },
                        yAxis : [
                            {
                                type : 'value',
                                axisTick:{
                                    show:false,
                                },
                            }
                        ],
                        series : series
                    };
                barChart.setOption(option,true);
                window.addEventListener('resize', function () {
                    barChart.resize();
                })
                
                
                
            }
        }

    }
</script>

<style>
</style>
