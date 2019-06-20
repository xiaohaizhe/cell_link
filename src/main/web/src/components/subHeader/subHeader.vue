<template>
    <header class="subHeader font-18 cl-flex">
       <span class="font-18 color000" style="cursor: pointer;" @click="callMethod">{{title}}</span><span v-if="subtitle" style="margin: 0 10px;">|</span> <span class="ellipsis font-18 color000" style="max-width:500px;display:inline-block" :title="subtitle">{{subtitle}}</span><span  class="font-18 color000" v-if="detail">-{{detail}}</span>
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
        direct:Function,
        detail:String
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
    
</style>
