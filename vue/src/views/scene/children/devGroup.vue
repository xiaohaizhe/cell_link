<template>
    <div class="bgWhite clBody">
        <div class="searchArea mgbot-20">
            <el-input style="width:250px" class="search mgbot-15"
                placeholder="关键词搜索" clearable
                v-model="dgForm.deviceGroupName">
                <el-button slot="append" icon="el-icon-search"></el-button>
            </el-input>
        </div>
        <el-table :data="tableData" class="mgbot-15 fullWidth" border @sort-change="sortChange">
            <el-table-column prop="deviceGroupName" label="设备组名称"></el-table-column>
            <el-table-column prop="dgId" label="设备组ID"></el-table-column>
            <el-table-column prop="description" label="简介"></el-table-column>
            <el-table-column prop="protocol.protocolName" label="接入协议"></el-table-column>
            <el-table-column prop="created" label="创建时间" sortable="custom"></el-table-column>
            <el-table-column label="操作">
                <template slot-scope="scope">
                    <el-button type="text">详情</el-button>
                    <el-button type="text" @click="deleteDevGroup(scope.row.dgId)">删除</el-button>
                    <el-button type="text">新增设备</el-button>
                    <el-button type="text">批量导入</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination class="cl-flex justifyEnd"
            background
            @current-change="findByScenario"
            :current-page.sync="dgForm.page"
            layout="prev, pager, next"
            :total="total">
        </el-pagination>
    </div>
</template>

<script>
    import { mapGetters } from 'vuex'
    import { findByScenario , deleteDevGroup} from 'api/devGroup'
    
    export default {
        name: 'devGroup',
        data () {
        return {
                tableData:[],
                dgForm:{
                    deviceGroupName:'',
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
            "dgForm.deviceGroupName"(){
                this.findByScenario();
            },
            'activeScene.scenarioId'(){
                this.findByScenario();
            }
        },
        computed: {
            ...mapGetters([
                'activeScene'
            ])
        },
        mounted(){
            this.findByScenario();
        },
        methods:{
            async findByScenario(){
                let resp = await findByScenario({...this.dgForm,scenarioId:this.activeScene.scenarioId})
                this.tableData = resp.data;
                this.total = resp.realSize;
                this.dgForm.page = resp.pageSize || 1;
            },
            async deleteDevGroup(val){
                let resp = await deleteDevGroup(val);
                this.$message({
                    message: "删除成功",
                    type: 'success'
                });
                this.findByScenario()
            },
            sortChange(filters){
                if(filters.order=="descending"){
                    this.dgForm.sorts = '';
                }else{
                    this.dgForm.sorts = 'created';
                }
                this.findByScenario();
            },
        }
    }
</script>
<style>
</style>