<template>
    <div>
        <cl-header headColor="#181818"></cl-header>
        <sub-header title="个人中心" subtitle="手机换绑" v-on:direct="navDirect"></sub-header>
        <div class="mainContent">
            <el-steps :active="active" finish-status="success" align-center>
                <el-step title="身份验证"></el-step>
                <el-step title="重新绑定"></el-step>
                <el-step title="修改完成"></el-step>
            </el-steps>
            <div class="editpsw">
                <div class="inner" v-if="active==0">
                    <p>已绑定手机号: {{phoned}}</p>
                    <div class="cl-flex">
                        <v-text-field placeholder="输入验证码" v-model="code" required></v-text-field>
                        <el-button type="primary" style="margin-bottom: 10px;background:#fff;color:#409EFF" @click="verification" :disabled="verifing">{{verifiBtn}}</el-button>
                    </div>
                    <el-button type="primary" style="width: 100%;height:50px;margin-top:40px" @click="nextStep">下一步</el-button>
                </div>
                <div class="inner" v-if="active==1">
                    <v-text-field placeholder="请输入新手机号" v-model="user.phone" required></v-text-field>
                    <div class="cl-flex">
                        <v-text-field placeholder="输入验证码" v-model="code" required></v-text-field>
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
    import {sendCode , vertifyCode,modifyPwd} from 'service/getData'

    export default {
        name: 'bindtel',
        data () {
            return {
                active: 0,
                code: '',
                verifing: false,
                verifiBtn: '获取验证码',
                countTime: 60,
                flag:false,
                reg:11 && /^((13|14|15|17|18)[0-9]{1}\d{8})$/
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
                if(this.user.phone !='' && this.reg.test(this.user.phone)){
                    let resp = await sendCode(this.user.userId,this.user.phone);
                    switch (resp.code){
                        case 0: this.open("验证码已发送");this.countDown();break;//成功
                        case 'error':break;
                        default: this.open(resp.msg);break;//失败
                    }
                }else{
                    this.open("请正确填写手机号！");
                }
            },
            //倒计时
            countDown() {
                this.verifing = true;
                this.flag = false;
                var clock = window.setInterval(() => {
                    this.countTime--
                    this.verifiBtn = this.countTime + 's后重新发送'
                    if (this.countTime < 0 || this.flag) {     //当倒计时小于0时清除定时器
                    window.clearInterval(clock)
                    this.verifiBtn = '发送验证码';
                    this.verifing = false;
                    this.countTime = 60;
                    }
                },1000)
            },
            //确认
            async submit(){
                if(this.user.phone !='' && this.code !='' && this.reg.test(this.user.phone)){
                    let resp = await vertifyCode(this.user.userId,this.user.phone,this.code);
                    if(resp.code==0){
                        let res = await modifyPwd(this.user.userId,this.user.pwd,this.user.phone,this.user.userName);
                        if(res.code==0){
                            this.active++;
                            this.$message({
                                message: res.msg,
                                type: 'success'
                            });
                            this.$store.commit('EDIT_USER', {phone:this.user.phone});
                        }else if(res.code=="error"){
                            return;
                        }else{
                            this.open(res.msg);
                        }
                    }else if(resp.code=="error"){
                        return;
                    }else{
                        this.open(resp.msg);
                    }
                }else{
                    this.open('请正确填写手机号及验证码！');
                }
                
            },
            //下一步
            async nextStep(){
                let resp = await vertifyCode(this.user.userId,this.user.phone,this.code);
                if(resp.code==0){
                    this.$message({
                        message: resp.msg,
                        type: 'success'
                    });
                    this.active++;
                    this.user.phone='';
                    this.code='';
                    this.flag = true;
                }else if(resp.code=="error"){
                    return;
                }else{
                    this.open(resp.msg);
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
        margin-top: 1.43rem;
    }
</style>
