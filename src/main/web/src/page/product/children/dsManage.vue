<template>
    <div>
        <div class="flexBtw mgbot-20">
            <el-input placeholder="输入关键词后按回车键"  v-model="keywords" @keyup.enter.native="getDsData()" 
                clearable style="width:320px;height:36px;"></el-input>
            <div>
                <el-button type="primary" @click="addVisible=true">+新建数据流</el-button>
            </div>
        </div>
        <div class="cl-table">
            <el-table :data="tableData" style="width: 100%">
                <el-table-column type="index" label="序号" width="100"></el-table-column>
                <el-table-column prop="name" label="数据流名称"></el-table-column>
                <el-table-column prop="unit_name" label="单位名称"></el-table-column>
                <el-table-column prop="unit_symbol" label="单位符号"></el-table-column>
                <el-table-column label="操作" width="200">
                    <template slot-scope="scope">
                        <el-tooltip class="item" effect="dark" content="编辑" placement="bottom">
                            <i class="editIcon cl-icon" @click="editDs(scope.row)"></i>
                        </el-tooltip>
                        <el-tooltip class="item" effect="dark" content="删除" placement="bottom">
                            <i class="delete cl-icon" @click="deleteDs(scope.row.id)"></i>
                        </el-tooltip>
                    </template>
                </el-table-column>
            </el-table>
            <div class="block center flex">
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
        <add-ds :dialogVisible="addVisible" v-if='addVisible' @getAddDialogVisible="setAddVisible"></add-ds>
        <edit-ds :dialogVisible="editVisible" v-if='editVisible' :data="dsData" @getEditDialogVisible="setEditVisible"></edit-ds>
    </div>
</template>

<script>
import {getDsData,deleteDs} from 'service/getData'
import {mapState} from 'vuex'
import addDs from 'components/dialogs/addDs'
import editDs from 'components/dialogs/editDs'

export default {
    name: 'dsManage',
    data () {
        return {
            tableData:[],
            dsOpt:{
                currentPage:1,
                page_size:10,
                realSize:0
            },
            dsData:{},
            keywords:'',
            addVisible:false,
            editVisible:false
        }
    },
    computed:{
        ...mapState([
            'product'
        ])
    },
    components:{
        'add-ds':addDs,
        'edit-ds':editDs
    },
    mounted(){
        this.getDsData();
    },
    methods: {
        async getDsData(currentPage=this.dsOpt.currentPage){
            let resp = await getDsData(currentPage,this.dsOpt.page_size,this.keywords,this.product.id);//this.product.id
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
        handleCurrentChange(val) {
            this.getDsData(val);
        },
        //弹出新建
        setAddVisible(val){
            this.addVisible = val;
            this.getDsData();
        },
        //弹出编辑
        setEditVisible(val){
            this.editVisible = val;
            this.getDsData();
        },
        //点击编辑触发事件
        editDs(dsData){
            this.editVisible=true;
            this.dsData = dsData;
        },
        //删除事件
        deleteDs(id){
            this.$confirm('删除后，相关资源将会被全部删除，且无法恢复。确定要删除吗？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.deleteDss(id);
            })
        },
        async deleteDss(id){
            let resp = await deleteDs(id);
            if(resp.code==0){
                this.$message({
                    type: 'success',
                    message: '删除成功!'
                });
                this.getDsData();
            }else if(resp.code=="error"){
                return;
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
