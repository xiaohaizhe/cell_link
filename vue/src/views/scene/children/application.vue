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
                        <el-button type="text" @click="goto(scope.row.appId)">详情</el-button>
                        <el-button type="text" @click="editApp(scope.row)">编辑</el-button>
                        <el-button type="text" @click="deleteItem(scope.row.appId)">删除</el-button>
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
    import { getAppList ,deleteApp} from 'api/application'
    
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
                this.$confirm('删除应用后，相关数据将会被全部删除，且无法恢复。确定要删除应用吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteApp(val)
                })
            },
            async deleteApp(val){
                let resp = await deleteApp(val);
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
                this.$store.dispatch('user/setScene',{appId:item});
                this.$router.push('/application/'+item);
            },
            editApp(data){
                this.$editApp.show({
                    appData:data,
                    onOk: () => {
                        this.getAppList();
                    },
                });
            }
        }
    }
</script>
<style>
</style>