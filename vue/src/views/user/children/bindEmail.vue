<template>
    <div style="overflow: hidden;">
        <!-- 未绑定邮箱 -->
        <div v-if="flag==0" class="fullHeight">
            <el-steps :active="active"  align-center class="bgWhite mgbot-20" style="padding: 30px 15% 0;">
                <el-step title="设置邮箱"><i class="shield" slot="icon"/></el-step>
                <el-step title="激活邮箱"><i class="complete" slot="icon"/></el-step>
            </el-steps>
            <div class="bgWhite fullHeight" style="padding: 50px 25%;" v-if="active==0">
                <el-form ref="ruleForm" :model="ruleForm" :rules="rules" label-position="left"  label-width="100px">
                    <el-form-item label="设置邮箱" prop="email">
                        <el-input v-model="ruleForm.email" placeholder="设置邮箱" style="width:300px"></el-input>
                    </el-form-item>
                    <el-form-item label="" >
                        <el-button class="clButton blueBtn" type="primary" @click="submitForm('ruleForm')">发送邮件</el-button>
                    </el-form-item>
                </el-form>
            </div>
            <div class="bgWhite fullHeight center" style="padding: 50px 25%;" v-if="active==1">
                <img :src="success"/>
                <p class="colorGray2 font-16 mgTop-20">请前往您的邮箱激活！</p>
            </div>
        </div>
        
        <!-- 换绑邮箱 -->
        <div v-if="flag==1" class="fullHeight">
            <el-steps :active="active"  align-center class="bgWhite mgbot-20" style="padding: 30px 15% 0;">
                <el-step title="验证身份"><i class="shield" slot="icon"/></el-step>
                <el-step title="设置邮箱"><i class="lock" slot="icon"/></el-step>
                <el-step title="激活邮箱"><i class="complete" slot="icon"/></el-step>
            </el-steps>
            <div class="bgWhite fullHeight" style="padding: 50px 25%;" v-if="active==0">
                <p class="mgbot-40">已绑定手机号：{{user.phone}}</p>
                <div class="cl-flex mgbot-40">
                    <el-input v-model="code" placeholder="请输入手机验证码" class="mgR-15" style="width:300px"></el-input>
                    <el-button class="clButton" @click="verification(user.phone)">{{verifiBtn}}</el-button>
                </div>
                <el-button class="clButton blueBtn " type="primary" @click="nextStep">下一步</el-button>
            </div>
            <div class="bgWhite fullHeight" style="padding: 50px 25%;" v-if="active==1">
                <el-form ref="ruleForm" :model="ruleForm" :rules="rules" label-position="left"  label-width="100px">
                    <el-form-item label="设置邮箱" prop="email">
                        <el-input v-model="ruleForm.email" placeholder="设置邮箱" style="width:300px"></el-input>
                    </el-form-item>
                    <el-form-item label="" >
                        <el-button class="clButton blueBtn" type="primary" @click="submitForm('ruleForm')">发送邮件</el-button>
                    </el-form-item>
                </el-form>
            </div>
            <div class="bgWhite fullHeight center" style="padding: 50px 25%;" v-if="active==2">
                <img :src="success"/>
                <p class="colorGray2 font-16 mgTop-20">请前往您的邮箱激活！</p>
            </div>
        </div>
    </div>
</template>

<script>
    import { mapGetters } from 'vuex'
    import { sendCode , vertifyCode ,sendEmail } from 'api/user'
    export default {
        name: 'editPwd',
        data () {
        return {
                flag:0,
                success:require('assets/success.svg'), 
                code:'',
                active: 0,
                verifing: false,
                verifiBtn: '获取验证码',
                countTime: 60,
                ruleForm:{
                    email:''
                },
                rules: {
                }
            }
        },
        computed: {
            ...mapGetters([
                'user'
            ])
        },
        components:{
        },
        mounted(){
            this.flag = this.$route.params.data;
            this.ruleForm.email = this.user.email;
        },
        methods:{
            //发送验证码
            async verification(){
                let resp = await sendCode(this.user.userId,this.user.phone);
                switch (resp.code){
                    case 0: {
                        this.$alert('验证码已发送', '提示', {
                            confirmButtonText: '确定',
                            callback: action => {
                            }
                        });
                        this.countDown();
                        break;//成功
                    }
                    default: {
                        this.$alert('操作过于频繁，请稍后再试！', '提示', {
                            confirmButtonText: '确定',
                            callback: action => {
                            }
                        });
                        break;//失败
                    }
                    
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
                }else{
                    this.$alert(resp.msg, '提示', {
                        confirmButtonText: '确定',
                        callback: action => {
                        }
                    });
                }
            },
            async submit(){
                let resp = await sendEmail(this.user.userId,this.ruleForm.email);
                if(resp.code==0){
                    this.active++;
                    this.$message({
                        message: resp.msg,
                        type: 'success'
                    });
                }else{
                    this.$alert(resp.msg, '提示', {
                        confirmButtonText: '确定',
                        callback: action => {
                        }
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
    .shield{
        background-image: url('../../../assets/shield.svg')
    }
    .lock{
        background: url('../../../assets/lock.svg')
    }
    .complete{
        background: url('../../../assets/complete.svg')
    }
    .el-step__head.is-wait .lock{
        background: url('../../../assets/locked.svg')
    }
    .el-step__head.is-wait .complete{
        background: url('../../../assets/completed.svg')
    }
</style>