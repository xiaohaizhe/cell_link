<template>
    <el-dialog
        title="新建应用"
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
                                            <v-select :rules="devRules" :items="devList" label="设备" v-model="v.devId" item-text="name" item-value="id"  @change="devChange($event,i,index)"></v-select>
                                        </v-flex>
                                        <v-flex xs6>
                                            <v-select :rules="dsRules" :items="dsList[i + '' +index]" label="数据流" v-model="v.dd_id" item-text="dm_name" item-value="id"></v-select>
                                        </v-flex>
                                    </v-layout>
                                </div>
                            </div>
                         </div>
                         <el-button type="primary" @click="addChart" style="margin-bottom:22px;">添加图表</el-button>
                    </v-container>
                </v-form>
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
            <el-button type="primary" @click="submitForm()">添 加</el-button>
            <el-button @click="isVisible = false">取 消</el-button>
        </span>
    </el-dialog>
</template>

<script>
    import {mapState} from 'vuex'
    import lineChart from 'components/charts/lineChart'
    import barChart from 'components/charts/barChart'
    import {getDevicelist,getDslist,getChartTypes,addChartApp} from 'service/getData'
  
  export default {
        name: 'addApp',
        data () {
            return{
                valid: false,
                isVisible:this.dialogVisible,
                devList:[],
                dsList:{},
                chartTypes:[],
                applicationChartList: [{
                        chartId:'',
                        applicationChartDatastreamList:[{
                            devId:'',
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
                previews:[]
            }
        },
        components:{
            'line-chart':lineChart,
            'bar-chart':barChart
        },
        props:{
            dialogVisible:{
                type:Boolean,
                default:false
            }
        },
        computed:{
            ...mapState([
                'product','lineData','barData'
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
                    }else if(resp.code=="error"){
                        return;
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
                        devId:'',
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
                    devId: '',
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
                let resp = await addChartApp(this.product.id,this.ruleForm.name,this.applicationChartList);//this.product.id
                if(resp.code==0){
                    this.$message({
                        message: "添加成功！",
                        type: 'success'
                    });
                    this.isVisible = false;
                }else if(resp.code=="error"){
                    return;
                }else{
                    this.$message({
                        message: "添加失败！"+resp.msg,
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
    