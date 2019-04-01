<template>
    <div>
        <div class="devTable cl-table">
            <el-table :data="tableData" style="width: 100%" @filter-change="filterTime">
                <el-table-column prop="device" :label="timeLabel" column-key='time' :filtered-value="time"  
                :filter-multiple='false' :filters="timeChosen" filter-placement="bottom" width="550"
                >
                    <template slot-scope="scope">
                        <div class="rowData flex cl-card">
                            <div class="report cl-cardIcon"></div>
                            <div>
                                <p class="font-18 colorBlack mgbot-10">{{scope.row.name}}</p>
                                <p class="colorGray">设备ID：{{scope.row.device_sn}}</p>
                                <p class="colorGray">创建时间：{{scope.row.create_time}}</p>
                            </div>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column prop="app_sum" label="关联应用数（个）"></el-table-column>
                <el-table-column label="操作">
                    <template slot-scope="scope">
                        <div v-if="isAdmin">
                            <i class="detail cl-icon" @click="goAddress('devDetail',{...scope.row,protocolId:product.protocolId})"></i>
                            <i class="monitor cl-icon" @click="goAddress('streamShow',scope.row)"></i>
                        </div>
                        <div v-if="!isAdmin">
                            <i class="editIcon cl-icon" @click="edit(scope.row)"></i>
                            <i class="detail cl-icon" @click="goAddress('devDetail',{...scope.row,protocolId:product.protocolId})"></i>
                            <i class="monitor cl-icon"  @click="goAddress('streamShow',scope.row)"></i>
                            <!-- <router-link :to="{path:'/trigger', query:{data:scope.row,productId:productId}}"> -->
                            <i class="circle cl-icon" @click="goAddress('trigger',{...scope.row,productId:productId})"></i>
                            <!-- </router-link > -->
                            <i class="publish cl-icon" @click="sendOrder(scope.row)"></i>
                            <i class="logIcon cl-icon" @click="goAddress('cmdLogs',{...scope.row,productId:productId})"></i>
                            <i class="delete cl-icon" @click="deleteItem(scope.row.id)"></i>
                        </div>
                    </template>
                </el-table-column>
            </el-table>
            <div class="block center flex">
                <el-pagination
                    @current-change="handleCurrentChange"
                    :current-page="deviceOpt.currentPage"
                    :page-sizes="[deviceOpt.page_size]"
                    :page-size="deviceOpt.page_size"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="deviceOpt.realSize">
                </el-pagination>
            </div>
        </div>
        <edit-device :dialogVisible="editVisible" :data="propData" v-if='editVisible' @getEditDialogVisible="setEditVisible"></edit-device>
        <send-order :dialogVisible="sendVisible" :data="propData" v-if='sendVisible' @getSendDialogVisible="setSendVisible"></send-order>
    </div>
</template>

<script>
    import {queryDevice,deleteDev} from 'service/getData'
    import {getDay,getPreMonthDay} from 'config/mUtils'
    import editDevice from 'components/dialogs/editDevice'
    import sendOrder from 'components/dialogs/sendOrder'
    import {mapState} from 'vuex'

  export default {
    name: 'devTable',
    data () {
      return {
            timeLabel:'全部',
            deviceOpt:{
                start:'',
                end:'',
                currentPage:1,
                page_size:5,
                realSize:0
            },
            maxSize:0,
            time:['3'],
            tableData: [],
            timeChosen:[
                { text: '最近三天', value: '0' }, 
                { text: '最近三十天', value: '1' },
                { text: '最近三个月', value: '2' },
                { text: '今年', value: '3' },
                { text: '去年', value: '4' },
                { text: '前年', value: '5' },
            ],
            editVisible:false,
            propData:{},
            sendVisible:false
      }
    },
    props:{
        keywords:String,
        productId:Number,
        isAdmin:Boolean
    },
    components:{
        'edit-device':editDevice,
        'send-order':sendOrder
    },
    computed:{
        ...mapState([
                'product'
            ])
    },
    mounted(){
        this.filterTime({time:this.time});
    },
    methods: {
        //获取列表接口数据
        async queryDevice(currentPage=this.deviceOpt.currentPage){
            let resp = await queryDevice(currentPage,this.deviceOpt.page_size,this.keywords,this.productId,this.deviceOpt.start,this.deviceOpt.end);
            if(resp.code==0){
                this.tableData = resp.data;
                this.deviceOpt.realSize = resp.realSize;
                if(this.maxSize<resp.realSize){
                    this.maxSize = resp.realSize;
                    this.$emit('deviceNum', this.maxSize);
                }
            }else if(resp.code=="error"){
                return;
            }else{
                this.$message({
                    message: "获取表格数据失败！",
                    type: 'error'
                });
            }
        },
         //表格页数改变事件
        handleCurrentChange(val){
            this.queryDevice(val);
        },
        //筛选时间
        filterTime(filters) {
            if(filters.time.length==1){
               switch (filters.time[0]){
                    case '0':{
                        //获取最近3天日期
                        this.deviceOpt.start = getDay(-2,' 00:00:00');//3天前日期
                        this.deviceOpt.end = getDay(0,' 23:59:59');//当天日期
                        this.timeLabel = '最近三天';
                        break;
                    }
                    case '1':{
                        //获取最近30天日期
                        this.deviceOpt.start = getDay(-29,' 00:00:00');//30天前日期
                        this.deviceOpt.end = getDay(0,' 23:59:59');//当天日期
                        this.timeLabel = '最近三十天';
                        break;
                    }
                    case '2':{
                        //获取最近3个月日期
                        this.deviceOpt.start = getPreMonthDay(new Date(),3,' 00:00:00');//90天前日期
                        this.deviceOpt.end = getDay(0,' 23:59:59');//当天日期
                        this.timeLabel = '最近三个月';
                        break;
                    }
                    case '3':{
                        //获取今年日期
                        this.deviceOpt.start = new Date().getFullYear() +'-01-01 00:00:00'//今年日期
                        this.deviceOpt.end = getDay(0,' 23:59:59');//当天日期
                        this.timeLabel = '今年';
                        break;
                    }
                    case '4':{
                        //获取去年日期
                        this.deviceOpt.start = new Date().getFullYear()-1 +'-01-01 00:00:00'//去年日期
                        this.deviceOpt.end = new Date().getFullYear()-1 +'-12-31 23:59:59'//去年日期
                        this.timeLabel = '去年';
                        break;
                    }
                    case '5':{
                        //获取前年日期
                        this.deviceOpt.start = new Date().getFullYear()-2 +'-01-01 00:00:00'//前年日期
                        this.deviceOpt.end = new Date().getFullYear()-2 +'-12-31 23:59:59'//前年日期
                        this.timeLabel = '前年';
                        break;
                    }
                    
                }
            }else{
                this.time = [];
                this.deviceOpt.start = '1999-01-01 00:00:00';//1999日期
                this.deviceOpt.end = getDay(0,' 23:59:59');//当天日期
                this.timeLabel = '全部';
            }
             this.queryDevice();
        },
        //编辑设备
        edit(data){
            this.propData = data;
            this.editVisible = true;
        },
        sendOrder(data){
            this.propData = data;
            this.sendVisible = true;
        },
        setEditVisible(val){
            this.editVisible = val;
            this.queryDevice();
        },
        setSendVisible(val){
            this.sendVisible = val;
        },
        //删除单个或多个
        deleteItem(id){
            this.$confirm('删除设备后，相关数据流等资源将会被全部删除，且无法恢复。确定要删除设备吗？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.deleteDev(id);
            })
        },
        async deleteDev(id){
            let resp = await deleteDev(id);
            if(resp.code==0){
                this.$message({
                    type: 'success',
                    message: '删除成功!'
                });
                this.queryDevice();
            }else if(resp.code=="error"){
                return;
            }else{
                this.$message({
                    type: 'error',
                    message: '删除失败!'
                });
            }
        },
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
</style>
