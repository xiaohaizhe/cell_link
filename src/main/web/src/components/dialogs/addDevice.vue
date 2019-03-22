<template>
    <el-dialog
        title="新建设备"
        :visible.sync="isVisible" width="40%">
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" class="noBorder add" style="padding:0 10%">
            <el-form-item prop="name" label=" ">
                <el-input placeholder="设备名称" v-model="ruleForm.name"></el-input>
            </el-form-item>
            <el-form-item prop="device_sn" label=" ">
                <el-input placeholder="鉴权信息" v-model="ruleForm.device_sn"></el-input>
            </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitForm('ruleForm')">确 定</el-button>
            <el-button @click="isVisible = false">返 回</el-button>
        </span>
    </el-dialog>
</template>

<script>
    import {mapState} from 'vuex'
    import {addDev} from 'service/getData'
  
  export default {
        name: 'addDevice',
        data () {
            return{
                isVisible:this.dialogVisible,
                ruleForm: {
                    name: '',
                    device_sn:''
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
            }
        },
        computed:{
            ...mapState([
                'product'
            ])
        },
        watch:{
            dialogVisible(val){
                this.isVisible = this.dialogVisible
            },
            isVisible(val){
                this.$emit('getAddDialogVisible', val)
            }
        },
        methods:{
            async submit(){
                let resp = await addDev(this.ruleForm.name,this.product.id,this.ruleForm.device_sn);
                if(resp.code==0){
                    this.$message({
                        message: "添加成功！",
                        type: 'success'
                    });
                    this.isVisible = false;
                }else{
                    this.$message({
                        message: "添加失败！"+resp.msg,
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
    