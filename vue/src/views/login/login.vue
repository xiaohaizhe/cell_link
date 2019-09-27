
<template>
  <div class="fullHeight login">
    <p class="center colorGray mgbot-20">欢迎来到智能感知平台，现在开始登录吧！
      <el-button type="text"  @click="$router.push('/overview')">返回首页</el-button>
    </p>
    <el-row class="content fullHeight">
      <el-col :span="11" class="fullHeight">
        <div class="fullHeight earthpic">
          <div class="cl-flex alignCenter justifyCenter directColumn fullHeight" style="background-color:rgb(5, 101, 185,0.57)">
            <img :src="logo" class="mgbot-20" style="width:90px;height:90px;"/>
            <p class="font-36 fontImpact colorWhite mgbot-5">cell-link!</p>
            <p class="colorWhite">Welcome to cell-link</p>
            <!-- <p class="font-36 fontImpact colorWhite">耘农大脑</p>
            <p class="colorWhite">Welcome</p> -->
          </div>
        </div>
      </el-col>
      <el-col :span="13"  class="fullHeight bgWhite" style="padding: 3.14rem 4.3rem;">
        <p class="center font-20 font-bold colorBlack">用户登录</p>
        <el-form ref="loginForm" :model="loginForm" class="mgTop-70">
          <el-form-item>
            <el-input v-model="loginForm.username" placeholder="请输入用户名">
              <template slot="prepend">
                <i class="el-icon-user font-18"></i>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-input type="password" v-model="loginForm.password" placeholder="请输入密码" show-password>
              <template slot="prepend">
                <i class="el-icon-lock font-18"></i>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item>
              <el-checkbox label="自动登录" v-model="loginForm.checked"></el-checkbox>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" class="fullWidth" @click="submitForm">立即登录</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
    <div class="foot colorBlack">
      <div class="cl-flex alignCenter">
        <img :src="A"/>
        <p>语言 | 中文</p>
      </div>
      <p>技术支持-海云智能公司服务部 | 联系我们 lizhenj@hiynn.com</p>
    </div>
  </div>
</template>

<script>

export default {
    name: 'login',
    data () {
      return {
        logo:require('assets/celllink.svg'),
        A:require('assets/A.svg'),
        loginForm:{
          username:'',
          password:'',
          checked:false,
        },
        rules: {
          username: [
            // { required: true, message: '请输入活动名称', trigger: 'blur' }
          ],
        }
      }
    },
    methods:{
      submitForm() {
        this.$refs['loginForm'].validate((valid) => {
          if (valid) {
          // this.loading = true
          let isRemember = 0;
          if(this.loginForm.checked){
            isRemember=1
          }
          this.$store.dispatch('user/login', {...this.loginForm,isRemember:isRemember})
            .then((data) => {
              this.$message({
                  message: "登陆成功！",
                  type: 'success'
              });
              if(data.type==0){
                //0:admin
                this.$router.push('/admin')
              }else if(data.type==1){
                //1:user
                if(data.isPwdModified==0){
                  this.$alert('初次登陆系统，请修改密码！', {
                    confirmButtonText: '确定'
                  });
                  this.$router.push('/firstLand');
                }else{
                  this.$router.push('/dashboard')
                }
              }
            })
            .catch(() => {
              this.loading = false
            })
        } else {
          console.log('error submit!!')
          return false
        }
        });
      },
    }
}
</script>
<style>
  .earthpic{
    width: 100%;
    height: 100%;
    background-size: cover;
    background-image: url('../../assets/earthpic.jpg');
  }
  .login .content{
    width: 55%;
    height: 50%;
    margin: 0 auto 50px;
    box-shadow: 0px 2px 7px 0px rgba(71, 85, 88, 0.74);
  }
  .login{
    top: 45%;
    display: -ms-flexbox;
    display: flex;
    -ms-flex-direction: column;
    flex-direction: column;
    -ms-flex-align: center;
    align-items: center;
    -ms-flex-pack: center;
    justify-content: center;
    height: 100%;
  }
  .login .el-input__inner,.login .el-input-group__prepend{
    background-color: #F8F8F8
  } 
  .login .el-input__inner{
    height: 40px;
    line-height: 40px
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
</style>