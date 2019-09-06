<template>
    <div class="bgWhite fullWidth clBody">
        <div class="searchArea mgbot-20">
            <el-input style="width:250px" class="search mgbot-15"
                placeholder="关键词搜索" clearable
                v-model="devForm.deviceName">
                <el-button slot="append" icon="el-icon-search"></el-button>
            </el-input>
            <div class="cl-flex alignCenter justifyBet">
                <el-form :inline="true" :model="devForm" class="inputForm">
                    <el-form-item label="场景">
                        <el-select v-model="devForm.scenarioId" placeholder="请选择场景" @change="changeScene">
                            <el-option
                            v-for="item in scenes"
                            :key="item.scenarioId"
                            :label="item.scenarioName"
                            :value="item.scenarioId">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="设备组">
                        <el-select v-model="devForm.dgId" placeholder="请选择设备组" @change="findByDeviceName">
                            <el-option
                            v-for="item in devGroups"
                            :key="item.dgId"
                            :label="item.deviceGroupName"
                            :value="item.dgId">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="创建时间">
                        <el-select v-model="devForm.time" placeholder="请选择创建时间" @change="changeTime">
                            <el-option
                            v-for="item in timeChosen"
                            :key="item.value"
                            :label="item.name"
                            :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="设备状态">
                        <el-select v-model="devForm.status" placeholder="设备状态" @change="findByDeviceName"> 
                            <el-option label="正常" value="1"></el-option>
                        </el-select>
                    </el-form-item>
                </el-form>
                <div>
                    <el-button class="clButton" type="warning">批量导入设备</el-button>
                    <el-button class="clButton clPrimary" type="primary">导出设备信息</el-button>
                    <el-button class="clButton clPrimary" type="primary">新增设备</el-button>
                </div>
            </div>
            
        </div>
        <el-table :data="tableData" class="mgbot-15 fullWidth" border @sort-change="sortChange">
            <el-table-column prop="deviceName" label="设备名称"></el-table-column>
            <el-table-column prop="deviceId" label="设备ID"></el-table-column>
            <el-table-column prop="devicesn" label="鉴权信息"></el-table-column>
            <el-table-column prop="status" label="状态"></el-table-column>
            <el-table-column prop="created" label="创建时间" sortable="custom"></el-table-column>
            <el-table-column label="操作">
                <template slot-scope="scope">
                    <el-button type="text">详情</el-button>
                    <el-button type="text">发送指令</el-button>
                    <el-button type="text" @click="deleteItem(scope.row.deviceId)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination class="cl-flex justifyEnd"
            background
            @current-change="findByDeviceName"
            :current-page.sync="devForm.page"
            layout="prev, pager, next"
            :total="total">
        </el-pagination>
    </div>
</template>

<script>
    import { mapGetters } from 'vuex'
    import { findListByScenario } from 'api/devGroup'
    import { findByDeviceName ,deleteDev} from 'api/dev'
    import { dateFormat } from '@/utils/mUtils'
    export default {
        name: 'devList',
        data () {
        return {
                devForm: {
                    deviceName:'',
                    scenarioId: '',
                    dgId: '',
                    start:'',
                    end:'',
                    time:'',
                    status:'',
                    page:1,
                    number:10,
                    sorts:''
                },
                total:0,
                scenarios:[],
                devGroups:[],
                tableData:[],
                timeChosen:[
                    { name: '最近三天', value: '0' }, 
                    { name: '最近三十天', value: '1' },
                    { name: '最近三个月', value: '2' },
                    { name: '今年', value: '3' },
                    { name: '去年', value: '4' },
                    { name: '前年', value: '5' },
                ],
            }
        },
        computed: {
            ...mapGetters([
                'user','scenes'
            ])
        },
        components:{
        },
        watch:{
            "devForm.deviceName"(){
                this.findByDeviceName();
            }
        },
        mounted(){
            this.findByDeviceName();
        },
        methods:{
            async changeScene(val){
                let resp = await findListByScenario(val);
                this.devForm.dgId = '';
                this.devGroups = resp.data;
                this.findByDeviceName();
            },
            async findByDeviceName(){
                let resp = await findByDeviceName({...this.devForm,userId:this.user.userId})
                this.tableData = resp.data
                this.total = resp.realSize;
                this.devForm.page = resp.pageSize;
            },
            deleteItem(val){
                this.$confirm('删除设备后，相关数据将会被全部删除，且无法恢复。确定要删除设备吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteDev(val)
                })
            },
            async deleteDev(val){
                let resp = await deleteDev(val);
                this.$message({
                    message: "删除成功",
                    type: 'success'
                });
                this.findByDeviceName()
            },
            sortChange(filters){
                if(filters.order=="descending"){
                    this.devForm.sorts = '';
                }else{
                    this.devForm.sorts = 'created';
                }
                this.findByDeviceName();
            },
            changeTime(val){
                switch(val){
                    case '0':{
                                //获取最近3天日期
                                this.devForm.end = dateFormat(new Date());
                                let start = new Date();
                                start.setTime(start.getTime() - 3600 * 1000 * 24 * 3);
                                this.devForm.start = dateFormat(start);
                                break;
                            }
                    case '1':{
                                //获取最近30天日期
                                this.devForm.end = dateFormat(new Date());
                                let start = new Date();
                                start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                                this.devForm.start = dateFormat(start);
                                break;
                            }
                    case '2':{
                                //获取最近3个月日期
                                this.devForm.end = dateFormat(new Date());
                                let start = new Date();
                                start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                                this.devForm.start = dateFormat(start);
                                break;
                            }
                    case '3':{
                                //获取今年日期
                                this.devForm.end = dateFormat(new Date())
                                this.devForm.start = new Date().getFullYear() +'-01-01 00:00:00';
                                break;
                            }
                    case '4':{
                                //获取去年日期
                                this.devForm.end = new Date().getFullYear()-1 +'-12-31 23:59:59';
                                this.devForm.start = new Date().getFullYear()-1 +'-01-01 00:00:00';
                                break;
                            }
                    case '5':{
                                //获取前年日期
                                this.devForm.start = new Date().getFullYear()-2 +'-01-01 00:00:00'//前年日期
                                this.devForm.end = new Date().getFullYear()-2 +'-12-31 23:59:59'//前年日期
                                break;
                            }
                }
                this.findByDeviceName();
            }
        }
    }
</script>
<style>
</style>