<template>
    <div>
        <cl-header headColor="#181818"></cl-header>
        <sub-header title="我的产品" subtitle="添加产品"></sub-header>
        <div class="mainContent addCont add noBorder">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm">
                <el-form-item prop="name" label=" ">
                    <el-input placeholder="产品名称" v-model="ruleForm.name" class="wid50"></el-input>
                </el-form-item>
                <!-- <div class="wid50">
                    <p style="color:#c6cad2;padding-left: 15px;">产品ID（系统分配）</p>
                    <el-input placeholder="产品ID" v-model="id" :disabled="true"></el-input>
                </div> -->
                <el-form-item required class="provinceCity wid50">
                        <el-form-item prop="province" label=" ">
                            <el-select v-model="ruleForm.province" placeholder="使用地区（省）" @change="provinceChanged">
                                <el-option
                                v-for="item in provinces"
                                :key="item.code"
                                :label="item.name"
                                :value="item.code">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item prop="city" label=" ">
                            <el-select v-model="ruleForm.city"
                                :loading="loadingCity"
                                placeholder="使用地区（市）">
                                <el-option
                                v-for="item in cities"
                                :key="item.code"
                                :label="item.name"
                                :value="item.code">
                                </el-option>
                            </el-select>
                        </el-form-item>
                </el-form-item>
                <!-- <el-form-item prop="coordinate" label=" ">
                    <province-city class="wid50 flex" @selectChange="getCoordinate"></province-city>
                </el-form-item> -->
                <el-form-item prop="description" label=" ">
                    <el-input type="textarea" :autosize="{ minRows: 1, maxRows: 5}" placeholder="产品描述（0/100）" v-model="ruleForm.description" maxlength="100" ></el-input>
                </el-form-item>
                <el-form-item prop="selectedproto" label=" ">
                    <el-select v-model="ruleForm.selectedproto" placeholder="设备接入方式（一旦选择不能更改）" class="wid50">
                        <el-option
                            v-for="item in protocols"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button @click="goBack">返回</el-button>
                    <el-button type="primary" @click="submitForm('ruleForm')">确认</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
    import headTop from 'components/header/head'
    import subHead from 'components/subHeader/subHeader'
    // import provinceCity from 'components/provinceCity/provinceCity'
    import { getProtocols ,addProduct} from 'service/getData'
    import {mapState} from 'vuex'
    import provinceCity from 'static/provinceCity.json'

    export default {
        name: 'addProduct',
        data () {
            return {
                loadingCity: false,
                provinces: [],
                cities: [],
                protocols:[],
                coordinate:[],
                ruleForm: {
                        name: '',
                        description:"",
                        selectedproto:null,
                        province: '',
                        city: '',
                    },
                rules: {
                    name: [
                        { required: true, message: '请输入产品名称', trigger: 'blur' }
                    ],
                    description: [
                        { required: true, message: '请输入产品描述', trigger: 'blur' }
                    ],
                    selectedproto:[
                        { required: true, message: '请选择设备接入方式', trigger: 'change' }
                    ],
                    province:[
                        { required: true, message: '请选择省', trigger: 'change' }
                    ],
                    city:[
                        { required: true, message: '请选择市', trigger: 'change' }
                    ]
                }
            }
        },
        created() {
            this.provinces = provinceCity.provinces
        },
        computed:{
            ...mapState([
                'userId'
            ]),
        },
        components:{
            'cl-header':headTop,
            'sub-header':subHead,
            // 'province-city':provinceCity
        },
        mounted(){
            this.getProtocols();
        },
        methods: {
            //获取协议
            async getProtocols(){
                let resp = await getProtocols();
                if(resp.code==0){
                    this.protocols = resp.data;
                }else if(resp.code=="error"){
                    return;
                }else{
                    this.$message({
                        message: "获取数据失败",
                        type: 'error'
                    });
                }
                
            },
            //获取经纬度
            getCoordinate(data){
                for (var item of this.cities) {
                    if (item.code === data) {
                        this.coordinate = item.coordinate;
                        break
                    } else {
                        continue
                    }
                }
            },
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                if (valid) {
                    this.getCoordinate(this.ruleForm.city);
                    this.submit();
                } else {
                    console.log('error submit!!');
                    return false;
                }
                });
            },
            //添加产品
            async submit(){
                let resp = await addProduct(this.ruleForm.name,this.ruleForm.description,
                    this.ruleForm.selectedproto,this.coordinate[0],this.coordinate[1],this.userId,this.ruleForm.city);
                if(resp.code==0){
                    this.$message({
                        type: 'success',
                        message: '添加成功!'
                    });
                    this.$router.push("/home")
                }else if(resp.code=="error"){
                    return;
                }else{
                    this.$message({
                        type: 'error',
                        message: '添加失败!'
                    });
                }
            },
            //变换省事件
            provinceChanged(value) {
                if (value !== '') {
                this.loadingCity = true
                for (var item of this.provinces) {
                    if (item.code === value) {
                        this.cities = item.cities
                        this.ruleForm.city = ''
                        this.loadingCity = false
                    break
                    } else {
                        continue
                    }
                }
                } else {
                    this.cities = []
                    this.ruleForm.city = ''
                }
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
    .addCont{
        padding: 50px 130px; 
    }
    
    .addCont .el-form>.el-form-item{
        margin-bottom: 40px;
    }
    .addCont .el-input.is-disabled .el-input__inner{
        background-color: #fff;
    }
    .addCont .el-button{
        min-width: 100px;
    }
    .addCont .el-form-item__content{
        display: flex;
    }
    .provinceCity .el-form-item:first-child{
        margin-right: 30px;
    }
</style>
