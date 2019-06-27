<template>
    <div>
        <cl-header headColor="#181818"></cl-header>
        <sub-header title="用户详情" :subtitle="`${productName}`"  detail="详情" v-on:direct="navDirect"></sub-header>
        <div class="mainContent">
            <p class="font-16 mgbot-20">产品信息</p>
            <div>
                <ul class="cl-flex tab">
                    <li class="active">设备</li>
                    <!-- <li :class="{active:activeTab==1}" @click="changeAct(1)">数据流</li> -->
                </ul>
                <div>
                    <div class="searchArea">
                        <el-input placeholder="输入设备鉴权信息或者设备名称后按回车键"  v-model="devKey" @keyup.enter.native="changeDevKey()" 
                            clearable style="width:320px;height:36px;" @clear="clearKey()"></el-input>
                    </div>
                    <dev-table :keywords='devKey' :productId='productId' :isAdmin='true' ref="child"></dev-table>
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
            productId:"0",
            productName:'',
            devKey: '',
            userId:"0"
        }
    },
    created(){
        //解密
        var x = new Buffer(decodeURIComponent(this.$route.params.productData), 'base64')
        var y = x.toString('utf8');
        let productData = JSON.parse(y);
        this.productName = productData.name || '';
        this.productId = productData.id;
        this.userId = productData.userId;
    },
    components:{
        'cl-header':headTop,
        'sub-header':subHead,
        'dev-table':devTable
    },
    methods: {
        //devKey改变触发表格刷新
        changeDevKey(){
            let flag  = /[`~!@#$^&*()=|{}':;',\\\[\]\.<>\/?~！@#￥……&*（）——|{}【】'；：""'。，、？\s]/g.test(this.devKey);
            if(!flag){
                this.$refs.child.queryDevice();
            }else{
                this.$alert('搜索内容不能包括特殊字符或空格！', '提示', {
                    confirmButtonText: '确定',
                    callback: action => {
                    }
                });
            }
        },
        navDirect(){
            //加密
            let b = new Buffer(JSON.stringify({"id":this.userId}));
            let s = b.toString('base64');
            let data = encodeURIComponent(s);
            this.$router.push('/userManage/'+data);
        },
        clearKey(){
            this.$refs.child.queryDevice(1,'');
        },
    }

    }
</script>

<style>
    .searchArea{
        background-color: #fff;
        padding: 2.14rem 40px 25px;
        border: 1px solid #cccccc;
        border-bottom: none;
    }
</style>
