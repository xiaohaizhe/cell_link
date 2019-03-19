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
        this.drawChart();
    },
    methods: {
            drawChart(){
               
                let linearChart1 = this.$echarts.init(document.getElementById('linear1'));
                let markLineOpt = {
                        animation: false,
                        label: {
                            normal: {
                                formatter: 'y = 0.5 * x + 3',
                                textStyle: {
                                    align: 'right'
                                }
                            }
                        },
                        lineStyle: {
                            normal: {
                                type: 'solid'
                            }
                        },
                        tooltip: {
                            formatter: 'y = 0.5 * x + 3'
                        },
                        data: [[{
                            coord: [0, 3],
                            symbol: 'none'
                        }, {
                            coord: [20, 13],
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
                            {gridIndex: 0, min: 0, max: 20},
                        ],
                        yAxis: [
                            {gridIndex: 0, min: 0, max: 15},
                        ],
                        series: [
                            {
                                name: 'I',
                                type: 'scatter',
                                xAxisIndex: 0,
                                yAxisIndex: 0,
                                data: [[10.0, 8.04],
                                        [8.0, 6.95],
                                        [13.0, 7.58],
                                        [9.0, 8.81],
                                        [11.0, 8.33],
                                        [14.0, 9.96],
                                        [6.0, 7.24],
                                        [4.0, 4.26],
                                        [12.0, 10.84],
                                        [7.0, 4.82],
                                        [5.0, 5.68]],
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
