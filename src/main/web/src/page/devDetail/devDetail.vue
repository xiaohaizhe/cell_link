<template>
    <div>
        <cl-header headColor="#181818"></cl-header>
        <sub-header title="设备关联" :subtitle="`${deviceName}-详情`"></sub-header>
        <div class="mainContent">
            <p class="font-16">基本信息</p>
            <div class="bg-fff" style="margin-top:20px;">
                <div class="flex cl-card">
                    <div class="report"></div>
                    <div>
                        <p class="font-18 colorBlack mgbot-30">{{name}}</p>
                        <p class="colorGray">设备ID：</p>
                        <p class="colorGray">创建时间：</p>
                    </div>
                    <div style="margin:34px 0 0 60px">
                        <p class="colorGray">鉴权信息：</p>
                        <p class="colorGray">接入方式：MQTT</p>
                        <p class="colorGray">设备API地址：http://api.heclouds.com/devices/47038555</p>
                    </div>
                </div>
            </div>
            <p class="font-16" style="margin:30px 0 20px">关联应用</p>
            <div class="apps flexBtw">
                <div class="bg-fff flex cl-card">
                    <div class="clock"></div>
                    <div>
                        <p class="font-18 colorBlack mgbot-10">温度测试应用</p>
                        <p class="colorGray">创建时间：</p>
                        <p class="colorGray">2018-10-24 16:00:02</p>
                    </div>
                </div>
                <div class="bg-fff flex cl-card">
                    <div class="clock"></div>
                    <div>
                        <p class="font-18 colorBlack mgbot-10">温度测试应用</p>
                        <p class="colorGray">创建时间：</p>
                        <p class="colorGray">2018-10-24 16:00:02</p>
                    </div>
                </div>
                <div class="bg-fff flex cl-card">
                    <div class="clock"></div>
                    <div>
                        <p class="font-18 colorBlack mgbot-10">温度测试应用</p>
                        <p class="colorGray">创建时间：</p>
                        <p class="colorGray">2018-10-24 16:00:02</p>
                    </div>
                </div>
                <div class="bg-fff flex cl-card">
                    <div class="clock"></div>
                    <div>
                        <p class="font-18 colorBlack mgbot-10">温度测试应用</p>
                        <p class="colorGray">创建时间：</p>
                        <p class="colorGray">2018-10-24 16:00:02</p>
                    </div>
                </div>
                <div class="bg-fff flex cl-card">
                    <div class="clock"></div>
                    <div>
                        <p class="font-18 colorBlack mgbot-10">温度测试应用</p>
                        <p class="colorGray">创建时间：</p>
                        <p class="colorGray">2018-10-24 16:00:02</p>
                    </div>
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
                deviceName:'',
                appId:'1551668550149'
            }
        },
        components:{
            'cl-header':headTop,
            'sub-header':subHead
        },
        computed:{
        },
        mounted(){
            this.deviceName = this.$route.query.data.name;
            // this.appId = this.$route.query.data.apps[0];
            this.getAppDetail();
        },
        methods: {
            async getAppDetail(){
                let resp = await getAppDetail(this.appId);
                if(resp.code==0){
                    this.name = resp.data.name;

                }
            }

        }

    }
</script>

<style>

</style>
