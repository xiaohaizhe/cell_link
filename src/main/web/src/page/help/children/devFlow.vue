<template>
    <div>
        <img :src="pic" style="width: 100%;"/>
    </div>
</template>

<script>
  export default {
    name: 'devFlow',
    data () {
      return {
        pic:require("assets/devFlow.png")
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
