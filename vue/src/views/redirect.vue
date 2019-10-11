<template>
    <div></div>
  </template>
  <script>
  export default {
    data() {
      return {
      }
    },
    created() {
      let url = this.$route.query;
      let str = '';
      for(let i in url) {
        str += url[i];
      }
      const path = JSON.parse(str)
      this.$router.push(path);
    }  
  }
  </script>
  <style>
  </style>