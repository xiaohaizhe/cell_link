<template>
    <div style="padding:0 1rem">
        <p class="mgbot-20">点击产品的详情按钮可以进入产品页面，点击上方“设备管理”页面进行对设备的增删改查。</p>
        <p class="mgbot-20">设备的添加有两种模式，“设备管理”页面上方的添加设备选项，或是自动添加设备。</p>
        <img :src="devPic" style="width: 100%;" class="mgbot-20"/>
        <ul class="mgbot-20">
          <li class="mgbot-20">1.选择“+新建设备”流程如下：</li>
          <img :src="addPic" style="width: 100%;" class="mgbot-20"/>
            <ul class="mgbot-20">
              <li class="mgbot-20">(1)填写设备的名称和鉴权信息点击确定。</li>
              <img :src="devPic1" style="width: 100%;" class="mgbot-20"/>
              <li class="mgbot-20">(2)此时在“设备管理”界面上会显示新添加的设备，点击编辑按钮可以看到“设备ID（系统分配）”的信息。</li>
              <img :src="devPic2" style="width: 100%;" class="mgbot-20"/>
              <li class="mgbot-20">(3)将该信息写入设备以便平台识别信息流。</li>
            </ul>
          <li class="mgbot-20">2.选择自动添加设备流程如下：</li>
          <img :src="editPic" style="width: 100%;" class="mgbot-20"/>
          <ul class="mgbot-20">
              <li class="mgbot-20">(1)在“设备管理”界面上方“设备接入”一栏获得设备注册码信息。</li>
              <img :src="devPic3" style="width: 100%;" class="mgbot-20"/>
              <li class="mgbot-20">(2)通过该接口进行设备的自动注册：</li>
              <img :src="devPic4" style="width: 100%;" class="mgbot-20"/>
              <li class="mgbot-20">(3)将接口返回的device_id字段写入设备，以便平台识别该设备上传的数据流</li>
            </ul>
        </ul>
        <p class="mgbot-10">获得设备的详细信息的接口如下：</p>
        <p class="mgbot-20">{device_sn}该值为“设备ID（系统分配）”，可以在设备的编辑选项中找到。</p>
        <img :src="devPic5" style="width: 100%;" class="mgbot-20"/>
        
    </div>
</template>

<script>
  export default {
    name: 'guideDev',
    data () {
      return {
        devPic:require("assets/guideDev1.jpg"),
        addPic:require("assets/guideDev2.png"),
        editPic:require("assets/guideDev3.png"),
        devPic1:require("assets/guideDev4.png"),
        devPic2:require("assets/guideDev5.png"),
        devPic3:require("assets/guideDev6.png"),
        devPic4:require("assets/guideDev7.png"),
        devPic5:require("assets/guideDev8.png"),
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
