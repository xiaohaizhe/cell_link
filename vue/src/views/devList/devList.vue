<template>
    <div class="bgWhite fullWidth clBody">
        <div class="searchArea mgbot-20">
            <el-input style="width:250px" class="search mgbot-15"
                placeholder="关键词搜索" clearable
                v-model="devForm.deviceName">
                <el-button slot="append" icon="el-icon-search"></el-button>
            </el-input>
            <div class="cl-flex alignCenter justifyBet">
                <el-form :inline="true" :model="devForm" class="inputForm">
                    <el-form-item label="场景">
                        <el-select v-model="devForm.scenarioId" placeholder="请选择场景" @change="changeScene">
                            <el-option
                            v-for="item in scenes"
                            :key="item.scenarioId"
                            :label="item.scenarioName"
                            :value="item.scenarioId">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="设备组">
                        <el-select v-model="devForm.dgId" placeholder="请选择设备组" @change="findByDeviceName">
                            <el-option
                            v-for="item in devGroups"
                            :key="item.dgId"
                            :label="item.deviceGroupName"
                            :value="item.dgId">
                            </el-option>
                        </el-select>
                    </el-form-item>
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
                <div>
                    <el-button class="clButton" type="warning" @click="batchImport">批量导入设备</el-button>
                    <el-button class="clButton clPrimary" type="primary" @click="exportDevice">导出设备信息</el-button>
                    <el-button class="clButton clPrimary" type="primary" @click="addDev">新增设备</el-button>
                </div>
            </div>
        </div>
        <dev-table ref="devtable"></dev-table>
    </div>
</template>

<script>
    import { mapGetters } from 'vuex'
    import { findListByScenario } from 'api/devGroup'
    import devTable from 'components/tables/devTable'
    import { dateFormat } from '@/utils/mUtils'
    import  batchImport from 'components/dialogs/batchImport'

    export default {
        name: 'devList',
        data () {
        return {
                devForm: {
                    deviceName:'',
                    scenarioId: '',
                    dgId: '',
                    start:'',
                    end:'',
                    time:'',
                    status:''
                },
                scenarios:[],
                devGroups:[],
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
        computed: {
            ...mapGetters([
                'scenes','user','token'
            ])
        },
        components:{
            devTable
        },
        watch:{
            "devForm.deviceName"(){
                this.findByDeviceName();
            }
        },
        mounted(){
            this.findByDeviceName()
        },
        methods:{
            findByDeviceName(){
                this.$refs.devtable.findByDeviceName(this.devForm);
            },
            async changeScene(val){
                let resp = await findListByScenario(val);
                this.devForm.dgId = '';
                this.devGroups = resp.data;
                this.findByDeviceName();
            },
            batchImport(){
                this.$batchImport.show({
                    userId:this.user.userId,
                    token:this.token,
                    onOk: (dgId) => {
                        this.$store.dispatch('user/getAside',{dgId:dgId});
                        this.$router.push('/devGroup/'+dgId)
                    },
                });
            },
            
            //导出设备信息
            async exportDevice(){
                window.URL = window.URL || window.webkitURL;  // Take care of vendor prefixes.
                var xhr = new XMLHttpRequest();
                xhr.open('GET', '/celllink/api/device/export?deviceName='+this.devForm.deviceName+'&userId='+this.user.userId+'&scenarioId='+this.devForm.scenarioId+
                '&dgId='+this.devForm.dgId+'&status='+this.devForm.status+'&start='+this.devForm.start+'&end='+this.devForm.end);
                xhr.responseType = 'blob';
                xhr.setRequestHeader('authorization', this.token);
                xhr.onload = function(e) {
                    if (this.status == 200) {
                        var blob = this.response;
                        var URL = window.URL || window.webkitURL;  //兼容处理
                        // for ie 10 and later
                        if (window.navigator.msSaveBlob) {
                            try { 
                                window.navigator.msSaveBlob(blob, 'cell_link_device_model.xls');
                            }
                            catch (e) {
                                console.log(e);
                            }
                        }else{
                                let blobUrl = URL.createObjectURL(blob);
                                const a = document.createElement('a');
                                a.style.display = 'none';
                                a.download = 'cell_link_device_model.xls';
                                a.href = blobUrl;
                                document.body.appendChild(a);
                                a.click();
                                document.body.removeChild(a);
                        }
                    }
                    
                };
                xhr.send();
                    
            },
            addDev(){
                this.$addDevice.show({
                    userId:this.user.userId,
                    onOk: (dgId) => {
                        this.$store.dispatch('user/getAside',{dgId:dgId});
                        this.$router.push('/devGroup/'+dgId)
                    },
                });
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