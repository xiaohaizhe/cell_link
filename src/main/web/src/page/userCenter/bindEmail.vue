<template>
    <div>
        <cl-header headColor="#181818"></cl-header>
        <sub-header title="个人中心" subtitle="邮箱绑定"></sub-header>
        <div class="mainContent">
            <div class="editpsw"> 
                <div class="inner" v-if="active==0">
                    <el-input placeholder="请输入绑定邮箱" type="email" v-model="email" clearable></el-input>
                    <div class="flexBtw">
                        <el-input placeholder="请输出邮箱验证码" v-model="code" clearable></el-input>
                        <el-button type="primary" style="margin-bottom: 10px;background:#fff;color:#409EFF" @click="verification" :disabled="verifing">{{verifiBtn}}</el-button>
                    </div>
                    <el-button type="primary" style="width: 100%;height:50px;margin-top:40px" @click="bind">立即绑定</el-button>
                </div>
                <div class="inner" v-if="active==1">
                    <p style="text-align: center;">绑定成功！返回
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
    import {sendEmail,vertifyEmail} from 'service/getData'

    export default {
        name: 'bindEmail',
        data () {
            return {
                reg:/^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/,
                email:'',
                code:'',
                active: 0,
                verifyCode: '',
                verifing: false,
                verifiBtn: '获取验证码',
                countTime: 60
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
        },
        methods: {
            //发送验证码
            async verification(){
                if(this.email !='' && this.reg.test(this.email)){
                    let resp = await sendEmail(this.user.userId,this.email);
                    switch (resp.code){
                        case 0: this.open("验证码已发送");this.countDown();break;//成功
                        case 'error':break;
                        default: this.open("操作过于频繁，请稍后再试！");break;//失败
                    }
                }else{
                    this.open("请正确填写邮箱！");
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
            //绑定
            async bind(){
                if(this.email !='' && this.code !='' && this.reg.test(this.email)){
                    let resp = await vertifyEmail(this.user.userId,this.email,this.code);
                    if(resp.code==0){
                        this.active++;
                        this.$message({
                            message: resp.msg,
                            type: 'success'
                        });
                        this.$store.commit('HANDLE_USER', {isvertifyemail:1});
                    }else if(resp.code=="error"){
                        return;
                    }else{
                        this.open(resp.msg);
                    }
                }else{
                    this.open('请正确填写邮箱及验证码！');
                }
                
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
    .editpsw input{
        padding: 0 !important;
        border: none !important;
    }
    .editpsw .inner{
        width: 50%;
        margin: 80px auto;
    }
    .editpsw .inner>div{
        border-bottom: 1px solid;
        margin-top: 20px;
    }
</style>
