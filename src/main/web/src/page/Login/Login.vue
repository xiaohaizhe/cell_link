<template>
  <div class="login colorBlack">
    <p class="center colorGray">欢迎来到智能感知平台，现在开始登录吧！
      <el-button type="text" style="padding-bottom: 0;" @click="gotoAddress('overview')">返回首页</el-button>
    </p>
    <div class="content">
      <div class="bg">
        <div class="center" style="background-color:rgb(59, 186, 240,0.4)">
          <img src="../../assets/logo.png"/>
          <p class="font-36 fontImpact colorWhite">耘农大脑</p>
          <p class="colorWhite">Welcome</p>
        </div>
        
      </div>
      <!-- <div class="splitLine"></div> -->
      <div class="contRight noBorder">
         <el-tabs v-model="activeName" stretch>
          <el-tab-pane label="用户登录" name="user">
            <v-text-field placeholder="账户" v-model="name" single-line required></v-text-field>
            <v-text-field placeholder="密码" type="password" single-line v-model="password" required></v-text-field>
            <v-layout v-show="!verifiedMobile">
              <v-flex class="cl-flex">
                <v-text-field label="手机验证码" v-model="verifyCode"></v-text-field>
                <el-button type="primary" style="margin-bottom: 10px;" @click="verification" :disabled="verifing">{{verifiBtn}}</el-button>
              </v-flex>
            </v-layout>
            <p class="flexBtw">
              <el-checkbox v-model="checked">自动登录</el-checkbox>
              <!-- <el-button type="text" style="padding: 0;">忘记密码</el-button> -->
              <span>初始密码：000000</span>
            </p>
            <el-button type="primary" style="width: 100%;height:50px;margin-top:1.43rem" @click="login">立即登录</el-button>
          </el-tab-pane>
          <el-tab-pane label="管理员登录" name="admin">
            <v-text-field placeholder="账户" single-line v-model="adminName" required></v-text-field>
            <v-text-field placeholder="密码" single-line type="password" v-model="adminPwd" required></v-text-field>
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
      <div class="cl-flex">
        <img src="../../assets/A.png"/>
        <p>语言 | 中文</p>
      </div>
      <!-- <p>技术支持-海云智能公司服务部 | 联系我们 lizhenj@hiynn.com</!-->
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
        userId: "0",
        verifing: false,
        verifiBtn: '发送验证码',
        countTime: 60,
        verifiedMobile: true,
        activeName: 'user',
        //websocket: null
      }
    },
    mounted(){
      // this.initWebSocket();
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
                  message: "登陆失败！"+resp.msg,
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
          default: this.open(resp.msg);break;//失败"操作过于频繁，请稍后再试！"
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
      // //websocket
      // initWebSocket(){
      //   this.websocket = new WebSocket('ws://192.168.1.107:8009/websocket');
      //    //连接错误
      //   this.websocket.onerror = this.setErrorMessage
  
      //   // //连接成功
      //   this.websocket.onopen = this.setOnopenMessage
  
      //   //收到消息的回调
      //   this.websocket.onmessage = this.setOnmessageMessage
  
      //   //连接关闭的回调
      //   this.websocket.onclose = this.setOncloseMessage
  
      //   //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
      //   window.onbeforeunload = this.onbeforeunload
      // },
      // setErrorMessage() {
      //   //"WebSocket连接发生错误" + '   状态码：' + this.websocket.readyState;
      // },
      // setOnopenMessage() {
      //   debugger
      //   console.log("WebSocket连接成功" + '   状态码：' + this.websocket.readyState)
      //   this.websocket.send('hello 小濮')
      // },
      // setOnmessageMessage(event) {
      //   debugger
      //   this.data = '服务端返回：' + event.data;
      // },
      // setOncloseMessage() {
      //   //"WebSocket连接关闭" + '   状态码：' + this.websocket.readyState;
      // },
      // onbeforeunload() {
      //   this.closeWebSocket();
      // },
      //登陆成功跳转
      success(userData){
        this.$message({
            message: "登陆成功！",
            type: 'success'
          });
        var that = this;
        
        if(userData.hasModifyPwd==0){
          // 跳转到修改密码页面
          setTimeout(function(){
              that.$alert('首次登陆，请修改密码！', '提示', {
                confirmButtonText: '确定',
                callback: action => {
                }
              });
              that.gotoAddress('editpsw');
          },1000)
        }else{
            // 跳转到首页
          setTimeout(function(){
              that.gotoAddress('home');
          },1000)
        }
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
 
</style>