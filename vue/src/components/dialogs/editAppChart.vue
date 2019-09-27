<template>
    <el-dialog
        title="新建应用图表"
        :visible.sync="visible" width="50%">
        <div style="padding:0 10%">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                <el-form-item label="图表类型" prop="chart.chartId">
                    <el-select v-model="ruleForm.chart.chartId" placeholder="请选择图表类型"  style="width:50%">
                        <el-option
                        v-for="item in charts"
                        :key="item.chartId"
                        :label="item.chartName"
                        :value="item.chartId">
                        </el-option>
                    </el-select>
                </el-form-item>
                <div v-for="(item,index) in ruleForm.appDatastreamList" :key="index">
                    <el-form-item label="数据流" :prop="'appDatastreamList.' + index + '.datastream.datastreamId'">
                        <div class="cl-flex">
                            <el-select v-model="item.dgId" class="mgR-10" placeholder="请选择设备组" @change="changeDg($event,index)">
                                <el-option
                                v-for="item in devGroups"
                                :key="item.dgId"
                                :label="item.deviceGroupName"
                                :value="item.dgId">
                                </el-option>
                            </el-select>
                            <el-select v-model="item.devId" class="mgR-10" placeholder="请选择设备" @change="changeDev($event,index)">
                                <el-option
                                v-for="item in devices[index]"
                                :key="item.deviceId"
                                :label="item.deviceName"
                                :value="item.deviceId">
                                </el-option>
                            </el-select>
                            <el-select v-model="item.datastream.datastreamId" class="mgR-10" placeholder="请选择数据流">
                                <el-option
                                v-for="item in dss[index]"
                                :key="item.datastreamId"
                                :label="item.datastreamName"
                                :value="item.datastreamId">
                                </el-option>
                            </el-select>
                            <el-button type="danger" icon="el-icon-delete" circle @click="deleteParam(index)" style="padding: 7px;" v-if="index<ruleForm.appDatastreamList.length-1"></el-button>
                            <el-button type="primary" icon="el-icon-plus" circle @click="addParam()" style="padding: 7px;" v-if="index==ruleForm.appDatastreamList.length-1"></el-button>
                        </div>
                    </el-form-item>
                </div>
                
                <el-form-item class="btnRight">
                    <el-button type="primary" @click="submitForm()">确 定</el-button>
                    <el-button @click="cancelClick">返 回</el-button>
                </el-form-item>
            </el-form>
        </div>
    </el-dialog>
</template>

<script>
  import { mapGetters } from 'vuex'
  import { addAppChart,getChart } from 'api/application'
  import { findListByScenario } from 'api/devGroup'
  import { findByDgId } from 'api/dev'
  import { findByDeviceId } from 'api/ds'
  export default {
        name: 'editApp',
        data () {
            return{
                charts:[],
                devGroups:[],
                devices:{},
                dss:{},
                ruleForm: {
                    chart:{
                        chartId:''
                    },
                    appDatastreamList:[
                        {   
                            dgId:'',
                            devId:'',       
                            datastream:{
                                datastreamId:''
                            }
                        }
                    ]
                },
                rules: {

                }
            }
        },
        props:{
            visible:{
                type:Boolean,
                default:false
            },
            appData:{
                type:Array
            }
        },
        methods:{
            okClick: (scenarioId) => {
                this.$emit('onOk',scenarioId)
            },
            cancelClick: () => {
                this.$emit('onCancel')
            },
            changeDg(val,index){
                this.ruleForm.appDatastreamList[index].devId = '';
                this.ruleForm.appDatastreamList[index].datastream.datastreamId = '';
                this.findByDgId(val,index)
            },
            changeDev(val,index){
                this.ruleForm.appDatastreamList[index].datastream.datastreamId = '';
                this.findByDeviceId(val,index) 
            },
            //获取设备组
            async findListByScenario(){
                let resp = await findListByScenario(this.appData.scenarioId);
                this.devGroups = resp.data;
            },
            //获取设备
            async findByDgId(val,index){
                let resp = await findByDgId(val);
                if(resp.data.length>0){
                        let temp = {};
                        Object.assign(temp,this.devices);
                        temp[index] = resp.data;
                        this.devices=temp;
                    }else{
                        this.$alert('该设备组下没有设备数据，请重新选择！', '提示', {
                            confirmButtonText: '确定',
                            callback: action => {
                                this.ruleForm.appDatastreamList[index].devId = '';
                                this.ruleForm.appDatastreamList[index].dgId = '';
                                this.ruleForm.appDatastreamList[index].datastream.datastreamId = '';
                                this.devices[index]=[]
                            }
                        });
                    }
            },
            //获取数据流
            async findByDeviceId(val,index){
                let resp = await findByDeviceId(val);
                if(resp.data.length>0){
                        let temp = {};
                        Object.assign(temp,this.dss);
                        temp[index] = resp.data;
                        this.dss=temp;
                    }else{
                        this.$alert('该设备下没有数据流，请重新选择！', '提示', {
                            confirmButtonText: '确定',
                            callback: action => {
                                this.ruleForm.appDatastreamList[index].devId = '';
                                this.ruleForm.appDatastreamList[index].datastream.datastreamId = '';
                                this.dss[index]=[]
                            }
                        });
                    }
            },
            async getChart(){
                let resp = await getChart();
                this.charts = resp.data;
            },
            async submit(){
                let resp = await addAppChart({...this.ruleForm,app:{appId:this.appData.appId}});
                this.$message({
                    message: "添加成功！",
                    type: 'success'
                });
                this.okClick();
            },
            //添加参数
            addParam(){
                if(this.ruleForm.appDatastreamList.length<5){
                    this.ruleForm.appDatastreamList.push({
                        dgId:'',
                        devId:'',       
                        datastream:{
                            datastreamId:''
                        }
                    });
                }else{
                    this.$alert('数据流不能大于5个！', '提示', {
                        confirmButtonText: '确定',
                        callback: action => {
                        }
                    });
                }
                
            },
            //删除参数
            deleteParam(index){
                if(index !== -1 && this.ruleForm.appDatastreamList.length>1){
                    this.ruleForm.appDatastreamList.splice(index, 1);
                }else{
                    this.$alert('数据流不能小于1个！', '提示', {
                        confirmButtonText: '确定',
                        callback: action => {
                        }
                    });
                }
            },
            submitForm() {
                if (this.$refs.ruleForm.validate()) {
                    this.submit();
                }else{
                    console.log('error submit!!');
                    return false;
                }
            },
        }
    }
    </script>

    <style>
    </style>
    