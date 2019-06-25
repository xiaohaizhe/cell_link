<template>
    <div>
        <div class="flexBtw" style="margin-bottom:1.43rem;">
            <el-input placeholder="输入关键词后按回车键"  v-model="keywords" @keyup.enter.native="changeKey()" 
                clearable style="width:320px;height:36px;"  @clear="clearKey()" ></el-input>
            <div>
                <el-button type="primary" @click="addVisible=true">+新建应用</el-button>
            </div>
        </div>
        <div class="apps flexBtw">
            <div class="bg-fff cl-flex cl-card" v-for="item in appDatas" :key="item.id">
                <router-link :to="{path:'/publish/'+item.id}">
                    <div class="cl-flex">
                        <div class="clock cl-cardIcon" v-if="item.applicationType==0"></div>
                        <div class="survey cl-cardIcon" v-if="item.applicationType==1"></div>
                        <div>
                            <p class="font-18 colorBlack mgbot-10 ellipsis" style="width:120px" :title="item.name">{{item.name}}</p>
                            <p class="colorGray">创建时间：</p>
                            <p class="colorGray">{{item.createTime}}</p>
                        </div>
                    </div>
                </router-link>
                <div class="appBtns">
                    <i class="editIcon cl-icon" @click="editApp(item)"></i>
                    <i class="delete cl-icon" @click="deleteItem(item.id)"></i>
                </div>
            </div>
        </div>
        <add-app :dialogVisible="addVisible" v-if='addVisible' @getAddDialogVisible="setAddVisible"></add-app>
        <edit-app :dialogVisible="editVisible" v-if='editVisible' :data="editData" @getEditDialogVisible="setEditVisible"></edit-app>
    </div>
</template>

<script>
import {getApp,delApp} from 'service/getData'
import {mapState} from 'vuex'
import addApp from 'components/dialogs/addApp'
import editApp from 'components/dialogs/editApp'
import triggerVue from '../../trigger/trigger.vue';

export default {
    name: 'appManage',
    data () {
        return {
            keywords:'',
            appDatas:[],
            addVisible:false,
            editVisible:false,
            editData:[]
        }
    },
    computed:{
        ...mapState([
            'product'
        ])
    },
    components:{
        'add-app':addApp,
        'edit-app':editApp
    },
    mounted(){
        if(this.$route.params.editVisible===true){
            this.editData = this.$route.params.data;
            this.editVisible = this.$route.params.editVisible;
        }
        this.getApp();
    },
    methods: {
        async getApp(){
            let resp = await getApp(this.product.id,encodeURI(this.keywords));//this.product.id
            if(resp.code==0){
                this.appDatas = resp.data;
            }else if(resp.code=="error"){
                return;
            }else{
                this.$message({
                    message: "获取数据失败",
                    type: 'error'
                });
            }
        },
        clearKey(){
            this.getApp();
        },
        //弹出新建
        setAddVisible(val){
            this.addVisible = val;
            this.getApp();
        },
        //弹出编辑
        setEditVisible(val){
            this.editVisible = val;
            this.getApp();
        },
        editApp(data){
            this.editVisible = true;
            this.editData= data;
        },
        changeKey(){
            let flag  = /[`~!@#$^&*()=|{}':;',\\\[\]\.<>\/?~！@#￥……&*（）——|{}【】'；：""'。，、？\s]/g.test(this.keywords);
            if(!flag){
                this.getApp();
            }else{
                this.$alert('搜索内容不能包括特殊字符或空格！', '提示', {
                    confirmButtonText: '确定',
                    callback: action => {
                    }
                });
            }
        },
        //删除app
        deleteItem(id){
            this.$confirm('删除应用后，相关数据流等资源将会被全部删除，且无法恢复。确定要删除设备吗？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.deleteApp(id);
            })
        },
        async deleteApp(id){
            let resp = await delApp(id);
            if(resp.code==0){
                this.$message({
                    type: 'success',
                    message: '删除成功!'
                });
                this.getApp();
            }else if(resp.code=="error"){
                return;
            }else{
                this.$message({
                    type: 'error',
                    message: '删除失败!'+resp.msg
                });
            }
        }
    }

}
</script>

<style>
    .apps .cl-card{
        position: relative;
    }
    .appBtns{
        position: absolute;
        right: 15px;
        top: 15px;
    }
    .appBtns i{
        width: 1.43rem;
        height: 1.43rem;
        background-size: cover;
        display: inline-block;
    }
</style>