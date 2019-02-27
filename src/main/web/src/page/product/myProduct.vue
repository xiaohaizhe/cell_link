<template>
    <div>
        <cl-header headColor="#181818"></cl-header>
        <div style="top:72px;position:absolute;width:100%">
            <div class="product bg-fff">
                <p class="font-24" style="margin-bottom:50px;">我的产品
                    <span class="font-24" style="margin: 0 10px;font-weight: lighter;">|</span>{{productDet[0].value}}
                </p>
                <div class="productInfo">
                    <div class="flex">
                        <span class="font-16" style="flex-shrink:0">产品信息</span>
                        <div class="splitline"></div>
                    </div>
                    <div class="proDet">
                        <ul v-for="item in productDet" :key="item.name">
                            <li class="colorGray">{{item.name}}</li>
                            <li class="proCont">{{item.value}}</li>
                        </ul>
                    </div>
                </div>
                <div class="productInfo">
                    <div class="flex">
                        <span class="font-16" style="flex-shrink:0">技术参数</span>
                        <div class="splitline"></div>
                    </div>
                    <div class="proDet">
                        <ul>
                            <li class="colorGray">设备接入方式</li>
                            <li class="proCont">{{protocolType}}</li>
                        </ul>
                        <router-link :to="{ name: 'editProduct', params: { prodId: productDet[1].value }}">
                            <el-button>编辑</el-button>
                        </router-link>
                    </div>
                </div>
                
            </div>
            <div class="product" style="background-color: #196c7f;color:#fff;padding:0 15%;">
                <ul class="flexAround tab">
                    <li v-for="item in navData" :key="item.id" :class="{active : activeNav == item.id }"
                        @click="handleClick(item.id)">{{item.name}}</li>
                </ul>
            </div>
            <div class="product" style="padding-top:60px;">
                <router-view :prodId="productDet[1].value" @changeNav="changeNav"/>
            </div>
        </div>
        
    </div>
</template>

<script>
    import headTop from 'components/header/head'
    import {getDetail} from 'service/getData'
    import provinceCity from 'static/provinceCity.json'
    
    export default {
        name: 'myProduct',
        data () {
            return {
                productDet:[{
                        name:'产品名称',
                        value:''
                    },{
                        name:'产品ID',
                        value:0
                    },{
                        name:'产品描述',
                        value:''
                    },{
                        name:'设备注册码',
                        value:'产品名称'
                    },{
                        name:'产品地址',
                        value:'产品名称'
                    }
                ],
                activeNav:'prodOverview',
                protocolId:0,
                navData:[{
                    name:'产品概况',
                    id:'prodOverview'
                },{
                    name:'设备管理',
                    id:'devManage'
                },{
                    name:'数据流管理',
                    id:'dsManage'
                },{
                    name:'应用管理',
                    id:'appManage'
                },{
                    name:'智能分析',
                    id:'intellAna'
                },{
                    name:'触发器管理',
                    id:'triggerManage'
                }]
            }
        },
        computed:{
            protocolType(){
                if(this.protocolId==1)
                    return 'MQTT'
                else
                    return 'HTTP'
            }
        },
        components:{
            'cl-header':headTop
        },
        created(){
            this.provinces = provinceCity.provinces
        },
        mounted(){
            this.productDet[1].value =this.$route.params.prodId || 0;
            this.getDetail();
        },
        methods: {
            async getDetail(){
                let resp = await getDetail(this.productDet[1].value);
                if(resp.code==0){
                    this.productDet[0].value = resp.data.product.name;
                    this.productDet[2].value = resp.data.product.description;
                    this.protocolId = resp.data.product.protocolId;
                    this.findAddress(resp.data.product.cityCode);
                }
            },
            findAddress(cityCode){
                let address = '';
                let allProvinces = this.provinces;
                let provinceCode = parseInt(cityCode/100)*100+"";
                for(let i=0;i<allProvinces.length;i++){
                    if(provinceCode==allProvinces[i].code){
                        address += allProvinces[i].name;
                        let allCities = allProvinces[i].cities;
                        for(let j=0;j<allCities.length;j++){
                            if(cityCode==allCities[j].code){
                                address += allCities[j].name;
                            }
                        }
                    }
                }
                this.productDet[4].value = address;
            },
            handleClick(id) {
                this.activeNav = id;
                this.$router.push({name:id});
            },
            changeNav(id){
                this.activeNav = id;
            }
        }

    }
</script>

<style>
    .splitline{
        display: inline-block;
        height: 1px;
        width: 96%;
        margin:0 20px;
        background: #cccccc;
        overflow: hidden;
        vertical-align: middle;
    }
    .productInfo .proDet{
        padding:15px 84px 0;
    }
    .productInfo .proDet>ul{
        margin: 7px 0;
        min-height: 40px;
        display: flex;
        align-items: baseline;
    }
    .productInfo .proDet>ul .colorGray{
        width: 90px;
        flex-shrink: 0;
    }
    .productInfo .proCont{
        margin-left: 50px;
    }
</style>
