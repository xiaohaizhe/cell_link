<template>
    <div style="padding:0 1rem">
        <p class="mgbot-20">在登录后，选择右上角“首页”则会进入“我的产品” ，该页面展示产品热点图、产品统计数据及产品数据列表。</p>
        <img :src="homePic"  class="mgbot-20" style="width: 100%;"/>
        <p class="mgbot-20">创建产品时需要选择设备接入方式（即数据流上传协议），该选项不可改。</p>
        <img :src="addPic"  class="mgbot-20" style="width: 100%;"/>
    </div>
</template>

<script>
  export default {
    name: 'guidePro',
    data () {
      return {
        homePic:require("assets/guidePro1.png"),
        addPic:require("assets/guidePro2.png")
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
