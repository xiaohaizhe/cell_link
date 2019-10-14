<template>
    <el-dialog
        title="下发命令"
        :visible.sync="visible" width="40%">
        <div style="padding:0 10%">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                <el-form-item label="命令类型" prop="type">
                    <el-radio-group v-model="ruleForm.type">
                        <el-radio label="0">字符串</el-radio>
                        <el-radio label="1">十六进制</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="命令内容" prop="content">
                    <el-input type="textarea" v-model="ruleForm.content" maxlength="100" show-word-limit></el-input>
                </el-form-item>
                <el-form-item class="btnRight">
                    <el-button type="primary" @click="submitForm('ruleForm')">确 定</el-button>
                    <el-button @click="cancelClick">返 回</el-button>
                </el-form-item>
            </el-form>
        </div>
    </el-dialog>
</template>

<script>
  import { sendcmd } from 'api/dev'

  export default {
        name: 'sendcmd',
        data () {
            return{
                ruleForm: {
                    type:"0",
                    content: ""
                },
                rules: {
                    type: [
                        { required: true, message: '请选择命令类型', trigger: 'change' },
                    ],
                    content:[
                        { required: true, message: '请输入命令内容', trigger: 'blur' },
                        { max: 100, message: '命令内容的最大长度为100', trigger: 'blur' }
                    ]
                }
            }
        },
        props:{
            visible:{
                type:Boolean,
                default:false
            },
            deviceId:{
                type:String
            }
        },
        methods:{
            okClick: () => {
                this.$emit('onOk')
            },
            cancelClick: () => {
                this.$emit('onCancel')
            },
            async submit(){
                let resp = await sendcmd({...this.ruleForm,deviceId:this.deviceId});
                this.$message({
                    message: "发送命令成功！",
                    type: 'success'
                });
                this.okClick();
            },
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.submit()
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
    