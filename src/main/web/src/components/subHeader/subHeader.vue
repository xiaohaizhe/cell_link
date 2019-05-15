<template>
    <header class="subHeader font-18 cl-flex">
       <span class="font-18 color000" style="cursor: pointer;" @click="callMethod">{{title}}</span><span v-if="subtitle" style="margin: 0 10px;">|</span>  {{subtitle}}
    </header>
</template>

<script>
  export default {
    name: 'subHead',
    data () {
      return {
      }
    },
    props:{
        title:String,
        subtitle:String,
        direct:Function
    },
    computed:{
    },
    methods: {
      callMethod(){
            this.$emit('direct'); 
            //第一个参数名为调用的方法名，第二个参数为需要传递的参数
        }
    }

  }
</script>

<style>
    .subHeader{
        position: fixed;
        margin-top: 72px;
        background-color: #fff;
        width: 100%;
        height: 60px;
        z-index: 999;
        font-weight: normal;
        padding-left: 15%;
    }
</style>
