<template>
    <div id="myChart" style="height:600px;"></div>
</template>

<script>
// let echarts = require('echarts/lib/echarts')
// // 散点图
// require('echarts/lib/chart/scatter')
// // 散点图放大
// require('echarts/lib/chart/effectScatter')
// // 地图
// require('echarts/lib/chart/map')
// // 提示框
// require('echarts/lib/component/tooltip')
// // 地图geo
// require('echarts/lib/component/geo')
//中国地图
require('echarts/map/js/china')

// import { getHeatmap } from 'service/getData'

    export default {
        name: 'scatterChart',
        data () {
            return {
                "data":{
                    "lonAndLat":{
                        "泰州市":[
                            119.923116,
                            32.455778
                        ],
                        "太原市":[
                            112.548879,
                            37.87059
                        ],
                        "南通市":[
                            120.894291,
                            31.980171
                        ],
                        "成都市":[
                            104.066541,
                            30.572269
                        ],
                        "青岛市":[
                            120.382639,
                            36.067082
                        ],
                        "天津市":[
                            117.200983,
                            39.084158
                        ],
                        "唐山市":[
                            118.180193,
                            39.630867
                        ],
                        "北京市":[
                            116.407526,
                            39.90403
                        ],
                        "阿里地区":[
                            80.105804,
                            32.501111
                        ],
                        "沈阳市":[
                            123.431474,
                            41.805698
                        ],
                        "南京市":[
                            118.796877,
                            32.060255
                        ]
                    },
                    "locWeight":[
                        {
                            "name":"泰州市",
                            "value":5
                        },
                        {
                            "name":"太原市",
                            "value":5
                        },
                        {
                            "name":"南通市",
                            "value":14
                        },
                        {
                            "name":"成都市",
                            "value":5
                        },
                        {
                            "name":"青岛市",
                            "value":3
                        },
                        {
                            "name":"天津市",
                            "value":6
                        },
                        {
                            "name":"唐山市",
                            "value":5
                        },
                        {
                            "name":"北京市",
                            "value":10
                        },
                        {
                            "name":"阿里地区",
                            "value":5
                        },
                        {
                            "name":"沈阳市",
                            "value":5
                        },
                        {
                            "name":"南京市",
                            "value":25
                        }
                    ]
                }
            }
        },
        computed: {},
        mounted(){
            this.drawChart();
        },
        methods: {
            convertData(data,geoCoordMap) {
                let res = [];
                if(!data){
                    return [];
                }
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
                // let resp = await getHeatmap();
                // if(resp.code=="error"){
                //     return
                // }else if(resp.code != 0){
                //     this.$message({
                //         message: "获取热力图失败",
                //         type: 'error'
                //     });
                //     return;
                // } 
                let data = this.data.locWeight;
                let geoCoordMap = this.data.lonAndLat;
                let myChart = this.$echarts.init(document.getElementById('myChart'))
                let option = {
                    backgroundColor: new this.$echarts.graphic.LinearGradient(0, 0, 1, 0,[{
                                        offset: 0, color: '#1C3A9E' // 0% 处的颜色
                                    },{
                                        offset: 1, color: '#018ABC' // 100% 处的颜色
                                    }]
                                ),
                    // title: {
                    //     text: '产品热点图',
                    //     right: '150',
                    //     top:'bottom',
                    //     textStyle: {
                    //         color: '#fff'
                    //     }
                    // },
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
                        center: [104.066541,
                            30.572269],
                        zoom:2,
                        roam: true,
                        itemStyle: {
                            normal: {
                                areaColor: '#0F60AC',//地图默认的背景颜色
                                borderColor: '#173E8D'//地图默认的边线颜色
                            },
                            emphasis: {
                                color: '#fff',//tooltip上的颜色
                                areaColor: '#173E8D'//地图触发地区的背景颜色
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
                            return val[2]
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
                            brushType: 'stroke',
                            scale:5
                        },
                        hoverAnimation: true,
                        label: {
                            normal: {
                                formatter: '{b}',
                                position: 'right',
                                show: false,
                                fontSize: 16
                            }
                        },
                        itemStyle: {
                            normal: {
                                color: '#FBB02F',
                                shadowBlur: 15,
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
