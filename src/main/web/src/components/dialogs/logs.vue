<template>
    <el-dialog
        title="日志信息"
        :visible.sync="isVisible"
        width="60%" top='2%'>
        <el-input placeholder="输入关键词后按回车键" v-if="flag"
                v-model="keyword"  clearable style="width:320px;height:36px;" @clear="clearKey()" @keyup.enter.native="changeKey()"></el-input>
        <div class='log'>
            <div v-for="item in logData" :key="item.id">
                <p class='logTime' v-if="flag">{{item.createTime}}</p><p class='logText'>{{item.msg}}</p>
                <p class='logTime' v-if="!flag">{{item.create_time}}</p>
                <p class='logText' v-if="!flag && item.status==0">正常</p>
                <p class='logText' v-if="!flag && item.status==1">异常：&lt; 50% </p>
                <p class='logText' v-if="!flag && item.status==2">异常：&gt; 150% </p>
            </div>
        </div>
        <span slot="footer" class="dialog-footer">
            <el-button @click="isVisible = false">返 回</el-button>
        </span>
    </el-dialog>
</template>

<script>
  import {getOperationLogs,getDsStatusLogs} from 'service/getData'
  
  export default {
        name: 'logs',
        data () {
                return{
                    isVisible:this.dialogVisible,
                    keyword:'',
                    logData:[]
                }
            },
        props:{
            dialogVisible:{
                type:Boolean,
                default:false
            },
            userId:{
                type:Number
            },
            flag:{
                type:Boolean
            }
        },
        mounted(){
            if(this.flag){
                this.getOperationLogs();
            }else{
                this.getDsStatusLogs();
            }
            
        },
        watch:{
            //监听isDialogVisible变更后对外发送事件通知，比如关闭弹窗时值变为false,通过$emit通知父组件的getDialogVisible，根据setDialogVisible方法去设置父组件的值
            dialogVisible(val){
                this.isVisible = this.dialogVisible
            },
            isVisible(val){
                this.$emit('getDialogVisible', val)
            }
        },
        methods:{
            //获取数据流日志数据
            async getDsStatusLogs(){
                let resp = await getDsStatusLogs(this.userId);//这里是数据流id
                if(resp.code==0){
                    this.logData = resp.data;
                }else if(resp.code=="error"){
                    return;
                }else{
                    this.$message({
                        message: "获取日志数据失败！",
                        type: 'error'
                    });
                }
            },
            async getOperationLogs(){
                let resp = await getOperationLogs(this.userId,this.keyword);
                if(resp.code==0){
                    this.logData = resp.data;
                }else if(resp.code=="error"){
                    return;
                }else{
                    this.$message({
                        message: "获取数据失败！",
                        type: 'error'
                    });
                }
                
            },
            changeKey(){
                let flag  = /[`~!@#$^&*()=|{}':;',\\\[\]\.<>\/?~！@#￥……&*（）——|{}【】'；：""'。，、？\s]/g.test(this.keyword);
                if(!flag){
                    this.getOperationLogs();
                }else{
                    this.$alert('搜索内容不能包括特殊字符或空格！', '提示', {
                        confirmButtonText: '确定',
                        callback: action => {
                        }
                    });
                }
            },
            clearKey(){
                this.getOperationLogs();
            },
        }
    }
    </script>

    <style>
        .log{
            margin: 1.43rem 0;
            padding: 2.14rem 0;
            border: 1px dotted #cccccc;
            background-color: #fcfdff;
            height: 420px;
            overflow: auto;
        }
        .log>div{
            display: flex;
            flex-direction: row;
        }
        .log .logTime{
            padding: 0px 2.14rem;
            flex-shrink: 0;
            border-right: 1px solid #cccccc;
        }
        .log .logText{
            padding-left: 2.14rem;
        }
        .log p{
            padding-bottom: 10px;
        }
    </style>
    