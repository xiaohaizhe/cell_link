<template>
    <div>
        <div class="flexBtw mgbot-20">
                <el-input placeholder="输入关键词后按回车键"  class="input-with-select" v-model="keywords" @keyup.enter.native="getDsData()" 
                    clearable style="width:320px;height:36px;" @clear="clearKey()">
                     <el-select v-model="type" slot="prepend" @change="typeChange" style="min-width: 120px;">
                        <el-option
                        v-for="item in types"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                        </el-option>
                    </el-select> 
                </el-input>
<!--                
            <div>
                <el-button type="primary" @click="addVisible=true">+新建数据流</el-button>
            </div> -->
        </div>
        <div class="cl-table">
            <el-table :data="tableData" style="width: 100%" @filter-change="filter">
                <el-table-column prop="device" :label="timeLabel" column-key='time' :filtered-value="time"  
                :filter-multiple='false' :filters="timeChosen" filter-placement="bottom" width="500"
                >
                    <template slot-scope="scope">
                        <div class="rowData cl-flex cl-card">
                            <div class="report cl-cardIcon"></div>
                            <div>
                                <!-- <p class="font-18 colorBlack mgbot-10">{{scope.row.name}}</p>
                                <p class="colorGray">设备ID：{{scope.row.device_sn}}</p>
                                <p class="colorGray">创建时间：{{scope.row.create_time}}</p> -->
                            </div>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column prop="device" label="设备"></el-table-column>
                <!-- <el-table-column type="index" label="序号" width="100"></el-table-column>
                <el-table-column prop="name" label="数据流名称"></el-table-column>
                <el-table-column prop="unit_name" label="单位名称"></el-table-column>
                <el-table-column prop="unit_symbol" label="单位符号"></el-table-column>-->
                <el-table-column label="操作" width="200">
                    <template slot-scope="scope">
                        <el-tooltip class="item" effect="dark" content="详情" placement="bottom">
                            <i class="detail cl-icon"></i>
                        </el-tooltip>
                        <!-- <el-tooltip class="item" effect="dark" content="编辑" placement="bottom">
                            <i class="editIcon cl-icon" @click="editDs(scope.row)"></i>
                        </el-tooltip> -->
                        <!-- <el-tooltip class="item" effect="dark" content="删除" placement="bottom">
                            <i class="delete cl-icon" @click="deleteDs(scope.row.id)"></i>
                        </el-tooltip> -->
                    </template>
                </el-table-column> 
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
            <div class="block center cl-flex">
                <el-pagination
                    @current-change="handleCurrentChange"
                    :current-page="dsOpt.currentPage"
                    :page-sizes="[dsOpt.page_size]"
                    :page-size="dsOpt.page_size"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="dsOpt.realSize">
                </el-pagination>
            </div>
        </div>
        <!-- <add-ds :dialogVisible="addVisible" v-if='addVisible' @getAddDialogVisible="setAddVisible"></add-ds>
        <edit-ds :dialogVisible="editVisible" v-if='editVisible' :data="dsData" @getEditDialogVisible="setEditVisible"></edit-ds> -->
    </div>
</template>

<script>
import {queryDs} from 'service/getData'
import {getDay,getPreMonthDay} from 'config/mUtils'
import {mapState} from 'vuex'
// import addDs from 'components/dialogs/addDs'
// import editDs from 'components/dialogs/editDs'

export default {
    name: 'dsManage',
    data () {
        return {
            type:'0',
            types:[{id:'0',name:'全部'},{id:'1',name:'设备名称'},{id:'2',name:'数据流名称'}],
            tableData:[],
            dsOpt:{
                currentPage:1,
                page_size:10,
                realSize:0,
                start:'',
                end:''
            },
            // dsData:{},
            keywords:'',
            timeLabel:'全部',
            time:['3'],
            timeChosen:[
                { text: '最近三天', value: '0' }, 
                { text: '最近三十天', value: '1' },
                { text: '最近三个月', value: '2' },
                { text: '今年', value: '3' },
                { text: '去年', value: '4' },
                { text: '前年', value: '5' },
            ],
            // addVisible:false,
            // editVisible:false
        }
    },
    computed:{
        ...mapState([
            'product'
        ])
    },
    // components:{
    //     'add-ds':addDs,
    //     'edit-ds':editDs
    // },
    mounted(){
        this.getDsData();
    },
    methods: {
        async getDsData(currentPage=this.dsOpt.currentPage){
            let resp = await queryDs(currentPage,this.dsOpt.page_size,this.keywords,this.product.id);//this.product.id
            if(resp.code==0){
                this.tableData = resp.data;
                this.dsOpt.realSize = resp.realSize;
            }else if(resp.code=="error"){
                return;
            }else{
                this.$message({
                    message: "获取表格数据失败！",
                    type: 'error'
                });
            }
        },
        //选择设备or数据流
        typeChange(){

        },
        //筛选时间
        filter(filters) {
            if(filters.time){
                if(filters.time.length==1){
                    switch (filters.time[0]){
                            case '0':{
                                //获取最近3天日期
                                this.dsOpt.start = getDay(-2,' 00:00:00');//3天前日期
                                this.dsOpt.end = getDay(0,' 23:59:59');//当天日期
                                this.timeLabel = '最近三天';
                                break;
                            }
                            case '1':{
                                //获取最近30天日期
                                this.dsOpt.start = getDay(-29,' 00:00:00');//30天前日期
                                this.dsOpt.end = getDay(0,' 23:59:59');//当天日期
                                this.timeLabel = '最近三十天';
                                break;
                            }
                            case '2':{
                                //获取最近3个月日期
                                this.dsOpt.start = getPreMonthDay(new Date(),3,' 00:00:00');//90天前日期
                                this.dsOpt.end = getDay(0,' 23:59:59');//当天日期
                                this.timeLabel = '最近三个月';
                                break;
                            }
                            case '3':{
                                //获取今年日期
                                this.dsOpt.start = new Date().getFullYear() +'-01-01 00:00:00'//今年日期
                                this.dsOpt.end = getDay(0,' 23:59:59');//当天日期
                                this.timeLabel = '今年';
                                break;
                            }
                            case '4':{
                                //获取去年日期
                                this.dsOpt.start = new Date().getFullYear()-1 +'-01-01 00:00:00'//去年日期
                                this.dsOpt.end = new Date().getFullYear()-1 +'-12-31 23:59:59'//去年日期
                                this.timeLabel = '去年';
                                break;
                            }
                            case '5':{
                                //获取前年日期
                                this.dsOpt.start = new Date().getFullYear()-2 +'-01-01 00:00:00'//前年日期
                                this.dsOpt.end = new Date().getFullYear()-2 +'-12-31 23:59:59'//前年日期
                                this.timeLabel = '前年';
                                break;
                            }
                            
                        }
                    }else{
                        this.time = [];
                        this.dsOpt.start = '1999-01-01 00:00:00';//1999日期
                        this.dsOpt.end = getDay(0,' 23:59:59');//当天日期
                        this.timeLabel = '全部';
                    }
            }
        },
        clearKey(){
            this.getDsData();
        },
        handleCurrentChange(val) {
            this.getDsData(val);
        },
        // //弹出新建
        // setAddVisible(val){
        //     this.addVisible = val;
        //     this.getDsData();
        // },
        // //弹出编辑
        // setEditVisible(val){
        //     this.editVisible = val;
        //     this.getDsData();
        // },
        // //点击编辑触发事件
        // editDs(dsData){
        //     this.editVisible=true;
        //     this.dsData = dsData;
        // },
        // //删除事件
        // deleteDs(id){
        //     this.$confirm('删除后，相关资源将会被全部删除，且无法恢复。确定要删除吗？', '提示', {
        //         confirmButtonText: '确定',
        //         cancelButtonText: '取消',
        //         type: 'warning'
        //     }).then(() => {
        //         this.deleteDss(id);
        //     })
        // },
        // async deleteDss(id){
        //     let resp = await deleteDs(id);
        //     if(resp.code==0){
        //         this.$message({
        //             type: 'success',
        //             message: '删除成功!'
        //         });
        //         this.getDsData();
        //     }else if(resp.code=="error"){
        //         return;
        //     }else{
        //         this.$message({
        //             type: 'error',
        //             message: '删除失败!'
        //         });
        //     }
        // }

    }

}
</script>

<style>
    .input-with-select .el-input-group__prepend {
        background-color: #fff;
    }
</style>
