<template>
    <el-dialog
        title="编辑设备组"
        :visible.sync="isVisible" width="40%">
        <div style="padding:0 10%">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="120px">
                <el-form-item label="场景" prop="scenarioId">
                    <el-select v-model="ruleForm.scenarioId" placeholder="请选择场景">
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
                    <el-input type="textarea" v-model="ruleForm.description" autosize maxlength="100" show-word-limit placeholder="请输入设备组描述"></el-input>
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
                    <el-button type="primary" @click="submitForm('ruleForm')">确 定</el-button>
                    <el-button @click="isVisible = false">返 回</el-button>
                </el-form-item>
            </el-form>
        </div>
    </el-dialog>
</template>

<script>
  import {mapGetters} from 'vuex'
  import { findListByUser } from 'api/scene'
  import { updateDevGroup } from 'api/devGroup'

  export default {
        name: 'editDevGroup',
        data () {
            return{
                isVisible:this.dialogVisible,
                scenarios:[],
                ruleForm: {
                    scenarioId:'',
                    deviceGroupName:'',
                    description: '',
                    serialNumber:'',
                    factory:'',
                    specification:'',
                    parameters:''
                },
                rules: {
                    "scenarioId": [
                        { required: true, message: '请选择场景', trigger: 'blur' },
                    ],
                    deviceGroupName:[
                        { required: true, message: '请输入设备组名称', trigger: 'blur' },
                        { min: 4, max: 10, message: '长度在 4 到 10 个字符', trigger: 'blur' }
                    ],
                    description:[
                        { max: 100, message: '设备组描述的最大长度为100', trigger: 'blur' }
                    ],
                }
            }
        },
        props:{
            dialogVisible:{
                type:Boolean,
                default:false
            },
            data:{
                type:Object
            }
        },
        computed:{
            ...mapGetters([
                'user','activeScene'
            ])
        },
        watch:{
            dialogVisible(val){
                this.isVisible = this.dialogVisible
            },
            isVisible(val){
                this.$emit('dgDialogVisible', val)
            }
        },
        mounted(){
            this.findListByUser();
            this.ruleForm.scenarioId = this.activeScene.scenarioId;
            this.ruleForm.deviceGroupName = this.data.deviceGroupName;
            this.ruleForm.description = this.data.description;
            this.ruleForm.serialNumber = this.data.serialNumber;
            this.ruleForm.factory = this.data.factory;
            this.ruleForm.specification = this.data.specification;
            this.ruleForm.parameters = this.data.parameters;
        },
        methods:{
            async findListByUser(){
                let resp = await findListByUser(this.user.userId);
                this.scenarios = resp.data;
            },
            async submit(){
                let temp = {...this.ruleForm,"protocol":{"protocolId":this.data.protocol.protocolId},dgId:this.data.dgId};
                let resp = await updateDevGroup(temp);
                this.$message({
                    message: "修改成功！",
                    type: 'success'
                });
                this.isVisible = false;
                this.$store.dispatch('user/setScene',{dgId:this.activeScene.dgId})
            },
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.submit()
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
        }
    }
    </script>

    <style>
    </style>
    