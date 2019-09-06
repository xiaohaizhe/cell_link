<template>
    <div>
        <div class="bgWhite topCard cl-flex justifyBet mgbot-20">
            <div class="detail">
                <p class="font-24 mgbot-20"><span class="mgR-10">{{devGroup.deviceGroupName}}</span><i class="el-icon-edit colorGray2 point" ></i></p>
                <div class="cl-flex mgbot-10">
                    <p class="colorBlack font-14" style="width:200px">设备ID：<span class="colorGray">{{devGroup.dgId}}</span></p>
                    <p class="colorBlack font-14 ">设备总数：<span class="colorGray">{{devGroup.dgId}}</span></p>
                </div>
                <div class="cl-flex mgbot-10">
                    <p class="colorBlack font-14" style="width:200px">设备接入协议：<span class="colorGray">{{devGroup.protocol.protocolName}}</span></p>
                    <p class="colorBlack font-14">设备注册码： <span class="colorGray">{{devGroup.registrationCode}}</span></p>
                </div>
            </div>
            <div>
                <el-button type="warning" class="clButton" icon="el-icon-delete"  @click="deleteItem()">删除设备组</el-button>
                <el-button type="primary" class="clButton">批量导入</el-button>
                <el-button type="primary" class="clButton">新增设备</el-button>
            </div>
        </div>
        <div class="bgWhite clBody">
            <div class="searchArea mgbot-20 cl-flex justifyBet">
                <div class="cl-flex">
                    <el-input style="width:250px" class="search mgbot-15 mgR-40"
                        placeholder="关键词搜索" clearable
                        v-model="dgForm.deviceGroupName">
                        <el-button slot="append" icon="el-icon-search"></el-button>
                    </el-input>
                    <el-form :inline="true" :model="dgForm" class="inputForm">
                        <el-form-item label="创建时间">
                            <el-select v-model="dgForm.time" placeholder="请选择创建时间" @change="changeTime">
                                <el-option
                                v-for="item in timeChosen"
                                :key="item.value"
                                :label="item.name"
                                :value="item.value">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="设备状态">
                            <el-select v-model="dgForm.status" placeholder="设备状态" @change="findByDeviceName"> 
                                <el-option label="正常" value="1"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-form>
                </div>
                <div>
                    <el-button class="clButton " >导出设备信息</el-button>
                </div>
            </div>
            <el-table :data="tableData" class="mgbot-15 fullWidth" border @sort-change="sortChange">
                <el-table-column prop="deviceGroupName" label="设备名称"></el-table-column>
                <el-table-column prop="dgId" label="设备组ID"></el-table-column>
                <el-table-column prop="description" label="简介"></el-table-column>
                <el-table-column prop="protocol.protocolName" label="接入协议"></el-table-column>
                <el-table-column prop="created" label="创建时间" sortable="custom"></el-table-column>
                <el-table-column label="操作">
                    <template slot-scope="scope">
                        <el-button type="text" @click="goto(scope.row)">详情</el-button>
                        <el-button type="text" @click="deleteItem(scope.row.dgId)">删除</el-button>
                        <el-button type="text">新增设备</el-button>
                        <el-button type="text">批量导入</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination class="cl-flex justifyEnd"
                background
                :current-page.sync="dgForm.page"
                layout="prev, pager, next"
                :total="total">
            </el-pagination>
        </div>
    </div>
</template>

<script>
    import { mapGetters } from 'vuex'
    export default {
        name: 'devGroup',
        data () {
        return {
                devGroup:{},
                tableData:[],
                dgForm:{
                    time:'',
                    status:'',
                    sorts:'',
                    page:1,
                    number:10
                },
                total:0,
                timeChosen:[
                    { name: '最近三天', value: '0' }, 
                    { name: '最近三十天', value: '1' },
                    { name: '最近三个月', value: '2' },
                    { name: '今年', value: '3' },
                    { name: '去年', value: '4' },
                    { name: '前年', value: '5' },
                ],
            }
        },
        components:{
        },
        computed: {
            ...mapGetters([
                'activeScene'
            ])
        },
        mounted(){
            var x = new Buffer(decodeURIComponent(this.$route.params.data), 'base64')
            var y = x.toString('utf8');
            this.devGroup = JSON.parse(y);
        },
        methods:{
        }
    }
</script>
<style>
</style>