<template>
    <div>
        <cl-header headColor="#181818"></cl-header>
        <sub-header title="我的产品" :subtitle="`${ruleForm.name}-编辑`" v-on:direct="navDirect"></sub-header>
        <div class="mainContent bg-fff">
            <v-form  ref="ruleForm" v-model="valid">
                <v-container fluid grid-list-md>
                    <v-layout row wrap>
                        <v-flex xs6>
                            <v-text-field label="产品名称"  hint="*必填" v-model="ruleForm.name" :rules="nameRules" required></v-text-field>
                        </v-flex>
                    </v-layout>
                    <v-layout row wrap >
                        <v-flex xs3>
                            <v-select :items="provinces" label="使用地区（省）" hint="*必填" v-model="ruleForm.province" item-text="name" :rules="provinceRules" item-value="code"  @change="provinceChanged"></v-select>
                        </v-flex>
                        <v-flex xs3>
                            <v-select :items="cities" label="使用地区（市）" hint="*必填" v-model="ruleForm.city" item-text="name"  item-value="code" :rules="cityRules"></v-select>
                        </v-flex>
                    </v-layout>
                    <v-layout row wrap>
                        <v-flex xs12>
                            <v-textarea label="产品描述"  auto-grow rows="1" hint="*必填" v-model="ruleForm.description" :rules="descriptionRules" required maxlength="100" :counter="100"></v-textarea>
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
    import {getDetail,modifyProduct} from 'service/getData'
    import {mapState} from 'vuex'
    import provinceCity from 'static/provinceCity.json'

    export default {
        name: 'editProduct',
        data () {
            return {
                valid:false,
                nextUrl:'',
                loadingCity: false,
                provinces: [],
                cities: [],
                coordinate:[],
                prodId: 0,
                ruleForm: {
                        name: '',
                        description:"",
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
                cityRules: [
                    v => !!v || '请选择市'
                ]
            }
        },
        created() {
            //解密
            var x = new Buffer(decodeURIComponent(this.$route.params.productId), 'base64')
            var y = x.toString('utf8');
            this.prodId = JSON.parse(y);
            this.provinces = provinceCity.provinces;
        },
        computed:{
            ...mapState([
                'user'
            ]),
        },
        components:{
            'cl-header':headTop,
            'sub-header':subHead
        },
        mounted(){
            this.getDetail();
        },
        methods: {
            async getDetail(){
                let resp = await getDetail(this.prodId);
                if(resp.code==0){
                    this.ruleForm.name = resp.data.product.name;
                    this.ruleForm.description = resp.data.product.description;
                    this.ruleForm.province = parseInt(resp.data.product.cityCode/100)*100+"";
                    this.provinceChanged(this.ruleForm.province);
                    this.ruleForm.city = resp.data.product.cityCode+"";
                }else if(resp.code=="error"){
                    return;
                }else{
                    this.$message({
                        message: "获取数据失败",
                        type: 'error'
                    });
                }
            },
            async modify(){
                let resp = await modifyProduct(this.ruleForm.name,this.ruleForm.description,
                this.prodId,this.coordinate[0],this.coordinate[1],this.user.userId,this.ruleForm.city);
                if(resp.code==0){
                    this.$message({
                        type: 'success',
                        message: '编辑成功!'
                    });
                    this.goBack();
                }else if(resp.code=="error"){
                    return;
                }else{
                    this.$message({
                        type: 'error',
                        message: '编辑失败!'+resp.msg
                    });
                }
            },
            submitForm() {
                if (this.$refs.ruleForm.validate()) {
                    this.getCoordinate(this.ruleForm.city);
                    this.modify();
                }else{
                    console.log('error submit!!');
                    return false;
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
                if(this.nextUrl){
                    //加密
                    let b = new Buffer(this.prodId);
                    let s = b.toString('base64');
                    let data = encodeURIComponent(s);
                    this.$router.push('/myProduct/'+data+'/overview')
                }else{
                    window.history.length > 1
                    ? this.$router.go(-1)
                    : this.$router.push('/')
                }   
                
            },
            navDirect(){
                this.$router.push('/home')
            },
        },
        
        beforeRouteEnter(to, from, next){
            if(from.matched.length>0 && from.matched[0].name!="home"){
                next(vm => {
                //因为当钩子执行前，组件实例还没被创建
                // vm 就是当前组件的实例相当于上面的 this，所以在 next 方法里你就可以把 vm 当 this 来用了。
                console.log(vm);//当前组件的实例
                vm.nextUrl  = from.matched[1].name;
                });
            }else{
                next();
            }
            
        }

    }
</script>

<style>
    .editCont{
        padding: 50px 130px; 
    }
    
    .editCont .el-button{
        min-width: 100px;
    }
    .editCont .el-form-item__content{
        display: flex;
        align-items: flex-end;
    }
    .provinceCity .el-form-item:first-child{
        margin-right: 2.14rem;
    }
    .editCont .el-textarea{
        margin-top: 8px;
    }
</style>
