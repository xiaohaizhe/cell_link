<template>
    <div>
        <cl-header headColor="#181818"></cl-header>
        <sub-header title="用户详情" :subtitle="`${productName}-详情`"></sub-header>
        <div class="mainContent">
            <p class="font-16" style="margin-bottom:20px;">产品信息</p>
            <div>
                <ul class="flex tab">
                    <li class="active">设备</li>
                    <!-- <li :class="{active:activeTab==1}" @click="changeAct(1)">数据流</li> -->
                </ul>
                <div>
                    <div class="searchArea">
                        <el-input placeholder="输入设备ID或者设备名称后按回车键"  v-model="devKey" @keyup.enter.native="changeDevKey()" 
                            clearable style="width:320px;height:36px;"></el-input>
                    </div>
                    <dev-table :keywords='devKey' :productId='productId' ref="child"></dev-table>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import headTop from 'components/header/head'
    import subHead from 'components/subHeader/subHeader'
    import devTable from 'components/tables/devTable'

    export default {
    name: 'userDetail',
    data () {
        return {
            productId:12,
            productName:'',
            devKey: 'test'
        }
    },
    mounted(){
        this.productName = this.$route.query.data.name || '';
        // this.productId = this.$route.query.data.id;
    },
    components:{
        'cl-header':headTop,
        'sub-header':subHead,
        'dev-table':devTable
    },
    methods: {
        //devKey改变触发表格刷新
        changeDevKey(){
            this.$refs.child.queryDevice();
        },
    }

    }
</script>

<style>
    .searchArea{
        background-color: #fff;
        padding: 30px 40px 25px;
        border: 1px solid #cccccc;
        border-bottom: none;
    }
</style>
