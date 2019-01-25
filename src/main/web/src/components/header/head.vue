<template>
    <header class="headTop">
      <div class="flex">
        <img src="../../assets/logo.png" style="width:45px;height:45px;"/>
        <span class="fontImpact font-30" style="margin-left:15px;">cell-link</span>
      </div>
        <p>
          <el-button type="text" style="padding:0;" @click="gotoAddress('home')">首页</el-button>
          <el-button type="text" style="padding:0;margin-left:100px;" @click="gotoAddress('login')" v-if="!userName">登录</el-button>
          <el-dropdown v-if="userName" trigger="click">
            <span class="el-dropdown-link">
              {{userName}}<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>个人中心</el-dropdown-item>
              <el-dropdown-item>退出账户</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </p>
    </header>
</template>

<script>
 
  export default {
    name: 'headTop',
    data () {
      return {
        userName: this.$store.state.userName
      }
    },
    computed: {
    },
    methods: {
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
    padding-left: 300px;
    padding-right: 350px;
    z-index: 999;
  }
  .headTop p button>span , .el-dropdown{
    color: #fff;
    cursor: pointer;
    margin-left: 50px;
  }
</style>
