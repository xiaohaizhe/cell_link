<template>
    <el-dialog
        :title="`${title}-编辑`"
        :visible.sync="isVisible" width="40%">
        <v-form  ref="ruleForm" v-model="valid" style="padding:0 10%" data-app="true">
            <v-container fluid grid-list-md>
                <v-layout row wrap>
                    <v-flex xs12>
                        <v-text-field label="设备名称" hint="*必填" v-model="ruleForm.name" :rules="nameRules" required></v-text-field>
                    </v-flex>
                </v-layout>
                <v-layout row wrap>
                    <v-flex xs12>
                        <v-text-field label="设备ID（系统分配）" v-model="ruleForm.id" disabled></v-text-field>
                    </v-flex>
                </v-layout>
                <v-layout row wrap>
                    <v-flex xs12>
                        <v-text-field label="鉴权信息" v-model="ruleForm.device_sn" disabled ></v-text-field>
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
    import {modifyDev} from 'service/getData'
  
  export default {
        name: 'editDevice',
        data () {
            return{
                valid:false,
                isVisible:this.dialogVisible,
                ruleForm: {
                    name: '',
                    device_sn:'',
                    id:0
                },
                nameRules: [
                    v => !!v || '请输入设备名称',
                    v => (!v || !/[`~!@#$^&*()=|{}':;',\\\[\]\.<>\/?~！@#￥……&*（）——|{}【】'；：""'。，、？\s]/g.test(v)) || '设备名称不能包含特殊字符',
                    v => (!v || v.length<=128) || '设备名称不能超过128个字'
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
    