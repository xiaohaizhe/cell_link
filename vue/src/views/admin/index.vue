<template>
    <el-container class="fullHeight is-vertical">
        <cl-header @updateTable="findByPage"></cl-header>
        <el-container class="fullHeight">
            <el-main class="fullHeight cl-main cl-flex directColumn">
                <breadcrumb class="mgbot-15"/>
                <cl-card class="mgbot-15" :totalData="totalData"></cl-card>
                <p class="mgbot-15">用户管理</p>
                <div class="bgWhite fullHeight">
                    <el-table :data="tableData" class="mgbot-15 fullWidth" border @sort-change="sortChange">
                        <el-table-column prop="userId" label="用户ID"></el-table-column>
                        <el-table-column prop="name" label="用户名"></el-table-column>
                        <el-table-column prop="phone" label="手机号"></el-table-column>
                        <el-table-column prop="email" label="邮箱"></el-table-column>
                        <el-table-column prop="created" label="创建时间"></el-table-column>
                        <el-table-column prop="modified" label="修改时间" sortable="custom"></el-table-column>
                        <el-table-column prop="status" label="是否禁用">
                            <!-- <template slot="header" slot-scope="slot">
                                <span>是否禁用</span>
                                <i class="disable"></i>
                            </template> -->
                            <template slot-scope="scope">
                                <el-switch 
                                    v-model="scope.row.status"
                                    :active-value="1"
                                    :inactive-value="0"
                                    active-color="#3bbaf0"
                                    inactive-color="#999999"
                                    @change="changeSwitch($event,scope.row.userId)">
                                </el-switch>
                            </template>
                        </el-table-column>
                        <el-table-column label="操作">
                            <template slot-scope="scope">
                                <!-- <el-button type="text" @click="goto(scope.row.dgId)">详情</el-button> -->
                                <el-button type="text" @click="edit(scope.row)">编辑</el-button>
                                <el-button type="text"  @click="reset(scope.row)">重置用户密码</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                    <el-pagination class="cl-flex justifyEnd"
                        background
                        :current-page.sync="userForm.page"
                        layout="prev, pager, next"
                        :total="total">
                    </el-pagination>
                </div>
                
            </el-main>
        </el-container>
        <edit-user :dialogVisible="editVisible" :euser="euser" v-if="editVisible" @userDialogVisible="editUserVisible"></edit-user>
    </el-container>    
</template>

<script>
    import clCard from 'components/card/card'
    import { mapGetters } from 'vuex'
    import clHeader from 'components/header/header'
    import breadcrumb from 'components/breadcrumb/breadcrumb'
    import { findByPage, reset ,changeValid,getOverview} from 'api/user'
    import editUser from 'components/dialogs/editUser'

    export default {
        name: 'index',
        data () {
        return {
                euser:{},
                editVisible:false,
                totalData:[{
                        name:'用户总数',
                        value:0,
                        class:'userNum'
                    },{
                        name:'已有设备组',
                        value:0,
                        class:"devGroup"
                    },{
                        name:'连接数据流',
                        value:0,
                        class:"ds"
                    },{
                        name:'应用数量',
                        value:0,
                        class:"appNum"
                    }
                ],
                tableData: [],
                userForm: {
                    page:1,
                    number:10,
                    sort:''
                },
                total:0
            }
        },
        components:{
            clCard,clHeader,breadcrumb,editUser
        },
        computed: {
            ...mapGetters([
                'user'
            ])
        },
        mounted(){
            this.getOverview();
            this.findByPage()
        },
        methods:{
            async findByPage(){
                let resp = await findByPage(this.userForm);
                this.tableData = resp.data;
                this.total = resp.realSize;
            },
            async reset(val){
                let resp = await reset(val.userId);
                this.$message({
                    message: "重置用户密码成功！",
                    type: 'success'
                });
            },
            sortChange(filters){
                if(filters.order=="descending"){
                    this.userForm.sort = '';
                }else{
                    this.userForm.sort = 'created';
                }
                this.findByPage();
            },
            async changeSwitch($event,userId){
                let resp = await changeValid(userId);
                this.$alert('修改禁用状态成功！', '提示', {
                    confirmButtonText: '确定',
                    callback: action => {
                    }
                });
                this.findByPage();
            },
            async getOverview(){
                let resp = await getOverview();
                this.totalData[0].value = resp.data.userSum;
                this.totalData[1].value = resp.data.dgSum;
                this.totalData[2].value = resp.data.datastreamSum;
                this.totalData[3].value = resp.data.appSum;
            },
            editUserVisible(val){
                this.editVisible = val;
                this.findByPage();
            },
            edit(val){
                this.editVisible=true;
                this.euser = val;
            }
        }
    }
</script>
<style>
    
    .dashboard .bgWhite{
        padding: 20px;
    }
</style>