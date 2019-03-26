<template>
    <div>
        <div id="linear"></div>
    </div>
</template>

<script>
    import 'echarts-gl'; 

    export default {
        name: 'linearChart',
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
                drawChart2(chartData){
                    let a1 = chartData.data[1][0];
                    let a2 = chartData.data[1][1];
                    let b = chartData.data[0][0];
                    let dom2 = document.getElementById('linear');
                    dom2.style.height='700px';
                    let linearChart2= this.$echarts.init(dom2);
                    let option = {
                            tooltip: {},
                            backgroundColor: '#fff',
                            visualMap: {
                                show: false,
                                dimension: 2,
                                min: -1,
                                max: 1,
                                inRange: {
                                    color: ['#313695', '#4575b4', '#74add1', '#abd9e9', '#e0f3f8', '#ffffbf', '#fee090', '#fdae61', '#f46d43', '#d73027', '#a50026']
                                }
                            },
                            xAxis3D: {
                                type: 'value'
                            },
                            yAxis3D: {
                                type: 'value'
                            },
                            zAxis3D: {
                                type: 'value'
                            },
                            grid3D: {
                                viewControl: {
                                    // projection: 'orthographic'
                                }
                            },
                            series: [{
                                type: 'surface',
                                wireframe: {
                                    // show: false
                                },
                                equation: {
                                    x: {
                                        step: 0.05
                                    },
                                    y: {
                                        step: 0.05
                                    },
                                    z: function (x, y) {
                                        
                                        return a1*x*a2*y+b
                                    }
                                }
                            }]
                        }
                    linearChart2.setOption(option);
                    window.addEventListener('resize', function () {
                        linearChart2.resize();
                    })
                    
                    
                    
                },
                drawChart1(chartData){
                    let a = chartData.data[1][0];
                    let b = chartData.data[0][0];
                    let point1 = [chartData.x_min,chartData.x_min*a+b]
                    let point2 = [chartData.x_max,chartData.x_max*a+b]
                    let dom1 = document.getElementById('linear');
                    dom1.style.height='300px';
                    let linearChart1= this.$echarts.init(dom1);
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
                                top:"50px",
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
