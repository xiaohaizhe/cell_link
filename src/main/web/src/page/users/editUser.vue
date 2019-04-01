<template>
    <div>
        <cl-header headColor="#181818"></cl-header>
        <sub-header title="操作管理" subtitle="账户编辑"></sub-header>
        <div class="mainContent edit editUser noBorder">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm">
                <el-form-item prop="name" label="账户名">
                    <el-input placeholder="账户名" v-model="ruleForm.name"></el-input>
                </el-form-item>
                <el-form-item prop="pwd" label="密码">
                    <el-input type="password" placeholder="密码" v-model="ruleForm.pwd" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item prop="checkPass" label="确认密码">
                    <el-input type="password" placeholder="确认密码" v-model="ruleForm.checkPass" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item prop="phone" label="手机">
                    <el-input placeholder="手机" v-model="ruleForm.phone"></el-input>
                </el-form-item>
                <el-form-item prop="email" label="邮箱">
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
    import {modifyUser} from 'service/getData'
    import md5 from 'js-md5';

    export default {
        name: 'editUser',
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
                        id:'',
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
        mounted(){
            //解密
            var x = new Buffer(decodeURIComponent(this.$route.params.userData), 'base64')
            var y = x.toString('utf8');
            let userData = JSON.parse(y);
            this.ruleForm.id = userData.id;
            this.ruleForm.name = userData.name;
            this.ruleForm.pwd = userData.pwd;
            this.ruleForm.checkPass = userData.pwd;
            this.ruleForm.phone = userData.phone;
            this.ruleForm.email = userData.email;
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
                let resp = await modifyUser(this.ruleForm);
                if(resp.code==0){
                    this.$message({
                        type: 'success',
                        message: '修改成功!'
                    });
                    this.$router.push("/index")
                }else if(resp.code=="error"){
                    return;
                }else{
                    this.$message({
                        type: 'error',
                        message: '修改失败!'
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
    .editUser .el-form{
        width: 400px;
        margin: 50px auto;
    }
</style>
