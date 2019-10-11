<template>
    <div class="bgWhite fullWidth clBody">
        <div class="searchArea mgbot-20">
            <el-input style="width:250px" class="search mgbot-15"
                placeholder="关键词搜索" clearable
                v-model="dsForm.datastreamName">
                <el-button slot="append" icon="el-icon-search"></el-button>
            </el-input>
        </div>
        <el-table :data="tableData" class="mgbot-15 fullWidth" border @expand-change="expandChange" ref="dsTable">
            <el-table-column prop="datastreamName" label="名称"></el-table-column>
            <el-table-column prop="datastreamId" label="ID"></el-table-column>
            <el-table-column prop="created" label="更新时间" sortable="custom"></el-table-column>
            <el-table-column prop="name" label="操作" type="expand" width="200">
                <template slot-scope="props">
                        <div>
                            <div>
                                <el-date-picker v-model="time[props.row.datastreamId]" type="daterange" range-separator="至"
                                    start-placeholder="开始日期"
                                    end-placeholder="结束日期" @change='dateChange($event,props.row.datastreamId)'> 
                                </el-date-picker>
                                <el-button  class="clButton " @click="findByDatastream(props.row.datastreamId)">查看最新</el-button>
                            </div>
                            <div>
                                <line-chart :ref="`dsChart${props.row.datastreamId}`" :chartId="`dsChart${props.row.datastreamId}`"></line-chart>
                                <div class="mgTop-20 mgbot-20 cl-flex alignCenter">
                                    <p class="colorBlack font-bold mgR-20">最近100个数据点异常情况</p>
                                    <el-button  class="clButton" @click="errorLog(props.row.datastreamId)">异常日志</el-button>
                                </div>
                                <dsChartAb :ref="`errorChart${props.row.datastreamId}`" :chartId="`errorChart${props.row.datastreamId}`"></dsChartAb>
                                <!-- <line-chart :ref="`errorChart${props.row.datastreamId}`" :chartId="`errorChart${props.row.datastreamId}`"></line-chart> -->
                            </div>
                        </div>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination class="cl-flex justifyEnd"
            background
            :current-page.sync="dsForm.page"
            layout="prev, pager, next"
            :total="total">
        </el-pagination>
    </div>
</template>

<script>
    import { findByDevice ,findByDatastream,getStatus} from 'api/ds'
    import { mapGetters } from 'vuex'
    import lineChart from 'components/charts/lineChart'
    import { dateFormat } from '@/utils/mUtils'
    import dsChartAbnormal from 'components/charts/dsChartAbnormal'

    export default {
        name: 'dataStream',
        data () {
        return {
            dsForm: {
                    datastreamName:'',
                    page:1,
                    number:10,
                    sorts:''
                },
                tableData:[],
                total:0,
                expands:[],
                time:{},
                start:dateFormat(new Date(new Date().toLocaleDateString())),//当天0点
                end:dateFormat(new Date(new Date(new Date().toLocaleDateString()).getTime()+24*60*60*1000-1))//当天23点59
            }
        },
        computed: {
            ...mapGetters([
                'activeScene'
            ])
        },
        watch:{
            "dsForm.datastreamName"(){
                this.findByDevice();
            }
        },
        components:{
            lineChart,
            'dsChartAb':dsChartAbnormal,
        },
        mounted(){
            this.findByDevice();
            
        },
        methods:{
            async findByDevice(){
                let resp = await findByDevice({...this.dsForm,deviceId:this.$route.params.deviceId});
                this.tableData = resp.data
                this.total = resp.realSize;
                this.dsForm.page = resp.pageSize || 1;
            },
            async findByDatastream(id,start=this.start,end=this.end){
                let resp = await findByDatastream(id,start,end);
                if(resp.data.length>0){
                    let str = "dsChart" + id;
                    this.$refs[str].drawChart(resp.data,'#3BBAF0');
                }else{
                    this.$message({
                        message: "没有数据流数据，请重新选择时间！",
                        type: 'warning'
                    });
                }
                
            },
            async getStatus(datastreamId){
                let resp = await getStatus(datastreamId)
                let str = "errorChart" + datastreamId;
                this.$refs[str].drawChart(resp.data);
            },
            errorLog(datastreamId){
                this.$log.show({
                    datastreamId:datastreamId,
                    onOk: () => {
                    },
                });
            },
            expandChange(row,expandedRows){
                var that = this
                if (expandedRows.length>1) {
                    that.expands = []
                    if (row) {
                        that.expands.push(row);
                    }
                    this.$refs.dsTable.toggleRowExpansion(expandedRows[0]);
                    this.time={};
                } else {
                    if(that.expands.length>0){
                        this.findByDatastream(that.expands[0].datastreamId)
                        this.getStatus(that.expands[0].datastreamId)
                    }else{
                        this.findByDatastream(row.datastreamId)
                        this.getStatus(row.datastreamId)
                    }
                    that.expands = [];
                    
                }
            },
            dateChange(date,id){
                let start = dateFormat(new Date(date[0].toLocaleDateString()));
                let end  = dateFormat(new Date(new Date(date[1].toLocaleDateString()).getTime()+24*60*60*1000-1));
                this.findByDatastream(id,start,end)
            },
        }
    }
</script>
<style>
</style>