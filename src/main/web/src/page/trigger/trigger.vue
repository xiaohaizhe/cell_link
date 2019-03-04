<template>
    <div>
        <cl-header headColor="#181818"></cl-header>
        <sub-header title="设备管理" :subtitle="`${deviceName}-触发器展示`"></sub-header>
        <div class="mainContent">
            <div class="flexAround center bg-fff devSum">
                <div>
                    <p class="devNum"><span class="font-24">{{associatedTrigger_sum}}</span>条</p>
                    <p>触发器总数</p>
                </div>
                <div>
                    <p class="devNum"><span class="font-24">{{yesterday_sum}}</span>条</p>
                    <p>昨日新增</p>
                </div>
                <div>
                    <p class="devNum"><span class="font-24">{{SevenDays_sum}}</span>条</p>
                    <p>最近7日新增</p>
                </div>
            </div>
            <p class="font-16" style="margin:40px 0 20px;">触发器展示</p>
            <div class="cl-table">
                <el-table :data="tableData" style="width: 100%">
                    <el-table-column prop="name" label="触发器名称">
                        <template slot-scope="scope">
                            <div style="padding: 10px 0;">
                                <p class="font-18 colorBlack mgbot-10">{{scope.row.name}}</p>
                                <p class="colorGray">数据流名称：{{scope.row.datastream_name}}</p>
                                <p class="colorGray">
                                    <span v-if="scope.row.triggerMode==0">邮箱地址：</span>
                                    <span v-if="scope.row.triggerMode==1">URL地址：</span>
                                    {{scope.row.modeValue}}
                                </p>
                                <p class="colorGray">创建时间：{{scope.row.createTime}}</p>
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="criticalValue" label="触发条件" width="150px">
                        <template slot-scope="scope">
                            <div style="padding: 10px 0;">
                                <span>{{scope.row.triggerType + scope.row.criticalValue}}</span>
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="associated_device_sum" label="控制设备" width="150px">
                    </el-table-column>
                    <el-table-column prop="triggerMode" label="触发器信息接受方式" width="180px">
                        <template slot-scope="scope">
                            <div style="padding: 10px 0;">
                                <span v-if="scope.row.triggerMode==0">开启邮箱</span>
                                <span v-if="scope.row.triggerMode==1">开启URL</span>
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" width="200">
                        <template slot-scope="scope">
                            <router-link :to="{name:'triggerManage', params:{prodId:productId}}">
                                <el-button type="text" style="padding:0;">进入触发器管理</el-button>
                            </router-link>
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
</template>

<script>
    import headTop from 'components/header/head'
    import subHead from 'components/subHeader/subHeader'
    import {getAssociatedTriggers,getTriggersOv} from 'service/getData'

    export default {
        name: 'trigger',
        data () {
            return {
                deviceName:'',
                device_sn:123500,
                associatedTrigger_sum:0,
                yesterday_sum:0,
                SevenDays_sum:0,
                triggerOpt:{
                    currentPage:1,
                    page_size:10,
                    realSize:0
                },
                time:'',
                tableData:[],
                expands:[],
                productId:0
            }
        },
        components:{
            'cl-header':headTop,
            'sub-header':subHead,
        },
        computed:{
        },
        mounted(){
            this.deviceName = this.$route.query.data.name;
            // this.device_sn = this.$route.query.data.device_sn;
            this.productId = this.$route.query.productId;
            this.getTriggersOv();
            this.getAssociatedTriggers();
        },
        methods: {
            async getAssociatedTriggers(currentPage=this.triggerOpt.currentPage){
                let resp = await getAssociatedTriggers(this.device_sn,currentPage,this.triggerOpt.page_size);
                if(resp.code==0){
                    this.tableData = resp.data;
                    this.triggerOpt.realSize = resp.realSize;
                }else{
                    this.$message({
                        message: "获取表格数据失败！",
                        type: 'error'
                    });
                }
            },
            async getTriggersOv(){
                let resp = await getTriggersOv(this.device_sn);
                if(resp.code==0){
                    this.SevenDays_sum= resp.data.SevenDays_sum;
                    this.yesterday_sum= resp.data.yesterday_sum;
                    this.associatedTrigger_sum= resp.data.associatedTrigger_sum;
                }
            },
            handleCurrentChange(val) {
                this.getDevicedslist(val);
            },
            
        }

    }
</script>

<style>
</style>
