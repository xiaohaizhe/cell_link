<template>
    <el-dialog
        title="新建设备"
        :visible.sync="isVisible" width="40%">
        <div style="padding:0 10%">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                <el-form-item label="场景" prop="name">
                    <el-input v-model="ruleForm.name"></el-input>
                </el-form-item>
                <el-form-item label="设备组" prop="name">
                    <el-input v-model="ruleForm.name"></el-input>
                </el-form-item>
                <el-form-item label="设备名称" prop="name">
                    <el-input v-model="ruleForm.name"></el-input>
                </el-form-item>
            </el-form>
        </div>
    </el-dialog>
</template>

<script>
  
  export default {
        name: 'addDevice',
        data () {
            return{
                valid:false,
                isVisible:this.dialogVisible,
                ruleForm: {
                    product_id:"0",
                    name: '',
                    unit_name:'',
                    unit_symbol:''
                },
                nameRules: [
                    v => !!v || '请输入数据流名称'
                ],
                unitNameRules: [
                    v => !!v || '请输入单位名称'
                ],
                symbolRules: [
                    v => !!v || '请输入单位符号'
                ]
            }
        },
        props:{
            dialogVisible:{
                type:Boolean,
                default:false
            }
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
                        message: "添加失败！"+resp.msg,
                        type: 'error'
                    });
                }
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
    