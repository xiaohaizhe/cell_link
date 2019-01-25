<template>
    <div>
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
                    <el-input placeholder="输入关键词后按回车键"  v-model="keywords" clearable style="width:320px;height:36px;"></el-input>
                    <div>
                        <el-button type="primary">+添加产品</el-button>
                        <el-button>查看日志</el-button>
                    </div>
                </div>
                <div>
                    <ul class="sortBtns">
                        <li>默认排序</li>
                        <li class="createtime">创建时间
                            <i class="sort">
                                <i class="el-icon-caret-top"></i>
                                <i class="el-icon-caret-bottom"></i>
                            </i>
                        </li>
                        <li>
                            <el-dropdown trigger="click">
                                <span class="el-dropdown-link">
                                    批量处理<i class="el-icon-arrow-down el-icon--right"></i>
                                </span>
                                <el-dropdown-menu slot="dropdown">
                                    <el-dropdown-item>全部删除</el-dropdown-item>
                                    <el-dropdown-item>删除选中</el-dropdown-item>
                                </el-dropdown-menu>
                            </el-dropdown>
                        </li>
                    </ul>
                    <div>
                        <div class="products flexBtw" v-for="item in products" :key="item.id">
                            <div>
                                <span class="font-18" style="font-weight: normal;">{{item.name}}</span>
                                <span class="prodLabel">产品标签</span>
                                <p style="margin:15px 0 10px">{{item.description}}</p>
                                <div style="color: #999999;">
                                    <span>产品ID：{{item.productTypeId}}</span>
                                    <span class="protocol">接入协议：{{item.protocolId}}</span>
                                    <span>创建时间：{{item.createTime}}</span>
                                </div>
                            </div>
                            <div class="btns flex">
                                <i class="detail"></i>
                                <i class="edit"></i>
                                <i class="delete"></i>
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
    </div>

</template>

<script>
  import { getProductQuantity, getGlobalData, queryProduct} from '../../service/getData'

  export default {
    name: 'prodOverview',
    data () {
        return{
            userId:3,//this.$store.state.userId,
            keywords: '',
            productOpt:{
                currentPage:1,
                page_size:5,
                sort:0,
                realSize:0
            },
            products:[],
            userData: {
                        img: require('../../assets/prod.png'),
                        total: 0,
                        text: '已拥有产品'
                    } ,
            prodData: [{
                        img: require('../../assets/user.png'),
                        total: 0,
                        text: '用户总量'
                    },{
                        img: require('../../assets/device.png'),
                        total: 0,
                        text: '连接设备'
                    },{
                        img: require('../../assets/stream.png'),
                        total: 0,
                        text: '连接数据流'
                    },  
            ],
        }
    },
    mounted(){
        this.getProductOverview();
        this.getProducts();
    },
    methods: {
        async getProductOverview(){
            let respUser = await getProductQuantity(this.userId);
            let resp = await getGlobalData();
            if(respUser.code !=0 || resp.code !=0){
                this.$message({
                        message: "获取统计数据失败",
                        type: 'error'
                    });
                    return;
            }
            this.userData.total = respUser.data.product_sum;
            this.prodData[0].total = resp.data.user_sum;
            this.prodData[1].total = resp.data.device_sum;
            this.prodData[2].total = resp.data.device_datastream_sum;
        },
        async getProducts(currentPage=this.productOpt.currentPage){
            let resp = await queryProduct(currentPage,this.productOpt.page_size,this.userId,this.productOpt.sort);
            this.products = resp.data;
            this.productOpt.realSize = resp.realSize;
        },
        handleCurrentChange(val) {
            this.getProducts(val);
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
    .sortBtns{
        display: flex;
        background-color: #f7f7f7;
        margin-top: 20px;
        border: 1px solid #cccccc;
    }
    .sortBtns li{
        width: 150px;
        color: #333333;
        text-align: center;
        padding: 10px 0;
        cursor: pointer;
        border-right: 1px solid #cccccc;
    }
    .sortBtns li .el-dropdown{
        color: #333333;
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
    .createtime{
        display: flex;
        align-items: center;
        justify-content: center;
    }
    .createtime .sort{
        display: inline-flex;
        flex-direction: column;
        margin-left: 5px;
    }
    .createtime .sort>i{
        font-size: 13px;
    }
    .createtime .sort>i.el-icon-caret-bottom{
        margin-top: -6px;
    }
</style>

