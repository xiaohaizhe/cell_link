<template>
    <div>
        <cl-header headColor="#181818"></cl-header>
        <sub-header title="设备关联" :subtitle="`${deviceName}-数据展示`"></sub-header>
        <div class="mainContent">
            <div class="flexAround center bg-fff devSum">
                <div>
                    <p class="devNum"><span class="font-24">{{dd_sum}}</span>条</p>
                    <p>设备数据总数</p>
                </div>
                <div>
                    <p class="devNum"><span class="font-24">{{dd_sum_y}}</span>条</p>
                    <p>昨日新增</p>
                </div>
                <div>
                    <p class="devNum"><span class="font-24">{{dd_sum_7}}</span>条</p>
                    <p>最近7日新增</p>
                </div>
            </div>
            <p class="font-16" style="margin:40px 0 20px;">数据流展示</p>
            <div class="streamTable cl-table">
                <el-table :data="streamData" style="width: 100%" @expand-change="expandChange" ref="dsTable">
                    <el-table-column prop="dm_name">
                        <template slot="header" slot-scope="scope">
                            <p class="flex">
                                <i class="show"></i>
                                <span>数据流名称</span>
                            </p>
                        </template>
                    </el-table-column>
                    <el-table-column prop="id" label="更新时间"></el-table-column>
                    <el-table-column type="expand" width="200">
                        <template slot-scope="props">
                            <div>
                                <div>
                                    <el-date-picker v-model="time" type="daterange" range-separator="至"
                                        start-placeholder="开始日期"
                                        end-placeholder="结束日期" @change='dateChange($event,props.row.id)'> 
                                    </el-date-picker>
                                    <el-button @click="getDeviceDS(props.row.id)">查看最新</el-button>
                                </div>
                                <div>
                                    <dsChart ref="dsChart" chartId="dsChart"></dsChart>
                                </div>
                            </div>
                        </template>
                    </el-table-column>
                </el-table>
                <div class="block center flex">
                    <el-pagination
                        @current-change="handleCurrentChange"
                        :current-page="streamOpt.currentPage"
                        :page-sizes="[streamOpt.page_size]"
                        :page-size="streamOpt.page_size"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="streamOpt.realSize">
                    </el-pagination>
                </div>
            </div>
            
        </div>
    </div>
</template>

<script>
    import headTop from 'components/header/head'
    import subHead from 'components/subHeader/subHeader'
    import {getDevicedslist,getDeviceDS} from 'service/getData'
    import dsChart from 'components/charts/dsChart'
    import {dateFormat} from 'config/mUtils'

    export default {
        name: 'streamShow',
        data () {
            return {
                deviceName:'',
                device_sn:123486,
                dd_sum_7:0,
                dd_sum_y:0,
                dd_sum:0,
                streamOpt:{
                    currentPage:1,
                    page_size:10,
                    realSize:0
                },
                time:'',
                streamData:[],
                expands:[],
            }
        },
        components:{
            'cl-header':headTop,
            'sub-header':subHead,
            'dsChart':dsChart
        },
        computed:{
        },
        mounted(){
            this.deviceName = this.$route.query.data.name;
            // this.device_sn = this.$route.query.device_sn;
            this.getDevicedslist();
        },
        methods: {
            async getDevicedslist(currentPage=this.streamOpt.currentPage){
                let resp = await getDevicedslist(this.device_sn,currentPage,this.streamOpt.page_size);
                if(resp.code==0){
                    this.streamData = resp.data;//DeviceDatastreams
                    this.dd_sum_7 = resp.data.dd_sum_7;
                    this.dd_sum_y = resp.data.dd_sum_y;
                    this.dd_sum = resp.data.dd_sum;
                    this.streamOpt.realSize = resp.data.realSize;
                }
            },
            async getDeviceDS(id,start=dateFormat(new Date()),end = dateFormat(new Date())){
                let resp = await getDeviceDS(1548985548907,start,end);
                if(resp.code==0){
                    this.$refs.dsChart.drawChart(resp.data);
                }else{
                    this.$alert('获取设备数据流失败', '提示', {
                        confirmButtonText: '确定',
                        callback: action => {
                        }
                    });
                }
            },
            expandChange(row,expandedRows){
                var that = this
                if (expandedRows.length>1) {
                    that.expands = []
                    if (row) {
                        that.expands.push(row);
                    }
                    this.$refs.dsTable.toggleRowExpansion(expandedRows[0]);
                    this.time='';
                } else {
                    that.expands = [];
                    this.getDeviceDS(row.id);
                }
            },
            dateChange(date,id){
                let start = dateFormat(date[0]);
                let end  = dateFormat(date[1]);
                this.getDeviceDS(id,start,end);
            },
            handleCurrentChange(val) {
                this.getDevicedslist(val);
            },
            
        }

    }
</script>

<style>
    .streamTable .el-table__expanded-cell[class*=cell]{
        padding: 20px 25px;
    }
</style>
