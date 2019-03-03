<template>
    <el-dialog
        :title="`${data.name}-发送命令`"
        :visible.sync="isVisible" width="40%">
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" class="noBorder add" style="padding:0 10%">
            <el-form-item prop="type" label=" ">
                <el-radio v-model="ruleForm.type" label="0">字符串</el-radio>
                <el-radio v-model="ruleForm.type" label="1">十六进制</el-radio>
            </el-form-item>
            <el-form-item prop="content" label=" ">
                <el-input placeholder="发送命令内容（0/1024）" v-model="ruleForm.content" maxlength="1024"></el-input>
            </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitForm('ruleForm')">确 定</el-button>
            <el-button @click="isVisible = false">返 回</el-button>
        </span>
    </el-dialog>
</template>

<script>
    import {mapState} from 'vuex'
    import {sendCmd} from 'service/getData'
  
  export default {
        name: 'sendOrder',
        data () {
            return{
                isVisible:this.dialogVisible,
                ruleForm: {
                    topic:0,
                    content: '',
                    type:'0'
                },
                rules: {
                    content: [
                        { required: true, message: '请输入发送命令内容', trigger: 'blur' }
                    ]
                }
            }
        },
        props:{
            dialogVisible:{
                type:Boolean,
                default:false
            },
            data:{
                type:Object
            }
        },
        watch:{
            dialogVisible(val){
                this.isVisible = this.dialogVisible
            },
            isVisible(val){
                this.$emit('getSendDialogVisible', val)
            }
        },
        mounted(){
            this.ruleForm.topic = this.data.device_sn;
        },
        methods:{
            async submit(){
                let resp = await sendCmd(this.ruleForm);
                if(resp.code==0){
                    this.$message({
                        message: "发送成功！",
                        type: 'success'
                    });
                    this.isVisible = false;
                }else{
                    this.$message({
                        message: "发送失败！",
                        type: 'error'
                    });
                }
            },
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.submit();
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
        }
    }
    </script>

    <style>
    </style>
    