<template>
    <el-header height="70px">
        <div class="cl-flex head">
            <div class="cl-flex alignEnd">
                <img :src="logo" style="width:40px;height:40px;"/>
                <span class="font-28 colorWhite" style="margin-left:15px;">智能感知平台</span>
            </div>
            <div class="cl-flex alignCenter" v-if="first">
                <el-input class="mgR-15 search" style="width:145px;"
                    placeholder=""
                    suffix-icon="el-icon-search"
                    v-model="search">
                </el-input>
                <el-button type="primary" class="mgR-15 blueBtn clButton" @click="$router.push('/')">返回首页</el-button>
                <el-dropdown class="mgR-20" @command="handleCommand">
                    <el-button type="primary" class="blueBtn clButton">
                        新建<i class="el-icon-arrow-down el-icon--right"></i>
                    </el-button>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item command="scene">新建场景</el-dropdown-item>
                        <el-dropdown-item command="devg">新建设备组</el-dropdown-item>
                        <el-dropdown-item command="dev">新建设备</el-dropdown-item>
                        <el-dropdown-item command="app">新建应用</el-dropdown-item>
                        <el-dropdown-item command="trigger">新建触发器</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
                <el-dropdown class="colorGray2 mgR-20">
                    <span class="el-dropdown-link">
                        欢迎您，{{user.name}}<i class="el-icon-arrow-down el-icon--right"></i>
                    </span>
                    <el-dropdown-menu slot="dropdown">
                        <router-link to="/user">
                            <el-dropdown-item>个人中心</el-dropdown-item>
                        </router-link>
                        <el-dropdown-item>帮助中心</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
                <el-button type="text" class="colorGray2" style="padding:0;" @click="logout">退出</el-button>
            </div>
            
            <div class="cl-flex alignCenter" v-if="!first">
                <p  class="colorGray2 mgR-20 font-14">欢迎您，{{user.name}}</p>
                <el-button type="text" class="colorGray2" style="padding:0;"  @click="logout">退出</el-button>
            </div>
        </div>
    </el-header>
</template>

<script>

import { mapGetters } from 'vuex'

export default {
    name: 'clHeader',
    data () {
      return {
          search:'',
          logo:require('assets/celllink.svg'),
      }
    },
    computed: {
        ...mapGetters([
            'user','activeScene'
        ])
    },
    props:{
        first: {
            type: Boolean,
            default: true
        }
    },
    methods: {
        async logout() {
            await this.$store.dispatch('user/logout')
            this.$message({
                message: "登出成功！",
                type: 'success'
            });
            this.$router.push('/login')
        },
         handleCommand(command) {
             switch (command)
             {
                case 'scene': this.$addScene.show({
                    userId:this.user.userId,
                    onOk: () => {
                        this.$store.dispatch('user/getAside');
                    },
                });break;
                case 'devg': this.$addDevGroup.show({
                    userId:this.user.userId,
                    onOk: (scenarioId) => {
                        this.$store.dispatch('user/getAside',{scenarioId:scenarioId});
                        this.$router.push('/scene/'+scenarioId+'/devGroup')
                    },
                });break;
                case 'dev':  this.$addDevice.show({
                    userId:this.user.userId,
                    onOk: (dgId) => {
                        this.$store.dispatch('user/getAside',{dgId:dgId});
                        this.$router.push('/devGroup/'+dgId)
                    },
                });break;
                case 'app': this.$addApp.show({
                    userId:this.user.userId,
                    onOk: (scenarioId) => {
                        this.$store.dispatch('user/getAside',{scenarioId:scenarioId});
                        this.$router.push('/scene/'+scenarioId+'/application')
                    },
                });break;
                case 'trigger': this.triggerVisible =true;break;
             }
        },
    }
}
</script>
<style lang="scss" scoped>
    .el-header{
        padding: 0;
    }
    .head{
        background: #00253B;
        padding: 15px 50px;
        position: fixed;
        justify-content: space-between;
        z-index: 999;
        width: 100%;
    }
</style>