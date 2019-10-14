<template>
    <div style="overflow: hidden;">
        <el-steps :active="active"  align-center class="bgWhite mgbot-20" style="padding: 30px 15% 0;">
            <el-step title="验证身份"><i class="shield" slot="icon"/></el-step>
            <el-step title="设置新密码"><i class="lock" slot="icon"/></el-step>
            <el-step title="修改完成"><i class="complete" slot="icon"/></el-step>
        </el-steps>
        <div class="bgWhite fullHeight" style="padding: 50px 25%;" v-if="active==0">
            <p class="mgbot-40">已绑定手机号：{{user.phone}}</p>
            <div class="cl-flex mgbot-40">
                <el-input v-model="code" placeholder="请输入手机验证码" class="mgR-15" style="width:300px"></el-input>
                <el-button class="clButton" @click="verification">{{verifiBtn}}</el-button>
            </div>
            <el-button class="clButton blueBtn " type="primary" @click="nextStep">下一步</el-button>
        </div>
        <div class="bgWhite fullHeight" style="padding: 50px 25%;" v-if="active==1">
            <el-form ref="ruleForm" :model="ruleForm" :rules="rules" label-position="left"  label-width="100px">
                <el-form-item label="设置新密码" prop="pass">
                    <!-- <el-input v-model="ruleForm.pass" placeholder="设置新密码" style="width:300px"></el-input> -->
                    <el-input type="password" v-model="ruleForm.pass" placeholder="设置新密码" show-password style="width:300px">
                    </el-input>
                </el-form-item>
                <el-form-item label="确认密码" prop="checkPass">
                    <!-- <el-input v-model="ruleForm.checkPass" placeholder="确认密码" style="width:300px"></el-input> -->
                    <el-input type="password" v-model="ruleForm.checkPass" placeholder="确认密码" show-password style="width:300px">
                    </el-input>
                </el-form-item>
            </el-form>
            <el-button class="clButton blueBtn" type="primary" @click="submitForm('ruleForm')">确定</el-button>
        </div>
        <div class="bgWhite fullHeight center" style="padding: 50px 25%;" v-if="active==2">
            <img :src="success"/>
            <p class="colorGray2 font-16 mgTop-20">修改成功！</p>
            <p class="colorGray font-16 mgTop-20">3s后自动跳转至首页，若未跳转可点击 <span class="colorBlack" @click="$router.push('/')">返回首页</span></p>
        </div>
    </div>
</template>

<script>
    import { mapGetters } from 'vuex'
    import { sendCode , vertifyCode ,modifyUser } from 'api/user'
    export default {
        name: 'editPwd',
        data () {
            var validatePass = (rule, value, callback) => {
                if (value != '') {
                    if(!/^(?=.*\d)(?=.*[a-zA-Z])(?=.*[~!@#$%^&*.,-_])[\da-zA-Z~!@#$%^&*.,-_]{6,16}$/.test(value)){
                        callback(new Error('密码由数字、英文字母、符号组成，长度为6-16个字符'));
                    }else if (this.ruleForm.checkPass !== '') {
                        this.$refs.ruleForm.validateField('checkPass');
                    }
                    callback();
                }
            };
            var validatePass2 = (rule, value, callback) => {
                if (value != '') {
                    if(!/^(?=.*\d)(?=.*[a-zA-Z])(?=.*[~!@#$%^&*.,-_])[\da-zA-Z~!@#$%^&*.,-_]{6,16}$/.test(value)){
                        callback(new Error('密码由数字、英文字母、符号组成，长度为6-16个字符'));
                    }else if (value !== this.ruleForm.pass) {
                        callback(new Error('两次输入密码不一致!'));
                    } else {
                        callback();
                    }
                }
            };
        return {
                success:require('assets/success.svg'), 
                code:'',
                active: 0,
                verifing: false,
                verifiBtn: '获取验证码',
                countTime: 60,
                ruleForm:{
                    pass:'',
                    checkPass:''
                },
                rules: {
                    pass: [
                        { required: true, message: '请输入密码', trigger: 'blur' },
                        { validator: validatePass, trigger: 'blur' }
                    ],
                    checkPass: [
                        { required: true, message: '请再次输入密码', trigger: 'blur' },
                        { validator: validatePass2, trigger: 'blur' }
                    ],
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
            //确认密码,修改user
            async submit(){
                this.$store.dispatch('user/modify',{userId:this.user.userId,pwd:this.ruleForm.pass,phone:this.user.phone,email:'',})
                    .then(() => {
                        let that = this;
                        this.active++;
                        this.$message({
                            message: '修改成功！',
                            type: 'success'
                        });
                        setTimeout(function(){
                            that.$router.push('/')
                        },3000)
                    })
                    .catch(() => {
                        this.$message({
                            message: '修改失败！',
                            type: 'error'
                        });
                    })
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