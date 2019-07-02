<template>
    <div>
        <cl-header headColor="#181818"></cl-header>
        <sub-header title="帮助中心"></sub-header>
        <div class="mainContent bg-fff" style="padding: 3rem 5rem;">
            <el-col :span="6">
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
            <el-col :span="18">
                <router-view></router-view>
            </el-col>
        </div>
    </div>
</template>

<script>
    import headTop from 'components/header/head'
    import subHead from 'components/subHeader/subHeader'

    export default {
        name: 'help',
        data () {
            return {
            }
        },
        components:{
            'cl-header':headTop,
            'sub-header':subHead
        },
        mounted(){
            debugger;
            this.$route.name;
        },
        computed:{
        },
        methods: {
        }

    }
</script>

<style>
    
</style>
