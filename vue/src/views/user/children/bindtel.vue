<template>
    <div style="overflow: hidden;">
        <el-steps :active="active"  align-center class="bgWhite mgbot-20" style="padding: 30px 15% 0;">
            <el-step title="验证身份"><i class="shield" slot="icon"/></el-step>
            <el-step title="设置新手机号"><i class="lock" slot="icon"/></el-step>
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
            <el-form ref="ruleForm" :model="ruleForm" :rules="rules" label-position="left"  label-width="120px">
                <el-form-item label="设置新手机号" prop="phone">
                    <el-input v-model="ruleForm.phone" placeholder="设置新手机号" style="width:300px"></el-input>
                </el-form-item>
                <el-form-item label="手机验证码" prop="code">
                    <el-input v-model="ruleForm.code" placeholder="手机验证码" style="width:300px"></el-input>
                    <el-button class="clButton">获取验证码</el-button>
                </el-form-item>
            </el-form>
            <el-button class="clButton blueBtn" type="primary" @click="submitForm('ruleForm')">确定</el-button>
        </div>
        <div class="bgWhite fullHeight center" style="padding: 50px 25%;" v-if="active==2">
            <img :src="success"/>
            <p class="colorGray2 font-16 mgTop-20">修改成功！</p>
        </div>
    </div>
</template>

<script>
    
    export default {
        name: 'bindtel',
        data () {
        return {
                success:require('assets/success.svg'), 
                code:'',
                active: 2,
                ruleForm:{
                    phone:'',
                    code:''
                },
                rules: {
                    phone: [
                        { required: true, message: '请输入手机号', trigger: 'blur' },
                        { pattern: /^((13|14|15|17|18)[0-9]{1}\d{8})$/, message: '请输入正确的手机号', trigger: 'blur' }
                    ],
                    code:[
                        { required: true, message: '请输入验证码', trigger: 'blur' }
                    ]
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
 
</style>