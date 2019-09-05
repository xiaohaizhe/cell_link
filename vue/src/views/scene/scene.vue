<template>
    <div>
        <div class="bgWhite topCard cl-flex justifyBet">
            <div class="detail">
                <p class="font-24 mgbot-20"><span class="mgR-10">{{activeScene.scenarioName}}</span><i class="el-icon-edit colorGray2 point" @click="editVisible=true"></i></p>
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
        <edit-scene :dialogVisible="editVisible" v-if="editVisible" @sceneDialogVisible="editSceneVisible"></edit-scene>
    </div>
</template>

<script>
    import { mapGetters } from 'vuex'
    import editScene from 'components/dialogs/editScene'

    export default {
        name: 'scene',
        data () {
        return {
                activeName:'devGroup',
                editVisible:false
            }
        },
        components:{
            editScene
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
                this.$router.push('/scene/nav'+this.activeScene.scenarioId+'/'+data.name)
            },
            editSceneVisible(val){
                this.editVisible = val;
            }
        }
    }
</script>
<style>
</style>