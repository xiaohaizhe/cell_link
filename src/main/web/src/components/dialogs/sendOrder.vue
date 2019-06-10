<template>
    <el-dialog
        :title="`${data.name}-发送命令`"
        :visible.sync="isVisible" width="40%">
        <v-form  ref="ruleForm" v-model="valid">
            <v-container fluid grid-list-md>
                <v-layout row wrap >
                    <v-flex xs12>
                        <v-radio-group v-model="ruleForm.type" row>
                            <v-radio label="字符串" value="0"></v-radio>
                            <v-radio label="十六进制" value="1"></v-radio>
                        </v-radio-group>
                    </v-flex>
                </v-layout>
                <v-layout row wrap>
                    <v-flex xs12>
                        <v-textarea label="发送命令内容"  auto-grow rows="1" hint="*必填" v-model="ruleForm.content" :rules="contentRules" required maxlength="1024" :counter="1024"></v-textarea>
                    </v-flex>
                </v-layout>
            </v-container>
        </v-form>
        <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitForm()">确 定</el-button>
            <el-button @click="isVisible = false">返 回</el-button>
        </span>
    </el-dialog>
</template>

<script>
    import {mapState} from 'vuex'
    import {sendCmd} from 'service/getData'
  
  export default {
        name: 'sendOrder',
        data () {
            return{
                valid: false,
                isVisible:this.dialogVisible,
                ruleForm: {
                    topic:0,
                    content: '',
                    type:'0'
                },
                contentRules: [
                    v => !!v || '请输入发送命令内容'
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
        watch:{
            dialogVisible(val){
                this.isVisible = this.dialogVisible
            },
            isVisible(val){
                this.$emit('getSendDialogVisible', val)
            }
        },
        computed:{
            ...mapState([
                'user'
            ])
        },
        mounted(){
            this.ruleForm.topic = this.data.id;
        },
        methods:{
            async submit(){
                let temp = {...this.ruleForm,userid:this.user.userId};
                let resp = await sendCmd(temp);
                if(resp.code==0){
                    this.$message({
                        message: "发送成功！",
                        type: 'success'
                    });
                    this.isVisible = false;
                }else if(resp.code=="error"){
                    return;
                }else{
                    this.$message({
                        message: "发送失败！"+resp.msg,
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
    