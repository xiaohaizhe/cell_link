<template>
    <el-dialog
        :title="`${title}-编辑`"
        :visible.sync="isVisible" width="40%">
        <v-form  ref="ruleForm" v-model="valid" style="padding:0 10%" data-app="true">
            <v-container fluid grid-list-md>
                <v-layout row wrap>
                    <v-flex xs12>
                        <v-text-field label="触发器名称" hint="*必填" v-model="ruleForm.name" :rules="nameRules" required></v-text-field>
                    </v-flex>
                </v-layout>
                <v-layout row wrap xs12>
                    <v-flex xs6>
                        <v-select :items="devList" label="设备" hint="*必填"   v-model="ruleForm.deviceId" item-text="name" :rules="devRules" item-value="id"   @change="devChange"></v-select>
                    </v-flex>
                    <v-flex xs6>
                        <v-select :items="dsList" label="数据流" hint="*必填"   v-model="ruleForm.datastreamId" item-text="dm_name" :rules="dsRules" item-value="id"></v-select>
                    </v-flex>
                </v-layout>
                <v-layout row wrap >
                    <v-flex xs6>
                        <span>触发条件：选定数据</span>
                    </v-flex>
                    <v-flex xs3>
                        <v-select :items="[{value: '1',label: '>'},{value: '2',label: '<'}]" label="条件" v-model="ruleForm.triggerTypeId" item-text="label" item-value="value"></v-select>
                    </v-flex>
                    <v-flex xs3>
                        <v-text-field label="数值" type="number" v-model="ruleForm.criticalValue" required :rules="criticalValueRules"></v-text-field>
                    </v-flex>
                </v-layout>
                <v-layout row wrap >
                    <v-flex xs6>
                        <span>接受信息方式</span>
                    </v-flex>
                    <v-flex xs6>
                        <v-radio-group v-model="ruleForm.triggerMode" row :rules="triggerModeRules">
                            <v-radio label="邮箱" value="0"></v-radio>
                            <v-radio label="URL地址" value="1"></v-radio>
                        </v-radio-group>
                    </v-flex>
                </v-layout>
                 <v-layout row wrap v-if="ruleForm.triggerMode==0">
                    <v-flex xs9>
                        <v-text-field label="邮箱" hint="*必填" v-model="ruleForm.email" :rules="emailRules"  :disabled="emailDis" ref="email"></v-text-field>
                    </v-flex>
                    <v-flex xs3>
                        <el-button type="primary" style="margin-bottom: 10px;background:#fff;color:#409EFF" v-if="!emailDis" @click="verification" :disabled="verifing">{{verifiBtn}}</el-button>
                    <el-button type="primary" style="margin-bottom: 10px;background:#fff;color:#409EFF" @click="rebind" v-if="emailDis">重新绑定</el-button>
                    </v-flex>
                </v-layout>
                <v-layout row wrap v-if="ruleForm.triggerMode==0 && !emailDis">
                    <v-flex xs12>
                        <v-text-field label="验证码" hint="*必填" v-model="ruleForm.code" :rules="codeRules" required></v-text-field>
                    </v-flex>
                </v-layout>
                <v-layout row wrap v-if="ruleForm.triggerMode==1">
                    <v-flex xs12>
                        <v-text-field label="URL地址" hint="*必填" v-model="ruleForm.url" :rules="urlRules" required></v-text-field>
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
    import {modifyTrigger,sendEmail,vertifyForTrigger,getDevicelist,getDslist} from 'service/getData'
  
  export default {
        name: 'editTrigger',
        data () {
            return{
                valid:false,
                isVisible:this.dialogVisible,
                verifing: false,
                verifiBtn: '发送验证码',
                emailDis:true,
                reg:/^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/,
                countTime: 60,
                devList:[],
                dsList:[],
                ruleForm: {
                    name: '',
                    deviceId:'',
                    datastreamId:'',
                    email:'',
                    url:'',
                    code:'',
                    triggerMode:'0',
                    triggerTypeId:'1',
                    criticalValue:0
                },
                nameRules: [
                    v => !!v || '请输入触发器名称',
                    v => (!v || !/[`~!@#$^&*()=|{}':;',\\\[\]\.<>\/?~！@#￥……&*（）——|{}【】'；：""'。，、？\s]/g.test(v)) || '触发器名称不能包含特殊字符',
                    v => (!v || v.length<=128) || '触发器名称不能超过128个字'
                ],
                devRules: [
                    v => !!v || '请选择设备'
                ],
                dsRules: [
                    v => !!v || '请选择数据流'
                ],
                triggerModeRules: [
                    v => !!v || '请选择接受信息方式'
                ],
                urlRules: [
                    v => !!v || '请输入URL地址',
                    v => (!v || /^((https|http|ftp|rtsp|mms)?:\/\/)+[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/g.test(v)) || '请输入正确格式的url地址，如https://www.baidu.com'
                ],
                emailRules: [
                    v => !!v || '请输入邮箱'
                ],
                codeRules: [
                    v => !!v || '请输入验证码'
                ],
                criticalValueRules:[
                    v => (!v || /^-?\d+$/g.test(v)) || '数值应为整数'
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
        mounted(){
            this.getDevicelist();  
            this.ruleForm.name = this.data.name;
            this.ruleForm.triggerMode = this.data.triggerMode+'';
            this.ruleForm.criticalValue = this.data.criticalValue;
            this.ruleForm.dsName = this.data.datastream_name;
            this.ruleForm.deviceId = this.data.deviceId;
            if(this.data.deviceId){
                this.getDslist(this.data.deviceId);
            }
            this.ruleForm.datastreamId = this.data.datastreamId;
            if(this.data.triggerMode==1){
                this.ruleForm.url = this.data.modeValue;
            }else{
                this.ruleForm.email = this.data.modeValue;
            }
            if(this.data.triggerType=="<"){
                this.ruleForm.triggerTypeId = '2';
            }else{
                this.ruleForm.triggerTypeId = '1';
            }
        },
        computed:{
            ...mapState([
                'product','user'
            ]),
            title(){
                if(this.data.name.length>30)
                    return  this.data.name.substring(0,30)+'...';
                else
                    return  this.data.name;
            }
                
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
            //获取设备
            async getDevicelist(){
                let resp = await getDevicelist(this.product.id);
                if(resp.code==0){
                    this.devList = resp.data;
                }else if(resp.code=="error"){
                    return;
                }else{
                    this.$message({
                        message: "获取数据失败！",
                        type: 'error'
                    });
                }
            },
            //获取数据流
            async getDslist(id){
                let resp = await getDslist(id);
                if(resp.code==0){
                    this.dsList = resp.data;
                }else if(resp.code=="error"){
                    return;
                }else{
                    this.$message({
                        message: "获取数据失败！",
                        type: 'error'
                    });
                }
            },
            //设备id改变
            devChange(val){
                this.getDslist(val);
            },
            //数据流为空，先选择设备
            dsFocus(val){
                if(val && !this.ruleForm.deviceId){
                    this.open("请先选择设备！");
                    return false;
                }
            },
            async submit(modeValue){
                let resp = await modifyTrigger(this.data.id,this.ruleForm.name,this.ruleForm.triggerTypeId-0,
                    this.ruleForm.criticalValue,this.ruleForm.triggerMode-0,modeValue,this.ruleForm.deviceId,
                    this.ruleForm.datastreamId,this.product.id);
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
                    if(this.ruleForm.triggerMode==0 && !this.emailDis){
                        //邮箱
                        this.bind();
                    }else if(this.ruleForm.triggerMode==0 && this.emailDis){
                        this.submit(this.ruleForm.email);
                    }else{
                        this.submit(this.ruleForm.url);
                    }
                }else{
                    console.log('error submit!!');
                    return false;
                }
            },
            //发送验证码
            async verification(){
                if(this.ruleForm.email !='' && this.reg.test(this.ruleForm.email)){
                    let resp = await sendEmail(this.user.userId,this.ruleForm.email);
                    switch (resp.code){
                        case 0: this.open("验证码已发送");this.countDown();break;//成功
                        case 'error':break;
                        default: this.open(resp.msg);break;//失败"操作过于频繁，请稍后再试！"
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
                    let resp = await vertifyForTrigger(this.user.userId,this.ruleForm.code);
                    if(resp.code==0){
                        this.$message({
                            message: resp.msg,
                            type: 'success'
                        });
                        this.submit(this.ruleForm.email);
                    }else if(resp.code=="error"){
                        return;
                    }else{
                        this.open(resp.msg);
                        return false;
                    }
                }else{
                    this.open('请正确填写邮箱及验证码！');
                    return false;
                }
                
            },
            //重新绑定
            rebind(){
                this.emailDis = false;
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
        /* .fix .el-form-item__label {
            position: static !important;
        } */
        .btnFix .el-input__inner{
            border: none;
        }
        .el-form-item .flexBtw{
            border-bottom:1px solid #333333;
            width: 100%;
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
    