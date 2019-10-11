<template>
    <div class="bgWhite fullWidth clBody">
        <div class="searchArea mgbot-20">
            <el-input style="width:250px" class="search mgbot-15"
                placeholder="关键词搜索" clearable
                v-model="formInline.cmd">
                <el-button slot="append" icon="el-icon-search"></el-button>
            </el-input>
            <div class="cl-flex alignCenter justifyBet">
                <el-form :inline="true" :model="formInline" class="inputForm" >
                    <el-form-item label="场景">
                        <el-select v-model="formInline.scenarioId" placeholder="请选择场景" @change="changeScene">
                            <el-option
                            v-for="item in scenes"
                            :key="item.scenarioId"
                            :label="item.scenarioName"
                            :value="item.scenarioId">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="设备组">
                        <el-select v-model="formInline.dgId" placeholder="请选择设备组" @change="changeDg">
                            <el-option
                            v-for="item in devGroups"
                            :key="item.dgId"
                            :label="item.deviceGroupName"
                            :value="item.dgId">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="设备">
                        <el-select v-model="formInline.devId" placeholder="请选择设备">
                            <el-option
                            v-for="item in devices"
                            :key="item.deviceId"
                            :label="item.deviceName"
                            :value="item.deviceId">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-form>
                <div>
                    <el-button class="clButton" @click="exportLog">导出日志</el-button>
                </div>
            </div>
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
        name: 'orderLog',
        data () {
        return {
                devGroups:[],
                devices:[],
                formInline: {
                    scenarioId:'',
                    dgId:'',
                    devId: '',
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
                'scenes','user','token'
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
            async changeScene(val){
                let resp = await findListByScenario(val);
                this.formInline.dgId = '';
                this.formInline.devId = '';
                this.devGroups = resp.data;
                this.findByCmd()
            },
            //获取设备
            async changeDg(val){
                let resp = await findByDgId(val);
                this.formInline.devId = '';
                this.devices = resp.data;
                this.findByCmd()
            },
            async findByCmd(){
                let resp = await findByCmd({...this.formInline,userId:this.user.userId})
                this.tableData = resp.data;
                this.total = resp.realSize;
            },
            sortChange(filters){
                if(filters.order=="descending"){
                    this.formInline.sorts = '';
                }else{
                    this.formInline.sorts = 'created';
                }
                this.findByDeviceName();
            },
            async exportLog(){
                window.URL = window.URL || window.webkitURL;  // Take care of vendor prefixes.
                var xhr = new XMLHttpRequest();
                xhr.open('GET', '/celllink/api/log/exportCmdLogs?cmd='+this.formInline.cmd+'&userId='+this.user.userId+'&scenarioId='+this.formInline.scenarioId+
                '&dgId='+this.formInline.dgId+'&deviceId='+this.formInline.devId);
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