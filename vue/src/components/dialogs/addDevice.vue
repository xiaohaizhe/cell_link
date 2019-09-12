<template>
    <el-dialog
        title="新建设备"
        :visible.sync="visible" width="40%">
        <div style="padding:0 10%">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                <el-form-item label="场景" prop="scenarioId">
                    <el-select v-model="ruleForm.scenarioId" placeholder="请选择场景" @change="changeScene">
                        <el-option
                        v-for="item in scenarios"
                        :key="item.scenarioId"
                        :label="item.scenarioName"
                        :value="item.scenarioId">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="设备组" prop="deviceGroup.dgId">
                    <el-select v-model="ruleForm.deviceGroup.dgId" placeholder="请选择设备组">
                        <el-option
                        v-for="item in devGroups"
                        :key="item.dgId"
                        :label="item.deviceGroupName"
                        :value="item.dgId">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="设备名称" prop="deviceName">
                    <el-input v-model="ruleForm.deviceName" placeholder="请输入设备名称"></el-input>
                </el-form-item>
                <el-form-item label="鉴权信息" prop="devicesn">
                    <el-input v-model="ruleForm.devicesn" placeholder="请输入鉴权信息"></el-input>
                </el-form-item>
                <el-form-item label="设备坐标" prop="latitude">
                    <el-input v-model="ruleForm.latitude" placeholder="请输入纬度">
                        <template slot="append">纬度</template>
                    </el-input>
                </el-form-item>
                <el-form-item label="" prop="longitude">
                    <el-input v-model="ruleForm.longitude" placeholder="请输入经度">
                        <template slot="append">经度</template>
                    </el-input>
                </el-form-item>
                <el-form-item label="设备描述" prop="description">
                    <el-input type="textarea" v-model="ruleForm.description" maxlength="100" show-word-limit placeholder="请输入设备描述"></el-input>
                </el-form-item>

                <el-form-item class="btnRight">
                    <el-button type="primary" @click="submitForm()">确 定</el-button>
                    <el-button @click="cancelClick">返 回</el-button>
                </el-form-item>
            </el-form>
        </div>
    </el-dialog>
</template>

<script>
    import {mapGetters} from 'vuex'
    import { findListByUser } from 'api/scene'
    import { findListByScenario } from 'api/devGroup'
    import { addDev } from 'api/dev'

  export default {
        name: 'addDevice',
        data () {
            return{
                scenarios:[],
                devGroups:[],
                ruleForm: {
                    scenarioId:'',
                    deviceGroup:{
                        dgId:''
                    },
                    deviceName:'',
                    devicesn:'',
                    description:'',
                    latitude:'',
                    longitude:''
                },
                rules: {
                    scenarioId: [
                        { required: true, message: '请选择场景', trigger: 'blur' },
                    ],
                    "deviceGroup.dgId":[
                        { required: true, message: '请选择设备组', trigger: 'blur' },
                    ],
                    description:[
                        { max: 100, message: '设备描述的最大长度为100', trigger: 'blur' }
                    ],
                    deviceName:[
                        { required: true, message: '请输入设备名称', trigger: 'blur' },
                        { min: 4, max: 10, message: '长度在 4 到 10 个字符', trigger: 'blur' }
                    ],
                    latitude:[
                        { required: true, message: '请输入纬度', trigger: 'blur' },
                        { pattern:  /^(\-|\+)?([0-8]?\d{1}\.\d{0,6}|90\.0{0,6}|[0-8]?\d{1}|90)$/, message: '请输入正确的纬度', trigger: 'blur' }
                    ],
                    longitude:[
                        { required: true, message: '请输入经度', trigger: 'blur' },
                        { pattern: /^(\-|\+)?(((\d|[1-9]\d|1[0-7]\d|0{1,3})\.\d{0,6})|(\d|[1-9]\d|1[0-7]\d|0{1,3})|180\.0{0,6}|180)$/, message: '请输入正确的经度', trigger: 'blur' }
                    ],
                    devicesn:[
                        { required: true, message: '请输入鉴权信息', trigger: 'blur' },
                        { min: 4, max: 32, message: '长度在 4 到 32 个字符', trigger: 'blur' },
                        { pattern: /^(?!([a-zA-Z]+|\d+)$)/, message: '必须由字母和数字组成', trigger: 'blur' }
                    ],
                }
            }
        },
        props:{
            visible:{
                type:Boolean,
                default:false
            },
            userId:{
                type:Number
            }
        },
        methods:{
            okClick: (dgId) => {
                this.$emit('onOk',dgId)
            },
            cancelClick: () => {
                this.$emit('onCancel')
            },
            async findListByUser(){
                let resp = await findListByUser(this.userId);
                this.scenarios = resp.data;
            },
            async changeScene(val){
                this.ruleForm.deviceGroup.dgId = '';
                this.findListByScenario(val);
            },
            async findListByScenario(val){
                let resp = await findListByScenario(val);
                this.devGroups = resp.data;
            },
            async submit(){
                let resp = await addDev(this.ruleForm);
                this.$message({
                    message: "添加成功！",
                    type: 'success'
                });
                this.okClick(this.ruleForm.deviceGroup.dgId)
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
    