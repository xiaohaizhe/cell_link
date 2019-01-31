<template>
    <div id="myChart" style="height:600px;"></div>
</template>

<script>
let echarts = require('echarts/lib/echarts')
// 散点图
require('echarts/lib/chart/scatter')
// 散点图放大
require('echarts/lib/chart/effectScatter')
// 地图
require('echarts/lib/chart/map')
// 提示框
require('echarts/lib/component/tooltip')
// 地图geo
require('echarts/lib/component/geo')
//中国地图
require('echarts/map/js/china')

import { getHeatmap } from 'service/getData'

    export default {
        name: 'scatterChart',
        data () {
            return {}
        },
        computed: {},
        mounted(){
            this.drawChart();
        },
        methods: {
            convertData(data,geoCoordMap) {
                let res = [];
                for (let i = 0; i < data.length; i++) {
                    let geoCoord = geoCoordMap[data[i].name];
                    if (geoCoord) {
                        res.push({
                            name: data[i].name,
                            value: geoCoord.concat(data[i].value)
                        });
                    }
                }
                return res;
            },
            async drawChart(){
                let resp = await getHeatmap();
                if(resp.code != 0){
                    this.$message({
                        message: "获取热力图失败",
                        type: 'error'
                    });
                    return;
                }
                let data = resp.data.locWeight;
                let geoCoordMap = resp.data.lonAndLat;
                let myChart = echarts.init(document.getElementById('myChart'))
                let option = {
                    backgroundColor: '#004e54',
                    title: {
                        text: '设备热点图',
                        right: '150',
                        top:'bottom',
                        textStyle: {
                            color: '#fff'
                        }
                    },
                    tooltip : {
                        trigger: 'item',
                        //鼠标移上去的显示信息
                        formatter: function(p){//自定义提示信息
                            let dataCon = p.data;
                            var htmlStr ='<div>';
                            htmlStr += p.name + '<br/>';//x轴的名称
                            htmlStr += "数量" + '：'+p.value[2];
                            htmlStr += '</div>';
                            return htmlStr;
                        },
                    },
                    geo: {
                        map: 'china',
                        label: {
                            emphasis: {
                                show: false
                            }
                        },
                        zoom: 3,
                        roam: true,
                        itemStyle: {
                            normal: {
                                areaColor: '#005c6b',//地图默认的背景颜色
                                borderColor: '#00b3a9'//地图默认的边线颜色
                            },
                            emphasis: {
                                color: '#fff',//tooltip上的颜色
                                areaColor: '#018297'//地图触发地区的背景颜色
                            }
                    }
                    },
                    series : [
                        {
                        name: '数量',
                        type: 'scatter',
                        coordinateSystem: 'geo',
                        data: this.convertData(data,geoCoordMap),
                        symbolSize: function (val) {
                            return val[2];
                        },
                        label: {
                            normal: {
                                formatter: '{b}',
                                position: 'right',
                                show: false
                            },
                            emphasis: {
                                show: true
                            }
                        },
                        itemStyle: {
                            normal: {
                                color: '#be5f0c'
                            }
                        }
                        },
                        {
                        name: '数量top',
                        type: 'effectScatter',
                        coordinateSystem: 'geo',
                        data: this.convertData(data,geoCoordMap),
                        symbolSize: function (val) {
                            return val[2]
                        },
                        showEffectOn: 'render',
                        rippleEffect: {
                            brushType: 'stroke'
                        },
                        hoverAnimation: true,
                        label: {
                            normal: {
                                formatter: '{b}',
                                position: 'right',
                                show: true,
                                fontSize: 16
                            }
                        },
                        itemStyle: {
                            normal: {
                                color: '#ea9800',
                                shadowBlur: 10,
                                shadowColor: '#333'
                            }
                        },
                        zlevel: 1
                        }
                    ]
                };
                myChart.setOption(option);
                window.addEventListener('resize', function () {
                     myChart.resize();
                })
            },

        }

    }
</script>

<style>
</style>
