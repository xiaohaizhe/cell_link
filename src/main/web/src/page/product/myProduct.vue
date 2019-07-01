<template>
    <div>
        <cl-header headColor="#181818"></cl-header>
        <div style="top:72px;position:absolute;width:100%">
            <div class="product bg-fff">
                
                    <p class="font-24 ellipsis" style="margin-bottom:50px;max-width:500px">
                        <router-link to="/home">
                            <span class="font-24 color000">我的产品</span>
                        </router-link>
                        <span class="font-24" style="margin: 0 10px;font-weight: lighter;">|</span>{{productDet[0].value}}
                    </p>
                
                
                <div class="productInfo">
                    <div class="cl-flex">
                        <span class="font-16" style="flex-shrink:0">产品信息</span>
                        <div class="splitline"></div>
                    </div>
                    <div class="proDet">
                        <ul v-for="item in productDet" :key="item.name">
                            <li class="colorGray font-16">{{item.name}}</li>
                            <li class="proCont font-16">{{item.value}}</li>
                        </ul>
                    </div>
                </div>
                <div class="productInfo">
                    <div class="cl-flex">
                        <span class="font-16" style="flex-shrink:0">技术参数</span>
                        <div class="splitline"></div>
                    </div>
                    <div class="proDet">
                        <ul>
                            <li class="colorGray font-16">设备接入方式</li>
                            <li class="proCont font-16">{{protocolType}}</li>
                        </ul>
                        <!-- <router-link :to="{ name: 'editProduct', params: { productId: productDet[1].value }}"> -->
                        <el-button @click="goAddress('editProduct')">编辑</el-button>
                        <!-- </router-link> -->
                    </div>
                </div>
                
            </div>
            <div class="prodTab">
                <ul class="flexAround tab">
                    <li v-for="item in navData" :key="item.id" :class="{active : prodTab == item.id }"
                        @click="handleClick(item.id)">{{item.name}}</li>
                </ul>
            </div>
            <div class="product" style="padding-top:60px;">
                <router-view :protocolType="protocolType"/>
            </div>
        </div>
        
    </div>
</template>

<script>
    import headTop from 'components/header/head'
    import {getDetail} from 'service/getData'
    import provinceCity from 'static/provinceCity.json'
    import {mapState} from 'vuex'

    export default {
        name: 'myProduct',
        data () {
            return {
                productDet:[{
                        name:'产品名称',
                        value:''
                    },{
                        name:'产品ID',
                        value:"0"
                    },{
                        name:'产品描述',
                        value:''
                    },{
                        name:'设备注册码',
                        value:''
                    },{
                        name:'产品地址',
                        value:'产品名称'
                    }
                ],
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
            },
            ...mapState([
                'prodTab','product'
            ])
        },
        components:{
            'cl-header':headTop
        },
        created(){
            this.provinces = provinceCity.provinces;
            this.$store.commit('SAVE_TAB', this.$route.name);
            //解密
            let x = new Buffer(decodeURIComponent(this.$route.params.productId), 'base64')
            let y = x.toString('utf8');
            this.productDet[1].value =y;
            this.$store.commit('SAVE_PRODUCT', {'id':y});
        },
        mounted(){
            this.getDetail();
        },
        methods: {
            async getDetail(){
                let resp = await getDetail(this.productDet[1].value);
                if(resp.code==0){
                    this.productDet[0].value = resp.data.product.name;
                    this.productDet[2].value = resp.data.product.description;
                    this.productDet[3].value = resp.data.product.registrationCode;
                    this.protocolId = resp.data.product.protocolId;
                    this.$store.commit('SAVE_PRODUCT', resp.data.product);
                    this.findAddress(resp.data.product.cityCode);
                }else if(resp.code=="error"){
                    return;
                }else{
                    this.$message({
                        message: "获取数据失败",
                        type: 'error'
                    });
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
                this.$store.commit('SAVE_TAB', id);
                this.$router.push({name:id});
            },
            goAddress(url){
                //加密
                let b = new Buffer(this.productDet[1].value);
                let s = b.toString('base64');
                let data = encodeURIComponent(s);
                this.$router.push({name:url,params:{productId:data}})
            }
        }

    }
</script>

<style>
    .splitline{
        display: inline-block;
        height: 1px;
        width: 96%;
        margin:0 1.43rem;
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
        width: 6rem;
        flex-shrink: 0;
    }
    .productInfo .proCont{
        margin-left: 50px;
    }
    .prodTab{
        background-color: rgb(25, 108, 127);
        color: rgb(255, 255, 255);
        position: sticky;
        padding: 0px 15%;
        top: 72px;
        z-index: 999;
    }
</style>
