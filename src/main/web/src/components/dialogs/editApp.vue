<template>
    <el-dialog
        :title="`${data.name}-编辑`"
        :visible.sync="isVisible" width="70%">
        <div style="padding:0 5%" class="flexAround">
            <div class="wid50">
                <el-form :model="ruleForm" :rules="rules" ref="ruleForm" class="noBorder edit" >
                    <el-form-item prop="name" label="应用名称">
                        <el-input placeholder="应用名称" v-model="ruleForm.name"></el-input>
                    </el-form-item>
                    <el-button type="primary" @click="addChart" style="margin-bottom:22px;">添加图表</el-button>
                    <div class="chartApp" v-for="(chart, i) in applicationChartList" :key="i">
                        <el-button type="danger" icon="el-icon-close" circle @click="deleteChart(i)" class="del"></el-button>
                        <el-form-item label="选择图表类型">
                            <el-select v-model="chart.chartId" placeholder="请选择图表类型" style="width:100%" @change="chartChange('testChart'+i,chart.chartId)">
                                <el-option
                                v-for="item in chartTypes"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                                </el-option>
                            </el-select> 
                        </el-form-item >
                            <div v-for="(v, index) in chart.applicationChartDatastreamList" :key="index" class="flex">
                                <el-form-item label="选择设备">
                                    <el-select v-model="v.device_id" placeholder="请选择设备" style="margin-right:20px;"  @change="devChange($event,i,index)">
                                        <el-option
                                        v-for="item in devList"
                                        :key="item.id"
                                        :label="item.name"
                                        :value="item.id">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                                <el-form-item label="选择数据流">
                                    <el-select v-model="v.dd_id" placeholder="请选择数据流" @visible-change="dsFocus($event,v.device_id)">
                                        <el-option
                                        v-for="item in dsList[i + '' +index]"
                                        :key="item.id"
                                        :label="item.dm_name"
                                        :value="item.id">
                                        </el-option>
                                    </el-select>                                                                                    
                                </el-form-item>
                                <el-button type="danger" icon="el-icon-delete" circle @click="deleteDevDs(chart,i,index)" style="padding: 5px;" v-if="index<chart.applicationChartDatastreamList.length-1"></el-button>
                                <el-button type="primary" icon="el-icon-plus" circle @click="addDevDs(i)" style="padding: 5px;" v-if="index==chart.applicationChartDatastreamList.length-1"></el-button>
                            </div>
                    </div>
                </el-form>
            </div>
            <div class="wid50 preview">
                <p class="font-16">图表预览区</p>
                <div v-for="item in previews" :key="item.chartId">
                    <bar-chart :chartId="item.chartId" :data="barData" v-if="item.chartType==2" class="chart"></bar-chart>
                    <line-chart :chartId="item.chartId" :data="lineData" v-if="item.chartType==1" class="chart"></line-chart>
                </div>
            </div>
        </div>
        <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitForm('ruleForm')">确 定</el-button>
            <el-button @click="isVisible = false">取 消</el-button>
        </span>
    </el-dialog>
</template>

<script>
    import {mapState} from 'vuex'
    import lineChart from 'components/charts/lineChart'
    import barChart from 'components/charts/barChart'
    import {getDevicelist,getDslist,getChartTypes,getAppDetail,modifyChartApp} from 'service/getData'
  
  export default {
        name: 'editApp',
        data () {
            return{
                appId:0,
                isVisible:this.dialogVisible,
                devList:[],
                dsList:{},
                chartTypes:[],
                applicationChartList: [{
                    chartId:'',
                    applicationChartDatastreamList:[{
                        device_id:'',
                        dd_id: ''
                    }],
                }],
                ruleForm: {
                    product_id:0,
                    name:''
                },
                rules: {
                    name: [
                        { required: true, message: '请输入数据流名称', trigger: 'blur' }
                    ]
                },
                previews:[],
                
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
            ...mapState([
                'product','lineData','barData'
            ])
        },
        components:{
            'line-chart':lineChart,
            'bar-chart':barChart
        },
        watch:{
            dialogVisible(val){
                this.isVisible = this.dialogVisible
            },
            isVisible(val){
                this.$emit('getEditDialogVisible', val)
            }
        },
        mounted () {
            this.getChartTypes();
            this.getDevicelist();
            this.getAppDetail();
            this.ruleForm.name = this.data.name;
        },
        methods:{
            initDs(){
                this.applicationChartList.forEach( ( item, i ) => {
                    this.previews.push({chartId:'testChart'+i,chartType:item.chartId});
                    item.applicationChartDatastreamList.forEach( ( v, index ) => {
                        this.getDslist(v.device_id,i,index);
                    } );
                } );
            },
            async getAppDetail(){
                let resp = await getAppDetail(this.data.id);
                if(resp.code==0){
                    this.applicationChartList = resp.data.applicationChartList;
                    this.appId = resp.data.id;
                    this.initDs();
                }else{
                    this.$message({
                        message: "获取数据失败！",
                        type: 'error'
                    });
                }
            },
            //改变选择图表
            chartChange(chartId,chartType){
                let flag = false;
                let temp = this.previews;
                for(let i=0;i<temp.length;i++){
                    if(temp[i].chartId==chartId){
                        temp[i].chartType = chartType;
                        flag = true;
                        return false;
                    }
                }
                if(!flag){
                    this.previews.push({chartId:chartId,chartType:chartType});
                }
                
            },
            //获取图表类型
            async getChartTypes(){
                let resp = await getChartTypes();
                if(resp.code==0){
                    this.chartTypes = resp.data;
                }else{
                    this.$message({
                        message: "获取数据失败！",
                        type: 'error'
                    });
                }
            },
            //获取设备
            async getDevicelist(){
                let resp = await getDevicelist(this.product.id);//this.product.id
                if(resp.code==0){
                    this.devList = resp.data;
                }else{
                    this.$message({
                        message: "获取数据失败！",
                        type: 'error'
                    });
                }
            },
            //获取数据流
            async getDslist(id,i,index){
                let resp = await getDslist(id);//id
                if(resp.code==0){
                    if(resp.data.length>0){
                        let temp = {};
                        Object.assign(temp,this.dsList);
                        temp[i+''+index] = resp.data;
                        this.dsList=temp;
                    }else{
                        this.applicationChartList[i].applicationChartDatastreamList[index].devId = '';
                        this.applicationChartList[i].applicationChartDatastreamList[index].ddId = '';
                        this.$alert('该设备下没有数据流，请重新选择！', '提示', {
                            confirmButtonText: '确定',
                            callback: action => {
                            }
                        });
                    }
                }else{
                    this.$message({
                        message: "获取数据失败",
                        type: 'error'
                    });
                }
            },
            //设备id改变
            devChange(val,i,index){
                this.getDslist(val,i,index);
            },
            //数据流为空，先选择设备
            dsFocus(val,devId){
                if(val && !devId){
                    this.$alert('请先选择设备！', '提示', {
                        confirmButtonText: '确定',
                        callback: action => {
                        }
                    });
                    return false;
                }
            },
            //添加图表
            addChart(){
                this.applicationChartList.push({
                    chartId:'',
                    applicationChartDatastreamList:[{
                        device_id:'',
                        dd_id: ''
                    }],
                    key: Date.now()
                });
            },
            //删除图表
            deleteChart(index){
                if(this.applicationChartList.length<=1){
                    this.$alert('图表不能少于一项！', '提示', {
                        confirmButtonText: '确定',
                        callback: action => {
                        }
                    });
                }else if(index !== -1){
                    this.applicationChartList.splice(index, 1)
                }
            },
            //添加设备数据流
            addDevDs(i){
                this.applicationChartList[i].applicationChartDatastreamList.push({
                    device_id: '',
                    dd_id:'',
                    key: Date.now()
                });
            },
            //删除设备数据流
            deleteDevDs(v,i,index){
                if(index !== -1){
                    v.applicationChartDatastreamList.splice(index, 1)
                }
            },
            async submit(){
                let resp = await modifyChartApp(this.appId,this.ruleForm.name,this.applicationChartList);
                if(resp.code==0){
                    this.$message({
                        message: "修改成功！",
                        type: 'success'
                    });
                    this.isVisible = false;
                }else{
                    this.$message({
                        message: "修改失败！",
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
        .chartApp{
            border: 1px solid #cccccc;
            background-color: #f7f7f7;
            padding: 15px;
            position: relative;
            margin-bottom: 20px;
        }
        .chartApp input{
            background-color: #f7f7f7;
        }
        .chartApp .del{
            padding: 2px;
            position: absolute;
            top: -10px;
            right: -10px;
        }
        .chartApp .el-form-item{
            margin-bottom: 10px;
        }
        .preview{
            margin-left: 40px;
            border-left: 1px solid;
            padding-left: 5%;
        }
    </style>
    