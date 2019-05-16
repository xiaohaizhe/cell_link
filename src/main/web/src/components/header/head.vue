<template>
    <header class="headTop" :style="{'background-color':headColor || '#181818'}">
      <div class="cl-flex">
        <img src="../../assets/logo.png" style="width:45px;height:45px;"/>
        <span class="fontImpact font-30" style="margin-left:15px;">cell-link</span>
      </div>
      <p>
        <el-button type="text" style="padding:0;margin-right:100px;" @click="gotoAddress">首页</el-button>
        <router-link to="/login">
          <el-button type="text" style="padding:0;" v-if="!user.userName&&!user.adminName">登录</el-button>
        </router-link>
        <el-dropdown  v-if="user.userName" trigger="click">
          <span class="el-dropdown-link">
            {{user.userName}}<i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <router-link to="/user">
              <el-dropdown-item>个人中心</el-dropdown-item>
            </router-link>
            <el-dropdown-item @click.native="logout">退出账户</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <!-- <el-dropdown trigger="click">
          <span class="el-dropdown-link">
            {{user.adminName}}<i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="adminLogout">退出账户</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown> -->
        <el-dropdown v-if="user.adminName" trigger="click">
          <span class="el-dropdown-link">
            {{user.adminName}}<i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="adminLogout">退出账户</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </p>
    </header>
</template>

<script>
  import {mapState} from 'vuex'
  import {logout,adminLogout} from 'service/getData'

  export default {
    name: 'headTop',
    data () {
      return {
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
          'user'
      ])
    },
    methods: {
      //登出
      async logout(){
        var that = this;
        let resp = await logout(this.user.userId);
        if(resp.code==0){
          setTimeout(function(){
            that.$router.push("/overview");
          },1000)
          this.$message({
              message: '退出成功！',
              type: 'success'
          });
          // 将登录名使用vuex传递到Home页面
          this.$store.commit('REMOVE_USER');
        }else if(resp.code=="error"){
            return;
        }else{
          this.$alert(resp.msg, '提示', {
            confirmButtonText: '确定',
              callback: action => {
            }
          });
        }
        
      },
      async adminLogout(){
        var that = this;
        let resp = await adminLogout(this.user.adminName);//'cladmin'
        if(resp.code==0){
          setTimeout(function(){
            that.$router.push("/overview");
          },1000)
          this.$message({
              message: '退出成功！',
              type: 'success'
          });
          // 将登录名使用vuex传递到Home页面
          this.$store.commit('REMOVE_USER');
        }else if(resp.code=="error"){
          this.$alert(resp.msg, '提示', {
            confirmButtonText: '确定',
              callback: action => {
            }
          });
        }
      },
      //跳转页面
      gotoAddress(){
        if(this.user.userName){
          this.$router.push('/home');
        }else if(this.user.adminName){
          this.$router.push('/index');
        }else{
          this.$router.push('/overview');
        }
        
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
    padding-right: 15%;
    z-index: 999;
  }
  .headTop p button>span,.headTop .el-dropdown{
    color: #fff;
  }
  .headTop p button,.headTop .el-dropdown{
    cursor: pointer;
    display: inline-block;
    width: 120px;
    text-align: right;
  }
</style>
