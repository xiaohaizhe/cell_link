<template>
  <div>
    <cl-header></cl-header>
    <scatter-chart></scatter-chart>
    <prodOverview v-if="userName"></prodOverview>
    <overview v-if="!userName"></overview>
    
  </div>
</template>

<script>
  import headTop from '../../components/header/head'
  import scatterChart from './scatterChart'
  import overview from './overview'
  import prodOverview from './prodOverview'
  

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
      'scatter-chart':scatterChart,
      'overview':overview,
      'prodOverview':prodOverview
    },


  }
</script>

