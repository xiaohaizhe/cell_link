<template>
    <div>
        <cl-header headColor="rgba(36,36,36,0.5)"></cl-header>
        <scatter-chart></scatter-chart>
        <div class="prodCenter">
            <div>
                <p class="font-24">数据中心</p>
                <div class="subtotal flex"> 
                    <div class="all">
                        <p class="flexBtw">全站总览<span class="font-12">单位/个</span></p>
                        <div class="content">
                            <div v-for="item in prodData" :key="item.text">
                                <img :src="item.img" style="width:22px;height:22px;margin-right: 20px;"/>
                                <div class="number">
                                    <p class="font-24">{{item.total}}</p>
                                    <p>{{item.text}}</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="myProduct">
            <div>
                <p class="font-24" style="margin-bottom:30px;">操作管理</p>
                <div class="flexBtw">
                    <el-input placeholder="输入关键词后按回车键"  v-model="keywords" @keyup.enter.native="queryUser(isValid)" 
                        clearable style="width:320px;height:36px;"></el-input>
                    <div>
                        <router-link to="/addUser">
                            <el-button type="primary">+添加用户</el-button>
                        </router-link>
                    </div>
                </div>
                <div class="userTable">
                    <el-table :data="tableData" style="width: 100%" @filter-change="filterValid">
                        <el-table-column type="index" label="序号" width="100"></el-table-column>
                        <el-table-column prop="id" label="用户ID"></el-table-column>
                        <el-table-column prop="name" label="账户名"></el-table-column>
                        <el-table-column prop="email" label="邮箱"></el-table-column>
                        <el-table-column prop="phone" label="手机号"></el-table-column>
                        <el-table-column prop="isvalid" label="是否禁用" column-key='isValid' :filtered-value="isValid" 
                        :filter-multiple='false' :filters="[{ text: '禁用状态', value: '0' }, { text: '非禁用状态', value: '1' }]"
                        >
                            <!-- <template slot="header" slot-scope="slot">
                                <span>是否禁用</span>
                                <i class="disable"></i>
                            </template> -->
                            <template slot-scope="scope">
                                <el-switch
                                    v-model="scope.row.isvalid"
                                    :active-value="1"
                                    :inactive-value="0"
                                    active-color="#3bbaf0"
                                    inactive-color="#999999">
                                </el-switch>
                            </template>
                        </el-table-column>
                        <el-table-column label="操作" width="100">
                            <template slot-scope="scope">
                                <router-link :to="{path:'/userDetail', query:{data:scope.row}}">
                                    <i class="detail"></i>
                                </router-link>
                                <router-link :to="{path:'/editUser', query:{data:scope.row}}">
                                    <i class="edit"></i>
                                </router-link>
                            </template>
                        </el-table-column>
                    </el-table>
                    <div class="block center flex">
                        <el-pagination
                            @current-change="handleCurrentChange"
                            :current-page="currentPage"
                            :page-sizes="[page_size]"
                            :page-size="page_size"
                            layout="total, sizes, prev, pager, next, jumper"
                            :total="realSize">
                        </el-pagination>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import {mapState} from 'vuex'
    import { getProductQuantity, getGlobalData,queryUser} from 'service/getData'
    import headTop from 'components/header/head'
    import scatterChart from 'components/charts/scatterChart'

    export default {
    name: 'index',
    data () {
        return {
            currentPage:1,
            page_size:10,
            realSize:0,
            keywords:'1',
            isValid:[],
            prodData: [{
                        img: require('assets/prod.png'),
                        total: 0,
                        text: '已拥有产品'
                    },{
                        img: require('assets/user.png'),
                        total: 0,
                        text: '用户总量'
                    },{
                        img: require('assets/device.png'),
                        total: 0,
                        text: '连接设备'
                    },{
                        img: require('assets/stream.png'),
                        total: 0,
                        text: '连接数据流'
                    },  
            ],
            tableData: []
        }
    },
    computed:{
        ...mapState([
            'adminName'
        ])
    },
    mounted(){
        this.getProductOverview();
        this.queryUser();
    },
    components:{
      'cl-header':headTop,
      'scatter-chart':scatterChart,
    },
    methods: {
        //获取表格数据
        async queryUser(arr=[],val=this.currentPage){
            let isValid = 2;
            if(arr.length>0){
                isValid=arr[0];
            }
            let resp = await queryUser(val,this.page_size,this.keywords,isValid);
            this.tableData = resp.data;
            this.realSize = resp.realSize;
        },
        //获取统计数据
        async getProductOverview(){
            let respUser = await getProductQuantity(0);
            let resp = await getGlobalData();
            if(respUser.code !=0 || resp.code !=0){
                this.$message({
                        message: "获取统计数据失败",
                        type: 'error'
                    });
                    return;
            }
            this.prodData[0].total = respUser.data.product_sum;
            this.prodData[1].total = resp.data.user_sum;
            this.prodData[2].total = resp.data.device_sum;
            this.prodData[3].total = resp.data.device_datastream_sum;
        },
        //筛选禁用状态
        filterValid(filters) {
            if(filters.isValid.length==1){
                this.isValid[0]=filters.isValid[0];
            }else{
                this.isValid = [];
            }
             this.queryUser(this.isValid);
        },
        //表格页数改变事件
        handleCurrentChange(val){
            this.queryUser(this.isValid,val);
        }
    }

    }
</script>

<style>
    .prodCenter{
        padding-top: 60px;
        padding-bottom: 50px;
        background-color: #fff;
    }
    .myProduct,.prodCenter{
        padding: 50px 17%;
    }
    .prodCenter .number p{
        color: #07aaa5;
    }
    .prodCenter .content{
        display: flex;
        justify-content: space-around;
    }
    .prodCenter .content>div{
        display: flex;
        align-items: baseline;
        margin: 60px;
    }
    .prodCenter .subtotal>div{
        border: solid 1px #cccccc;
        margin-top: 30px;
    }
    .prodCenter .subtotal .all{
        flex-grow: 1;
    }
    .prodCenter .subtotal>div>p{
        margin: 20px 30px 0;
        font-size: 16px;
    }
    .userTable{
        margin-top: 20px;
    }
    .userTable .block{
        background-color: #fff;
        height: 80px;
        justify-content: center;
    }
</style>
