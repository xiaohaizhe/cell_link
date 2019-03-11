<template>
    <el-dialog
        title="新建应用"
        :visible.sync="isVisible" width="40%">
        <div style="padding:0 10%">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" class="noBorder add" >
                <el-form-item prop="name" label=" ">
                    <el-input placeholder="应用名称" v-model="ruleForm.name"></el-input>
                </el-form-item>
                <el-button type="primary" @click="isVisible = false">添加图表</el-button>
                <div>
                    <el-form-item>
                         <el-select v-model="chartId" placeholder="请选择图表类型">
                            <el-option
                            v-for="item in chartTypes"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                            </el-option>
                        </el-select>
                         <el-select v-model="devId" placeholder="请选择设备">
                            <el-option
                            v-for="item in devList"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                            </el-option>
                        </el-select>
                         <el-select v-model="dd_id" placeholder="请选择数据流">
                            <el-option
                            v-for="item in dsList"
                            :key="item.id"
                            :label="item.dm_name"
                            :value="item.id">
                            </el-option>
                        </el-select>
                        <el-button @click="addChart">+</el-button>
                    </el-form-item>
                </div>
            </el-form>
        </div>
        <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitForm('ruleForm')">确 定</el-button>
            <el-button @click="isVisible = false">返 回</el-button>
        </span>
    </el-dialog>
</template>

<script>
    import {mapState} from 'vuex'
    import {getDevicelist,getDslist,getChartTypes} from 'service/getData'
  
  export default {
        name: 'addApp',
        data () {
            return{
                isVisible:this.dialogVisible,
                devList:[],
                dsList:[],
                chartTypes:[],
                chartId: 0,
                    devId:0,
                    dd_id:0,
                ruleForm: {
                    product_id:0,
                    name:'',
                    
                },
                rules: {
                    name: [
                        { required: true, message: '请输入数据流名称', trigger: 'blur' }
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
        mounted () {
            this.getChartTypes();
            this.getDevicelist();
        },
        methods:{
            async getChartTypes(){
                let resp = await getChartTypes();
                if(resp.code==0){
                    this.chartTypes = resp.data;
                }
            },
            async getDevicelist(){
                let resp = await getDevicelist(10);//this.product.id
                if(resp.code==0){
                    this.devList = resp.data;
                    this.getDslist(resp.data.id);
                }
            },
            async getDslist(id){
                let resp = await getDslist(1547795900304);//id
                if(resp.code==0){
                    this.dsList = resp.data;
                }
            },
            addChart(){
                this.dynamicValidateForm.domains.push({
                    value: '',
                    key: Date.now()
                });
            },
            async submit(){
                this.ruleForm.product_id = this.product.id;
                let resp = await addDs(this.ruleForm);
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
                        this.submit();
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
        }
    }
    </script>

    <style>
    </style>
    