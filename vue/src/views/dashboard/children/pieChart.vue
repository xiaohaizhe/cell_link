<template>
    <div :id="chartId" style="height:240px"></div>
</template>

<script>
    // let echarts = require('echarts/lib/echarts')
    // // 引入折线图
    // require('echarts/lib/chart/line');
    // // 提示框
    // require('echarts/lib/component/tooltip')

    export default {
        name: 'pieChart',
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
        },
        methods: {
            drawChart(data,height){
                let chartDom = document.getElementById(this.chartId);
                // chartDom.style.height = height+'px';
                let pieChart = this.$echarts.init(chartDom);
                let option = {
                        color:['#3BBAF0','#FBB02F'],
                        tooltip: {
                            trigger: 'item',
                            formatter: "{a} <br/>{b}: {c} ({d}%)"
                        },
                        legend: {
                            orient: 'vertical',
                            x: '3%',
                            y: 'center',
                            data:['正常','异常']
                        },
                        title: {
                            text:'总计',
                            subtext:data.deviceSum,
                            subtextStyle:{
                                fontSize:18,
                                color:'#0565B9',
                                fontWeight:'bold'
                            },
                            left:'center',
                            top:'38%',
                            textStyle:{
                                color:'#0565B9',
                                fontSize:14,
                                align:'center',
                                fontWeight:'normal'
                            }
                        },
                        series: [
                            {
                                name:'设备状态',
                                type:'pie',
                                radius: ['50%', '75%'],
                                // avoidLabelOverlap: false,
                                // roseType: 'radius',
                                itemStyle: {
                                    emphasis: {
                                        shadowBlur: 10,
                                        shadowOffsetX: 0,
                                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                                    },
                                    normal:{ 
								           label:{ 
								              show: true, 
								                formatter: '{b} , {d}%' 
								              }, 
								                labelLine :{show:true} 
								              }
                                },
                                data:[
                                    {value:data.deviceSum_normal, name:'正常'},
                                    {value:data.deviceSum_abnormal, name:'异常'},
                                ]
                            }
                        ]
                    };
                pieChart.setOption(option,true);
                window.addEventListener('resize', function () {
                    pieChart.resize();
                })
                
                
                
            }
        }

    }
</script>

<style>
</style>
