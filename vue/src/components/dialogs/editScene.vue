<template>
    <el-dialog
        title="编辑场景"
        :visible.sync="isVisible" width="40%">
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
                    <el-button @click="isVisible = false">返 回</el-button>
                </el-form-item>
            </el-form>
        </div>
    </el-dialog>
</template>

<script>
  import {mapGetters} from 'vuex'
  import { updateScene } from 'api/scene'

  export default {
        name: 'editScene',
        data () {
            return{
                valid:false,
                isVisible:this.dialogVisible,
                ruleForm: {
                    scenarioName:"",
                    description: '',
                },
                rules: {
                    scenarioName: [
                        { required: true, message: '请输入场景名称', trigger: 'blur' },
                        { min: 4, max: 10, message: '长度在 4 到 10 个字符', trigger: 'blur' }
                    ],
                    description:[
                        { max: 100, message: '场景描述的最大长度为100', trigger: 'blur' }
                    ]
                }
            }
        },
        props:{
            dialogVisible:{
                type:Boolean,
                default:false
            }
        },
        computed:{
            ...mapGetters([
                'activeScene'
            ])
        },
        watch:{
            dialogVisible(val){
                this.isVisible = this.dialogVisible
            },
            isVisible(val){
                this.$emit('sceneDialogVisible', val)
            }
        },
        mounted(){
            this.ruleForm.scenarioName = this.activeScene.scenarioName;
            this.ruleForm.description = this.activeScene.description;
        },
        methods:{
            async submit(){
                let resp = await updateScene({...this.ruleForm,scenarioId:this.activeScene.scenarioId});
                this.$message({
                    message: "修改成功！",
                    type: 'success'
                });
                this.$store.dispatch('user/getAside',{scenarioId:this.activeScene.scenarioId})
                this.isVisible = false;
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
    