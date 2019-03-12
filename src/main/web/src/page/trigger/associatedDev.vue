<template>
    <div>   
        <cl-header headColor="#181818"></cl-header>
        <sub-header title="触发器管理" :subtitle="`${triggerData.name}-设置关联`"></sub-header>
        <div class="mainContent">
            <p class="font-16" style="margin-bottom:20px;">关联信息</p>
            <div>
                <ul class="flex tab">
                    <li :class="{active:activeTab==0}" @click="changeActive(0)">未关联设备</li>
                    <li :class="{active:activeTab==1}" @click="changeActive(1)">已关联设备</li>
                </ul>
                <div>
                    <div class="searchArea flexBtw">
                        <el-input placeholder="输入设备名称后按回车键"  v-model="triggerKey" @keyup.enter.native="changeTriKey()" 
                            clearable style="width:320px;height:36px;"></el-input>
                        <el-button v-if="activeTab==0">一键关联</el-button>
                        <el-button v-if="activeTab==1">一键断链</el-button>
                    </div>
                    <div class="cl-table">
                        <el-table :data="tableData" style="width: 100%" @filter-change="filterTime">
                            <el-table-column prop="trigger" label="全部" column-key='time' :filtered-value="time"  
                                :filter-multiple='false' :filters="timeChosen" filter-placement="bottom" width="550"
                                >
                                    <template slot-scope="scope">
                                        <div class="rowData flex cl-card">
                                            <div class="report"></div>
                                            <div>
                                                <p class="font-18 colorBlack mgbot-10">{{scope.row.name}}</p>
                                                <p class="colorGray">设备ID：{{scope.row.id}}</p>
                                                <p class="colorGray">创建时间：{{scope.row.create_time}}</p>
                                            </div>
                                        </div>
                                    </template>
                                </el-table-column>
                            <el-table-column label="状态（可操作）">
                                <template slot-scope="scope">
                                    <i class="linkin cl-icon" v-if="activeTab==0"></i>
                                    <i class="unlinkIcon cl-icon" v-if="activeTab==1"></i>
                                </template>
                            </el-table-column>
                        </el-table>
                        <div class="block center flex">
                            <el-pagination
                                @current-change="handleCurrentChange"
                                :current-page="triggerOpt.currentPage"
                                :page-sizes="[triggerOpt.page_size]"
                                :page-size="triggerOpt.page_size"
                                layout="total, sizes, prev, pager, next, jumper"
                                :total="triggerOpt.realSize">
                            </el-pagination>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import headTop from 'components/header/head'
    import subHead from 'components/subHeader/subHeader'
    import {getAssociatedDevices,getNotAssociatedDevices} from 'service/getData'
    import {getDay,getPreMonthDay} from 'config/mUtils'

    export default {
        name: 'associatedDev',
        data () {
            return {
                triggerData:{},
                activeTab:0,
                triggerKey:'',
                triggerOpt:{
                    start:'',
                    end:'',
                    currentPage:1,
                    page_size:5,
                    realSize:0
                },
                productId:0,
                time:['3'],
                tableData: [],
                timeChosen:[
                    { text: '最近三天', value: '0' }, 
                    { text: '最近三十天', value: '1' },
                    { text: '最近三个月', value: '2' },
                    { text: '今年', value: '3' },
                    { text: '去年', value: '4' },
                    { text: '前年', value: '5' },
                ],
            }
        },
        props:{
        },
        computed:{
        },
        components:{
            'cl-header':headTop,
            'sub-header':subHead
        },
        mounted(){
            this.triggerData = this.$route.params.data;
            this.productId = this.$route.params.productId;
            this.filterTime({time:this.time});
        },
        methods: {
            async getAssociatedDevices(currentPage=this.triggerOpt.currentPage){
                let resp = await getAssociatedDevices(1548827934548,currentPage,this.triggerOpt.page_size,this.triggerOpt.start,this.triggerOpt.end,this.triggerKey);//this.triggerData.id
                if(resp.code==0){
                    this.tableData = resp.data;
                    this.triggerOpt.realSize = resp.realSize;
                }
            },
            async getNotAssociatedDevices(currentPage=this.triggerOpt.currentPage){
                let resp = await getNotAssociatedDevices(1548827934548,currentPage,this.triggerOpt.page_size,5,this.triggerOpt.start,this.triggerOpt.end,this.triggerKey);//this.triggerData.id，this.productId
                if(resp.code==0){
                    this.tableData = resp.data;
                    this.triggerOpt.realSize = resp.realSize;
                }
            },
             //表格页数改变事件
            handleCurrentChange(val){
                if(this.activeTab==0){
                    this.getAssociatedDevices(val);
                }else{
                    this.getNotAssociatedDevices(val);
                }
            },
            //关键词改变
            changeTriKey(){
                if(this.activeTab==0){
                    this.getAssociatedDevices();
                }else{
                    this.getNotAssociatedDevices();
                }
            },
            //切换tab
            changeActive(val){
                if(val==0){
                    this.getAssociatedDevices();
                }else{
                    this.getNotAssociatedDevices();
                }
                this.activeTab = val;
            },
            //筛选时间
            filterTime(filters) {
                if(filters.time.length==1){
                switch (filters.time[0]){
                        case '0':{
                            //获取最近3天日期
                            this.triggerOpt.start = getDay(-2);//3天前日期
                            this.triggerOpt.end = getDay(0);//当天日期
                            break;
                        }
                        case '1':{
                            //获取最近30天日期
                            this.triggerOpt.start = getDay(-29);//30天前日期
                            this.triggerOpt.end = getDay(0);//当天日期
                            break;
                        }
                        case '2':{
                            //获取最近3个月日期
                            this.triggerOpt.start = getPreMonthDay(new Date(),3);//90天前日期
                            this.triggerOpt.end = getDay(0);//当天日期
                            break;
                        }
                        case '3':{
                            //获取今年日期
                            this.triggerOpt.start = new Date().getFullYear() +'-01-01 00:00:00'//今年日期
                            this.triggerOpt.end = getDay(0);//当天日期
                            break;
                        }
                        case '4':{
                            //获取去年日期
                            this.triggerOpt.start = new Date().getFullYear()-1 +'-01-01 00:00:00'//去年日期
                            this.triggerOpt.end = new Date().getFullYear()-1 +'-12-31 00:00:00'//去年日期
                            break;
                        }
                        case '5':{
                            //获取前年日期
                            this.triggerOpt.start = new Date().getFullYear()-2 +'-01-01 00:00:00'//前年日期
                            this.triggerOpt.end = new Date().getFullYear()-2 +'-12-31 00:00:00'//前年日期
                            break;
                        }
                        
                    }
                }else{
                    this.time = [];
                    this.triggerOpt.start = '';
                    this.triggerOpt.end = '';
                }
                if(this.activeTab==0){
                    this.getAssociatedDevices();
                }else{
                    this.getNotAssociatedDevices();
                }
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
