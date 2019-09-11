<template>
    <div class="bgWhite fullWidth clBody">
        <div class="searchArea mgbot-20">
            <el-input style="width:250px" class="search mgbot-15"
                placeholder="关键词搜索" clearable
                v-model="dsForm.datastreamName">
                <el-button slot="append" icon="el-icon-search"></el-button>
            </el-input>
        </div>
        <el-table :data="tableData" class="mgbot-15 fullWidth" border @expand-change="expandChange">
            <el-table-column prop="datastreamName" label="名称"></el-table-column>
            <el-table-column prop="datastreamId" label="ID"></el-table-column>
            <el-table-column prop="created" label="更新时间" sortable="custom"></el-table-column>
            <el-table-column prop="name" label="操作" type="expand">
                <template slot-scope="props">
                        <div>
                            <div>
                                <el-date-picker v-model="time" type="daterange" range-separator="至"
                                    start-placeholder="开始日期"
                                    end-placeholder="结束日期" @change='dateChange($event,props.row.dd_id)'> 
                                </el-date-picker>
                                <el-button @click="getDeviceDS(props.row.dd_id)">查看最新</el-button>
                            </div>
                            <div>
                                <dsChart ref="dsChart" chartId="dsChart"></dsChart>
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
    import { findByDevice} from 'api/ds'
    import { mapGetters } from 'vuex'

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
                total:0
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
        mounted(){
            this.findByDevice();
        },
        methods:{
            async findByDevice(){
                let resp = await findByDevice({...this.dsForm,deviceId:this.$route.params.deviceId});
                this.tableData = resp.data
                this.total = resp.realSize;
                this.dsForm.page = resp.pageSize || 1;
            }
        }
    }
</script>
<style>
</style>