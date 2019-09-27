<template>
    <el-container class="fullHeight is-vertical">
        <cl-header></cl-header>
        <el-container class="fullHeight">
            <el-main class="fullHeight cl-main cl-flex directColumn">
                <breadcrumb class="mgbot-15"/>
                <el-row class="bgWhite fullHeight">
                    <el-col :span="4">
                        <el-menu router 
                        :default-openeds='["guide"]'
                        :default-active="$route.name"
                        class="el-menu-vertical-demo">
                            <el-menu-item index="introduction" :route="{name:'introduction'}">
                                <span slot="title">简介</span>
                            </el-menu-item>
                            <el-menu-item index="devFlow" :route="{name:'devFlow'}">
                                <span slot="title">开发流程</span>
                            </el-menu-item>
                            <el-submenu index="guide">
                                <span slot="title">入门手册</span>
                                <el-menu-item-group>
                                    <el-menu-item index="guideAcc" :route="{name:'guideAcc'}">账户与登陆</el-menu-item>
                                    <el-menu-item index="guidePro" :route="{name:'guidePro'}">产品</el-menu-item>
                                    <el-menu-item index="guideDev" :route="{name:'guideDev'}">设备</el-menu-item>
                                    <el-menu-item index="guideDs" :route="{name:'guideDs'}">数据流与数据点</el-menu-item>
                                    <el-menu-item index="guideApp" :route="{name:'guideApp'}">应用管理</el-menu-item>
                                    <el-menu-item index="guideSec" :route="{name:'guideSec'}">安全鉴权</el-menu-item>
                                </el-menu-item-group>
                            </el-submenu>
                        </el-menu>
                    </el-col>
                    <el-col :span="20">
                        <router-view></router-view>
                    </el-col>
                </el-row>
                
            </el-main>
        </el-container>
    </el-container>
</template>


<script>
    import clHeader from 'components/header/header'
    import breadcrumb from 'components/breadcrumb/breadcrumb'

    export default {
        name: 'help',
        data () {
            return {
            }
        },
        components:{
            clHeader,
        breadcrumb
        },
        mounted(){
        },
        computed:{
        },
        methods: {
        }

    }
</script>

<style>
    
</style>
