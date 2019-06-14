<template>
    <el-dialog
        :title="`${title}-编辑`"
        :visible.sync="isVisible" width="40%">
        <div style="padding:0 10%">
            <v-form  ref="ruleForm" v-model="valid" data-app="true">
                <v-container fluid grid-list-md>
                    <v-layout row wrap>
                        <v-flex xs12>
                            <v-text-field label="数据流名称"  hint="*必填" v-model="ruleForm.name" :rules="nameRules" required></v-text-field>
                        </v-flex>
                    </v-layout>
                    <v-layout row wrap>
                        <v-flex xs6>
                            <v-text-field label="单位名称" hint="*必填" v-model="ruleForm.unit_name" :rules="unitNameRules" required></v-text-field>
                        </v-flex>
                        <v-flex xs6>
                            <v-text-field label="单位符号" hint="*必填" v-model="ruleForm.unit_symbol" :rules="symbolRules" required></v-text-field>
                        </v-flex>
                    </v-layout>
                    <v-layout row wrap class="btns">
                        <el-button type="primary" @click="submitForm()">确 定</el-button>
                        <el-button @click="isVisible = false">返 回</el-button>
                    </v-layout>
                </v-container>
            </v-form>
        </div>
    </el-dialog>
</template>

<script>
    import {mapState} from 'vuex'
    import {modifyDs} from 'service/getData'
  
  export default {
        name: 'editDs',
        data () {
            return{                
                valid:false,
                isVisible:this.dialogVisible,
                ruleForm: {
                    id:0,
                    product_id:0,
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
            },
            data:{
                type:Object
            }
        },
        computed:{
            title(){
                if(this.data.name.length>30)
                    return  this.data.name.substring(0,30)+'...';
                else
                    return  this.data.name;
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
                this.$emit('getEditDialogVisible', val)
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
                }else if(resp.code=="error"){
                    return;
                }else{
                    this.$message({
                        message: "修改失败！"+resp.msg,
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
    