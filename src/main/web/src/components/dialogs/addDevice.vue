<template>
    <el-dialog
        title="新建设备"
        :visible.sync="isVisible" width="40%">
        <v-form  ref="ruleForm" v-model="valid" style="padding:0 10%" data-app="true">
            <v-container fluid grid-list-md>
                <v-layout row wrap>
                    <v-flex xs12>
                        <v-text-field label="设备名称"  hint="*必填" v-model="ruleForm.name" :rules="nameRules" required></v-text-field>
                    </v-flex>
                </v-layout>
                <v-layout row wrap>
                    <v-flex xs12>
                        <v-text-field label="鉴权信息" hint="*必填" v-model="ruleForm.device_sn" :rules="devRules" required></v-text-field>
                    </v-flex>
                </v-layout>
                <v-layout row wrap class="btns">
                    <el-button type="primary" @click="submitForm()">确 定</el-button>
                    <el-button @click="isVisible = false">返 回</el-button>
                </v-layout>
            </v-container>
        </v-form>
    </el-dialog>
</template>

<script>
    import {mapState} from 'vuex'
    import {addDev} from 'service/getData'
  
  export default {
        name: 'addDevice',
        data () {
            return{
                valid:false,
                isVisible:this.dialogVisible,
                ruleForm: {
                    name: '',
                    device_sn:''
                },
                nameRules: [
                    v => !!v || '请输入设备名称',
                    v => (!v || !/[`~!@#$^&*()=|{}':;',\\\[\]\.<>\/?~！@#￥……&*（）——|{}【】'；：""'。，、？\s]/g.test(v)) || '设备名称不能包含特殊字符',
                    v => (!v || v.length<=128) || '设备名称不能超过128个字'
                ],
                devRules: [
                    v => !!v || '请输入鉴权信息',
                    v => (!v || v.length<=128) || '鉴权信息不能超过128个字',
                    v => (!v || !/[`~!@#$^&*()=|{}':;',\\\[\]\.<>\/?~！@#￥……&*（）——|{}【】'；：""'。，、？\s]/g.test(v)) || '鉴权信息不能包含特殊字符'
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
                let resp = await addDev(this.ruleForm.name,this.product.id,this.ruleForm.device_sn);
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
    