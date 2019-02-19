<template>
    <div>
        <cl-header headColor="#181818"></cl-header>
        <sub-header title="用户详情" :subtitle="`${productName}-详情`"></sub-header>
        <div class="mainContent">
            <p class="font-16">产品信息</p>
            <div>
                <ul class="flex">
                    <li>设备</li>
                    <li>数据流</li>
                </ul>
            </div>
        </div>
    </div>
</template>

<script>

    import headTop from 'components/header/head'
    import subHead from 'components/subHeader/subHeader'

    export default {
    name: 'userDetail',
    data () {
        return {
            productName:''
        }
    },
    mounted(){
        this.productName = this.$route.query.data.name;
    },
    components:{
        'cl-header':headTop,
        'sub-header':subHead,
    },
    methods: {

    }

    }
</script>

<style>
</style>
