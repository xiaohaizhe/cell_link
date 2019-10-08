<template>
    <div style="padding:0 1rem">
        <p class="mgbot-20">创建账户：Cell-link平台尚未开放公众注册，向管理员提供用户名、密码及手机号码，管理员即可创建新账号。</p>
        <p class="mgbot-20">登录：进入cell-link主页，点击右上角选择“用户登录”会进入登录画面</p>
        <img :src="loginPic"  class="mgbot-20" style="width: 100%;"/>
        <ul class="mgbot-20">
          <li class="mgbot-20">1.若该用户第一次登陆，需验证手机号，发送验证码并填写正确的验证码，点击“立即登录”，会跳转至用户首页；</li>
          <li class="mgbot-20">2.若该用户不是第一次登陆，点击“立即登录”，会跳转至用户首页；</li>
          <li class="mgbot-20">3.若勾选“自动登录”，则后期登陆系统不需要再登陆；若不勾选，则会5小时后过期，需重新登陆。</li>
        </ul>
    </div>
</template>

<script>
  export default {
    name: 'guideAcc',
    data () {
      return {
        loginPic:require('assets/guideAcc1.png')
      }
    },
    computed:{
    },
    methods: {
    }

  }
</script>

<style>
    
</style>
