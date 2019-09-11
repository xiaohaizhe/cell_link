<template>
    <div>
        <el-table :data="tableData" class="mgbot-15 fullWidth" border @sort-change="sortChange">
            <el-table-column prop="deviceName" label="设备名称"></el-table-column>
            <el-table-column prop="deviceId" label="设备ID"></el-table-column>
            <el-table-column prop="devicesn" label="鉴权信息"></el-table-column>
            <el-table-column prop="status" label="状态"></el-table-column>
            <el-table-column prop="created" label="创建时间" sortable="custom"></el-table-column>
            <el-table-column label="操作">
                <template slot-scope="scope">
                    <el-button type="text" @click="goto(scope.row.deviceId)">详情</el-button>
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
    import { findByDeviceName ,deleteDev} from 'api/dev'
    import { mapGetters } from 'vuex'
    export default {
        name: 'devTable',
        data () {
        return {
                devForm: {
                    page:1,
                    number:10,
                    sorts:''
                },
                tableData:[],
                total:0
            }
        },
        props:{
            deviceName:String,
        },
        computed: {
            ...mapGetters([
                'user','activeScene'
            ])
        },
        mounted(){
        },
        methods:{
            async findByDeviceName(data){
                let resp = await findByDeviceName({...this.devForm,...data,userId:this.user.userId})
                this.tableData = resp.data;
                this.total = resp.realSize;
                this.devForm.page = resp.pageSize || 1;
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
            goto(item){
                this.$store.dispatch('user/setScene',{deviceId:item});
                this.$router.push('/device/'+item+'/dataStream');
            }
        }
    }
</script>
<style>
</style>