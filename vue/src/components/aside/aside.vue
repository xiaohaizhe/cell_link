<template>
    <el-menu
        :default-active="activeMenu"
        class="fullHeight"
        style="border: none;"
        background-color="inherit"
        text-color="#BACBDB"
        @select="selectNav"
        active-text-color="#0565B9">
        <p class="workspace font-16">个人工作区</p>
            <el-menu-item index="dashboard">
            <i class="devov asideIcon"></i>
            <span>设备概况</span>
        </el-menu-item>
        <el-submenu index="scene">
            <template slot="title">
                <i class="scene asideIcon"></i>
                <span slot="title">我的场景</span>
            </template>
            <el-menu-item :index="`${item.scenarioId}`" v-for="item in scenes" :key="item.scenarioId">{{item.scenarioName}}</el-menu-item>
        </el-submenu>
        <el-menu-item index="devList">
            <i class="devList asideIcon"></i>
            <span slot="title">设备列表</span>
        </el-menu-item>
        <el-menu-item index="log">
            <i class="log asideIcon"></i>
            <span slot="title">日志信息</span>
        </el-menu-item>
    </el-menu>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
    name: 'clAside',
    data () {
      return {
      }
    },
    computed: {
        ...mapGetters([
                'scenes','activeScene'
            ]),
        activeMenu() {
            const route = this.$route
            const { meta, name ,params} = route
            if(meta.clMatch =='scene'){
                return this.activeScene.scenarioId+'';
            }else{
                return name
            }
        }
    },
    created(){
        this.getAside();
    },
    methods:{
        selectNav(index, indexPath){
            if(indexPath[0]!='scene'){
                this.$router.push('/'+index);
            }
            else{
                this.$store.dispatch('user/setScene',{scenarioId:index})
                this.$router.push('/scene/'+index+'/devGroup');
            }
        },
        getAside(){
            this.$store.dispatch('user/getAside',this.$route.params);
        }
    }
}
</script>
<style lang="scss" scoped>
    .workspace{
        // background-color: #0A335B;
        color: #fff;
        padding:25px 50px;
    }
    
</style>