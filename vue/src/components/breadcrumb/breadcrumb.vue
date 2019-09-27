<template>
  <el-breadcrumb separator-class="el-icon-arrow-right">
    <transition-group name="breadcrumb">
      <el-breadcrumb-item v-for="(item,index) in levelList" :key="item.path">
        <span v-if="item.noRedirect || index==levelList.length-1" class="no-redirect font-16 color000">{{ item.meta.title }}</span>
        <a v-else @click.prevent="handleLink(item)" class="font-16 color000">{{ item.meta.title }}</a>
      </el-breadcrumb-item>
    </transition-group>
  </el-breadcrumb>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  data() {
    return {
      // levelList: null
    }
  },
  // watch: {
  //   $route(route) {
  //     this.getBreadcrumb()
  //   }
  // },
  // created() {
  //   this.getBreadcrumb()
  // },
  computed: {
      ...mapGetters([
          'activeScene'
      ]),
      levelList(){
        let matched = this.$route.matched.filter(item => item.meta && item.meta.title);
        let first = matched[0];
        if(first.meta){
          if(first.meta.sceneFlag){
            return [{meta:{title:'我的场景'},path:'myScene',noRedirect:true},{meta:{title:this.activeScene.scenarioName},path:'sceneName'}]
          }
          else if(first.meta.dgFlag){
            return [{meta:{title:this.activeScene.scenarioName},path:'/scene/'+this.activeScene.scenarioId+'/devGroup'},{meta:{title:this.activeScene.deviceGroupName},path:'dgName'}]
          }else if(first.meta.devFlag){
            return [{meta:{title:this.activeScene.scenarioName},path:'/scene/'+this.activeScene.scenarioId+'/devGroup'},
                    {meta:{title:this.activeScene.deviceGroupName},path:'/devGroup/'+this.activeScene.dgId,dgId:this.activeScene.dgId},
                    {meta:{title:this.activeScene.deviceName},path:'devName'}]
          }else if(first.meta.appFlag){
            return [{meta:{title:this.activeScene.scenarioName},path:'/scene/'+this.activeScene.scenarioId+'/devGroup'},
                    {meta:{title:this.activeScene.appName},path:'application'}]
          }else{
            return matched.filter(item => item.meta && item.meta.title && item.meta.breadcrumb !== false)
          }
          
        }
      }
  },
  methods: {
    handleLink(item) {
      const { redirect, path ,dgId} = item
      if (redirect) {
        this.$router.push(redirect)
        return
      }
      if(dgId){
        this.$store.dispatch('user/setScene',{dgId:dgId})
      }
      this.$router.push(path)
    }
  }
}
</script>

