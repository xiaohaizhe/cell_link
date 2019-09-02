<template>
    <div style="overflow: hidden;">
        <!-- 未绑定邮箱 -->
        <div>
            <el-steps :active="active"  align-center class="bgWhite mgbot-20" style="padding: 30px 15% 0;">
                <el-step title="验证身份"><i class="shield" slot="icon"/></el-step>
                <el-step title="设置新邮箱"><i class="lock" slot="icon"/></el-step>
                <el-step title="修改完成"><i class="complete" slot="icon"/></el-step>
            </el-steps>
            <div class="bgWhite fullHeight" style="padding: 50px 25%;" v-if="active==0">
                <el-form ref="ruleForm" :model="ruleForm" :rules="rules" label-position="left"  label-width="100px">
                    <el-form-item label="设置新密码" prop="pass">
                        <el-input v-model="ruleForm.pass" placeholder="设置新密码" style="width:300px"></el-input>
                    </el-form-item>
                    <el-form-item label="确认密码" prop="checkPass">
                        <el-input v-model="ruleForm.checkPass" placeholder="确认密码" style="width:300px"></el-input>
                    </el-form-item>
                </el-form>
                <el-button class="clButton blueBtn" type="primary" @click="submitForm('ruleForm')">确定</el-button>
            </div>
        </div>
        
        <!-- 修改邮箱 -->

        <!-- <el-steps :active="active"  align-center class="bgWhite mgbot-20" style="padding: 30px 15% 0;">
            <el-step title="验证身份"><i class="shield" slot="icon"/></el-step>
            <el-step title="设置新邮箱"><i class="lock" slot="icon"/></el-step>
            <el-step title="修改完成"><i class="complete" slot="icon"/></el-step>
        </el-steps>
        <div class="bgWhite fullHeight" style="padding: 50px 25%;" v-if="active==0">
            <p class="mgbot-40">已绑定手机号：13299199299</p>
            <div class="cl-flex mgbot-40">
                <el-input v-model="code" placeholder="请输入手机验证码" class="mgR-15" style="width:300px"></el-input>
                <el-button class="clButton">获取验证码</el-button>
            </div>
            <el-button class="clButton blueBtn " type="primary">下一步</el-button>
        </div>
        <div class="bgWhite fullHeight" style="padding: 50px 25%;" v-if="active==1">
            <el-form ref="ruleForm" :model="ruleForm" :rules="rules" label-position="left"  label-width="100px">
                <el-form-item label="设置新密码" prop="pass">
                    <el-input v-model="ruleForm.pass" placeholder="设置新密码" style="width:300px"></el-input>
                </el-form-item>
                <el-form-item label="确认密码" prop="checkPass">
                    <el-input v-model="ruleForm.checkPass" placeholder="确认密码" style="width:300px"></el-input>
                </el-form-item>
            </el-form>
            <el-button class="clButton blueBtn" type="primary" @click="submitForm('ruleForm')">确定</el-button>
        </div>
        <div class="bgWhite fullHeight center" style="padding: 50px 25%;" v-if="active==2">
            <img :src="success"/>
            <p class="colorGray2 font-16 mgTop-20">修改成功！</p>
        </div> -->
    </div>
</template>

<script>
    
    export default {
        name: 'editPwd',
        data () {
        return {
                success:require('assets/success.svg'), 
                code:'',
                active: 0,
                ruleForm:{
                    pass:'',
                    checkPass:''
                },
                rules: {
                }
            }
        },
        components:{
        },
        mounted(){
        },
        methods:{
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                if (valid) {
                    alert('submit!');
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