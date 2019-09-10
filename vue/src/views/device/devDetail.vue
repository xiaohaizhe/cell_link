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
            <el-tab-pane label="设备组" name="devGroup"></el-tab-pane>
            <el-tab-pane label="事件" name="trigger"></el-tab-pane>
            <el-tab-pane label="应用" name="application"></el-tab-pane>
        </el-tabs>
        <router-view></router-view>
        <!-- <edit-scene :dialogVisible="editVisible" v-if="editVisible" @sceneDialogVisible="editSceneVisible"></edit-scene> -->
    </div>
</template>

<script>
    import { mapGetters } from 'vuex'

    export default {
        name: 'devDetail',
        data () {
        return {
            }
        },
        computed: {
            ...mapGetters([
                'activeScene','user'
            ]),
        },
        mounted(){
        },
        methods:{
        }
    }
</script>
<style>
</style>