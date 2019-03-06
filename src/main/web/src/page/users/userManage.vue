<template>
    <div>
        <cl-header headColor="#181818"></cl-header>
        <sub-header title="用户管理" subtitle="用户详情"></sub-header>
        <div class="mainContent">
            <div>
                <div class="flexBtw">
                    <el-input placeholder="输入关键词后按回车键"  v-model="keywords" @keyup.enter.native="getProducts()" 
                        clearable style="width:320px;height:36px;"></el-input>
                    <el-button @click="showDialog">查看日志</el-button>
                </div>
                <div class="myProducts">
                    <ul class="sortBtns">
                        <li @click="handelSort(false)">默认排序</li>
                        <li class="createtime" @click="handelSort(true)">创建时间
                            <i class="sort">
                                <i class="el-icon-caret-top" :class="{active : productOpt.sortFlag && productOpt.sort==0}"></i>
                                <i class="el-icon-caret-bottom" :class="{active : productOpt.sortFlag && productOpt.sort==-1}"></i>
                            </i>
                        </li>
                    </ul>
                    <div>
                        <div v-for="item in products" :key="item.id">
                            <div class="products" :style="{'background-color':item.id==activeId?'#3bbaf0':'#fff'}">
                                <div class="flexBtw" @click="queryDet(item.id)">
                                    <div :style="{color:item.id!=activeId?'#333333':'#fff'}">
                                        <span class="font-18" style="font-weight: normal;">{{item.name}}</span>
                                        <p style="margin:15px 0 0">{{item.description}}</p>
                                    </div>
                                    <div class="btns flex">
                                        <el-button type="text" :style="{color:item.id!=activeId?'#3bbaf0':'#fff'}" >查看设备</el-button>
                                    </div>
                                </div>
                            </div>
                            <div v-show="item.id==activeId" class="bg-fff">
                                <router-link :to="{path:'/userDetail', query:{data:item}}">
                                    <ul class="detail flex">
                                        <li>设备关联</li>
                                        <li>{{deviceSum}}</li>
                                    </ul>
                                    <ul class="detail flex">
                                        <li>产品数据流</li>
                                        <li>{{datastreamSum}}</li>
                                    </ul>
                                </router-link>
                            </div>
                        </div>
                        <div class="block center">
                            <el-pagination
                                @current-change="handleCurrentChange"
                                :current-page="productOpt.currentPage"
                                :page-sizes="[productOpt.page_size]"
                                :page-size="productOpt.page_size"
                                layout="total, sizes, prev, pager, next, jumper"
                                :total="productOpt.realSize">
                            </el-pagination>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <logs :dialogVisible="dialogVisible" :userId='userId' @getDialogVisible="setDialogVisible" v-if='dialogVisible'></logs>
    </div>
</template>

<script>
    import headTop from 'components/header/head'
    import subHead from 'components/subHeader/subHeader'
    import logs from 'components/dialogs/logs'
    import {getProductOverview,queryProduct} from 'service/getData'

    export default {
        name: 'userManage',
        data () {
            return {
                userId:0,
                activeId: 0,
                keywords: '',
                productOpt:{
                    currentPage:1,
                    page_size:10,
                    sort:0,
                    sortFlag: false,
                    realSize:0
                },
                dialogVisible: false,
                datastreamSum:0,
                deviceSum:0,
                products:[],
            }
        },
        components:{
            'cl-header':headTop,
            'sub-header':subHead,
            'logs':logs
        },
        computed:{
        },
        mounted(){
            this.userId = this.$route.query.data.id;
            this.getProducts();
        },
        methods: {
            async getProducts(currentPage=this.productOpt.currentPage){
                let resp = await queryProduct(currentPage,this.productOpt.page_size,this.userId,this.productOpt.sort,this.keywords);
                this.products = resp.data;
                this.productOpt.realSize = resp.realSize;
            },
            async getProductOverview(id){
                let resp = await getProductOverview(id);
                if(resp.code==0){
                    if(resp.data.device_sum!=0){
                        this.datastreamSum = resp.data.datastream_sum;
                    }
                    this.deviceSum=resp.data.device_sum;//设备关联
                }else{
                    //获取失败
                    console.log('获取失败');
                }
            },
            handleCurrentChange(val) {
                this.getProducts(val);
            },
            handelSort(type){
                if(!type){
                    this.productOpt.sortFlag=false;
                    this.productOpt.sort=0;
                }else{
                    this.productOpt.sortFlag=true;
                    this.productOpt.sort==-1?this.productOpt.sort=0:this.productOpt.sort=-1;
                }
                this.getProducts();
            },
            queryDet(id){
                if(this.activeId == id){
                    this.activeId = 0;
                }else
                    this.activeId = id;
                this.getProductOverview(id);
            },
            showDialog(){
                this.dialogVisible = true;  //点击button时，设值为true，触发动态绑定的:isDialogVisible
            },
            setDialogVisible(val){
                this.dialogVisible = val;
            },
        }

    }
</script>

<style>
    .myProducts .products,.myProducts .block{
        background-color: #fff;
        border: solid 1px #cccccc;
        border-top:none; 
        padding: 20px 45px;
    }
    .myProducts .products .prodLabel{
        color: #fc4f08;margin-left:5px;
    }
    .myProducts .products .protocol{
        margin: 0 10px 0 15px;
    }
    .myProducts .block{
        padding-top: 30px;
    }
    .myProducts .detail{
        border: 1px solid #cccccc;
        border-top: none;
        cursor: pointer;
    }
    .myProducts .flexBtw{
        cursor: pointer;
    }
    .myProducts .detail li{
        padding: 15px 0px 15px 45px;
    }
    .myProducts .detail li:first-child{
        flex-basis: 150px;
        color: #333333;
        border-right: 1px solid #cccccc;
    }
    .myProducts .detail li:last-child{
        color: #3bbaf0;
    }
</style>
