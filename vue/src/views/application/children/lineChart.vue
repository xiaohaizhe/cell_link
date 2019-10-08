<template>
        <div :id="chartId" style="height:300px"></div>
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
            // this.drawChart();
        },
        methods: {
            drawChart(data,color,height){
                let chartDom = document.getElementById(this.chartId);
                // chartDom.style.height = (height-70)+'px';
                let dsData = [
                        {
                            "time":"2019-08-01",
                            "value":0
                        },
                        {
                            "time":"2019-08-02",
                            "value":0
                        },
                        {
                            "time":"2019-08-03",
                            "value":10
                        },
                        {
                            "time":"2019-08-04",
                            "value":0
                        },
                        {
                            "time":"2019-08-05",
                            "value":0
                        },
                        {
                            "time":"2019-08-06",
                            "value":0
                        },
                        {
                            "time":"2019-08-07",
                            "value":10
                        },
                        {
                            "time":"2019-08-08",
                            "value":0
                        },
                        {
                            "time":"2019-08-09",
                            "value":0
                        },
                        {
                            "time":"2019-08-10",
                            "value":0
                        },
                        {
                            "time":"2019-08-11",
                            "value":0
                        },
                        {
                            "time":"2019-08-12",
                            "value":0
                        },
                        {
                            "time":"2019-08-13",
                            "value":0
                        },
                        {
                            "time":"2019-08-14",
                            "value":0
                        },
                        {
                            "time":"2019-08-15",
                            "value":0
                        },
                        {
                            "time":"2019-08-16",
                            "value":0
                        },
                        {
                            "time":"2019-08-17",
                            "value":0
                        },
                        {
                            "time":"2019-08-18",
                            "value":0
                        },
                        {
                            "time":"2019-08-19",
                            "value":0
                        },
                        {
                            "time":"2019-08-20",
                            "value":0
                        },
                        {
                            "time":"2019-08-21",
                            "value":0
                        },
                        {
                            "time":"2019-08-22",
                            "value":0
                        },
                        {
                            "time":"2019-08-23",
                            "value":0
                        },
                        {
                            "time":"2019-08-24",
                            "value":0
                        }
                    ]
                let labels = [];
                for (let v of dsData) {
                    labels.push(v.time);
                }
                let lineChart = this.$echarts.init(chartDom);
                let option = {
                        color:['#3BBAF0','#FBB02F','#A3E26B','#736BE2','#E26BBE'],
                        tooltip: {
                            trigger: 'axis',
                            formatter: '{b}:<br/>{c}'
                        },
                        legend:{
                            data:[1,2]
                        },
                        xAxis: {
                            type: 'category',
                            // boundaryGap: false,
                            axisLine :{
                                lineStyle:{
                                    color:"#D5DDE5"
                                }
                                
                            },
                            axisTick:{
                                show:false,
                            },
                            axisLabel:{
                                color :'#000',
                                formatter : function(params){
                                    var newParamsName = "";// 最终拼接成的字符串
                                    var paramsNameNumber = params.length;// 实际标签的个数
                                    var provideNumber = 10;// 每行能显示的字的个数
                                    var rowNumber = 2// 换行的话，需要显示几行
                                    /**
                                     * 判断标签的个数是否大于规定的个数， 如果大于，则进行换行处理 如果不大于，即等于或小于，就返回原标签
                                     */
                                    // 条件等同于rowNumber>1
                                    if (paramsNameNumber > provideNumber) {
                                        /** 循环每一行,p表示行 */
                                        for (var p = 0; p < rowNumber; p++) {
                                            var tempStr = "";// 表示每一次截取的字符串
                                            var start = p * provideNumber;// 开始截取的位置
                                            var end = start + provideNumber;// 结束截取的位置
                                            // 此处特殊处理最后一行的索引值
                                            if (p == rowNumber - 1) {
                                                // 最后一次不换行
                                                tempStr = params.substring(start, paramsNameNumber);
                                            } else {
                                                // 每一次拼接字符串并换行
                                                tempStr = params.substring(start, end) + "\n";
                                            }
                                            newParamsName += tempStr;// 最终拼成的字符串
                                        }

                                    } else {
                                        // 将旧标签的值赋给新标签
                                        newParamsName = params;
                                    }
                                    //将最终的字符串返回
                                    return newParamsName
                                }

                            },
                            data: labels
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
                        // grid:{
                        //     top:"20px",
                        //     left:"40px",
                        //     right:"35px",
                        //     bottom:"20px"
                        // },
                        series: [{
                            data: dsData,
                            type: 'line',
                            itemStyle : {  
                                normal : { 
                                    color: color
                                }  
                            }, 
                            symbolSize:2,
                            lineStyle:{  
                                color:color,
                                width:2
                            }, 
                            areaStyle: {
                                color:new this.$echarts.graphic.LinearGradient(0, 0, 0, 1,[{
                                        offset: 0, color: color// 0% 处的颜色
                                    },{
                                        offset: 0.9, color: '#fff' // 100% 处的颜色
                                    }]
                                ), //背景渐变色
                            }
                        }]
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
