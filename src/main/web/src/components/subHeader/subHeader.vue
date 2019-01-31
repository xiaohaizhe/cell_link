<template>
    <header class="subHeader font-18 flex">
        {{title}}  |  {{subtitle}}
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
        subtitle:String
    },
    computed:{
    },
    methods: {

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
