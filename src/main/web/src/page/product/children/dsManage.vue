<template>
    <div>
        <div class="flexBtw mgbot-20">
            <el-input placeholder="输入关键词后按回车键"  class="input-with-select" v-model="keywords" @keyup.enter.native="getDsData()" 
                clearable style="width:320px;height:36px;" @clear="clearKey()">
                    <el-select v-model="type" slot="prepend" style="min-width: 120px;">
                    <el-option
                    v-for="item in types"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                    </el-option>
                </el-select> 
            </el-input>
        </div>
        <div class="cl-table">
            <el-table :data="tableData" style="width: 100%" @filter-change="filter" @expand-change="expandChange" ref="dsTable">
                <el-table-column prop="device" :label="timeLabel" column-key='time' :filtered-value="time"  
                :filter-multiple='false' :filters="timeChosen" filter-placement="bottom" width="500"
                >
                    <template slot-scope="scope">
                        <div class="rowData cl-flex cl-card">
                            <div class="report cl-cardIcon"></div>
                            <div>
                                <p class="font-18 colorBlack mgbot-10">{{scope.row.name}}</p>
                                <p class="colorGray">更新时间：{{scope.row.date}}</p>
                            </div>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column prop="device_name" label="设备"></el-table-column>
                <el-table-column label="操作"  width="100">
                    <template slot-scope="scope">
                        <el-tooltip class="item" effect="dark" content="详情" placement="bottom">
                            <i class="detail cl-icon" @click="goAddress('dsDetail',{...scope.row,productId:product.id})"></i>
                        </el-tooltip>
                    </template>
                </el-table-column> 
                <el-table-column type="expand" width="100">
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
    </div>
</template>

<script>
    import {queryDs,getDeviceDS} from 'service/getData'
    import {getDay,getPreMonthDay,dateFormat} from 'config/mUtils'
    import {mapState} from 'vuex'
    import dsChart from 'components/charts/dsChart'

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
            expands:[],
        }
    },
    computed:{
        ...mapState([
            'product'
        ])
    },
    components:{
        'dsChart':dsChart
    },
    mounted(){
        this.filter({time:this.time});
    },
    methods: {
        async getDsData(currentPage=this.dsOpt.currentPage){
            let resp = await queryDs(this.product.id,this.dsOpt.start,this.dsOpt.end,this.type,this.keywords,currentPage,this.dsOpt.page_size,);//this.product.id
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
        async getDeviceDS(id,start=dateFormat(new Date(),' 00:00:00'),end = dateFormat(new Date(),' 23:59:59')){
            this.time = [start,end];
            let resp = await getDeviceDS(id,start,end);
            if(resp.code==0){
                this.$refs.dsChart.drawChart(resp.data);
            }else if(resp.code=="error"){
                return;
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
                if(that.expands.length>0){
                    this.getDeviceDS(that.expands[0].dd_id);
                }else{
                    this.getDeviceDS(row.dd_id);
                }
                that.expands = [];
                
            }
        },
        dateChange(date,id){
            let start = dateFormat(date[0],' 00:00:00');
            let end  = dateFormat(date[1],' 23:59:59');
            this.getDeviceDS(id,start,end);
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
            this.getDsData();
        },
        clearKey(){
            this.getDsData();
        },
        //分页页数改变
        handleCurrentChange(val) {
            this.getDsData(val);
        },
        //地址跳转
        goAddress(url,item){
            //加密
            let b = new Buffer(JSON.stringify(item));
            let s = b.toString('base64');
            let data = encodeURIComponent(s);
            this.$router.push({path:'/'+url+'/'+data})
        }

    }

}
</script>

<style>
    .input-with-select .el-input-group__prepend {
        background-color: #fff;
    }
</style>
