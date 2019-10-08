<template>
        <div :id="chartId" style="height:330px"></div>
</template>

<script>
    // let echarts = require('echarts/lib/echarts')
    // // 引入折线图
    // require('echarts/lib/chart/line');
    // // 提示框
    // require('echarts/lib/component/tooltip')

    export default {
        name: 'lineChart',
        data () {
            return {
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
        mounted(){
            // let height = document.getElementById("clMain").offsetHeight*0.2;
            // if(height>200){
            //     this.height = height+'px';
            // }
            this.drawChart();
        },
        methods: {
            drawChart(){
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
                    legend.push(v.datastreamName)
                    series.push({
                            name: v.datastreamName,
                            type: 'line',
                            data: v.datapointList,
                            showSymbol: false,
                            symbolSize:2,
                        })
                }
                let chartDom = document.getElementById(this.chartId);
                let color = ['#3BBAF0','#FBB02F','#A3E26B','#736BE2','#E26BBE'];
                
                let lineChart = this.$echarts.init(chartDom);
                let option = {
                        tooltip: {
                            trigger: 'axis',
                            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                            }
                        },
                        legend:{
                            top:'20',
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
                        yAxis: {
                            type: 'value',
                            axisLine :{
                                show:false,
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
                        series:series
                    };
                lineChart.setOption(option,true);
                window.addEventListener('resize', function () {
                    lineChart.resize();
                })
                
                
                
            }
        }

    }
</script>

<style>
</style>
