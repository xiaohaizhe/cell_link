<template>
    <div>
        <div class="flexBtw" style="margin-bottom:20px;">
            <el-input placeholder="输入关键词后按回车键"  v-model="keywords" @keyup.enter.native="getTriggers()" 
                clearable style="width:320px;height:36px;"></el-input>
            <div>
                <el-button type="primary" @click="addVisible = true;">+新建触发器</el-button>
            </div>
        </div>
        <div class="cl-table">
            <el-table :data="tableData" style="width: 100%">
                <el-table-column prop="name" label="触发器名称">
                    <template slot-scope="scope">
                        <div style="padding: 10px 0;">
                            <p class="font-18 colorBlack mgbot-10">{{scope.row.trigger.name}}</p>
                            <p class="colorGray">数据流名称：</p>
                            <p class="colorGray">
                                <span v-if="scope.row.triggerMode==0">邮箱地址：</span>
                                <span v-if="scope.row.triggerMode==1">URL地址：</span>
                                {{scope.row.modeValue}}
                            </p>
                            <p class="colorGray">创建时间：{{scope.row.trigger.createTime}}</p>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column prop="criticalValue" label="触发条件" width="150px">
                    <template slot-scope="scope">
                        <div style="padding: 10px 0;">
                            <span>{{scope.row.triggerType + scope.row.criticalValue}}</span>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column prop="associated_device_sum" label="控制设备" width="150px">

                </el-table-column>
                <el-table-column prop="triggerMode" label="触发器信息接受方式" width="180px">
                    <template slot-scope="scope">
                        <div style="padding: 10px 0;">
                            <span v-if="scope.row.trigger.triggerMode==0">开启邮箱</span>
                            <span v-if="scope.row.trigger.triggerMode==1">开启URL</span>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="200">
                    <template slot-scope="scope">
                        <i class="editIcon cl-icon"></i>
                        <i class="detail cl-icon"></i>
                        <i class="linkIcon cl-icon"></i>
                        <i class="delete cl-icon"></i>
                    </template>
                </el-table-column>
            </el-table>
            <div class="block center flex">
                <el-pagination
                    @current-change="handleCurrentChange"
                    :current-page="triggerOpt.currentPage"
                    :page-sizes="[triggerOpt.page_size]"
                    :page-size="triggerOpt.page_size"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="triggerOpt.realSize">
                </el-pagination>
            </div>
        </div>
        <add-trigger :dialogVisible="addVisible" v-if='addVisible' @getAddDialogVisible="setAddVisible"></add-trigger>
    </div>
</template>

<script>
import {getByName} from 'service/getData'
import {mapState} from 'vuex'
import addTrigger from 'components/dialogs/addTrigger'


export default {
    name: 'triggerManage',
    data () {
        return {
            tableData:[],
            triggerOpt:{
                currentPage:1,
                page_size:10,
                realSize:0
            },
            keywords:'',
            addVisible:false
        }
    },
    computed:{
        ...mapState([
            'product'
        ])
    },
    mounted(){
        this.getTriggers();
    },
    components:{
        'add-trigger':addTrigger
    },
    methods: {
        async getTriggers(currentPage=this.triggerOpt.currentPage){
            let resp = await getByName(12,currentPage,this.triggerOpt.page_size,this.keywords);//this.product.id
            if(resp.code==0){
                this.tableData = resp.data;
                this.triggerOpt.realSize = resp.realSize;
            }else{
                this.$message({
                    message: "获取表格数据失败！",
                    type: 'error'
                });
            }
        },
        handleCurrentChange(val) {
            this.getTriggers(val);
        },
        //弹出新建
        setAddVisible(val){
            this.addVisible = val;
            this.getTriggers();
        },
    }

}
</script>

<style>
</style>
