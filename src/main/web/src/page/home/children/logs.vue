<template>
    <el-dialog
        title="日志信息"
        :visible.sync="isVisible"
        width="60%" top='2%'>
        <el-input placeholder="输入关键词后按回车键"
                v-model="keyword"  clearable style="width:320px;height:36px;" @keyup.enter.native="getOperationLogs()"></el-input>
        <div class='log'>
            <div v-for="item in logData" :key="item.id">
                <p class='logTime'>{{item.createTime}}</p><p class='logText'>{{item.msg}}</p>
            </div>
        </div>
        <span slot="footer" class="dialog-footer">
            <el-button @click="isVisible = false">返 回</el-button>
        </span>
    </el-dialog>
</template>

<script>
  import {getOperationLogs} from 'service/getData'
  import {mapState} from 'vuex'
  
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
            }
        },
        computed:{
            ...mapState([
                'userId'
            ]),
        },
        mounted(){
            this.getOperationLogs();
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
            async getOperationLogs(){
                let resp = await getOperationLogs(this.userId,this.keyword);
                this.logData = resp.data;
            }
        }
    }
    </script>

    <style>
        .log{
            margin: 20px 0;
            padding: 30px 0;
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
            padding: 0px 30px;
            flex-shrink: 0;
            border-right: 1px solid #cccccc;
        }
        .log .logText{
            padding-left: 30px;
        }
        .log p{
            padding-bottom: 10px;
        }
    </style>
    