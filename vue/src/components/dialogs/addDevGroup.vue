<template>
    <el-dialog
        title="新建设备组"
        :visible.sync="visible" width="40%">
        <div style="padding:0 10%">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="120px">
                <el-form-item label="场景" prop="scenario.scenarioId">
                    <el-select v-model="ruleForm.scenario.scenarioId" placeholder="请选择场景">
                        <el-option
                        v-for="item in scenarios"
                        :key="item.scenarioId"
                        :label="item.scenarioName"
                        :value="item.scenarioId">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="设备组名称" prop="deviceGroupName">
                    <el-input v-model="ruleForm.deviceGroupName" placeholder="请输入设备组名称"></el-input>
                </el-form-item>
                <el-form-item label="设备组描述" prop="description">
                    <el-input type="textarea" v-model="ruleForm.description" maxlength="100" show-word-limit placeholder="请输入设备组描述"></el-input>
                </el-form-item>
                <el-form-item label="设备接入方式" prop="protocol.protocolId">
                    <el-select v-model="ruleForm.protocol.protocolId" placeholder="请选择设备接入方式">
                        <el-option
                        v-for="item in protocols"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="设备序列号" prop="serialNumber">
                    <el-input v-model="ruleForm.serialNumber" placeholder="请输入设备序列号"></el-input>
                </el-form-item>
                <el-form-item label="生产厂家" prop="factory">
                    <el-input v-model="ruleForm.factory" placeholder="请输入生产厂家"></el-input>
                </el-form-item>
                <el-form-item label="设备规格" prop="specification">
                    <el-input v-model="ruleForm.specification" placeholder="请输入设备规格"></el-input>
                </el-form-item>
                <el-form-item label="生产参数" prop="parameters">
                    <el-input v-model="ruleForm.parameters" placeholder="请输入生产参数"></el-input>
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
  import { findListByUser } from 'api/scene'
  import { addDevGroup } from 'api/devGroup'

  export default {
        name: 'addDevGroup',
        data () {
            return{
                scenarios:[],
                protocols:[{
                        value:1,
                        label:'HTTP'
                    },{
                        value:2,
                        label:'MQTT'
                    },{
                        value:3,
                        label:'TCP'
                    },{
                        value:4,
                        label:'ModBus'
                    },{
                        value:5,
                        label:'CoAP'
                    }],
                ruleForm: {
                    scenario:{
                        scenarioId:''
                    },
                    deviceGroupName:'',
                    description: '',
                    protocol:{
                        protocolId:''
                    },
                    serialNumber:'',
                    factory:'',
                    specification:'',
                    parameters:''
                },
                rules: {
                    "scenario.scenarioId": [
                        { required: true, message: '请选择场景', trigger: 'blur' },
                    ],
                    deviceGroupName:[
                        { required: true, message: '请输入设备组名称', trigger: 'blur' },
                        { min: 4, max: 10, message: '长度在 4 到 10 个字符', trigger: 'blur' }
                    ],
                    description:[
                        { max: 100, message: '设备组描述的最大长度为100', trigger: 'blur' }
                    ],
                    "protocol.protocolId": [
                        { required: true, message: '请选择设备接入方式', trigger: 'blur' },
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
        mounted(){
        },
        methods:{
            okClick: (scenarioId) => {
                this.$emit('onOk',scenarioId)
            },
            cancelClick: () => {
                this.$emit('onCancel')
            },
            async findListByUser(){
                let resp = await findListByUser(this.userId);
                this.scenarios = resp.data;
            },
            async submit(){
                let resp = await addDevGroup(this.ruleForm);
                this.$message({
                    message: "添加成功！",
                    type: 'success'
                });
                this.okClick(this.ruleForm.scenario.scenarioId);
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
    