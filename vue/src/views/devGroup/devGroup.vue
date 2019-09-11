<template>
    <div>
        <div class="bgWhite topCard cl-flex justifyBet mgbot-20">
            <div class="detail">
                <p class="font-24 mgbot-20"><span class="mgR-10">{{activeScene.deviceGroupName}}</span><i class="el-icon-edit colorGray2 point" @click="editVisible=true"></i></p>
                <div class="cl-flex mgbot-10">
                    <p class="colorBlack font-14" style="width:200px">设备组ID：<span class="colorGray">{{activeScene.dgId}}</span></p>
                    <p class="colorBlack font-14 " style="width:200px">设备总数：<span class="colorGray">{{activeScene.sum}}</span></p>
                    <p class="colorBlack font-14 " style="width:200px">今日新增：<span class="colorGray">{{activeScene.sum_today}}</span></p>
                    <p class="colorBlack font-14 ">昨日新增：<span class="colorGray">{{activeScene.sum_yesterday}}</span></p>
                </div>
                <div class="cl-flex mgbot-10">
                    <p class="colorBlack font-14" style="width:200px">设备组接入协议：<span class="colorGray">{{protocolName}}</span></p>
                    <p class="colorBlack font-14">设备组注册码： <span class="colorGray">{{activeScene.registrationCode}}</span></p>
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
                        v-model="devForm.deviceName">
                        <el-button slot="append" icon="el-icon-search"></el-button>
                    </el-input>
                    <el-form :inline="true" :model="devForm" class="inputForm">
                        <el-form-item label="创建时间">
                            <el-select v-model="devForm.time" placeholder="请选择创建时间" @change="changeTime">
                                <el-option
                                v-for="item in timeChosen"
                                :key="item.value"
                                :label="item.name"
                                :value="item.value">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="设备状态">
                            <el-select v-model="devForm.status" placeholder="设备状态" @change="findByDeviceName"> 
                                <el-option label="正常" value="1"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-form>
                </div>
                <div>
                    <el-button class="clButton " >导出设备信息</el-button>
                </div>
            </div>
            <dev-table ref="devtable" @getDevTotal="getDevTotal"></dev-table>
        </div>
        <edit-dev-group :dialogVisible="editVisible" :data="activeScene" v-if="editVisible" @dgDialogVisible="editDgVisible"></edit-dev-group>

    </div>
</template>

<script>
    import { mapGetters } from 'vuex'
    import { deleteDevGroup} from 'api/devGroup'
    import { findByDeviceName ,deleteDev} from 'api/dev'
    import { dateFormat } from '@/utils/mUtils'
    import devTable from 'components/tables/devTable'
    import editDevGroup from 'components/dialogs/editDevGroup'

    export default {
        name: 'devGroup',
        data () {
        return {
                devForm:{
                    deviceName:'',
                    time:'',
                    status:'',
                    start:'',
                    end:'',
                },
                timeChosen:[
                    { name: '最近三天', value: '0' }, 
                    { name: '最近三十天', value: '1' },
                    { name: '最近三个月', value: '2' },
                    { name: '今年', value: '3' },
                    { name: '去年', value: '4' },
                    { name: '前年', value: '5' },
                ],
                editVisible:false,
            }
        },
        watch:{
            "devForm.deviceName"(){
                this.findByDeviceName();
            }
        },
        components:{
            editDevGroup,
            devTable
        },
        computed: {
            ...mapGetters([
                'activeScene','user'
            ]),
            protocolName(){
                if(this.activeScene.protocol){
                    return this.activeScene.protocol.protocolName
                }else return ''
            }
        },
        mounted(){
            this.findByDeviceName()
        },
        methods:{
            findByDeviceName(){
                this.$refs.devtable.findByDeviceName({...this.devForm,scenarioId:this.activeScene.scenarioId,dgId:this.$route.params.dgId});
            },
            editDgVisible(val){
                this.editVisible = val;
            },
            deleteItem(){
                this.$confirm('删除设备组后，相关数据将会被全部删除，且无法恢复。确定要删除设备组吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteDevGroup()
                })
            },
            async deleteDevGroup(){
                let resp = await deleteDevGroup(this.activeScene.dgId);
                this.$message({
                    message: "删除成功",
                    type: 'success'
                });
                this.$router.push('/scene/'+this.activeScene.scenarioId+'/devGroup')
            },
            changeTime(val){
                switch(val){
                    case '0':{
                                //获取最近3天日期
                                this.devForm.end = dateFormat(new Date());
                                let start = new Date();
                                start.setTime(start.getTime() - 3600 * 1000 * 24 * 3);
                                this.devForm.start = dateFormat(start);
                                break;
                            }
                    case '1':{
                                //获取最近30天日期
                                this.devForm.end = dateFormat(new Date());
                                let start = new Date();
                                start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                                this.devForm.start = dateFormat(start);
                                break;
                            }
                    case '2':{
                                //获取最近3个月日期
                                this.devForm.end = dateFormat(new Date());
                                let start = new Date();
                                start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                                this.devForm.start = dateFormat(start);
                                break;
                            }
                    case '3':{
                                //获取今年日期
                                this.devForm.end = dateFormat(new Date())
                                this.devForm.start = new Date().getFullYear() +'-01-01 00:00:00';
                                break;
                            }
                    case '4':{
                                //获取去年日期
                                this.devForm.end = new Date().getFullYear()-1 +'-12-31 23:59:59';
                                this.devForm.start = new Date().getFullYear()-1 +'-01-01 00:00:00';
                                break;
                            }
                    case '5':{
                                //获取前年日期
                                this.devForm.start = new Date().getFullYear()-2 +'-01-01 00:00:00'//前年日期
                                this.devForm.end = new Date().getFullYear()-2 +'-12-31 23:59:59'//前年日期
                                break;
                            }
                }
                this.findByDeviceName();
            }

        }
    }
</script>
<style>
</style>