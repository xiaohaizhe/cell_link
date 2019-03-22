<template>
    <div>
        <cl-header headColor="rgba(36,36,36,0.5)"></cl-header>
        <scatter-chart></scatter-chart>
        <div class="prodCenter">
            <div>
                <p class="font-24">数据中心</p>
                <div class="subtotal flex"> 
                    <div class="personal">
                        <p>个人产品</p>
                        <div class="content">
                            <div>
                                <img :src="userData.img" style="width:22px;height:22px;margin-right: 20px;"/>
                                <div class="number">
                                    <p class="font-24">{{userData.total}}</p>
                                    <p>{{userData.text}}</p>
                                </div>
                            </div>
                        </div>
                    </div>
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
                <p class="font-24" style="margin-bottom:30px;">我的产品</p>
                <div class="flexBtw">
                    <el-input placeholder="输入关键词后按回车键"  v-model="keywords" @keyup.enter.native="getProducts()" 
                        clearable style="width:320px;height:36px;"></el-input>
                    <div>
                        <router-link to="/addProduct">
                            <el-button type="primary">+添加产品</el-button>
                        </router-link>
                        <el-button @click="showDialog">查看日志</el-button>
                    </div>
                </div>
                <div>
                    <ul class="sortBtns">
                        <li @click="handelSort(false)">默认排序</li>
                        <li class="createtime" @click="handelSort(true)">创建时间
                            <i class="sort">
                                <i class="el-icon-caret-top" :class="{active : productOpt.sortFlag && productOpt.sort==0}"></i>
                                <i class="el-icon-caret-bottom" :class="{active : productOpt.sortFlag && productOpt.sort==-1}"></i>
                            </i>
                        </li>
                        <li>
                            <el-dropdown trigger="click">
                                <span class="el-dropdown-link">
                                    批量处理<i class="el-icon-arrow-down el-icon--right"></i>
                                </span>
                                <el-dropdown-menu slot="dropdown">
                                    <el-dropdown-item  @click.native="deleteAll()">全部删除</el-dropdown-item>
                                    <el-dropdown-item @click.native="deleteItem()">删除选中</el-dropdown-item>
                                </el-dropdown-menu>
                            </el-dropdown>
                        </li>
                    </ul>
                    <div>
                        <div class="products flexBtw" v-for="item in products" :key="item.id"  :class="{selected:selectedIds.includes(item.id)}">
                            <div @click="selectPart(item.id)">
                                <span class="font-18" style="font-weight: normal;">{{item.name}}</span>
                                <span class="prodLabel">产品标签</span>
                                <p style="margin:15px 0 10px">{{item.description}}</p>
                                <div style="color: #999999;">
                                    <span>产品ID：{{item.id}}</span>
                                    <span class="protocol">接入协议：<span v-if="item.protocolId==1">MQTT</span><span v-if="item.protocolId==2">HTTP</span></span>
                                    <span>创建时间：{{item.createTime}}</span>
                                </div>
                            </div>
                            <div class="btns flex">
                                    <i class="detail" @click="goAddress(item)"></i>
                                <router-link :to="{ name: 'editProduct', params: { prodId: item.id }}">
                                    <i class="editIcon"></i>
                                </router-link>
                                <i class="delete" @click="deleteItem(item.id)"></i>
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
        <footer>
            技术支持-海云智能公司服务部 | 联系我们
        </footer>
        <logs :dialogVisible= "dialogVisible"  :userId='userId' @getDialogVisible="setDialogVisible" v-if='dialogVisible'></logs>
    </div>

</template>

<script>
  import {mapState} from 'vuex'
  import {getProductQuantity, getGlobalData, queryProduct ,deleteByUserId,deleteProducts} from 'service/getData'
  import headTop from 'components/header/head'
  import scatterChart from 'components/charts/scatterChart'
  import logs from 'components/dialogs/logs'
  
  export default {
    name: 'home',
    data () {
        return{
            keywords: '',
            productOpt:{
                currentPage:1,
                page_size:5,
                sort:0,
                sortFlag: false,
                realSize:0
            },
            dialogVisible: false,
            selectedIds:[],
            products:[],
            userData: {
                        img: require('assets/prod.png'),
                        total: 0,
                        text: '已拥有产品'
                    } ,
            prodData: [{
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
        }
    },
    computed:{
        ...mapState([
            'userId'
        ]),
    },
    components:{
      'cl-header':headTop,
      'scatter-chart':scatterChart,
      'logs':logs
    },
    mounted(){
        this.getProductOverview();
        this.getProducts();
    },
    methods: {
        showDialog(){
            this.dialogVisible = true;  //点击button时，设值为true，触发动态绑定的:isDialogVisible
        },
        setDialogVisible(val){
            this.dialogVisible = val;
        },
        async getProductOverview(){
            let respUser = await getProductQuantity(this.userId);
            this.userData.total = respUser.data.product_sum;
            let resp = await getGlobalData();
            if(respUser.code !=0 || resp.code !=0){
                this.$message({
                        message: "获取统计数据失败",
                        type: 'error'
                    });
                    return;
            }
            this.prodData[0].total = resp.data.user_sum;
            this.prodData[1].total = resp.data.device_sum;
            this.prodData[2].total = resp.data.device_datastream_sum;
        },
        async getProducts(currentPage=this.productOpt.currentPage){
            let resp = await queryProduct(currentPage,this.productOpt.page_size,this.userId,this.productOpt.sort,this.keywords);
            this.products = resp.data;
            this.productOpt.realSize = resp.realSize;
        },
        //删除全部
        deleteAll(){
            this.$confirm('删除产品后，相关设备、数据流等资源将会被全部删除，且无法恢复。确定要删除全部产品吗？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.deleteByUserId()
            })
        },
        async deleteByUserId(){
            let resp = await deleteByUserId(this.userId);
            if(resp.code==0){
                this.$message({
                    type: 'success',
                    message: '删除成功!'
                });
                this.getProductOverview();
                this.getProducts();
            }else{
                this.$message({
                    type: 'error',
                    message: '删除失败!'
                });
            }
        },
        //删除单个或多个
        deleteItem(id){
            this.$confirm('删除产品后，相关设备、数据流等资源将会被全部删除，且无法恢复。确定要删除产品吗？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                if(id){
                    let tempId = new Array();
                    tempId.push(id);
                    this.deleteProducts(tempId);
                }else
                    this.deleteProducts(this.selectedIds);
            })
        },
        async deleteProducts(ids){
            let resp = await deleteProducts(ids);
            if(resp.code==0){
                this.$message({
                    type: 'success',
                    message: '删除成功!'
                });
                this.getProductOverview();
                this.getProducts();
            }else{
                this.$message({
                    type: 'error',
                    message: '删除失败!'
                });
            }
        },
        selectPart(id){
            let pos = this.selectedIds.indexOf(id);
            if(pos<0){
                this.selectedIds.push(id);
            }else{
                this.selectedIds.splice(pos, 1); 
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
        goAddress(item){
            //加密
            let b = new Buffer(JSON.stringify(item.id));
            let s = b.toString('base64');
            let data = encodeURIComponent(s);
            //解密
            // var x = new Buffer(decodeURIComponent(data), 'base64')
            // var y = b.toString('utf8');
            this.$router.push({path:'/myProduct/'+data+'/overview'})
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
    .prodCenter .subtotal .personal{
        margin-right: 10px;
    }
    .prodCenter .subtotal .all{
        flex-grow: 1;
    }
    .prodCenter .subtotal>div>p{
        margin: 20px 30px 0;
        font-size: 16px;
    }
    
    .myProduct .products,.myProduct .block{
        background-color: #fff;
        border: solid 1px #cccccc;
        border-top:none; 
        padding: 50px;
    }
    .myProduct .products .prodLabel{
        color: #fc4f08;margin-left:5px;
    }
    .myProduct .products .protocol{
        margin: 0 10px 0 15px;
    }
    .myProduct .block{
        padding-top: 30px;
    }
    
    .el-message-box__content {
        padding: 30px;
    }
    .myProduct .products.selected{
        background-color: #ecf5ff !important;
    }
</style>

