<template>
    <el-container class="fullHeight is-vertical">
        <cl-header></cl-header>
        <el-container class="fullHeight">
            <el-aside class="fullHeight" style="width:13rem">
                <cl-aside></cl-aside>
            </el-aside>
            <el-main class="fullHeight cl-main">
                <breadcrumb id="breadcrumb-container" class="mgbot-15" />
                <router-view>
                </router-view>
            </el-main>
        </el-container>
    </el-container>
</template>

<script>
import clHeader from 'components/header/header'
import clAside from 'components/aside/aside'
import breadcrumb from 'components/breadcrumb/breadcrumb'
export default {
    name: 'index',
    data () {
      return {
      }
    },
    components:{
        clHeader,
        clAside,
        breadcrumb
    },
}
</script>