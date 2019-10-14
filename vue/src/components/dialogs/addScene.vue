<template>
    <el-dialog
        title="新建场景"
        :visible.sync="visible" width="40%">
        <div style="padding:0 10%">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                <el-form-item label="场景名称" prop="scenarioName">
                    <el-input v-model="ruleForm.scenarioName"></el-input>
                </el-form-item>
                <el-form-item label="场景描述" prop="description">
                    <el-input type="textarea" v-model="ruleForm.description" maxlength="100" show-word-limit></el-input>
                </el-form-item>
                <el-form-item class="btnRight">
                    <el-button type="primary" @click="submitForm('ruleForm')">确 定</el-button>
                    <el-button @click="cancelClick">返 回</el-button>
                </el-form-item>
            </el-form>
        </div>
    </el-dialog>
</template>

<script>
  import { addScene } from 'api/scene'

  export default {
        name: 'addScene',
        data () {
            return{
                ruleForm: {
                    scenarioName:"",
                    description: '',
                },
                rules: {
                    scenarioName: [
                        { required: true, message: '请输入场景名称', trigger: 'blur' },
                        { pattern: /^[^`~!@#$^&*()=|{}':;'\\\[\]\<>\/?~！@#￥……&*（）——|{}【】'；：""'。，、？\s]*$/, message: '场景名称不能包含特殊字符', trigger: 'blur' },
                        { min: 4, max: 10, message: '长度在 4 到 10 个字符', trigger: 'blur' }
                    ],
                    description:[
                        { max: 100, message: '场景描述的最大长度为100', trigger: 'blur' },
                        { pattern: /^[^`~!@#$^&*()=|{}':;'\\\[\]\<>\/?~！@#￥……&*（）——|{}【】'；：""'。，、？\s]*$/, message: '场景描述不能包含特殊字符', trigger: 'blur' },
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
            okClick: () => {
                this.$emit('onOk')
            },
            cancelClick: () => {
                this.$emit('onCancel')
            },
            async submit(){
                let resp = await addScene({...this.ruleForm,user:{userId:this.userId}});
                this.$message({
                    message: "添加成功！",
                    type: 'success'
                });
                this.okClick();
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
    