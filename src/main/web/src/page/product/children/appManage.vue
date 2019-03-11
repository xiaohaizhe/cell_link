<template>
    <div>
        <div class="flexBtw" style="margin-bottom:20px;">
            <el-input placeholder="输入关键词后按回车键"  v-model="keywords" @keyup.enter.native="getApp()" 
                clearable style="width:320px;height:36px;"></el-input>
            <div>
                <el-button type="primary" @click="addVisible=true">+新建应用</el-button>
            </div>
        </div>
        <div class="apps flexBtw">
            <div class="bg-fff flex cl-card" v-for="item in appDatas" :key="item.id">
                <div class="clock" v-if="item.applicationType==0"></div>
                <div class="survey" v-if="item.applicationType==1"></div>
                <div>
                    <p class="font-18 colorBlack mgbot-10">{{item.name}}</p>
                    <p class="colorGray">创建时间：</p>
                    <p class="colorGray">{{item.createTime}}</p>
                </div>
                <div class="appBtns">
                    <i class="editIcon cl-icon" @click="editDs(scope.row)"></i>
                    <i class="delete cl-icon" @click="deleteDs(scope.row.id)"></i>
                </div>
            </div>
        </div>
        <add-app :dialogVisible="addVisible" v-if='addVisible' @getAddDialogVisible="setAddVisible"></add-app>
    </div>
</template>

<script>
import {getApp} from 'service/getData'
import {mapState} from 'vuex'
import addApp from 'components/dialogs/addApp'

export default {
    name: 'appManage',
    data () {
        return {
            keywords:'test',
            appDatas:[],
            addVisible:false
        }
    },
    computed:{
        ...mapState([
            'product'
        ])
    },
    components:{
        'add-app':addApp
    },
    mounted(){
        this.getApp();
    },
    methods: {
        async getApp(){
            let resp = await getApp(10,this.keywords);//this.product.id
            if(resp.code==0){
                this.appDatas = resp.data;
            }
        },
        //弹出新建
        setAddVisible(val){
            this.addVisible = val;
            this.getApp();
        },
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
        width: 20px;
        height: 20px;
        background-size: cover;
        display: inline-block;
    }
</style>