<template>
    <el-container class="fullHeight is-vertical">
        <cl-header :first="false"></cl-header>
        <el-container class="fullHeight">
            <el-main class="fullHeight cl-main cl-flex directColumn">
                <router-view class="fullHeight">
                </router-view>
            </el-main>
        </el-container>
    </el-container>
</template>
<script>
import clHeader from 'components/header/header'
export default {
    name: 'index',
    data () {
      return {
      }
    },
    components:{
        clHeader
    },
}
</script>
<style>
   .shield{
        background-image: url('../../assets/shield.svg')
    }
    .lock{
        background: url('../../assets/lock.svg')
    }
    .complete{
        background: url('../../assets/complete.svg')
    }
    .el-step__head.is-wait .lock{
        background: url('../../assets/locked.svg')
    }
    .el-step__head.is-wait .complete{
        background: url('../../assets/completed.svg')
    }
</style>