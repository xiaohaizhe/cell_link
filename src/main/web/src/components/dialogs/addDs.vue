<template>
    <el-dialog
        title="新建数据流"
        :visible.sync="isVisible" width="40%">
        <div style="padding:0 10%">
            <p style="margin:20px 0;">注：新建数据流模板后，如果设备下有同名的数据流，对应数据流会关联到该数据流模板</p>
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" class="noBorder add" >
                <el-form-item prop="name" label=" ">
                    <el-input placeholder="数据流名称" v-model="ruleForm.name"></el-input>
                </el-form-item>
                <div class="flex">
                    <el-form-item prop="unit_name" label=" " class="wid50" style="margin-right:20px;">
                        <el-input placeholder="单位名称" v-model="ruleForm.unit_name"></el-input>
                    </el-form-item>
                    <el-form-item prop="unit_symbol" label=" " class="wid50">
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
    import {addDs} from 'service/getData'
  
  export default {
        name: 'addDs',
        data () {
            return{
                isVisible:this.dialogVisible,
                ruleForm: {
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
                this.ruleForm.product_id = this.product.id;
                let resp = await addDs(this.ruleForm);
                if(resp.code==0){
                    this.$message({
                        message: "添加成功！",
                        type: 'success'
                    });
                    this.isVisible = false;
                }else if(resp.code=="error"){
                    return;
                }else{
                    this.$message({
                        message: "添加失败！",
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
    