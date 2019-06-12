<template>
    <div>
        <cl-header headColor="#181818"></cl-header>
        <sub-header title="操作管理" subtitle="账户编辑" v-on:direct="navDirect"></sub-header>
        <div class="mainContent bg-fff">
            <v-form  ref="ruleForm" v-model="valid" data-app="true">
                <v-container fluid grid-list-md>
                    <v-layout row wrap>
                        <v-flex xs6>
                            <v-text-field label="账户名" hint="*必填" v-model="ruleForm.name" :rules="nameRules" required></v-text-field>
                        </v-flex>
                    </v-layout>
                    <!-- <v-layout row wrap>
                        <v-flex xs6>
                            <v-text-field type="password" label="密码" hint="*必填" v-model="ruleForm.password" :rules="pwdRules" required></v-text-field>
                        </v-flex>
                    </v-layout>
                    <v-layout row wrap>
                        <v-flex xs6>
                            <v-text-field type="password" label="二次密码" hint="*必填" v-model="ruleForm.checkPass" :rules="checkPassRules" required></v-text-field>
                        </v-flex>
                    </v-layout> -->
                    <v-layout row wrap>
                        <v-flex xs6>
                            <v-text-field label="手机" hint="*必填" v-model="ruleForm.phone" :rules="phoneRules" required></v-text-field>
                        </v-flex>
                    </v-layout>
                    <v-layout row wrap>
                        <v-flex xs6>
                            <v-text-field label="邮箱" v-model="ruleForm.email" :rules="emailRules"></v-text-field>
                        </v-flex>
                    </v-layout>
                    <el-button @click="goBack">返回</el-button>
                    <el-button type="primary" @click="submitForm()">确认</el-button>
                </v-container>
            </v-form>
        </div>
    </div>
</template>

<script>
    import headTop from 'components/header/head'
    import subHead from 'components/subHeader/subHeader'
    import {modifyUser} from 'service/getData'
    // import md5 from 'js-md5';

    export default {
        name: 'editUser',
        data () {
            return {
                valid: false,
                userData:[],
                ruleForm: {
                    id:'',
                    name: '',
                    // password:'88888888',
                    phone: '',
                    email: '',
                    // checkPass:'',
                },
                // realPwd:'',
                nameFlag:false,
                nameRules: [
                    v => !!v || '请输入账户名',
                    v => !this.nameFlag || '账户名已存在',
                    v => (!v || (v.length>=6 && v.length<=16)) || '账户名的长度为6-16个字符',
                    v => (!v || !/[`~!@#$^&*()=|{}':;',\\\[\]\.<>\/?~！@#￥……&*（）——|{}【】'；：""'。，、？\s]/g.test(v)) || '账户名不能包含特殊字符'
                ],
                phoneRules: [
                    v => !!v || '请输入手机',
                    v => (/^1(3|4|5|7|8)\d{9}$/.test(v)) || "手机号码格式有误，请重填"
                ],
                // pwdRules: [
                //     v => !!v || '请输入密码',
                //     v => (!v || (v.length>=6 && v.length<=16)) || '密码的长度为6-16个字符',
                //     v => (!v || !/[`~!@#$^&*()=|{}':;',\\\[\]\.<>\/?~！@#￥……&*（）——|{}【】'；：""'。，、？\s]/g.test(v)) || '密码不能包含特殊字符',
                // ],
                // checkPassRules: [
                //     v => !!v || '请再次输入密码',
                //     v => (!v || (v.length>=6 && v.length<=16)) || '密码的长度为6-16个字符',
                //     v => (!v || !/[`~!@#$^&*()=|{}':;',\\\[\]\.<>\/?~！@#￥……&*（）——|{}【】'；：""'。，、？\s]/g.test(v)) || '密码不能包含特殊字符',
                //     v => v==this.ruleForm.password || '两次输入密码不一致'
                // ],
                emailRules:[
                    v => (!v || /^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z\d]{2,4}$/.test(v))  || "邮箱格式有误，请重填"
                ]
                
            }
        },
        mounted(){
            //解密
            var x = new Buffer(decodeURIComponent(this.$route.params.userData), 'base64')
            var y = x.toString('utf8');
            let userData = JSON.parse(y);
            this.userData = userData;
            this.ruleForm.id = userData.id;
            this.ruleForm.name = userData.name;
            // this.realPwd = userData.pwd;
            this.ruleForm.phone = userData.phone;
            this.ruleForm.email = userData.email;
        },
        components:{
            'cl-header':headTop,
            'sub-header':subHead,
        },
        methods: {
            submitForm() {
                if (this.$refs.ruleForm.validate()) {
                    this.submit();
                }else{
                    console.log('error submit!!');
                    // return false;
                }
            },
            async vertifyName(){
                let resp = await vertifyName(this.ruleForm.name);
                if(resp.code==2){
                    this.nameFlag = false;
                    this.submitForm();
                }else if(resp.code=="error"){
                    return;
                }else{
                    this.nameFlag = true;
                    this.submitForm();
                } 
            },
            async submit(){
                // let temp;
                // if(this.userData.pwd!=this.ruleForm.password){
                //     temp = md5(this.ruleForm.password);
                // }else{
                //     temp=null;
                // }
                let resp = await modifyUser(this.ruleForm);
                if(resp.code==0){
                    this.$message({
                        type: 'success',
                        message: '修改成功!'
                    });
                    this.$router.push("/index")
                }else if(resp.code=="error"){
                    return;
                }else{
                    this.$message({
                        type: 'error',
                        message: '修改失败!'+resp.msg
                    });
                }
            },
            navDirect(){
                this.$router.push('/index');
            },
            //返回事件
            goBack () {
                window.history.length > 1
                    ? this.$router.go(-1)
                    : this.$router.push('/')
                }
        }

    }
</script>

<style>
    .editUser .el-form{
        width: 400px;
        margin: 50px auto;
    }
</style>
