<template>
  <div>
    <cl-header headColor="rgba(36,36,36,0.5)"></cl-header>
    <scatter-chart></scatter-chart>
    <router-view></router-view>
    <footer>
        技术支持-海云智能公司服务部 | 联系我们
    </footer>
  </div>
</template>

<script>
  import headTop from 'components/header/head'
  import scatterChart from 'components/charts/scatterChart'
  

  export default {
    name: 'index',
     data () {
        return{
          userName: this.$store.state.userName
        }
      },
    components:
    {
      'cl-header':headTop,
      'scatter-chart':scatterChart
    },


  }
</script>

