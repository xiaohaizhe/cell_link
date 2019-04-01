<template>
    <div>
        <cl-header headColor="#181818"></cl-header>
        <sub-header title="操作管理" subtitle="添加用户"></sub-header>
        <div class="mainContent addUser add noBorder">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm">
                <el-form-item prop="name" label=" ">
                    <el-input placeholder="账户名" v-model="ruleForm.name"></el-input>
                </el-form-item>
                <el-form-item prop="pwd" label=" ">
                    <el-input type="password" placeholder="密码" v-model="ruleForm.pwd" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item prop="checkPass" label=" ">
                    <el-input type="password" placeholder="二次密码" v-model="ruleForm.checkPass" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item prop="phone" label=" ">
                    <el-input placeholder="手机" v-model="ruleForm.phone"></el-input>
                </el-form-item>
                <el-form-item prop="email" label=" ">
                    <el-input placeholder="邮箱" v-model="ruleForm.email"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button @click="goBack">返回</el-button>
                    <el-button type="primary" @click="submitForm('ruleForm')">确认</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
    import headTop from 'components/header/head'
    import subHead from 'components/subHeader/subHeader'
    import {addUser} from 'service/getData'
    import md5 from 'js-md5';

    export default {
        name: 'addUser',
        data () {
            var validatePass = (rule, value, callback) => {
                if (value === '') {
                callback(new Error('请输入密码'));
                } else {
                if (this.ruleForm.checkPass !== '') {
                    this.$refs.ruleForm.validateField('checkPass');
                }
                callback();
                }
            };
            var validatePass2 = (rule, value, callback) => {
                if (value === '') {
                callback(new Error('请再次输入密码'));
                } else if (value !== this.ruleForm.pwd) {
                callback(new Error('两次输入密码不一致!'));
                } else {
                callback();
                }
            };
            return {
                ruleForm: {
                        name: '',
                        pwd:'',
                        phone: '',
                        email: '',
                        checkPass:'',
                    },
                rules: {
                    name: [
                        { required: true, message: '请输入账户名', trigger: 'blur' }
                    ],
                    pwd: [
                        { required: true, validator: validatePass, trigger: 'blur' }
                    ],
                    phone:[
                        { required: true, message: '请输入手机', trigger: 'blur' }
                    ],
                    checkPass:[
                        { required: true, validator: validatePass2, trigger: 'blur' }
                    ]
                }
            }
        },
        computed:{
        },
        components:{
            'cl-header':headTop,
            'sub-header':subHead,
        },
        methods: {
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
            async submit(){
                this.ruleForm.pwd = md5(this.ruleForm.pwd);
                let resp = await addUser(this.ruleForm);
                if(resp.code==0){
                    this.$message({
                        type: 'success',
                        message: '添加成功!'
                    });
                    this.$router.push("/index")
                }else if(resp.code=="error"){
                    return;
                }else{
                    this.$message({
                        type: 'error',
                        message: '添加失败!'
                    });
                }
            },
            //返回事件
            goBack () {
                window.history.length > 1
                    ? this.$router.go(-1)
                    : this.$router.push('/')
                }
        }

    }
</script>

<style>
    .addUser .el-form{
        width: 400px;
        margin: 50px auto;
    }
</style>
