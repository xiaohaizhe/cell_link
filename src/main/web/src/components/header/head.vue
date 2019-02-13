<template>
    <header class="headTop" :style="{'background-color':headColor || '#181818'}">
      <div class="flex">
        <img src="../../assets/logo.png" style="width:45px;height:45px;"/>
        <span class="fontImpact font-30" style="margin-left:15px;">cell-link</span>
      </div>
      <p>
        <router-link to="/home">
          <el-button type="text" style="padding:0;" v-if="userName">首页</el-button>
        </router-link>
        <router-link to="/overview">
          <el-button type="text" style="padding:0;" v-if="!userName">首页</el-button>
        </router-link>
        <router-link to="/login">
          <el-button type="text" style="padding:0;margin-left:100px;" v-if="!userName">登录</el-button>
        </router-link>
        <el-dropdown v-if="userName" trigger="click">
          <span class="el-dropdown-link">
            {{userName}}<i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <router-link to="/user">
              <el-dropdown-item>个人中心</el-dropdown-item>
            </router-link>
            <el-dropdown-item @click.native="logout">退出账户</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </p>
    </header>
</template>

<script>
  import {mapState} from 'vuex'
  import {logout} from 'service/getData'

  export default {
    name: 'headTop',
    data () {
      return {
        // userName: this.$store.state.userName
      }
    },
    props:{
      headColor:{
        type:String,
        // required:true
      }
    },
    computed:{
      ...mapState([
          'userName',
          'userId'
      ]),
    },
    methods: {
      //登出
      async logout(){
        var that = this;
        let resp = await logout(this.userId);
        if(resp.code==0){
          setTimeout(function(){
            that.$router.push("/login");
          },1000)
          // 将登录名使用vuex传递到Home页面
          this.$store.commit('REMOVE_USER');
        }else{
          this.$alert(resp.msg, '提示', {
            confirmButtonText: '确定',
              callback: action => {
            }
          });
        }
        
      },
     //跳转页面
      gotoAddress(path){
        this.$router.push(path)
      },

    }

  }
</script>

<style>
  .headTop{
    display: flex;
    position: fixed;
    align-items: center;
    justify-content: space-between;
    width: 100%;
    height: 72px;
    color:#fff;
    background-color: rgba(36,36,36,0.5);
    padding-left: 15%;
    padding-right: 17%;
    z-index: 999;
  }
  .headTop p button>span ,.headTop .el-dropdown{
    color: #fff;
    cursor: pointer;
    margin-left: 50px;
  }
</style>
