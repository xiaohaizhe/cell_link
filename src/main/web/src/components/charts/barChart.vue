<template>
    <div>
        <!-- <p class="center" v-show="!hasData" style="height:50px;line-height:50px;">暂无数据</p> -->
        <div :id="chartId" style="height:250px;"></div>
    </div>
    
</template>

<script>
    let echarts = require('echarts/lib/echarts')
    // 引入柱状图
    require('echarts/lib/chart/bar');
    // 提示框
    require('echarts/lib/component/tooltip')

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
                debugger
                this.drawChart(this.data);
            }
        },
        mounted(){
            this.drawChart(this.data);
            debugger
        },
        methods: {
            async drawChart(dsData){
                if(dsData.length==0){
                    this.$message({
                        message: "暂无统计数据",
                        type: 'warning'
                    });
                }
                let labels = [];
                for (let v of dsData) {
                    labels.push(v.createTime);
                }
                let dsChart = echarts.init(document.getElementById(this.chartId));
                let option = {
                        color: ['#07aaa5'],
                        tooltip : {
                            trigger: 'axis',
                            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                            }
                        },
                        grid: {
                            top:"20px",
                            left:"20px",
                            right:"20px",
                            bottom:"20px",
                            containLabel: true
                        },
                        xAxis : [
                            {
                                type : 'category',
                                data : ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
                                axisTick: {
                                    alignWithLabel: true
                                }
                            }
                        ],
                        yAxis : [
                            {
                                type : 'value'
                            }
                        ],
                        series : [
                            {
                                name:'直接访问',
                                type:'bar',
                                barWidth: '60%',
                                data:[10, 52, 200, 334, 390, 330, 220]
                            }
                        ]
                    };
                dsChart.setOption(option);
                window.addEventListener('resize', function () {
                    dsChart.resize();
                })
                
                
                
            }
        }

    }
</script>

<style>
</style>
