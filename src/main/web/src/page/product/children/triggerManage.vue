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
            <el-table :data="tableData" style="width: 100%" v-loading="loading">
                <el-table-column prop="name" label="触发器名称">
                    <template slot-scope="scope">
                        <div style="padding: 10px 0;">
                            <p class="font-18 colorBlack mgbot-10">{{scope.row.name}}</p>
                            <p class="colorGray">数据流名称：{{scope.row.datastream_name}}</p>
                            <p class="colorGray">
                                <span v-if="scope.row.triggerMode==0">邮箱地址：</span>
                                <span v-if="scope.row.triggerMode==1">URL地址：</span>
                                {{scope.row.modeValue}}
                            </p>
                            <p class="colorGray">创建时间：{{scope.row.createTime}}</p>
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
                <el-table-column prop="associatedDeviceSum" label="控制设备" width="150px">
                    <template slot-scope="scope">
                        <div>{{scope.row.associatedDeviceSum}}个</div>
                    </template>
                </el-table-column>
                <el-table-column prop="triggerMode" label="触发器信息接受方式" width="180px">
                    <template slot-scope="scope">
                        <div style="padding: 10px 0;">
                            <span v-if="scope.row.triggerMode==0">开启邮箱</span>
                            <span v-if="scope.row.triggerMode==1">开启URL</span>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="200">
                    <template slot-scope="scope">
                        <i class="editIcon cl-icon" @click="edit(scope.row)"></i>
                        <router-link :to="{ name: 'triggerDetail', params: { data: scope.row ,productId:product.id}}">
                            <i class="detail cl-icon"></i>
                        </router-link>
                        <router-link :to="{name:'associatedDev',params:{ data: scope.row,productId:product.id}}">
                            <i class="linkIcon cl-icon"></i>
                        </router-link>
                        <i class="delete cl-icon" @click="deleteItem(scope.row.id)"></i>
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
        <edit-trigger :dialogVisible="editVisible" :data="editData" v-if='editVisible' @getEditDialogVisible="setEditVisible"></edit-trigger>
    </div>
</template>

<script>
import {getByName,deleteTrigger} from 'service/getData'
import {mapState} from 'vuex'
import addTrigger from 'components/dialogs/addTrigger'
import editTrigger from 'components/dialogs/editTrigger'


export default {
    name: 'triggerManage',
    data () {
        return {
            loading:true,
            tableData:[],
            triggerOpt:{
                currentPage:1,
                page_size:10,
                realSize:0
            },
            keywords:'',
            addVisible:false,
            editVisible:false,
            editData:{}
        }
    },
    computed:{
        ...mapState([
            'product'
        ])
    },
    mounted(){
        if(this.$route.params){
            this.editData = this.$route.params.data;
            this.editVisible = this.$route.params.editVisible;
        }
        this.getTriggers();
    },
    components:{
        'add-trigger':addTrigger,
        'edit-trigger':editTrigger
    },
    methods: {
        async getTriggers(currentPage=this.triggerOpt.currentPage){
            let resp = await getByName(5,currentPage,this.triggerOpt.page_size,this.keywords);//this.product.id
            if(resp.code==0){
                this.tableData = resp.data;
                this.triggerOpt.realSize = resp.realSize;
                this.loading = false;
            }else{
                this.$message({
                    message: "获取表格数据失败！",
                    type: 'error'
                });
                this.loading = false;
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
        setEditVisible(val){
            this.editVisible = val;
            this.getTriggers();
        },
        //编辑设备
        edit(data){
            this.editData = data;
            this.editVisible = true;
        },
        //删除事件
        deleteItem(id){
            this.$confirm('删除后，相关资源将会被全部删除，且无法恢复。确定要删除吗？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.deleteTrigger(id);
            })
        },
        async deleteTrigger(id){
            let resp = await deleteTrigger(id);
            if(resp.code==0){
                this.$message({
                    type: 'success',
                    message: '删除成功!'
                });
                this.getTriggers();
            }else{
                this.$message({
                    type: 'error',
                    message: '删除失败!'
                });
            }
        }
    }

}
</script>

<style>
</style>
