<template>
    <div class="bgWhite fullWidth clBody">
        <div class="searchArea mgbot-20 cl-flex justifyBet">
            <el-input style="width:250px" class="search mgbot-15"
                placeholder="关键词搜索" clearable
                v-model="formInline.cmd">
                <el-button slot="append" icon="el-icon-search"></el-button>
            </el-input>
            <el-button class="clButton" @click="exportLog">导出日志</el-button>
        </div>
        <el-table :data="tableData" class="mgbot-15 fullWidth" border @sort-change="sortChange">
            <el-table-column prop="deviceId" label="设备ID"></el-table-column>
            <el-table-column prop="id" label="cmd_uuid"></el-table-column>
            <el-table-column prop="created" label="下发时间" sortable="custom"></el-table-column>
            <el-table-column prop="cmd" label="命令内容"></el-table-column>
            <el-table-column prop="rec_code" label="响应状态">
                <template slot-scope="scope">
                    <span v-if="scope.row.rec_code==0">命令已发往设备</span>
                    <span v-if="scope.row.rec_code==1">错误</span>
                </template>
            </el-table-column>
            <el-table-column prop="rec_msg" label="响应内容"></el-table-column>
        </el-table>
        <el-pagination class="cl-flex justifyEnd"
            background
            @current-change="findByCmd"
            :current-page.sync="formInline.page"
            layout="prev, pager, next"
            :total="total">
        </el-pagination>
    </div>
</template>

<script>
    import { mapGetters } from 'vuex'
    import {findByCmd} from 'api/log'
    import { findListByScenario } from 'api/devGroup'
    import { findByDgId } from 'api/dev'

    export default {
        name: 'devOrderLog',
        data () {
        return {
                formInline: {
                    cmd:'',
                    page:1,
                    number:10,
                    sorts:''
                },
                tableData:[],
                total:0
            }
        },
        computed: {
            ...mapGetters([
                'scenes','user','token','activeScene'
            ])
        },
        watch:{
            "formInline.cmd"(){
                this.findByCmd();
            }
        },
        components:{
        },
        mounted(){
            this.findByCmd()
            
        },
        methods:{
            async findByCmd(){
                let resp = await findByCmd({...this.formInline,...this.activeScene,userId:this.user.userId})
                this.tableData = resp.data;
                this.total = resp.realSize;
            },
            sortChange(filters){
                if(filters.order=="descending"){
                    this.formInline.sorts = '';
                }else{
                    this.formInline.sorts = 'created';
                }
                this.findByCmd();
            },
            async exportLog(){
                window.URL = window.URL || window.webkitURL;  // Take care of vendor prefixes.
                var xhr = new XMLHttpRequest();
                xhr.open('GET', '/celllink/api/log/exportCmdLogs?cmd='+this.formInline.cmd+'&userId='+this.user.userId+'&scenarioId='+this.activeScene.scenarioId+
                '&dgId='+this.activeScene.dgId+'&deviceId='+this.activeScene.devId);
                xhr.responseType = 'blob';
                xhr.setRequestHeader('authorization', this.token);
                xhr.onload = function(e) {
                    if (this.status == 200) {
                        var blob = this.response;
                        var URL = window.URL || window.webkitURL;  //兼容处理
                        // for ie 10 and later
                        if (window.navigator.msSaveBlob) {
                            try { 
                                window.navigator.msSaveBlob(blob, 'cell_link_log.xls');
                            }
                            catch (e) {
                                console.log(e);
                            }
                        }else{
                                let blobUrl = URL.createObjectURL(blob);
                                const a = document.createElement('a');
                                a.style.display = 'none';
                                a.download = 'cell_link_log.xls';
                                a.href = blobUrl;
                                document.body.appendChild(a);
                                a.click();
                                document.body.removeChild(a);
                        }
                    }
                    
                };
                xhr.send();
                    
            },
        }
    }
</script>
<style>
</style>