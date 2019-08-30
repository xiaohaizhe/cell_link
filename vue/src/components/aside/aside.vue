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
            <el-menu-item index="1-1">选项1</el-menu-item>
            <el-menu-item index="1-2">选项2</el-menu-item>
            <el-menu-item index="1-3">选项3</el-menu-item>
            <el-menu-item index="1-3">选项3</el-menu-item>
            <el-menu-item index="1-3">选项3</el-menu-item>
            <el-menu-item index="1-3">选项3</el-menu-item>
            <el-menu-item index="1-3">选项3</el-menu-item>
            <el-menu-item index="1-3">选项3</el-menu-item>
            <el-menu-item index="1-3">选项3</el-menu-item>
            <el-menu-item index="1-3">选项3</el-menu-item>
            <el-menu-item index="1-3">选项3</el-menu-item>
            <el-menu-item index="1-3">选项3</el-menu-item>
            <el-menu-item index="1-3">选项3</el-menu-item>
            <el-menu-item index="1-3">选项3</el-menu-item>
            <el-menu-item index="1-3">选项3</el-menu-item>
            <el-menu-item index="1-3">选项3</el-menu-item>
            <el-menu-item index="1-3">选项3</el-menu-item>
            <el-menu-item index="1-3">选项3</el-menu-item>
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
        //   activeMenu:'dashboard'
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
    methods:{
        selectNav(index, indexPath){
            this.$router.push(index)
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