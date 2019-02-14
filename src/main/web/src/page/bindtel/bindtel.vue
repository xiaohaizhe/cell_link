<template>
    <div>
        <cl-header headColor="#181818"></cl-header>
        <sub-header title="个人中心" subtitle="手机换绑"></sub-header>
        <div class="mainContent">
            <el-steps :active="active" finish-status="success" align-center>
                <el-step title="身份验证"></el-step>
                <el-step title="重新绑定"></el-step>
                <el-step title="修改完成"></el-step>
            </el-steps>
            <div class="editpsw">
                <div class="inner" v-if="active==0">
                    <p>已绑定手机号: {{phoned}}</p>
                    <div class="flexBtw">
                        <el-input placeholder="输入验证码" v-model="code" clearable></el-input>
                        <el-button type="primary" style="margin-bottom: 10px;background:#fff;color:#409EFF" @click="verification" :disabled="verifing">{{verifiBtn}}</el-button>
                    </div>
                    <el-button type="primary" style="width: 100%;height:50px;margin-top:40px" @click="nextStep">下一步</el-button>
                </div>
                <div class="inner" v-if="active==1">
                    <el-input placeholder="请输入新手机号" v-model="phone" clearable></el-input>
                    <div class="flexBtw">
                        <el-input placeholder="输入验证码" v-model="code" clearable></el-input>
                        <el-button type="primary" style="margin-bottom: 10px;background:#fff;color:#409EFF" @click="verification" :disabled="verifing">{{verifiBtn}}</el-button>
                    </div>
                    <el-button type="primary" style="width: 100%;height:50px;margin-top:40px" @click="submit">确认</el-button>
                </div>
                <div class="inner" v-if="active==2">
                    <p style="text-align: center;">重新绑定成功！返回
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
    import {sendCode , vertifyCode} from 'service/getData'

    export default {
        name: 'bindtel',
        data () {
            return {
                active: 0,
                code: '',
                verifing: false,
                verifiBtn: '获取验证码',
                countTime: 60,
                reg:11 && /^((13|14|15|17|18)[0-9]{1}\d{8})$/
            }
        },
        components:{
            'cl-header':headTop,
            'sub-header':subHead
        },
        computed:{
            ...mapState([
                'phone',
                'userId'
            ]),
            phoned: function(){
                return this.phone.substr(0,3) + '****' + this.phone.substr(7,4)
            }
        },
        methods: {
            //发送验证码
            async verification(){
                if(this.phone !='' && this.reg.test(this.phone)){
                    let resp = await sendCode(this.userId,this.phone);
                    switch (resp.code){
                        case 0: this.open("验证码已发送");this.countDown();break;//成功
                        default: this.open("操作过于频繁，请稍后再试！");break;//失败
                    }
                }else{
                    this.open("请正确填写手机号！");
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
            //确认
            async submit(){
                if(this.phone !='' && this.code !='' && this.reg.test(this.phone)){
                    let resp = await vertifyCode(this.userId,this.phone,this.code);
                    if(resp.code==0){
                        this.active++;
                        this.$store.commit('HANDLE_USER', {userData:{phone:this.phone}});
                    }else{
                        this.open(resp.msg);
                    }
                }else{
                    this.open('请正确填写手机号及验证码！');
                }
                
            },
            //下一步
            async nextStep(){
                let resp = await vertifyCode(this.userId,this.phone,this.code);
                if(resp.code==0){
                    this.active++;
                    this.phone='';
                    this.code='';
                }else{
                    this.open(resp.msg);
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
