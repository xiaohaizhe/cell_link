<template>
    <el-dialog
        title="编辑用户"
        :visible.sync="isVisible" width="40%">
        <div style="padding:0 10%">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                <el-form-item label="用户名" prop="name">
                    <el-input v-model="ruleForm.name"></el-input>
                </el-form-item>
                <el-form-item label="手机号" prop="phone">
                    <el-input  v-model="ruleForm.phone"></el-input>
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                    <el-input  v-model="ruleForm.email"></el-input>
                </el-form-item>
                <el-form-item class="btnRight">
                    <el-button type="primary" @click="submitForm('ruleForm')">确 定</el-button>
                    <el-button @click="isVisible=false">返 回</el-button>
                </el-form-item>
            </el-form>
        </div>
    </el-dialog>
</template>

<script>
  import { mapGetters } from 'vuex'
  import { updateUser } from 'api/user'

  export default {
        name: 'editUser',
        data () {
            return{
                ruleForm: {
                    name:'',
                    phone:'',
                    email:''
                },
                isVisible:this.dialogVisible,
                rules: {
                    name: [
                        { required: true, message: '请输入用户名', trigger: 'blur' },
                        { min: 4, max: 10, message: '长度在 4 到 10 个字符', trigger: 'blur' }
                    ],
                    phone:[
                        { required: true, message: '请输入手机号', trigger: 'blur' },
                        { pattern: /^((13|14|15|17|18)[0-9]{1}\d{8})$/, message: '请输入正确的手机号', trigger: 'blur' }
                    ],
                    email:[
                        { pattern:/^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z\d]{2,4}$/, message: '请输入正确的邮箱', trigger: 'blur' }
                    ]
                }
            }
        },
        watch:{
            dialogVisible(val){
                this.isVisible = this.dialogVisible
            },
            isVisible(val){
                this.$emit('userDialogVisible', val)
            }
        },
        props:{
            dialogVisible:{
                type:Boolean,
                default:false
            },
            euser:{
                type:Object
            }
        },
        mounted(){
            this.ruleForm.name = this.euser.name;
            this.ruleForm.phone = this.euser.phone;
            this.ruleForm.email = this.euser.email;
        },
        methods:{
            async submit(){
                let resp = await updateUser({...this.ruleForm,userId:this.euser.userId});
                this.$message({
                    message: "修改成功！",
                    type: 'success'
                });
                this.isVisible=false;
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
    