<template>
    <el-dialog
        :title="`${data.name}-编辑`"
        :visible.sync="isVisible" width="40%">
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" class="noBorder edit" style="padding:0 10%">
            <el-form-item prop="name" label="设备名称">
                <el-input placeholder="设备名称" v-model="ruleForm.name"></el-input>
            </el-form-item>
            <el-form-item prop="id" label="设备ID（系统分配）">
                <el-input placeholder="设备ID（系统分配）" v-model="ruleForm.id" disabled></el-input>
            </el-form-item>
            <el-form-item prop="device_sn" label="鉴权信息">
                <el-input placeholder="鉴权信息" v-model="ruleForm.device_sn" disabled></el-input>
            </el-form-item>
            <!-- <p>设备图标</p>
            <div class="report" style="width: 100px;height: 100px;background-size: cover;margin:10px 0;"></div>
            <el-button>修改默认</el-button>
            <el-button>本地上传</el-button> -->
        </el-form>
        <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitForm('ruleForm')">确 定</el-button>
            <el-button @click="isVisible = false">取消</el-button>
        </span>
    </el-dialog>
</template>

<script>
    import {mapState} from 'vuex'
    import {modifyDev} from 'service/getData'
  
  export default {
        name: 'editDevice',
        data () {
            return{
                isVisible:this.dialogVisible,
                ruleForm: {
                    name: '',
                    device_sn:'',
                    id:0
                },
                rules: {
                    name: [
                        { required: true, message: '请输入设备名称', trigger: 'blur' }
                    ],
                    device_sn: [
                        { required: true, message: '请输入鉴权信息', trigger: 'blur' }
                    ]
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
        mounted(){
            this.ruleForm.name = this.data.name;
            this.ruleForm.id = this.data.id;
            this.ruleForm.device_sn = this.data.device_sn;
        },
        watch:{
            dialogVisible(val){
                this.isVisible = this.dialogVisible
            },
            isVisible(val){
                this.$emit('getEditDialogVisible', val)
            }
        },
        methods:{
            async submit(){
                let resp = await modifyDev(this.ruleForm);
                if(resp.code==0){
                    this.$message({
                        message: "修改成功！",
                        type: 'success'
                    });
                    this.isVisible = false;
                }else{
                    this.$message({
                        message: "修改失败！",
                        type: 'error'
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
        }
    }
    </script>

    <style>
    </style>
    