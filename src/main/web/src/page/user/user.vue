<template>
    <div>
        user
    </div>
</template>

<script>
  import {mapState} from 'vuex'

  export default {
    name: 'user',
    data () {
      return {
      }
    },
    computed:{
      ...mapState([
          'userName',
      ]),
    },
    methods: {

    }

  }
</script>

<style>
</style>
