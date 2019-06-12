<template>
    <div>
        <cl-header headColor="#181818"></cl-header>
        <sub-header title="设备管理" :subtitle="`${deviceData.name}-触发器展示`" v-on:direct="navDirect"></sub-header>
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
            <p class="font-16" style="margin:40px 0 1.43rem;">触发器展示</p>
            <div class="cl-table">
                <el-table :data="tableData" style="width: 100%">
                    <el-table-column prop="name" label="触发器名称">
                        <template slot-scope="scope">
                            <div style="padding: 10px 0;">
                                <p class="font-18 colorBlack mgbot-10 ellipsis" style="width:120px" :title="scope.row.name">{{scope.row.name}}</p>
                                <p class="colorGray">数据流名称：{{scope.row.datastreamName}}</p>
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
                    <el-table-column prop="associatedDeviceSum" label="控制设备" width="150px">
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
                            <el-button type="text" style="padding:0;" @click="goAddress(deviceData.productId)">进入触发器管理</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <div class="block center cl-flex">
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
                deviceData:{},
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
                expands:[]
            }
        },
        components:{
            'cl-header':headTop,
            'sub-header':subHead,
        },
        computed:{
        },
        mounted(){
            //解密
            var x = new Buffer(decodeURIComponent(this.$route.params.data), 'base64')
            var y = x.toString('utf8');
            this.deviceData = JSON.parse(y);
            this.getTriggersOv();
            this.getAssociatedTriggers();
        },
        methods: {
            async getAssociatedTriggers(currentPage=this.triggerOpt.currentPage){
                let resp = await getAssociatedTriggers(this.deviceData.id,currentPage,this.triggerOpt.page_size);
                if(resp.code==0){
                    this.tableData = resp.data;
                    this.triggerOpt.realSize = resp.realSize;
                }else if(resp.code=="error"){
                    return;
                }else{
                    this.$message({
                        message: "获取表格数据失败！",
                        type: 'error'
                    });
                }
            },
            async getTriggersOv(){
                let resp = await getTriggersOv(this.deviceData.id);
                if(resp.code==0){
                    this.SevenDays_sum= resp.data.SevenDaysSum;
                    this.yesterday_sum= resp.data.yesterdaySum;
                    this.associatedTrigger_sum= resp.data.associatedTriggerSum;
                }else if(resp.code=="error"){
                    return;
                }else{
                    this.$message({
                        message: "获取数据失败",
                        type: 'error'
                    });
                }
            },
            handleCurrentChange(val) {
                this.getAssociatedTriggers(val);
            },
            navDirect(){
                //加密
                let b = new Buffer(JSON.stringify(this.deviceData.productId));
                let s = b.toString('base64');
                let data = encodeURIComponent(s);
                this.$router.push('/myProduct/'+data+'/devManage')
            },
            goAddress(productId){
                //加密
                let b = new Buffer(JSON.stringify(productId));
                let s = b.toString('base64');
                let data = encodeURIComponent(s);
                this.$router.push('/myProduct/'+data+'/triggerManage')
            }
        }

    }
</script>

<style>
</style>
