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
            <el-menu-item index="/dashboard">
            <i class="devov asideIcon"></i>
            <span>设备概况</span>
        </el-menu-item>
        <el-submenu index="/scene">
            <template slot="title">
                <i class="scene asideIcon"></i>
                <span slot="title">我的场景</span>
            </template>
            <el-menu-item :index="`/scene/${item.scenarioId}`" v-for="item in submenu" :key="item.scenarioId">{{item.scenarioName}}</el-menu-item>
        </el-submenu>
        <el-menu-item index="/devList">
            <i class="devList asideIcon"></i>
            <span slot="title">设备列表</span>
        </el-menu-item>
        <el-menu-item index="/log">
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
            const { meta, path } = route
            // debugger
            return path
        }
    },
    mounted(){
        this.getAside()
    },
    methods:{
        selectNav(index, indexPath){
            this.$router.push(index)
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