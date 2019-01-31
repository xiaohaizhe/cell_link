<template>
    <header>
      <div class="headTop" :style="{'background-color':headColor || '#181818'}">
          <div class="flex">
            <img src="../../assets/logo.png" style="width:45px;height:45px;"/>
            <span class="fontImpact font-30" style="margin-left:15px;">cell-link</span>
          </div>
          <p>
            <router-link to="/index/products">
              <el-button type="text" style="padding:0;" v-if="userName">首页</el-button>
            </router-link>
            <router-link to="/index">
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
                <el-dropdown-item>个人中心</el-dropdown-item>
                <el-dropdown-item>退出账户</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </p>
      </div>
      
    </header>
</template>

<script>
  import {mapState} from 'vuex'

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
      ]),
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
  .headTop p button>span ,.headTop .el-dropdown{
    color: #fff;
    cursor: pointer;
    margin-left: 50px;
  }
</style>
