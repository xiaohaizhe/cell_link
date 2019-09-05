<template>
    <div>
        <div class="bgWhite topCard cl-flex justifyBet">
            <div class="detail">
                <p class="font-24 mgbot-20"><span class="mgR-10">{{activeScene.scenarioName}}</span><i class="el-icon-edit colorGray2 point"></i></p>
                <p class="colorGray font-14">{{activeScene.description}}</p>
            </div>
            <div>
                <el-button type="warning" class="clButton" icon="el-icon-delete">删除场景</el-button>
                <el-button type="primary" class="clButton">添加设备组</el-button>
                <el-button type="primary" class="clButton">添加触发器</el-button>
                <el-button type="primary" class="clButton">添加应用</el-button>
            </div>
        </div>
        <el-tabs v-model="activeName" @tab-click="onTabClick">
            <el-tab-pane label="设备组" name="devGroup"></el-tab-pane>
            <el-tab-pane label="触发器" name="trigger"></el-tab-pane>
            <el-tab-pane label="应用" name="application"></el-tab-pane>
        </el-tabs>
        <router-view></router-view>
    </div>
</template>

<script>
    import { mapGetters } from 'vuex'
    export default {
        name: 'scene',
        data () {
        return {
                activeName:'devGroup'
            }
        },
        components:{
        },
        computed: {
            ...mapGetters([
                'activeScene','user'
            ])
        },
        mounted(){
        },
        methods:{
            onTabClick(data){
                this.$router.push('/scene/'+this.activeScene.scenarioId+'/'+data.name)
            }
        }
    }
</script>
<style>
</style>