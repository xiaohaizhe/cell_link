<template>
    <div>
        <cl-header headColor="#181818"></cl-header>
        <sub-header title="设备管理" :subtitle="`${device.name}`"  detail="下发日志" v-on:direct="navDirect"></sub-header>
        <div class="mainContent">
            <div class="mgbot-20" style="overflow:hidden">
                <!-- <el-input placeholder="输入关键词后按回车键"  v-model="keywords" @keyup.enter.native="changeDevKey()" 
                    clearable style="width:320px;height:36px;"></el-input> -->
                <div style="float:right">
                    <el-button @click="exportLogs">导出日志</el-button>
                </div>
            </div>
            <div class="cl-table logTable">
                <el-table ref="table" :data="tableData" style="width: 100%" >
                    <el-table-column type="index" label="序号" width="100"></el-table-column>
                    <el-table-column prop="device_id" label="设备ID"></el-table-column>
                    <el-table-column prop="id" label="cmd_uuid"></el-table-column>
                    <el-table-column prop="sendTime" label="下发时间"></el-table-column>
                    <el-table-column prop="msg" label="命令内容"></el-table-column>
                    <el-table-column prop="res_code" label="响应状态">
                        <template slot-scope="scope">
                            <span v-if="scope.row.res_code==0">命令已发往设备</span>
                            <span v-if="scope.row.res_code==1">错误</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="响应内容">
                        <template slot-scope="scope">
                            <el-button type="text" @click="toogleExpand(scope.row)">查看内容</el-button>
                            <!-- <span v-if="scope.row.res_code==1">-</span> -->
                        </template>
                    </el-table-column>
                    <el-table-column width="1" type="expand">
                        <template  slot-scope="props">
                            <span>响应内容：{{props.row.res_msg}}</span>
                            <!-- <el-button type="text" style="padding:0;">查看内容</el-button> -->
                        </template>
                    </el-table-column>
                </el-table>
                <div class="block center cl-flex">
                    <el-pagination
                        @current-change="handleCurrentChange"
                        :current-page.sync="logsOpt.currentPage"
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
                direct:'',
                keywords:'',
                logsOpt:{
                    currentPage:1,
                    page_size:10,
                    realSize:0
                },
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
            this.getCmdLogs();
        },
        created(){
             //解密
            var x = new Buffer(decodeURIComponent(this.$route.params.data), 'base64')
            var y = x.toString('utf8');
            this.device = JSON.parse(y);
            //加密
            let b = new Buffer(this.device.productId);
            let s = b.toString('base64');
            let data = encodeURIComponent(s);
            this.direct= '/myProduct/'+data+'/devManage';

        },
        methods: {
            async getCmdLogs(currentPage=this.logsOpt.currentPage){
                let resp = await getCmdLogs(currentPage,this.logsOpt.page_size,this.device.id);
                if(resp.code==0){
                    this.tableData = resp.data;
                    this.logsOpt.realSize = resp.realSize;
                }else if(resp.code=="error"){
                    return;
                }else{
                    this.$message({
                        message: "获取表格数据失败！",
                        type: 'error'
                    });
                }
            },
            //导出设备信息
            async exportLogs(){
                // fetch('/dev/api/device/export_cmd_logs?device_id='+this.device.id, {
                //         method: 'GET',
                //         headers: {
                //             'Content-Type': 'application/json',
                //         }
                //     })
                //     .then(res => res.blob())
                //     .then(data => {
                //         let blobUrl = window.URL.createObjectURL(data);
                //         this.download(blobUrl);
                //     });
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                window.URL = window.URL || window.webkitURL;  // Take care of vendor prefixes.
                var xhr = new XMLHttpRequest();
                xhr.open('GET', '/dev/api/device/export_cmd_logs?device_id='+this.device.id, true);
                xhr.responseType = 'blob';

                xhr.onload = function(e) {
                    loading.close();
                    if (this.status == 200) {
                        var blob = this.response;
                        var URL = window.URL || window.webkitURL;  //兼容处理
                        // for ie 10 and later
                        if (window.navigator.msSaveBlob) {
                            try { 
                                window.navigator.msSaveBlob(blob, 'cell_link_cmd_logs.xls');
                            }
                            catch (e) {
                                console.log(e);
                            }
                        }else{
                                let blobUrl = URL.createObjectURL(blob);
                                const a = document.createElement('a');
                                a.style.display = 'none';
                                a.download = 'cell_link_cmd_logs.xls';
                                a.href = blobUrl;
                                a.click();
                                document.body.removeChild(a);
                        }
                    }
                    
                };

                xhr.send();
            },
            // download(blobUrl){
            //     const a = document.createElement('a');
            //     a.style.display = 'none';
            //     a.download = 'cell_link_cmd_logs.xls';
            //     a.href = blobUrl;
            //     a.click();
            //     // document.body.removeChild(a);
            // },
            // setClassName({row, index}){
            //     // 通过自己的逻辑返回一个class或者空
            //     debugger
            //     return row.res_code==1 ? 'expand' : '';
            // },
            handleCurrentChange(val) {
                this.getCmdLogs(val);
            },
            navDirect(){
                this.$router.push(this.direct)
            },
            toogleExpand(row) {
                let $table = this.$refs.table;
                $table.toggleRowExpansion(row)
            }
            
        }

    }
</script>

<style>
.expand .el-table__expand-column .cell {
    display: none;
}
.logTable .el-table__header-wrapper thead tr th.el-table__expand-column {
    border-left: none;
    background-color: #f7f7f7;
    }
</style>
