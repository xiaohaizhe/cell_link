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
            <el-menu-item :index="`nav${item.scenarioId}`" v-for="item in submenu" :key="item.scenarioId" @click="changeAside(item)">{{item.scenarioName}}</el-menu-item>
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
export default {
    name: 'clAside',
    data () {
      return {
        submenu:[]
      }
    },
    computed: {
        activeMenu() {
            const route = this.$route
            const { meta, name ,params} = route
            if(name =='scene'){
                return 'nav'+params.scenarioId
            }else{
                return name
            }
            
        }
    },
    mounted(){
        this.getAside()
    },
    methods:{
        changeAside(item){
            this.$store.dispatch('user/setScene',item)
        },
        selectNav(index, indexPath){
            if(indexPath[0]!='scene')
                this.$router.push('/'+index);
            else
                this.$router.push('/scene/'+index+'/devGroup');
        },
        getAside(){
            this.$store.dispatch('user/getAside')
            .then((data) => {
                this.submenu = data;
            })
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