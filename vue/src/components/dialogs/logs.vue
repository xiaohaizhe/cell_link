<template>
    <el-dialog
        title="日志信息"
        :visible.sync="visible"
        width="60%" top='2%'>
        <div class='errorLog'>
            <div v-for="item in logData" :key="item.id">
                <p class='logTime'>{{item.created}}</p>
                <p class='logText' v-if=" item.status==0">正常</p>
                <p class='logText' v-if="item.status==1">异常：&lt; 50% </p>
                <p class='logText' v-if="item.status==2">异常：&gt; 150% </p>
            </div>
        </div>
        <span slot="footer" class="dialog-footer">
            <el-button @click="cancelClick">返 回</el-button>
        </span>
    </el-dialog>
</template>

<script>
  import {getStatusLog} from 'api/ds'
  
  export default {
        name: 'logs',
        data () {
                return{
                    logData:[]
                }
            },
        props:{
            visible:{
                type:Boolean,
                default:false
            },
            datastreamId:{
                type:String
            }
        },
        methods:{
            //获取数据流日志数据
            async getStatusLog(){
                let resp = await getStatusLog(this.datastreamId);//这里是数据流id
                this.logData = resp.data;
            },
        }
    }
    </script>

    <style>
        .errorLog{
            margin: 1.43rem 0;
            padding: 2.14rem 0;
            border: 1px dotted #cccccc;
            background-color: #fcfdff;
            height: 420px;
            overflow: auto;
        }
        .errorLog>div{
            display: flex;
            flex-direction: row;
        }
        .errorLog .logTime{
            padding: 0px 2.14rem;
            flex-shrink: 0;
            border-right: 1px solid #cccccc;
        }
        .errorLog .logText{
            padding-left: 2.14rem;
        }
        .errorLog p{
            padding-bottom: 10px;
        }
    </style>
    