<template>
    <div>
        <cl-header headColor="rgba(36,36,36,0.5)"></cl-header>
        <scatter-chart></scatter-chart>
        <div class="prodCenter">
            <div>
                <p class="font-24">数据中心</p>
                <div class="subtotal cl-flex"> 
                    <div class="all">
                        <p class="flexBtw">全站总览<span class="font-12">单位/个</span></p>
                        <div class="content">
                            <div v-for="item in prodData" :key="item.text">
                                <img :src="item.img" style="width:2.14rem;height:2.14rem;margin-right: 1.43rem;"/>
                                <div class="number">
                                    <p class="font-30">{{item.total}}</p>
                                    <p class="font-18">{{item.text}}</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="myProduct">
            <div>
                <p class="font-24" style="margin-bottom:2.14rem;">操作管理</p>
                <div class="flexBtw">
                    <el-input placeholder="输入关键词后按回车键"  v-model="keywords" @clear="clearKey()" @keyup.enter.native="queryUser(isValid)" 
                        clearable style="width:320px;height:36px;"></el-input>
                    <div>
                        <router-link to="/addUser">
                            <el-button type="primary">+添加用户</el-button>
                        </router-link>
                    </div>
                </div>
                <div class="userTable cl-table">
                    <el-table :data="tableData" style="width: 100%" @filter-change="filterValid" @sort-change="sortChange">
                        <el-table-column type="index" label="序号"></el-table-column>
                        <el-table-column prop="id" label="用户ID"></el-table-column>
                        <el-table-column prop="name" label="账户名"></el-table-column>
                        <el-table-column prop="email" label="邮箱"></el-table-column>
                        <el-table-column prop="phone" label="手机号"></el-table-column>
                        <el-table-column prop="createTime" label="创建时间" sortable="custom"></el-table-column>
                        <el-table-column prop="modifyTime" label="最后修改时间" sortable="custom"></el-table-column>
                        <el-table-column prop="isvalid" label="是否禁用" column-key='isValid' :filtered-value="isValid"  filter-placement="bottom"
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
                                    inactive-color="#999999"
                                    @change="changeSwitch($event,scope.row.id)">
                                </el-switch>
                            </template>
                        </el-table-column>
                        <el-table-column label="操作">
                            <template slot-scope="scope">
                                <el-tooltip class="item" effect="dark" content="详情" placement="bottom">
                                    <i class="detail cl-icon" @click="goAddress('userManage',scope.row)"></i>
                                </el-tooltip>
                                <el-tooltip class="item" effect="dark" content="编辑" placement="bottom">
                                    <i class="editIcon cl-icon" @click="goAddress('editUser',scope.row)"></i>
                                </el-tooltip>
                                <el-tooltip class="item" effect="dark" content="重置用户密码" placement="bottom">
                                    <i class="circle cl-icon" @click="reset(scope.row)"></i>
                                </el-tooltip>
                            </template>
                        </el-table-column>
                    </el-table>
                    <div class="block center cl-flex">
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
    import { getProductQuantity, getGlobalData,queryUser,changeValid,resetPwd} from 'service/getData'
    import headTop from 'components/header/head'
    import scatterChart from 'components/charts/scatterChart'

    export default {
    name: 'index',
    data () {
        return {
            currentPage:1,
            page_size:10,
            realSize:0,
            keywords:'',
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
            tableData: [],
            createSort:0,
            modifySort:0

        }
    },
    computed:{
        ...mapState([
                'user'
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
            let resp = await queryUser(val,this.page_size,this.keywords,isValid,this.createSort,this.modifySort);
            if(resp.code==0){
                this.tableData = resp.data;
                this.realSize = resp.realSize;
            }else if(resp.code=="error"){
                return;
            }else{
                this.$message({
                    message: "获取表格数据失败！",
                    type: 'error'
                });
            }
            
        },
        //获取统计数据
        async getProductOverview(){
            let respUser = await getProductQuantity(0);
            let resp = await getGlobalData();
            if(resp.code=="error" || respUser.code=="error"){
                return;
            }else if(respUser.code !=0 || resp.code !=0){
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
        async reset(val){
            let resp = await resetPwd(val.id);
             if(resp.code==0){
                this.$message({
                    message: "重置用户密码成功！",
                    type: 'success'
                });
            }else if(resp.code=="error"){
                return;
            }else{
                this.$message({
                    message: "重置用户密码失败！"+resp.msg,
                    type: 'error'
                });
            }
        },
        clearKey(){
            this.queryUser();
        },
        sortChange(filters){
            if(filters.order=="descending"){
                if(filters.prop=="createTime"){
                    this.createSort=1;
                    this.modifySort=0
                }else{
                    this.modifySort=1;
                    this.createSort=0;
                }
            }else if(filters.order=="ascending"){
               if(filters.prop=="createTime"){
                    this.createSort=-1;
                    this.modifySort=0
                }else{
                    this.modifySort=-1;
                    this.createSort=0;
                }
            }
            this.currentPage = 1;
            this.queryUser();
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
        },
        async changeValid(userId,admin_name){
            let resp = await changeValid(userId,admin_name);
            if(resp.code!=0){
                this.$alert('修改禁用状态失败，请重试！', '提示', {
                    confirmButtonText: '确定',
                    callback: action => {
                    }
                });
                this.queryUser(this.isValid);
            }else if(resp.code=="error"){
                return;
            }else{
                this.$message({
                    message: "修改禁用状态成功！",
                    type: 'success'
                });
            }
        },
        //
        changeSwitch: function($event,userId){
            this.changeValid(userId,this.user.adminName);
        },
        goAddress(url,item){
            //加密
            let b = new Buffer(JSON.stringify(item));
            let s = b.toString('base64');
            let data = encodeURIComponent(s);
            this.$router.push({name:url,params:{userData:data}})
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
        height: 2.14rem;
        line-height: 2.14rem;
    }
    .prodCenter .content{
        display: flex;
        justify-content: space-around;
    }
    .prodCenter .content>div{
        display: flex;
        /* align-items: baseline; */
        margin: 4rem;
    }
    .prodCenter .subtotal>div{
        border: solid 1px #cccccc;
        margin-top: 2.14rem;
    }
    .prodCenter .subtotal .all{
        flex-grow: 1;
    }
    .prodCenter .subtotal>div>p{
        margin: 1.43rem 2.14rem 0;
        font-size: 16px;
    }
    .userTable{
        margin-top: 1.43rem;
    }
    .userTable .el-table .cell {
        padding-left: 8px;
        padding-right: 0;
    }
</style>
