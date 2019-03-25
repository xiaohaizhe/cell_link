<template>
    <div>
        <div id="linear1" style="height:250px;"></div>
    </div>
</template>

<script>
  export default {
    name: 'linearChart1',
    data () {
      return {
      }
    },
    props:{
    },
    computed:{
    },
    mounted(){
    },
    methods: {
            drawChart(chartData){
                let a = chartData.data[1][0];
                let b = chartData.data[0][0];
                let point1 = [chartData.x_min,chartData.x_min*a+b]
                let point2 = [chartData.x_max,chartData.x_max*a+b]
                let linearChart1 = this.$echarts.init(document.getElementById('linear1'));
                let markLineOpt = {
                        animation: false,
                        label: {
                            normal: {
                                formatter: 'y = '+a.toFixed(3)+' * x '+(b<0?'':'+')+b.toFixed(3),
                                textStyle: {
                                    align: 'right',
                                    verticalAlign: 'bottom'
                                }
                            }
                        },
                        lineStyle: {
                            normal: {
                                type: 'solid'
                            }
                        },
                        tooltip: {
                            formatter: 'y = '+a.toFixed(3)+' * x '+(b<0?'':'+')+b.toFixed(3),
                        },
                        data: [[{
                            coord: point1,
                            symbol: 'none'
                        }, {
                            coord: point2,
                            symbol: 'none'
                        }]]
                    };

                let option = {
                        tooltip: {
                            formatter: '({c})'
                        },
                        grid:{
                            top:"20px",
                            left:"35px",
                            right:"35px",
                            bottom:"30px"
                        },
                        xAxis: [
                            {gridIndex: 0},
                        ],
                        yAxis: [
                            {gridIndex: 0},
                        ],
                        series: [
                            {
                                name: 'I',
                                type: 'scatter',
                                xAxisIndex: 0,
                                yAxisIndex: 0,
                                data: chartData.datapoints,
                                markLine: markLineOpt
                            }
                        ]
                    };
                linearChart1.setOption(option);
                window.addEventListener('resize', function () {
                    linearChart1.resize();
                })
                
                
                
            }
    }

  }
</script>

<style>
</style>
