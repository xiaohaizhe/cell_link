<template>
  <div class="login colorBlack">
    <p class="center colorGray">欢迎来到智能感知平台，现在开始登录吧！
      <el-button type="text" style="padding-bottom: 0;" @click="gotoAddress('overview')">返回首页</el-button>
    </p>
    <div class="content">
      <div class="bg">
        <div class="center" style="background-color:rgb(59, 186, 240,0.4)">
          <img src="../../assets/logo.png"/>
          <p class="font-36 fontImpact colorWhite">cell-link!</p>
          <p class="colorWhite">Welcome to cell-link</p>
        </div>
        
      </div>
      <!-- <div class="splitLine"></div> -->
      <div class="contRight">
         <el-tabs v-model="activeName" stretch>
          <el-tab-pane label="用户登录" name="user">
            <div>
              <p>账户</p>
              <el-input placeholder="请输入账户" v-model="name" clearable></el-input>
            </div>
            <div>
              <p>密码</p>
              <el-input placeholder="请输入密码"  type="password" v-model="password" clearable></el-input>
            </div>
            <div class="flexBtw" v-show="!verifiedMobile">
              <el-input placeholder="输入绑定手机验证码" v-model="verifyCode" clearable></el-input>
              <el-button type="primary" style="margin-bottom: 10px;" @click="verification" :disabled="verifing">{{verifiBtn}}</el-button>
            </div>
            <p class="flexBtw">
              <el-checkbox v-model="checked">自动登录</el-checkbox>
              <!-- <el-button type="text" style="padding: 0;">忘记密码</el-button> -->
            </p>
            <el-button type="primary" style="width: 100%;height:50px;margin-top:1.43rem" @click="login">立即登录</el-button>
          </el-tab-pane>
          <el-tab-pane label="管理员登录" name="admin">
            <div>
              <p>账户</p>
              <el-input placeholder="请输入账户" v-model="adminName" clearable></el-input>
            </div>
            <div>
              <p>密码</p>
              <el-input placeholder="请输入密码"  type="password" v-model="adminPwd" clearable></el-input>
            </div>
            <p class="flexBtw">
              <el-checkbox v-model="adminChecked">自动登录</el-checkbox>
              <!-- <el-button type="text" style="padding: 0;">忘记密码</el-button> -->
            </p>
            <el-button type="primary" style="width: 100%;height:50px;margin-top:1.43rem" @click="adminLogin">立即登录</el-button>
          </el-tab-pane>
        </el-tabs>
        
        
      </div>
    </div>
    <div class="foot">
      <div class="flex">
        <img src="../../assets/A.png"/>
        <p>语言 | 中文</p>
      </div>
      <p>技术支持-海云智能公司服务部 | 联系我们</p>
    </div>
  </div>
</template>

<script>
  import { getUser, verification , getUserVertified,getAdmin } from 'service/getData'
  import md5 from 'js-md5';

  export default {
    name: 'login',
    data () {
      return {
        name: '',
        adminName:'',
        checked: false,
        adminChecked:false,
        password: '',
        adminPwd:'',
        verifyCode: '',
        userId: 0,
        verifing: false,
        verifiBtn: '发送验证码',
        countTime: 60,
        verifiedMobile: true,
        activeName: 'user'
      }
    },
    methods: {
      //管理员登录
      async adminLogin(){
        let resp = await getAdmin(this.adminName,this.adminPwd);
        if(resp.code==0){
          this.$message({
              message: "登陆成功！",
              type: 'success'
            });
          var that = this;
          // 跳转到首页
          setTimeout(function(){
              that.gotoAddress('index');
          },1000)
          // 将登录名使用vuex传递到Home页面
          this.$store.commit('HANDLE_ADMIN', {autoLogin:this.adminChecked, adminName:this.adminName,startTime:new Date().getTime()});
        }else if(resp.code=="error"){
              return;
          }else{
              this.$message({
                  message: "登陆失败！",
                  type: 'error'
              });
          }
      },
      //立即登录点击事件
      async login(){
        if(!this.verifiedMobile){
          // let resp = await vertifySMS(this.userId,this.verifyCode);
          // if(resp.code == 0) this.getVertifiedUser();
          // else if(resp.code=="error"){
          //       return;
          //   }
          // else  this.open(resp.msg);
          let resp = await getUserVertified(this.verifyCode,this.userId);
          if(resp.code == 0)  this.success(resp.data)
          else if(resp.code=="error"){
                  return;
          } else  this.open(resp.msg);
        }else
          this.getUser();
      },
      
      //发送验证码
      async verification(){
        let resp = await verification(this.userId);
        switch (resp.code){
          case 0: this.open("验证码已发送");this.countDown();break;//成功
          case 'error':break;
          default: this.open("操作过于频繁，请稍后再试！");break;//失败
        }
      },

      //用户登陆
      async getUser(){
        // this.success();
        // return;
        let resp = await getUser(this.name,md5(this.password));
        switch (resp.code){
          case 0: {
            this.success(resp.data);
            break;//成功
          }
          case 4: {
            //手机号未验证
            this.open(resp.msg);
            this.verifiedMobile = false;
            this.userId = resp.data.id;
            break;
          }
          case 'error':break;
          default: this.open(resp.msg);this.name='';this.password='';this.verifiedMobile = true;break;//失败
        }
      },
      //登陆成功跳转
      success(userData){
        this.$message({
            message: "登陆成功！",
            type: 'success'
          });
        var that = this;
        // 跳转到首页
        setTimeout(function(){
            that.gotoAddress('home');
        },1000)
        // 将登录名使用vuex传递到Home页面
        this.$store.commit('HANDLE_USER', {...userData,autoLogin:this.checked,startTime:new Date().getTime()});
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
      
      //跳转页面
      gotoAddress(path){
        this.$router.push(path)
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
  .login{
    position: relative;
    top: 50%;
    transform: translateY(-50%);
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 100%;
  }
  .login>p{
    margin-bottom: 2.14rem;
  }
  .login .content{
    width: 55%;
    height: 60%;
    background-color: #ffffff;
    box-shadow: 0px 2px 7px 0px rgba(71, 85, 88, 0.74);
    margin: 0 auto;
    display: flex;
    align-items: center
  }
  .login .content .bg{
    flex-basis: 45%;
    height: 100%;
  }
  .login .bg .center{
    display: flex;
    flex-direction: column;
    height: 100%;
    align-items: center;
    justify-content: center;
  }
  .login .content img{
    margin-bottom: 27px;
    width:90px;height:90px;
  }
  .login .content .fontImpact{
    margin-bottom: 23px;
  }
  .splitLine{
    width: 1px;
    height: 300px;
    background-color: #333333;
    opacity: 0.2;
  }
  .login .content .contRight{
    padding: 44px 56px;
    flex-basis: 55%;
  }
  .login .content .contRight input{
    padding: 0 !important;
  }
  .login .content .contRight .el-tab-pane>div{
    border-bottom: 1px solid;
    margin-top: 15px;
  }
  .login .content .contRight .el-tab-pane>p{
    margin-top: 2.14rem;
  }
  .foot{
    width: 60%;
    margin: 60px auto 0;
    border-top:1px solid rgba(153, 153, 153, 0.5);
  }
  .foot>div{
    margin-top: 1.43rem;
    margin-bottom: 15px;
  }
  .foot img{
    width: 24px;
    height: 24px;
    margin-right: 13px;
  }
  .login input {
    border: none !important;
  }
</style>