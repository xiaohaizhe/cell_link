<template>
    <div>
        <cl-header headColor="#181818"></cl-header>
        <sub-header title="设备关联" :subtitle="`${device.name}-详情`"></sub-header>
        <div class="mainContent">
            <p class="font-16">基本信息</p>
            <div class="bg-fff" style="margin-top:20px;">
                <div class="flex cl-card">
                    <div class="report cl-cardIcon"></div>
                    <div>
                        <p class="font-18 colorBlack mgbot-20">{{device.name}}</p>
                        <div>
                            <p class="colorGray">设备ID：{{device.device_sn}}</p>
                            <p class="colorGray">创建时间：{{device.create_time}}</p>
                            <p class="colorGray">接入方式：{{protocolType}}</p>
                            <!-- <p class="colorGray">设备API地址：http://api.heclouds.com/devices/47038555</p> -->
                        </div>
                    </div>
                </div>
            </div>
            <p class="font-16" style="margin:30px 0 20px">关联应用</p>
            <div class="apps flexBtw">
                <div class="bg-fff  cl-card"  v-for="item in appDatas" :key="item.id">
                    <router-link :to="{path:'/publish/'+item.id}">
                        <div class="flex" style="cursor: pointer;">
                            <div class="clock cl-cardIcon" v-if="item.applicationType==0"></div>
                            <div class="survey cl-cardIcon" v-if="item.applicationType==1"></div>
                            <div>
                                <p class="font-18 colorBlack mgbot-10">{{item.name}}</p>
                                <p class="colorGray">创建时间：</p>
                                <p class="colorGray">{{item.createTime}}</p>
                            </div>
                        </div>
                    </router-link>
                    
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import headTop from 'components/header/head'
    import subHead from 'components/subHeader/subHeader'
    import {getAppDetail} from 'service/getData'

    export default {
        name: 'devDetail',
        data () {
            return {
                device:{},
                appDatas:[],
            }
        },
        components:{
            'cl-header':headTop,
            'sub-header':subHead
        },
        computed:{
            protocolType(){
                if(this.device.protocolId==1)
                    return 'MQTT'
                else
                    return 'HTTP'
            },
        },
        created(){
             //解密
            var x = new Buffer(decodeURIComponent(this.$route.params.devData), 'base64')
            var y = x.toString('utf8');
            this.device = JSON.parse(y);
        },
        mounted(){
            this.initData();
        },
        methods: {
            initData(){
                for(let data of this.device.apps){
                    this.getAppDetail(data);
                }
            },
            async getAppDetail(data){
                let resp = await getAppDetail(data);
                if(resp.code==0){
                    this.appDatas.push(resp.data);
                }else if(resp.code=="error"){
                    return;
                }else{
                    this.$message({
                        message: "获取数据失败！",
                        type: 'error'
                    });
                }
            }

        }

    }
</script>

<style>

</style>
