<template>
    <el-dialog
        title="新建应用"
        :visible.sync="visible" width="40%">
        <div style="padding:0 10%">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                <el-form-item label="应用名称" prop="appName">
                    <el-input v-model="ruleForm.appName"></el-input>
                </el-form-item>
                <el-form-item label="应用简介" prop="description">
                    <el-input type="textarea" v-model="ruleForm.description" maxlength="100" show-word-limit></el-input>
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
  import { mapGetters } from 'vuex'
  import { editApp } from 'api/application'

  export default {
        name: 'editApp',
        data () {
            return{
                ruleForm: {
                    appName:"",
                    description: '',
                },
                rules: {
                    appName: [
                        { required: true, message: '请输入应用名称', trigger: 'blur' },
                        { min: 4, max: 10, message: '长度在 4 到 10 个字符', trigger: 'blur' }
                    ],
                    description:[
                        { max: 100, message: '应用简介的最大长度为100', trigger: 'blur' }
                    ]
                }
            }
        },
        props:{
            visible:{
                type:Boolean,
                default:false
            },
            appData:{
                type:Array
            }
        },
        mounted(){
        },
        methods:{
            okClick: () => {
                this.$emit('onOk')
            },
            cancelClick: () => {
                this.$emit('onCancel')
            },
            async submit(){
                let resp = await editApp({...this.ruleForm,appId:this.appData.appId});
                this.$message({
                    message: "修改成功！",
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
    