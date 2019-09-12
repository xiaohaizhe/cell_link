<template>
    <el-dialog
        title="新建应用"
        :visible.sync="visible" width="40%">
        <div style="padding:0 10%">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
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
                <el-form-item label="应用名称" prop="appName">
                    <el-input v-model="ruleForm.appName"></el-input>
                </el-form-item>
                <el-form-item label="应用简介" prop="description">
                    <el-input type="textarea" v-model="ruleForm.description" maxlength="100" show-word-limit></el-input>
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
  import { mapGetters } from 'vuex'
  import { findListByUser } from 'api/scene'
  import { addApp } from 'api/application'

  export default {
        name: 'addApp',
        data () {
            return{
                scenarios:[],
                ruleForm: {
                    scenario:{
                        scenarioId:''
                    },
                    appName:"",
                    description: '',
                },
                rules: {
                    "scenario.scenarioId": [
                        { required: true, message: '请选择场景', trigger: 'blur' },
                    ],
                    appName: [
                        { required: true, message: '请输入应用名称', trigger: 'blur' },
                        { min: 4, max: 10, message: '长度在 4 到 10 个字符', trigger: 'blur' }
                    ],
                    description:[
                        { max: 100, message: '应用简介的最大长度为100', trigger: 'blur' }
                    ]
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
                let resp = await addApp(this.ruleForm);
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
    