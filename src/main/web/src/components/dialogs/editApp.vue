<template>
    <el-dialog
        :title="`${title}-编辑`"
        :visible.sync="isVisible" width="70%">
        <div style="padding:0 5%" class="flexAround">
            <div class="wid50">
                <v-form  ref="ruleForm" v-model="valid" data-app="true">
                    <v-container fluid grid-list-md>
                        <v-layout row wrap>
                            <v-flex xs6>
                                <v-text-field label="应用名称" hint="*必填" v-model="ruleForm.name" :rules="nameRules" required></v-text-field>
                            </v-flex>
                        </v-layout>
                         <div>
                            <div class="chartApp" v-for="(chart, i) in applicationChartList" :key="i">  
                                <el-button type="danger" icon="el-icon-close" circle @click="deleteChart(i)" class="del"></el-button> 
                                <v-layout row wrap >
                                    <v-flex xs12>
                                        <v-select :rules="chartRules" :items="chartTypes" label="图表类型" v-model="chart.chartId" item-text="name" item-value="id"  @change="chartChange('testChart'+i,chart.chartId)"></v-select>
                                    </v-flex>
                                </v-layout>
                                <div v-for="(v, index) in chart.applicationChartDatastreamList" :key="index" class="cl-flex">
                                    <v-layout row wrap >
                                        <v-flex xs6>
                                            <v-select  :rules="devRules" :items="devList" label="设备" v-model="v.device_id" item-text="name" item-value="id"  @change="devChange($event,i,index)"></v-select>
                                        </v-flex>
                                        <v-flex xs6>
                                            <v-select  :rules="dsRules" :items="dsList[i + '' +index]" label="数据流" v-model="v.dd_id" item-text="dm_name" item-value="id"></v-select>
                                        </v-flex>
                                    </v-layout>
                                </div>
                            </div>
                         </div>
                         <el-button type="primary" @click="addChart" style="margin-bottom:22px;">添加图表</el-button>
                    </v-container>
                </v-form>
            </div>
            <!-- <div class="wid50">
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
                                    <el-select v-model="v.device_id" placeholder="请选择设备" style="margin-right:1.43rem;"  @change="devChange($event,i,index)">
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
                                </el-form-item> -->
                                <!-- <el-button type="danger" icon="el-icon-delete" circle @click="deleteDevDs(chart,i,index)" style="padding: 5px;" v-if="index<chart.applicationChartDatastreamList.length-1"></el-button>
                                <el-button type="primary" icon="el-icon-plus" circle @click="addDevDs(i)" style="padding: 5px;" v-if="index==chart.applicationChartDatastreamList.length-1"></el-button> -->
                            <!-- </div>
                    </div>
                </el-form>
            </div> -->
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
                valid: false,
                appId:"0",
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
                    name:''
                },
                nameRules: [
                    v => !!v || '请输入应用名称',
                    v => (!v || !/[`~!@#$^&*()=|{}':;',\\\[\]\.<>\/?~！@#￥……&*（）——|{}【】'；：""'。，、？\s]/g.test(v)) || '应用名称不能包含特殊字符',
                    v => (!v || v.length<=128) || '应用名称不能超过128个字'
                ],
                chartRules:[
                    v => !!v || '请选择图表类型'
                ],
                devRules:[
                    v => !!v || '请选择设备'
                ],
                dsRules:[
                    v => !!v || '请选择数据流'
                ],
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
            ]),
            title(){
                if(this.data.name.length>30)
                    return  this.data.name.substring(0,30)+'...';
                else
                    return  this.data.name;
            }
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
                }else if(resp.code=="error"){
                    return;
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
                }else if(resp.code=="error"){
                    return;
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
            async getDslist(id,i,index){
                let resp = await getDslist(id);//id
                if(resp.code==0){
                    if(resp.data.length>0){
                        let temp = {};
                        Object.assign(temp,this.dsList);
                        temp[i+''+index] = resp.data;
                        this.dsList=temp;
                    }else{
                        this.$alert('该设备下没有数据流，请重新选择！', '提示', {
                            confirmButtonText: '确定',
                            callback: action => {
                                this.applicationChartList[i].applicationChartDatastreamList[index].devId = '';
                                this.applicationChartList[i].applicationChartDatastreamList[index].dd_id = '';
                                this.dsList[i + '' +index]=[]
                            }
                        });
                    }
                }else if(resp.code=="error"){
                    return;
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
            }
        }
    }
    </script>