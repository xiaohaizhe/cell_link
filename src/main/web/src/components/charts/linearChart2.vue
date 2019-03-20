<template>
    <div>
        <div id="linear2" style="height:700px;"></div>
    </div>
</template>

<script>
    import 'echarts-gl'; 

    export default {
        name: 'linearChart2',
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
                    let linearChart2= this.$echarts.init(document.getElementById('linear2'));
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
                                        
                                        return 0.3790645718449502*x*0.3790645718449502*y+6.3038357099785784
                                    }
                                }
                            }]
                        }
                    linearChart2.setOption(option);
                    window.addEventListener('resize', function () {
                        linearChart2.resize();
                    })
                    
                    
                    
                }
        }

    }
</script>

<style>
</style>
