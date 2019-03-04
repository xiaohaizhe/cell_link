<template>
    <el-dialog
        :title="`${data.name}-编辑`"
        :visible.sync="isVisible" width="40%">
        <div style="padding:0 10%">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" class="noBorder edit" >
                <el-form-item prop="name" label="数据流名称">
                    <el-input placeholder="数据流名称" v-model="ruleForm.name"></el-input>
                </el-form-item>
                <div class="flex">
                    <el-form-item prop="unit_name" label="单位名称" class="wid50" style="margin-right:20px;">
                        <el-input placeholder="单位名称" v-model="ruleForm.unit_name"></el-input>
                    </el-form-item>
                    <el-form-item prop="unit_symbol" label="单位符号" class="wid50">
                        <el-input placeholder="单位符号" v-model="ruleForm.unit_symbol"></el-input>
                    </el-form-item>
                </div>
                
            </el-form>
        </div>
        <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitForm('ruleForm')">确 定</el-button>
            <el-button @click="isVisible = false">返 回</el-button>
        </span>
    </el-dialog>
</template>

<script>
    import {mapState} from 'vuex'
    import {modifyDs} from 'service/getData'
  
  export default {
        name: 'editDs',
        data () {
            return{
                isVisible:this.dialogVisible,
                ruleForm: {
                    id:0,
                    product_id:0,
                    name: '',
                    unit_name:'',
                    unit_symbol:''
                },
                rules: {
                    name: [
                        { required: true, message: '请输入数据流名称', trigger: 'blur' }
                    ],
                    unit_name: [
                        { required: true, message: '请输入单位名称', trigger: 'blur' }
                    ],
                    unit_symbol: [
                        { required: true, message: '请输入单位符号', trigger: 'blur' }
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
            this.ruleForm.id = this.data.id;
            this.ruleForm.product_id = this.data.productId;
            this.ruleForm.name = this.data.name;
            this.ruleForm.unit_name = this.data.unit_name;
            this.ruleForm.unit_symbol = this.data.unit_symbol;
            
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
                let resp = await modifyDs(this.ruleForm);
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
    