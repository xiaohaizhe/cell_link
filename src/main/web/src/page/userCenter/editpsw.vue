<template>
    <div>
        <cl-header headColor="#181818"></cl-header>
        <sub-header title="个人中心" subtitle="修改密码" v-on:direct="navDirect"></sub-header>
        <div class="mainContent">
            <el-steps :active="active" finish-status="success" align-center>
                <el-step title="身份验证"></el-step>
                <el-step title="设置新密码"></el-step>
                <el-step title="修改完成"></el-step>
            </el-steps>
            <div class="editpsw">
                <div class="inner" v-if="active==0">
                    <p>已绑定手机号:{{phoned}}</p>
                    <div class="cl-flex">
                        <v-text-field placeholder="请输入绑定手机验证码" v-model="code" required></v-text-field>
                        <el-button type="primary" style="margin-bottom: 10px;background:#fff;color:#409EFF" @click="verification" :disabled="verifing">{{verifiBtn}}</el-button>
                    </div>
                    <el-button type="primary" style="width: 100%;height:50px;margin-top:40px" @click="nextStep">下一步</el-button>
                </div>
                <div class="inner" v-if="active==1">
                    <v-form  ref="ruleForm" v-model="valid" data-app="true">
                        <v-container fluid grid-list-md>
                            <v-layout row wrap>
                                <v-flex xs12>
                                    <v-text-field type="password" label="新密码" hint="*必填" v-model="newPwd" :rules="pwdRules" required></v-text-field>
                                </v-flex>
                            </v-layout>
                            <v-layout row wrap>
                                <v-flex xs12>
                                    <v-text-field type="password" label="确认密码" hint="*必填" v-model="confirmPwd" :rules="checkPassRules" required></v-text-field>
                                </v-flex>
                            </v-layout>
                        </v-container>
                    </v-form>
                    <!-- <el-input placeholder="请输入新密码" type="password" v-model="newPwd" clearable></el-input>
                    <el-input placeholder="请确认密码" type="password" v-model="confirmPwd" clearable></el-input> -->
                    <el-button type="primary" style="width: 100%;height:50px;margin-top:40px" @click="submitForm">确认</el-button>
                </div>
                <div class="inner" v-if="active==2">
                    <p style="text-align: center;">密码修改成功！返回
                        <router-link to="/user">
                            <el-button type="text" style="padding-bottom: 0;">个人中心</el-button>
                        </router-link>
                    </p>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import headTop from 'components/header/head'
    import subHead from 'components/subHeader/subHeader'
    import {mapState} from 'vuex'
    import md5 from 'js-md5';
    import { sendCode , vertifyCode , modifyPwd } from 'service/getData'

    export default {
        name: 'editpsw',
        data () {
            return {
                valid:false,
                active: 0,
                code: '',
                verifing: false,
                verifiBtn: '获取验证码',
                countTime: 60,
                newPwd:'',
                confirmPwd:'',
                pwdRules: [
                    v => !!v || '请输入密码',
                    // v => (!v || (v.length>=6 && v.length<=16)) || '密码的长度为6-16个字符',
                    v => (!v || /^[a-zA-Z]\w{5,15}$/g.test(v)) || '密码必须以字母开头，长度在6-16之间，只能包含字符、数字和下划线',
                ],
                checkPassRules: [
                    v => !!v || '请再次输入密码',
                    // v => (!v || (v.length>=6 && v.length<=16)) || '密码的长度为6-16个字符',
                    v => (!v || /^[a-zA-Z]\w{5,15}$/g.test(v)) || '密码必须以字母开头，长度在6-16之间，只能包含字符、数字和下划线',
                    v => v==this.newPwd || '两次输入密码不一致'
                ],
            }
        },
        components:{
            'cl-header':headTop,
            'sub-header':subHead
        },
        computed:{
            ...mapState([
                'user'
            ]),
            phoned: function(){
                return this.user.phone.substr(0,3) + '****' + this.user.phone.substr(7,4)
            }
        },
        methods: {
            //发送验证码
            async verification(){
                let resp = await sendCode(this.user.userId,this.user.phone);
                switch (resp.code){
                    case 0: this.open("验证码已发送");this.countDown();break;//成功
                    case 'error':break;
                    default: this.open("操作过于频繁，请稍后再试！");break;//失败
                }
            },
            //倒计时
            countDown() {
                this.verifing = true;
                let clock = window.setInterval(() => {
                    this.countTime--
                    this.verifiBtn = this.countTime + 's后重新发送'
                    if (this.countTime < 0) {     //当倒计时小于0时清除定时器
                    window.clearInterval(clock)
                    this.verifiBtn = '发送验证码';
                    this.verifing = false;
                    this.countTime = 60;
                    }
                },1000)
            },
            //下一步
            async nextStep(){
                let resp = await vertifyCode(this.user.userId,this.user.phone,this.code);
                if(resp.code==0){
                    this.active++;
                    this.$message({
                        message: resp.msg,
                        type: 'success'
                    });
                }else if(resp.code=="error"){
                    return;
                }else{
                    this.open(resp.msg);
                }
            },
            submitForm() {
                if (this.$refs.ruleForm.validate()) {
                    this.submit();
                }else{
                    console.log('error submit!!');
                    return false;
                }
            },
            //确认密码
            async submit(){
                if(this.newPwd===this.confirmPwd){
                    let resp = await modifyPwd(this.user.userId,md5(this.newPwd),this.user.phone,this.user.userName);
                    if(resp.code==0){
                        this.active++;
                        this.$message({
                            message: resp.msg,
                            type: 'success'
                        });
                    }else if(resp.code=="error"){
                        return;
                    }else{
                        this.open(resp.msg);
                    }
                }else{
                    this.open('密码输入不一致，请重新输入！');
                    this.newPwd='';
                    this.confirmPwd='';
                }
            },
            navDirect(){
                this.$router.push('/user');
            },
            //提示消息
            open(msg) {
                this.$alert(msg, '提示', {
                    confirmButtonText: '确定',
                    callback: action => {
                    }
                });
            }
        }

    }
</script>

<style>
    .mainContent .el-steps{
        margin: 50px auto 0;
        width: 50%;
        
    }
    .editpsw{
        border: 1px dotted #cccccc;
        margin: 40px 100px;
        background-color: #fcfdff;
    }
    .editpsw .inner{
        width: 50%;
        margin: 80px auto;
    }
    .editpsw .inner>div{
        /* border-bottom: 1px solid; */
        margin-top: 1.43rem;
    }
</style>
