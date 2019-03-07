<template>
    <el-dialog
        title="新建触发器"
        :visible.sync="isVisible" width="40%">
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" class="noBorder add" style="padding:0 10%">
            <el-form-item prop="name" label=" ">
                <el-input placeholder="触发器名称" v-model="ruleForm.name"></el-input>
            </el-form-item>
            <div class="flex">
                <el-form-item prop="devName" label=" " class="wid50" style="margin-right:20px">
                    <el-input placeholder="设备名称" v-model="ruleForm.devName"></el-input>
                </el-form-item>
                <el-form-item prop="dsName" label=" " class="wid50">
                    <el-input placeholder="数据流名称" v-model="ruleForm.dsName"></el-input>
                </el-form-item>
            </div>
            <el-form-item prop="criticalValue" label="触发条件：选定数据" class="fix flex">
                <el-select v-model="ruleForm.triggerTypeId" placeholder="条件" style="width:80px">
                    <el-option
                    v-for="item in [{value: '1',label: '>'},{value: '2',label: '<'}]"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                    </el-option>
                </el-select>
                <el-input-number v-model="ruleForm.criticalValue" controls-position="right" style="width:80px" :min="0"></el-input-number>
            </el-form-item>
            <el-form-item prop="triggerMode" label="接受信息方式" class="fix flex">
                <el-radio v-model="ruleForm.triggerMode" label="0">邮箱</el-radio>
                <el-radio v-model="ruleForm.triggerMode" label="1">URL地址</el-radio>
            </el-form-item>
            <el-form-item prop="email" label=" " v-if="ruleForm.triggerMode==0">
                <div class="flexBtw">
                    <el-input placeholder="填写邮箱" v-model="ruleForm.email" class="btnFix"></el-input>
                    <el-button type="primary" style="margin-bottom: 10px;background:#fff;color:#409EFF" @click="verification" :disabled="verifing">{{verifiBtn}}</el-button>
                </div>
            </el-form-item>
            <el-form-item prop="code" label=" " v-if="ruleForm.triggerMode==0">
                <el-input placeholder="输入验证码" v-model="ruleForm.code"></el-input>
            </el-form-item>
            <el-form-item prop="url" label=" " v-if="ruleForm.triggerMode==1">
                <el-input placeholder="输入URL地址" v-model="ruleForm.url"></el-input>
            </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitForm('ruleForm')">确 定</el-button>
            <el-button @click="isVisible = false">取 消</el-button>
        </span>
    </el-dialog>
</template>

<script>
    import {mapState} from 'vuex'
    import {addTrigger,sendEmail,vertifyForTrigger} from 'service/getData'
  
  export default {
        name: 'editTrigger',
        data () {
            return{
                isVisible:this.dialogVisible,
                verifing: false,
                verifiBtn: '发送验证码',
                reg:/^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/,
                countTime: 60,
                ruleForm: {
                    name: '',
                    devName:'',
                    dsName:'',
                    email:'',
                    url:'',
                    code:'',
                    triggerMode:'0',
                    triggerTypeId:'1',
                    criticalValue:0
                },
                rules: {
                    name: [
                        { required: true, message: '请输入触发器名称', trigger: 'blur' }
                    ],
                    devName: [
                        { required: true, message: '请输入设备名称', trigger: 'blur' }
                    ],
                    dsName: [
                        { required: true, message: '请输入数据流名称', trigger: 'blur' }
                    ],
                    triggerMode: [
                        { required: true, message: '请选择接受信息方式', trigger: 'blur' }
                    ],
                    url: [
                        { required: true, message: '请输入URL地址', trigger: 'blur' }
                    ],
                    email: [
                        { required: true, message: '请输入邮箱', trigger: 'blur' }
                    ],
                    code: [
                        { required: true, message: '请输入验证码', trigger: 'blur' }
                    ],
                    criticalValue: [
                        { required: true, message: '请输入触发条件', trigger: 'blur' }
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
                'product',
                'userId'
            ])
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
            async submit(modeValue){
                let resp = await addTrigger(this.ruleForm.name,this.product.id,this.ruleForm.triggerTypeId-0,
                    this.ruleForm.criticalValue,this.ruleForm.triggerMode-0,modeValue,this.ruleForm.devName,
                    this.ruleForm.dsName);
                if(resp.code==0){
                    this.$message({
                        message: "添加成功！",
                        type: 'success'
                    });
                    this.isVisible = false;
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
                        if(this.ruleForm.triggerMode==0){
                            //邮箱
                            this.bind();
                        }else{
                            this.submit(this.ruleForm.url);
                        }
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            //发送验证码
            async verification(){
                if(this.ruleForm.email !='' && this.reg.test(this.ruleForm.email)){
                    let resp = await sendEmail(this.userId,this.ruleForm.email);
                    switch (resp.code){
                        case 0: this.open("验证码已发送");this.countDown();break;//成功
                        default: this.open("操作过于频繁，请稍后再试！");break;//失败
                    }
                }else{
                    this.open("请正确填写邮箱！");
                }
                
            },
            //倒计时
            countDown() {
                this.verifing = true;
                let clock = window.setInterval(() => {
                    this.countTime--
                  this.verifiBtn = this.countTime + 's后重新发送'
                    if (this.countTime < 0) {     //当倒计时小于0时清除定时器
                    window.clearInterval(clock)
                    this.verifiBtn = '发送验证码';
                    this.verifing = false;
                    this.countTime = 60;
                    }
                },1000)
              },
            //绑定
            async bind(){
                if(this.ruleForm.email !='' && this.ruleForm.code !='' && this.reg.test(this.ruleForm.email)){
                    let resp = await vertifyForTrigger(this.userId,this.ruleForm.code);
                    if(resp.code==0){
                        
                        this.$message({
                            message: resp.msg,
                            type: 'success'
                        });
                    }else{
                        this.submit(this.ruleForm.email);
                        this.open(resp.msg);
                        return false;
                    }
                }else{
                    this.open('请正确填写邮箱及验证码！');
                    return false;
                }
                
            },
            //提示消息
            open(msg) {
                this.$alert(msg, '提示', {
                    confirmButtonText: '确定',
                    callback: action => {
                    }
                });
            }
        }
    }
    </script>

    <style>
        .fix .el-form-item__label {
            position: static !important;
        }
        .btnFix .el-input__inner{
            border: none;
        }
        .el-form-item .flexBtw{
            border-bottom:1px solid #333333;
        }
        .el-form-item.is-error .flexBtw{
            border-bottom:1px solid #f56c6c;
        }
        .el-form-item.is-success .flexBtw{
            border-bottom:1px solid #67c23a;
        }
        .el-form-item .flexBtw:hover {
            border-bottom:1px solid #c0c4cc;
        }
        .el-form-item .flexBtw:focus {
            border-bottom:1px solid #409EFF;
        }
    </style>
    