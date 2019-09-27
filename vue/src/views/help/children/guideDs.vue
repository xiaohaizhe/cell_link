<template>
    <div style="padding:0 1rem">
        <p class="mgbot-20">如何上传信息流至平台</p>
        <ul class="mgbot-20">
          <li class="mgbot-20">对于MQTT协议产品的信息格式要求：</li>
            <img :src="dsPic1" style="width: 100%;"/>
            <p class="mgbot-20">Payload中，数据流名称类型为字符串，数据流数据类型为long。</p>
          <li class="mgbot-20">对于HTTP协议产品的信息获取接口为：</li>
            <p class="mgbot-20">{device_sn}该值为“设备ID（系统分配）”，可以在设备的编辑选项中找到。</p>
            <img :src="dsPic2" style="width: 100%;"/>
            <p class="mgbot-20">其中，datastream消息体的格式示例：</p>
            <img :src="dsPic3" style="width: 100%;"/>
        </ul>
        <p class="mgbot-20">在“设备”的“数据流展示”页面可以看到设备下所有设备流的情况。</p>
        <img :src="dsPic4" style="width: 100%;"/>
        <p class="mgbot-20">在“数据流管理”页面下可以看到产品下所有的数据流情况。点击详情可以看到详细的数据流图。</p>
        <p class="mgbot-10">获取设备下数据流的具体数据的接口为：</p>
        <p class="mgbot-20">{device_sn}该值为“设备ID（系统分配）”，可以在设备的编辑选项中找到。</p>
        <img :src="dsPic5" style="width: 100%;"/>
        <p class="mgbot-10">获取设备信息的接口为：</p>
        <p class="mgbot-20">{device_sn}该值为“设备ID（系统分配）”，可以在设备的编辑选项中找到。</p>
        <img :src="dsPic6" style="width: 100%;"/>
    </div>
</template>

<script>
  export default {
    name: 'guideDs',
    data () {
      return {
        dsPic1:require("assets/guideDs1.png"),
        dsPic2:require("assets/guideDs2.png"),
        dsPic3:require("assets/guideDs3.png"),
        dsPic4:require("assets/guideDs4.png"),
        dsPic5:require("assets/guideDs5.png"),
        dsPic6:require("assets/guideDs6.png")
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