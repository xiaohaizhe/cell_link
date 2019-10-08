<template>
    <div class="mgTop-20">
        <el-row>
            <el-col :span="14">
                <el-form  :model="ruleForm" ref="ruleForm" style="width:100%">
                    <div v-for="(item,index) in ruleForm.analysisDatastreams" :key="index" class="cl-flex">
                        <span style="flex-shrink:0">参数{{index+1}}：</span>
                        <el-form-item label="" :prop="'analysisDatastreams.' + index + '.dgId'" class="mgR-10"
                        :rules="{ required: true, message: '请选择设备组', trigger: 'change' }">
                            <el-select v-model="item.dgId" placeholder="设备组" style="width:120px;" @change="changeDg($event,index)">
                                <el-option
                                v-for="item in devGroups"
                                :key="item.dgId"
                                :label="item.deviceGroupName"
                                :value="item.dgId">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="" :prop="'analysisDatastreams.' + index + '.deviceId'"
                                    class="mgR-10"
                                    :rules="{ required: true, message: '请选择设备', trigger: 'change' }">
                            <el-select v-model="item.deviceId" placeholder="设备" style="width:120px;" @change="changeDev($event,index)">
                                <el-option
                                v-for="item in devices[index]"
                                :key="item.deviceId"
                                :label="item.deviceName"
                                :value="item.deviceId">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="" class="mgR-10" 
                                    :prop="'analysisDatastreams.' + index + '.datastreamId'"
                                    :rules="{ required: true, message: '请选择数据流', trigger: 'change' }">
                            <el-select v-model="item.datastreamId" placeholder="数据流" style="width:120px;" @change="selectGet($event,index)" >
                                <el-option
                                v-for="item in dss[index]"
                                :key="item.datastreamId"
                                :label="item.datastreamName"
                                :value="item.datastreamId">
                                </el-option>
                            </el-select>  
                        </el-form-item>
                        <el-form-item label="" class="mgR-10"
                                    :prop="'analysisDatastreams.' + index + '.time'"
                                    :rules="{ required: true, message: '时间段', trigger: 'change' }">
                            <el-date-picker v-model="item.time" type="datetimerange" range-separator="至"
                                start-placeholder="开始日期"  style="width:250px"
                                end-placeholder="结束日期" @change='dateChange($event,index)'> 
                            </el-date-picker>
                        </el-form-item>
                        <el-form-item label="" class="mgR-10" 
                                    :prop="'analysisDatastreams.' + index + '.frequency'"
                                    :rules="{ required: true, message: '频率的范围0.5-5' , type: 'number',  trigger: 'blur'}">
                            <el-input-number v-model="item.frequency" controls-position="right" :min="0.5" :max="5" :step="0.5" style="width:80px"></el-input-number>
                        </el-form-item>
                        <el-button type="danger" icon="el-icon-delete" circle @click="deleteParam(index)" style="padding: 5px;width: 30px;height: 30px;" v-if="index<ruleForm.analysisDatastreams.length-1"></el-button>
                        <el-button type="primary" icon="el-icon-plus" circle @click="addParam()" style="padding: 5px;width: 30px;height: 30px;" v-if="index==ruleForm.analysisDatastreams.length-1"></el-button>
                    </div>
                </el-form>
                <div style="margin-top:2.14rem;">
                    <el-button type="primary" @click="submitForm('ruleForm')">生成</el-button>
                </div>
            </el-col>
            <el-col :span="10">
                <img :src="heatmapPic" v-if="showpic">
                <heat-map chartId="heatmaps" ref="heatmaps"></heat-map>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    import heatmapChart from 'components/charts/heatmapChart'
    import { findListByScenario } from 'api/devGroup'
    import { findByDgId } from 'api/dev'
    import { findByDeviceId } from 'api/ds'
    import { analysisApplication } from 'api/application'
    import { mapGetters } from 'vuex'
    import { dateFormat } from '@/utils/mUtils'

    export default {
        name: 'heatmap',
        data () {
        return {
                showpic:true,
                heatmapPic:require("assets/heatmappic.png"),
                devGroups:[],
                ruleForm:{
                    applicationType:0,
                    analysisDatastreams:[{
                            dgId:'',
                            deviceId:'',
                            datastreamId:'',
                            type:1,
                            start:'',
                            end:'',
                            gap:0,
                            frequency:5,
                            time:'',
                        },
                        {
                            dgId:'',
                            deviceId:'',
                            datastreamId:'',
                            type:1,
                            start:'',
                            end:'',
                            gap:0,
                            frequency:5,
                            time:'',
                        }
                    ],
                },
                devices:{},
                dss:{},
                labels:[]
            }
        },
        components:{
            'heat-map':heatmapChart
        },
        computed: {
            ...mapGetters([
                'activeScene'
            ])
        },
        mounted(){
            this.findListByScenario()
        },
        methods:{
            changeDg(val,index){
                this.ruleForm.analysisDatastreams[index].deviceId = '';
                this.ruleForm.analysisDatastreams[index].datastreamId = '';
                this.findByDgId(val,index)
            },
            changeDev(val,index){
                this.ruleForm.analysisDatastreams[index].datastreamId = '';
                this.findByDeviceId(val,index) 
            },
            //获取设备组
            async findListByScenario(){
                let resp = await findListByScenario(this.activeScene.scenarioId);
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
                                this.ruleForm.analysisDatastreams[index].deviceId = '';
                                this.ruleForm.analysisDatastreams[index].dgId = '';
                                this.ruleForm.analysisDatastreams[index].datastreamId = '';
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
                                this.ruleForm.analysisDatastreams[index].deviceId = '';
                                this.ruleForm.analysisDatastreams[index].datastreamId = '';
                                this.dss[index]=[]
                            }
                        });
                    }
            },
            selectGet(vId,index){
                let obj = {};
                obj = this.dss[index].find((item)=>{//这里的selectList就是上面遍历的数据源
                    return item.datastreamId === vId;//筛选出匹配数据
                });
                if(obj.datastreamName.length>5){
                    this.labels[index]=obj.datastreamName.substring(0,4)+'...';
                }else{
                    this.labels[index]=obj.datastreamName;//我这边的name就是对应label的
                }
                
                // console.log(obj.id);
            },
            //添加参数
            addParam(){
                this.ruleForm.analysisDatastreams.push(
                {
                    dgId:'',
                    deviceId:'',
                    datastreamId:'',
                    type:1,
                    start:'',
                    end:'',
                    gap:0,
                    frequency:5,
                    time:'',
                    key: Date.now()
                },);
            },
            //删除参数
            deleteParam(index){
                if(index !== -1 && this.ruleForm.analysisDatastreams.length>2){
                    this.ruleForm.analysisDatastreams.splice(index, 1);
                    this.labels.splice(index, 1);
                }else{
                    this.$alert('参数不能小于2个！', '提示', {
                        confirmButtonText: '确定',
                        callback: action => {
                        }
                    });
                }
            },
            dateChange(date,index){
                if(date[1].getTime()-date[0].getTime()<=2592000000){
                    this.ruleForm.analysisDatastreams[index].gap = date[1].getTime()-date[0].getTime();
                    this.ruleForm.analysisDatastreams[index].start =dateFormat(date[0]);
                    this.ruleForm.analysisDatastreams[index].end = dateFormat(date[1]);
                }else{
                    this.$alert('请不要选择超过30天的数据！', '提示', {
                        confirmButtonText: '确定',
                        callback: action => {
                        }
                    });
                }
                
            },
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                if (valid) {
                    this.submit();
                } else {
                    console.log('error submit!!');
                    return false;
                }
                });
            },
            async submit(){
                let resp = await analysisApplication(this.ruleForm);//this.productId,this.analysisDatastreams
                if(resp.data.data){
                    // let labels = []; 
                    this.$refs.heatmaps.drawChart(this.labels,resp.data.data);
                }
            },
        }
    }
</script>
<style>
</style>