<template>
    <div class="bgWhite fullWidth clBody fullHeight">
        <div class="searchArea mgbot-20">
            <el-input style="width:250px" class="search mgbot-15"
                placeholder="关键词搜索"
                v-model="keyword">
                <el-button slot="append" icon="el-icon-search"></el-button>
            </el-input>
            <div class="cl-flex alignCenter justifyBet">
                <el-form :inline="true" :model="formInline" class="inputForm">
                    <el-form-item label="场景">
                        <el-select v-model="formInline.region" placeholder="场景">
                            <el-option label="区域一" value="shanghai"></el-option>
                            <el-option label="区域二" value="beijing"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="设备组">
                        <el-select v-model="formInline.region" placeholder="设备组">
                            <el-option label="区域一" value="shanghai"></el-option>
                            <el-option label="区域二" value="beijing"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="设备">
                        <el-select v-model="formInline.region" placeholder="设备">
                            <el-option label="区域一" value="shanghai"></el-option>
                            <el-option label="区域二" value="beijing"></el-option>
                        </el-select>
                    </el-form-item>
                </el-form>
                <div>
                    <el-button class="clButton">导出日志</el-button>
                </div>
            </div>
        </div>
        <el-table :data="tableData" class="mgbot-15 fullWidth" border>
            <el-table-column type="name" label="设备ID"></el-table-column>
            <el-table-column prop="id" label="cmd_uuid"></el-table-column>
            <el-table-column prop="createTime" label="下发时间" sortable="custom"></el-table-column>
            <el-table-column prop="name" label="命令内容"></el-table-column>
            <el-table-column prop="email" label="响应状态"></el-table-column>
            <el-table-column prop="phone" label="响应内容"></el-table-column>
            
        </el-table>
        <el-pagination class="cl-flex justifyEnd"
            background
            layout="prev, pager, next"
            :total="1000">
        </el-pagination>
    </div>
</template>

<script>
    export default {
        name: 'orderLog',
        data () {
        return {
                keyword:'',
                formInline: {
                    user: '',
                    region: ''
                },
                tableData:[]
            }
        },
        components:{
        },
        mounted(){
        },
        methods:{
        }
    }
</script>
<style>
</style>