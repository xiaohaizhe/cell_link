<template>
    <div>
        <cl-header headColor="#181818"></cl-header>
        <sub-header title="我的产品" subtitle="添加产品" v-on:direct="navDirect"></sub-header>
        <div class="mainContent bg-fff">
            <v-form  ref="ruleForm" v-model="valid">
                <v-container fluid grid-list-md>
                    <v-layout row wrap>
                        <v-flex xs6>
                            <v-text-field label="产品名称" hint="*必填" v-model="ruleForm.name" :rules="nameRules" required></v-text-field>
                        </v-flex>
                    </v-layout>
                    <v-layout row wrap >
                        <v-flex xs3>
                            <v-select :items="provinces" label="使用地区（省）" hint="*必填"   v-model="ruleForm.province" item-text="name" :rules="provinceRules" item-value="code"  @change="provinceChanged"></v-select>
                        </v-flex>
                        <v-flex xs3>
                            <v-select :items="cities" label="使用地区（市）"  hint="*必填"  v-model="ruleForm.city" item-text="name"  item-value="code" :rules="cityRules"></v-select>
                        </v-flex>
                    </v-layout>
                    <v-layout row wrap>
                        <v-flex xs12>
                            <v-textarea label="产品描述"  auto-grow rows="1" hint="*必填" v-model="ruleForm.description" :rules="descriptionRules" required maxlength="100" :counter="100"></v-textarea>
                        </v-flex>
                    </v-layout>
                    <v-layout row wrap>
                        <v-flex xs6>
                            <v-select :items="protocols" label="设备接入方式（一旦选择不能更改）"  hint="*必填" :rules="protoRules" v-model="ruleForm.selectedproto" item-text="name"  item-value="id"></v-select>
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
    // import provinceCity from 'components/provinceCity/provinceCity'
    import { getProtocols ,addProduct} from 'service/getData'
    import {mapState} from 'vuex'
    import provinceCity from 'static/provinceCity.json'

    export default {
        name: 'addProduct',
        data () {
            return {
                valid: false,
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
                nameRules: [
                    v => !!v || '请输入产品名称'
                ],
                provinceRules: [
                    v => !!v || '请选择省'
                ],
                descriptionRules: [
                    v => !!v || '请输入产品描述'
                ],
                protoRules: [
                    v => !!v || '请选择设备接入方式'
                ],
                cityRules: [
                    v => !!v || '请选择市'
                ]
            }
        },
        created() {
            this.provinces = provinceCity.provinces
        },
        computed:{
            ...mapState([
                'user'
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
            submitForm() {
                if (this.$refs.ruleForm.validate()) {
                    this.getCoordinate(this.ruleForm.city);
                    this.submit();
                }else{
                    console.log('error submit!!');
                    return false;
                }
            },
            //添加产品
            async submit(){
                let resp = await addProduct(this.ruleForm.name,this.ruleForm.description,
                    this.ruleForm.selectedproto,this.coordinate[0],this.coordinate[1],this.user.userId,this.ruleForm.city);
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
            navDirect(){
                this.$router.push('/home')
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
</style>
