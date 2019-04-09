<template>
    <div>
        <!-- <p class="center" v-show="!hasData" style="height:50px;line-height:50px;">暂无数据</p> -->
        <div :id="chartId" style="width:400px;height:450px;margin:0 auto"></div>
    </div>
    
</template>

<script>
    // let echarts = require('echarts/lib/echarts')
    // // 引入柱状图
    // require('echarts/lib/chart/heatmap');
    // // 提示框
    // require('echarts/lib/component/tooltip')

    export default {
        name: 'heatmapChart',
        data () {
            return {
            }
        },
        props: {
            chartId:{
                type:String
            },
            // data:{
            //     type:Array
            // }
        },
        mounted(){
        },
        methods: {
            async drawChart(labels,data){
                if(!data){
                    this.$message({
                        message: "暂无统计数据",
                        type: 'warning'
                    });
                }
                for(let i=0;i<data.length;i++){
                    data[i][2] = data[i][2].toFixed(2);
                }
                let clChart = this.$echarts.init(document.getElementById(this.chartId));
                let option = {
                    tooltip: {
                        position: 'top'
                    },
                    animation: false,
                    grid: {
                        height: '60%',
                        y: '6%',
                        x:'15%'
                    },
                    xAxis: {
                        type: 'category',
                        data: labels,
                        splitArea: {
                            show: true
                        }
                    },
                    yAxis: {
                        type: 'category',
                        data: labels,
                        splitArea: {
                            show: true
                        }
                    },
                    visualMap: {
                        min: 0,
                        max: 1,
                        calculable: true,
                        orient: 'horizontal',
                        left: 'center',
                        bottom: '15%'
                    },
                    series: [{
                        type: 'heatmap',
                        data: data,
                        label: {
                            normal: {
                                show: true
                            }
                        },
                        itemStyle: {
                            emphasis: {
                                shadowBlur: 10,
                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                            }
                        }
                    }]
                };
                clChart.setOption(option);
                window.addEventListener('resize', function () {
                    clChart.resize();
                })
                
                
                
            }
        }

    }
</script>

<style>
</style>
