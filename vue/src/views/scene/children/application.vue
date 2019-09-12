<template>
    <div>
        <div class="bgWhite clBody">
            <div class="searchArea mgbot-20">
                <el-input style="width:250px" class="search mgbot-15"
                    placeholder="关键词搜索" clearable
                    v-model="appForm.appName">
                    <el-button slot="append" icon="el-icon-search"></el-button>
                </el-input>
            </div>
            <el-table :data="tableData" class="mgbot-15 fullWidth" border @sort-change="sortChange">
                <el-table-column prop="appName" label="应用名称"></el-table-column>
                <el-table-column prop="created" label="创建时间" sortable="custom"></el-table-column>
                <el-table-column label="操作">
                    <template slot-scope="scope">
                        <el-button type="text" @click="goto(scope.row.dgId)">详情</el-button>
                        <el-button type="text">编辑</el-button>
                        <el-button type="text" @click="deleteItem(scope.row.dgId)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination class="cl-flex justifyEnd"
                background
                @current-change="getAppList"
                :current-page.sync="appForm.page"
                layout="prev, pager, next"
                :total="total">
            </el-pagination>
        </div>
    </div>
</template>

<script>
    import { mapGetters } from 'vuex'
    import { getAppList} from 'api/application'
    
    export default {
        name: 'application',
        data () {
        return {
                tableData:[],
                appForm:{
                    appName:'',
                    sorts:'',
                    page:1,
                    number:10
                },
                total:0
            }
        },
        components:{
        },
        watch:{
            "appForm.appName"(){
                this.getAppList();
            }
        },
        computed: {
            ...mapGetters([
                'activeScene'
            ])
        },
        mounted(){
            this.getAppList();
        },
        methods:{
            async getAppList(){
                let resp = await getAppList({...this.appForm,scenarioId:this.$route.params.scenarioId})
                this.tableData = resp.data;
                this.total = resp.realSize;
            },
            deleteItem(val){
                this.$confirm('删除设备组后，相关数据将会被全部删除，且无法恢复。确定要删除设备组吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteDevGroup(val)
                })
            },
            async deleteDevGroup(val){
                let resp = await deleteDevGroup(val);
                this.$message({
                    message: "删除成功",
                    type: 'success'
                });
                this.getAppList()
            },
            sortChange(filters){
                if(filters.order=="descending"){
                    this.appForm.sorts = '';
                }else{
                    this.appForm.sorts = 'created';
                }
                this.getAppList();
            },
            goto(item){
                this.$store.dispatch('user/setScene',{dgId:item});
                this.$router.push('/devGroup/'+item);
            }
        }
    }
</script>
<style>
</style>