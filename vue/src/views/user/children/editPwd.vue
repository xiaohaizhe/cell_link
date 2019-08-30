<template>
    <div style="overflow: hidden;">
        <el-steps :active="active"  align-center class="bgWhite mgbot-20" style="padding-top:30px">
            <el-step title="验证身份"><i class="shield" slot="icon"/></el-step>
            <el-step title="设置新密码"><i class="lock" slot="icon"/></el-step>
            <el-step title="修改完成"><i class="complete" slot="icon"/></el-step>
        </el-steps>
        <div class="bgWhite fullHeight" style="padding:50px 20%">
            <p class="mgbot-40">已绑定手机号：13299199299</p>
            <div class="cl-flex mgbot-40">
                <el-input v-model="code" placeholder="请输入手机验证码" class="mgR-15" style="width:300px"></el-input>
                <el-button class="clButton">获取验证码</el-button>
            </div>
            <el-button class="clButton blueBtn " type="primary">下一步</el-button>
        </div>
    </div>
</template>

<script>
    
    export default {
        name: 'editPwd',
        data () {
        return {
            // shield:require('assets/shield.svg'),
            // lock:require('assets/lock.svg'),
            // complete:require('assets/complete.svg'),
            active: 1,
            }
        },
        components:{
        },
        mounted(){
        },
        methods:{
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