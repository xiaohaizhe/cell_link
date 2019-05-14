<template>
    <el-dialog
        title="新建数据流"
        :visible.sync="isVisible" width="40%">
        <div style="padding:0 10%">
            <p style="margin:1.43rem 0;">注：新建数据流模板后，如果设备下有同名的数据流，对应数据流会关联到该数据流模板</p>
            <v-form  ref="ruleForm" v-model="valid">
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
    import {addDs} from 'service/getData'
  
  export default {
        name: 'addDs',
        data () {
            return{
                valid:false,
                isVisible:this.dialogVisible,
                ruleForm: {
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
    