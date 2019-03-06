<template>
    <div>
        <cl-header headColor="#181818"></cl-header>
        <sub-header title="设备管理" :subtitle="`${deviceName}-下发日志`"></sub-header>
        <div class="mainContent">
            <div class="flexBtw" style="margin-bottom:20px;">
                <el-input placeholder="输入关键词后按回车键"  v-model="keywords" @keyup.enter.native="changeDevKey()" 
                    clearable style="width:320px;height:36px;"></el-input>
                <div>
                    <el-button>导出日志</el-button>
                </div>
            </div>
            <div class="cl-table">
                <el-table :data="tableData" style="width: 100%">
                    <el-table-column type="index" label="序号" width="100"></el-table-column>
                    <el-table-column prop="associated_device_sum" label="设备ID"></el-table-column>
                    <el-table-column prop="associated_device_sum" label="cmd_uuid"></el-table-column>
                    <el-table-column prop="sendTime" label="下发时间"></el-table-column>
                    <el-table-column prop="associated_device_sum" label="响应状态"></el-table-column>
                    <el-table-column label="响应内容" width="200">
                        <template slot-scope="scope">
                            <!-- <router-link :to="{name:'triggerManage', params:{prodId:productId}}"> -->
                                <el-button type="text" style="padding:0;">查看内容</el-button>
                            <!-- </router-link> -->
                        </template>
                    </el-table-column>
                </el-table>
                <div class="block center flex">
                    <el-pagination
                        @current-change="handleCurrentChange"
                        :current-page="logsOpt.currentPage"
                        :page-sizes="[logsOpt.page_size]"
                        :page-size="logsOpt.page_size"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="logsOpt.realSize">
                    </el-pagination>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import headTop from 'components/header/head'
    import subHead from 'components/subHeader/subHeader'
    import {getCmdLogs} from 'service/getData'

    export default {
        name: 'cmdLogs',
        data () {
            return {
                keywords:'',
                device_sn:123491,
                logsOpt:{
                    currentPage:1,
                    page_size:10,
                    realSize:0
                },
                deviceName:'',
                tableData:[],
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
            this.getCmdLogs();
        },
        methods: {
            async getCmdLogs(currentPage=this.logsOpt.currentPage){
                let resp = await getCmdLogs(currentPage,this.logsOpt.page_size,this.device_sn);
                if(resp.code==0){
                    this.tableData = resp.data;
                    this.logsOpt.realSize = resp.realSize;
                }else{
                    this.$message({
                        message: "获取表格数据失败！",
                        type: 'error'
                    });
                }
            },
            handleCurrentChange(val) {
                this.getCmdLogs(val);
            },
            
        }

    }
</script>

<style>
</style>
