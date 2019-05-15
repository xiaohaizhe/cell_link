<template>
    <div>
        <div id="pieChart" style="height:250px;"></div>
    </div>
</template>

<script>
  export default {
    name: 'pieChart',
    data () {
      return {
      }
    },
    computed:{
    },
    mounted(){
    },
    methods: {
            drawChart(data){
                let pieChart = this.$echarts.init(document.getElementById('pieChart'));
                let option = {
                        color:['#61a0a8','#c23531'],
                        tooltip: {
                            trigger: 'item',
                            formatter: "{a} <br/>{b}: {c} ({d}%)"
                        },
                        series: [
                            {
                                name:'设备状态',
                                type:'pie',
                                radius : '70%',
                                // center: ['50%', '60%'],
                                data:[
                                    {value:data.normal_sum, name:'正常'},
                                    {value:data.abnormal_sum, name:'异常'},
                                    
                                ],
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
                                }
                            }
                        ]
                    };
                pieChart.setOption(option);
                window.addEventListener('resize', function () {
                    pieChart.resize();
                })
                
                
                
            }
    }

  }
</script>

<style>
</style>
