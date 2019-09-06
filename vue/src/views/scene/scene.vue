<template>
    <div>
        <div class="bgWhite topCard cl-flex justifyBet">
            <div class="detail">
                <p class="font-24 mgbot-20"><span class="mgR-10">{{activeScene.scenarioName}}</span><i class="el-icon-edit colorGray2 point" @click="editVisible=true"></i></p>
                <p class="colorGray font-14">{{activeScene.description}}</p>
            </div>
            <div>
                <el-button type="warning" class="clButton" icon="el-icon-delete"  @click="deleteItem()">删除场景</el-button>
                <el-button type="primary" class="clButton">添加设备组</el-button>
                <el-button type="primary" class="clButton">添加触发器</el-button>
                <el-button type="primary" class="clButton">添加应用</el-button>
            </div>
        </div>
        <el-tabs v-model="activeName" @tab-click="onTabClick">
            <el-tab-pane label="设备组" name="devGroup"></el-tab-pane>
            <el-tab-pane label="事件" name="trigger"></el-tab-pane>
            <el-tab-pane label="应用" name="application"></el-tab-pane>
        </el-tabs>
        <router-view :key=" key"></router-view>
        <edit-scene :dialogVisible="editVisible" v-if="editVisible" @sceneDialogVisible="editSceneVisible"></edit-scene>
    </div>
</template>

<script>
    import { mapGetters } from 'vuex'
    import { deleteScene } from 'api/scene'

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
            ]),
             key(){
                return this.$route.path+new Date()
            }
        },
        created(){
        },
        methods:{
            onTabClick(data){
                this.$router.push('/scene/'+this.activeScene.scenarioId+'/'+data.name)
            },
            editSceneVisible(val){
                this.editVisible = val;
            },
            deleteItem(){
                this.$confirm('删除场景后，相关数据将会被全部删除，且无法恢复。确定要删除场景吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteScene()
                })
            },
            async deleteScene(){
                let resp = await deleteScene(this.activeScene.scenarioId);
                this.$message({
                    message: "删除成功",
                    type: 'success'
                });
                this.$store.dispatch('user/getAside');
                this.$router.push('/')
            },
        }
    }
</script>
<style>
</style>