<template>
    <div class="bgWhite fullWidth clBody">
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
                    <el-form-item label="创建时间">
                        <el-select v-model="formInline.region" placeholder="创建时间">
                            <el-option label="区域一" value="shanghai"></el-option>
                            <el-option label="区域二" value="beijing"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="设备状态">
                        <el-select v-model="formInline.region" placeholder="设备状态">
                            <el-option label="区域一" value="shanghai"></el-option>
                            <el-option label="区域二" value="beijing"></el-option>
                        </el-select>
                    </el-form-item>
                </el-form>
                <div>
                    <el-button class="clButton" type="warning">批量导入设备</el-button>
                    <el-button class="clButton clPrimary" type="primary">导出设备信息</el-button>
                    <el-button class="clButton clPrimary" type="primary">新增设备</el-button>
                </div>
            </div>
            
        </div>
        <el-table :data="tableData" class="mgbot-15 fullWidth" border>
            <el-table-column type="name" label="设备名称"></el-table-column>
            <el-table-column prop="id" label="设备ID"></el-table-column>
            <el-table-column prop="name" label="鉴权信息"></el-table-column>
            <el-table-column prop="email" label="关联设备数"></el-table-column>
            <el-table-column prop="phone" label="状态"></el-table-column>
            <el-table-column prop="createTime" label="创建时间" sortable="custom"></el-table-column>
            <el-table-column label="操作">
                <template slot-scope="scope">
                    <el-button type="text">详情</el-button>
                    <el-button type="text">发送指令</el-button>
                    <el-button type="text">删除</el-button>
                </template>
            </el-table-column>
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
        name: 'devList',
        data () {
        return {
                keyword:'',
                formInline: {
                    user: '',
                    region: ''
                },
                tableData:[{
                        name:'123',
                        id:'45464'
                    },{
                        name:'123',
                        id:'45464'
                    },{
                        name:'123',
                        id:'45464'
                    },{
                        name:'123',
                        id:'45464'
                    },{
                        name:'123',
                        id:'45464'
                    },{
                        name:'123',
                        id:'45464'
                    },{
                        name:'123',
                        id:'45464'
                    },{
                        name:'123',
                        id:'45464'
                    },{
                        name:'123',
                        id:'45464'
                    },{
                        name:'123',
                        id:'45464'
                    }]
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