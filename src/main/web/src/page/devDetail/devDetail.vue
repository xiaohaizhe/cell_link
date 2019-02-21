<template>
    <div>
        <cl-header headColor="#181818"></cl-header>
        <sub-header title="设备关联" :subtitle="`${deviceName}-详情`"></sub-header>
        <div class="mainContent">
            
        </div>
    </div>
</template>

<script>
    import headTop from 'components/header/head'
    import subHead from 'components/subHeader/subHeader'

    export default {
        name: 'devDetail',
        data () {
            return {
                deviceName:''
            }
        },
        components:{
            'cl-header':headTop,
            'sub-header':subHead
        },
        computed:{
        },
        mounted(){
            this.deviceName = this.$route.query.data.name;
        },
        methods: {

        }

    }
</script>

<style>
</style>
