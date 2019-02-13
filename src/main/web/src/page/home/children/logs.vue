<template>
    <el-dialog
        title="日志信息"
        :visible.sync="dialogVisible"
        width="60%">
        <el-input placeholder="输入关键词后按回车键"
                    clearable style="width:320px;height:36px;"></el-input>
        <div>
            
        </div>
        <span slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">返 回</el-button>
        </span>
    </el-dialog>
</template>

<script>
  import {getOperationLogs} from 'service/getData'

  export default {
        name: 'logs',
        data () {
                return{
                }
            },
        props:{
            dialogVisible:{
                type:Boolean,
                default:false
            }
        }
    }
    </script>