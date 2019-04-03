<template>
    <div>
        <!-- <p class="center" v-show="!hasData" style="height:50px;line-height:50px;">暂无数据</p> -->
        <div :id="chartId" style="height:250px;"></div>
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
            async drawChart(data){
                if(data.length==0){
                    this.$message({
                        message: "暂无统计数据",
                        type: 'warning'
                    });
                }
                let labels = [];
                for (let v of data) {
                    labels.push(v.create_time);
                }
                let barChart = this.$echarts.init(document.getElementById(this.chartId));
                let option = {
                        color: ['#07aaa5'],
                        tooltip : {
                            trigger: 'axis',
                            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                            }
                        },
                        grid:{
                            top:"20px",
                            left:"40px",
                            right:"35px",
                            bottom:"30px"
                        },
                        xAxis : [
                            {
                                type : 'category',
                                data : labels,
                                axisTick:{
                                    show:false,
                                },
                                axisLabel:{
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
                            }
                        ],
                        yAxis : [
                            {
                                type : 'value',
                                axisTick:{
                                    show:false,
                                },
                            }
                        ],
                        series : [
                            {
                                // name:'直接访问',
                                type:'bar',
                                barWidth: '60%',
                                data:data
                            }
                        ]
                    };
                barChart.setOption(option);
                window.addEventListener('resize', function () {
                    barChart.resize();
                })
                
                
                
            }
        }

    }
</script>

<style>
</style>
