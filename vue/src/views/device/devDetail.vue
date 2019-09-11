<template>
    <div>
        <div class="bgWhite topCard cl-flex justifyBet">
            <div class="detail">
                <p class="font-24 mgbot-20"><span class="mgR-10">{{activeScene.deviceName}}</span><i class="el-icon-edit colorGray2 point" @click="editVisible=true"></i></p>
                <div class="cl-flex mgbot-10">
                    <p class="colorBlack font-14" style="width:220px">设备ID：<span class="colorGray">{{activeScene.dgId}}</span></p>
                    <p class="colorBlack font-14 " style="width:220px">接入方式：<span class="colorGray">{{activeScene.protocol}}</span></p>
                    <p class="colorBlack font-14 ">设备位置：<span class="colorGray">{{activeScene.latitude}}°N {{activeScene.longitude}}°</span></p>
                </div>
                <div class="cl-flex mgbot-10">
                    <p class="colorBlack font-14" style="width:220px">鉴权信息： <span class="colorGray">{{activeScene.devicesn}}</span></p>
                    <p class="colorBlack font-14">创建时间： <span class="colorGray">{{activeScene.created}}</span></p>
                </div>
                <p class="colorBlack font-14 mgbot-10">设备描述： <span class="colorGray">{{activeScene.description}}</span></p>
            </div>
            <div>
                <el-button type="warning" class="clButton" icon="el-icon-delete"  @click="deleteItem()">删除设备</el-button>
                <el-button type="primary" class="clButton">下发命令</el-button>
            </div>
        </div>
        <el-tabs v-model="activeName" @tab-click="onTabClick">
            <el-tab-pane label="数据流展示" name="dataStream"></el-tab-pane>
            <el-tab-pane label="下发日志" name="orderLog"></el-tab-pane>
        </el-tabs>
        <router-view></router-view>
        <edit-device :dialogVisible="editVisible" v-if="editVisible" @devDialogVisible="editDevVisible"></edit-device>
    </div>
</template>

<script>
    import { mapGetters } from 'vuex'
    import { deleteDev} from 'api/dev'
    import editDevice from 'components/dialogs/editDevice'
    export default {
        name: 'devDetail',
        data () {
            return {
                activeName:'dataStream',
                editVisible:false
            }
        },
        computed: {
            ...mapGetters([
                'activeScene','user'
            ]),
        },
        mounted(){
        },
        components:{
            editDevice
        },
        methods:{
            editDevVisible(val){
                this.editVisible = val;
            },
            onTabClick(data){
                this.$router.push('/device/'+this.activeScene.deviceId+'/'+data.name)
            },
            deleteItem(){
                this.$confirm('删除设备后，相关数据将会被全部删除，且无法恢复。确定要删除设备吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteDev()
                })
            },
            async deleteDev(){
                let resp = await deleteDev(this.activeScene.deviceId);
                this.$message({
                    message: "删除成功",
                    type: 'success'
                });
                this.$router.push('/devGroup/'+this.activeScene.dgId)
            },
        }
    }
</script>
<style>
</style>