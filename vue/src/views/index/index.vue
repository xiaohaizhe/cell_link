<template>
    <el-container class="fullHeight is-vertical">
        <cl-header></cl-header>
        <el-container class="fullHeight">
            <el-aside class="fullHeight" width="220px">
                <cl-aside></cl-aside>
            </el-aside>
            <el-main class="fullHeight cl-main">
                <router-view>

                </router-view>
            </el-main>
        </el-container>
    </el-container>
</template>

<script>
import clHeader from 'components/header/header'
import clAside from 'components/aside/aside'
export default {
    name: 'index',
    data () {
      return {
      }
    },
    components:{
        'cl-header':clHeader,
        'cl-aside':clAside
    },
}
</script>