<template>
    <div>
        <img :src="pic" style="width: 100%;"/>
    </div>
</template>

<script>
  export default {
    name: 'introduction',
    data () {
      return {
          pic:require("assets/introduction.png")
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
