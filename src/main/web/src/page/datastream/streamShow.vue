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
            <p>数据流展示</p>
            <div>
                <el-table :data="streamData" style="width: 100%" @expand-change="expandChange">
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
                                    end-placeholder="结束日期">
                                    </el-date-picker>
                                    <el-button>查看最新</el-button>
                                </div>
                            </div>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
        </div>
    </div>
</template>

<script>
    import headTop from 'components/header/head'
    import subHead from 'components/subHeader/subHeader'
    import {getDevicedslist,getDeviceDS} from 'service/getData'

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
                    page_size:5,
                    realSize:0
                },
                time:'',
                streamData:[]
            }
        },
        components:{
            'cl-header':headTop,
            'sub-header':subHead
        },
        computed:{
        },
        mounted(){
            this.deviceName = this.$route.query.data.name;
            // this.device_sn = this.$route.query.device_sn;
            this.getDevicedslist();
        },
        methods: {
            async getDevicedslist(){
                let resp = await getDevicedslist(this.device_sn);
                if(resp.code==0){
                    this.streamData = resp.data;//DeviceDatastreams
                    this.dd_sum_7 = resp.data.dd_sum_7;
                    this.dd_sum_y = resp.data.dd_sum_y;
                    this.dd_sum = resp.data.dd_sum;
                }
            },
            async getDeviceDS(id,start=getDay(0),end = getDay(0)){
                let resp = await getDeviceDS(id,start,end);
            },
            expandChange(row){
                this.getDeviceDS(row.id);
            }
            
        }

    }
</script>

<style>
    
</style>
