<template>
    <div>
        <cl-header headColor="#181818"></cl-header>
        <sub-header title="智能分析" subtitle="线性回归"  detail="新建"  v-on:direct="navDirect"></sub-header>
        <div class="mainContent bg-fff intellAna" style="padding:2% 5%">
            <el-form  :model="ruleForm" ref="ruleForm" label-width="80px" label-position="top" >
                <p class="font-16">输入值</p>
                <div v-for="(item,index) in ruleForm.analysisDatastreams" :key="index" class="cl-flex">
                    <span style="flex-shrink:0">参数{{index+1}}：</span>
                    <el-form-item label="设备" :prop="'analysisDatastreams.' + index + '.devId'"
                                class="mgR-20"
                                :rules="{ required: true, message: '请选择设备', trigger: 'change' }">
                        <el-select v-model="item.devId" placeholder="请选择设备" style="width:150px;" @change="devChange($event,index)">
                            <el-option
                            v-for="item in devList"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="数据流" class="mgR-20" 
                                :prop="'analysisDatastreams.' + index + '.ddId'"
                                :rules="{ required: true, message: '请选择数据流', trigger: 'change' }">
                        <el-select v-model="item.ddId" placeholder="请选择数据流" style="width:150px;">
                            <el-option
                            v-for="item in dsList[index]"
                            :key="item.id"
                            :label="item.dm_name"
                            :value="item.id">
                            </el-option>
                        </el-select>  
                    </el-form-item>
                    <el-form-item label="时间段" class="mgR-20" 
                                :prop="'analysisDatastreams.' + index + '.time'"
                                :rules="{ required: true, message: '请选择时间段', trigger: 'change' }">
                        <el-date-picker v-model="item.time" type="datetimerange" range-separator="至"
                            start-placeholder="开始日期" style="border: none;border-bottom: 1px solid;border-radius: 0;"
                            end-placeholder="结束日期" @change='dateChange($event,index)'> 
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item label="间隔（秒）" class="mgR-20" 
                                :prop="'analysisDatastreams.' + index + '.frequency'"
                                :rules="{ required: true, message: '频率的范围0.5-5' , type: 'number',  trigger: 'blur'}">
                        <el-input-number v-model="item.frequency" controls-position="right" :min="0.5" :max="5" :step="0.5" style="width:90px"></el-input-number>
                    </el-form-item>
                    <el-button type="danger" icon="el-icon-delete" circle @click="deleteParam(index)" style="padding: 5px;" v-if="index<ruleForm.analysisDatastreams.length-1"></el-button>
                    <el-button type="primary" icon="el-icon-plus" circle @click="addParam()" style="padding: 5px;" v-if="index==ruleForm.analysisDatastreams.length-1"></el-button>
                </div>
                <p class="font-16" style="margin-top:2.14rem;">输出值</p>
                <div class="cl-flex">
                    <span style="flex-shrink:0">参数1：</span>
                    <el-form-item label="设备" :prop="'output.devId'"
                                class="mgR-20"
                                :rules="{ required: true, message: '请选择设备', trigger: 'change' }">
                        <el-select v-model="ruleForm.output.devId" placeholder="请选择设备" style="width:150px;" @change="devChange($event,-1)">
                            <el-option
                            v-for="item in devList"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="数据流" class="mgR-20" 
                                :prop="'output.ddId'"
                                :rules="{ required: true, message: '请选择数据流', trigger: 'change' }">
                        <el-select v-model="ruleForm.output.ddId" placeholder="请选择数据流" style="width:150px;">
                            <el-option
                            v-for="item in dsList[-1]"
                            :key="item.id"
                            :label="item.dm_name"
                            :value="item.id">
                            </el-option>
                        </el-select>  
                    </el-form-item>
                    <el-form-item label="时间段" class="mgR-20" 
                                :prop="'output.time'"
                                :rules="{ required: true, message: '请选择时间段', trigger: 'change' }">
                        <el-date-picker v-model="ruleForm.output.time" type="datetimerange" range-separator="至"
                            start-placeholder="开始日期" style="border: none;border-bottom: 1px solid;border-radius: 0;"
                            end-placeholder="结束日期" @change='dateChange($event,-1)'> 
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item label="间隔（秒）" class="mgR-20" 
                                :prop="'output.frequency'"
                                :rules="{ required: true, message: '频率的范围0.5-5' , type: 'number',  trigger: 'blur'}">
                        <el-input-number v-model="ruleForm.output.frequency" controls-position="right" :min="0.5" :max="5" :step="0.5" style="width:90px"></el-input-number>
                    </el-form-item>
                </div>
            </el-form>
            <div style="margin-top:2.14rem;">
                <el-button type="primary" @click="submitForm('ruleForm')">确 认</el-button>
                <el-button @click="goBack">返 回</el-button>
            </div>
            <linear-chart ref="linear"></linear-chart>
            <div class="cl-flex" v-if="linearFlag">
                <table border="1" cellspacing="0" cellpadding="15" style="border-color:#ebeef5;margin:2.14rem auto">
                    <thead>
                        <tr>
                            <th>Y\X</th>
                            <th v-for="(p,i) in dsParams" :key="i">{{p}}</th>
                        </tr>
                    </thead>
                    <tr>
                        <td>Y</td>
                        <td v-for="(v,index) in linearParams" :key="index">{{v.toFixed(3)}}</td>
                    </tr>
                </table>
            </div>
        </div>
        <!-- <div class="mainContent bg-fff intellAna" style="padding:2% 5%">
            <p class="font-16">输入值</p>
            <v-layout v-for="(item,index) in analysisDatastreams" :key="index" row >
                <v-flex xs12 class="cl-flex">
                    <span style="flex-shrink:0">参数{{index+1}}：</span>
                    <v-flex xs2>
                        <v-select class="mgR-20" :items="devList" label="设备" v-model="item.devId" item-text="name" item-value="id" @change="devChange($event,index)"></v-select>
                    </v-flex>
                    <v-flex xs2>
                        <v-select class="mgR-20" :items="dsList[index]" label="数据流"  v-model="item.ddId" item-text="dm_name" item-value="id"></v-select>
                    </v-flex>
                    <v-flex xs5>
                        <el-date-picker class="mgR-20" v-model="item.time" type="datetimerange" range-separator="至"
                            start-placeholder="开始日期" style="border:none;border-bottom:  1px solid rgba(0,0,0,.42);border-radius: 0;"
                            end-placeholder="结束日期" @change='dateChange($event,index)'> 
                        </el-date-picker>
                    </v-flex>
                    
                    <v-flex xs1>
                        <v-text-field class="mgR-20" label="频率" v-model="item.frequency" type="number" :min="0.5" :max="5" :step="0.5"></v-text-field>
                    </v-flex>
                    <el-button type="danger" icon="el-icon-delete" circle @click="deleteParam(index)" style="padding: 5px;" v-if="index<analysisDatastreams.length-1"></el-button>
                    <el-button type="primary" icon="el-icon-plus" circle @click="addParam()" style="padding: 5px;" v-if="index==analysisDatastreams.length-1"></el-button>
                </v-flex>
                    
            </v-layout>
            <p class="font-16" style="margin-top:2.14rem;">输出值</p>
            <v-layout row>
                <v-flex xs12 class="cl-flex">
                    <span style="flex-shrink:0">参数1：</span>
                    <v-flex xs2>
                        <v-select class="mgR-20" :items="devList" label="设备" v-model="output.devId" item-text="name" item-value="id" @change="devChange($event,-1)"></v-select>
                    </v-flex>
                    <v-flex xs2>
                        <v-select class="mgR-20" :items="dsList[-1]" label="数据流"  v-model="output.ddId" item-text="dm_name" item-value="id"></v-select>
                    </v-flex>
                    <v-flex xs5>
                        <el-date-picker class="mgR-20" v-model="output.time" type="datetimerange" range-separator="至"
                            start-placeholder="开始日期" style="border:none;border-bottom:  1px solid rgba(0,0,0,.42);border-radius: 0;"
                            end-placeholder="结束日期" @change='dateChange($event,-1)'> 
                        </el-date-picker>
                    </v-flex>
                    <v-flex xs1>
                        <v-text-field class="mgR-20" label="频率" v-model="output.frequency" type="number" :min="0.5" :max="5" :step="0.5"></v-text-field>
                    </v-flex>
                </v-flex>
            </v-layout>
            <v-layout style="justify-content: center;">
                <el-button type="primary" @click="submit()">确 认</el-button>
                <el-button @click="goBack">返 回</el-button>
            </v-layout>
            <linear-chart ref="linear"></linear-chart>
            <div class="cl-flex" v-if="linearFlag">
                <table border="1" cellspacing="0" cellpadding="15" style="border-color:#ebeef5;margin:2.14rem auto">
                    <thead>
                        <tr>
                            <th>Y\X</th>
                            <th v-for="(p,i) in dsParams" :key="i">{{p}}</th>
                        </tr>
                    </thead>
                    <tr>
                        <td>Y</td>
                        <td v-for="(v,index) in linearParams" :key="index">{{v.toFixed(3)}}</td>
                    </tr>
                </table>
            </div>
        </div> -->
    </div>
</template>

<script>
    import headTop from 'components/header/head'
    import subHead from 'components/subHeader/subHeader'
    import linearChart from 'components/charts/linearChart'
    import {getDevicelist,getDslist,addApp} from 'service/getData'
    import {dateFormat} from 'config/mUtils'

    export default {
        name: 'linear',
        data () {
            return {
                linearFlag:false,
                linearParams:[],
                productId:0,
                dsParams:[],
                ruleForm:{
                    analysisDatastreams:[{
                        devId:'',
                        ddId:'',
                        type:1,
                        start:'',
                        end:'',
                        frequency:5,
                        time:'',
                        }
                    ],
                    output:{
                        devId:'',
                        ddId:'',
                        type:0,
                        start:'',
                        end:'',
                        frequency:5,
                    },
                },
                devList:[],
                dsList:{},
            }
        },
        computed:{
        },
        components:{
            'cl-header':headTop,
            'sub-header':subHead,
            'linear-chart':linearChart
        },
        mounted(){
            this.productId = this.$route.params.productId;
            this.getDevicelist();
        },
        methods: {
            //添加参数
            addParam(){
                this.ruleForm.analysisDatastreams.push({
                    devId:'',
                    ddId:'',
                    type:1,
                    start:'',
                    end:'',
                    frequency:5,
                    time:'',
                    key: Date.now()
                });
            },
            //删除参数
            deleteParam(index){
                if(index !== -1){
                    this.ruleForm.analysisDatastreams.splice(index, 1);
                }
            },
            //设备id改变
            devChange(val,index){
                if(index>-1){
                    this.ruleForm.analysisDatastreams[index].ddId = '';
                }else{
                    this.ruleForm.output.ddId = '';
                }
                this.getDslist(val,index);
                
            },
            getDsName(val,index){
                let obj = {};
                obj = this.dsList[index].find((item)=>{
                    return item.id === val;
                });
                this.dsParams[index] = obj.dm_name;
            },
            dateChange(date,index){
                if(index>-1){
                    this.ruleForm.analysisDatastreams[index].start = dateFormat(date[0]);
                    this.ruleForm.analysisDatastreams[index].end = dateFormat(date[1]);
                }else{
                    this.ruleForm.output.start = dateFormat(date[0]);
                    this.ruleForm.output.end = dateFormat(date[1]);
                }
                
            },
            //获取设备
            async getDevicelist(){
                let resp = await getDevicelist(this.productId);
                if(resp.code==0){
                    this.devList = resp.data;
                }else if(resp.code=="error"){
                    return;
                }else{
                    this.$message({
                        message: "获取数据失败",
                        type: 'error'
                    });
                }
            },
            //获取数据流
            async getDslist(id,index){
                let resp = await getDslist(id);
                if(resp.code==0){
                    if(resp.data.length>0){
                        let temp = {};
                        Object.assign(temp,this.dsList);
                        temp[index] = resp.data;
                        this.dsList=temp;
                    }else{
                        this.ruleForm.analysisDatastreams[index].devId = '';
                        this.ruleForm.analysisDatastreams[index].ddId = '';
                        this.dsList[index] =[];
                        this.$alert('该设备下没有数据流，请重新选择！', '提示', {
                            confirmButtonText: '确定',
                            callback: action => {
                            }
                        });
                    }
                }else if(resp.code=="error"){
                    return;
                }else{
                    this.$message({
                        message: "获取统计数据失败",
                        type: 'error'
                    });
                }
            },
            navDirect(){
                //加密
                let b = new Buffer(JSON.stringify(this.productId));
                let s = b.toString('base64');
                let data = encodeURIComponent(s);
                this.$router.push('/myProduct/'+data+'/intellAna')
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
                }else{
                    this.getDslist(devId);
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
            async submit(){
                let that = this;
                this.dsParams = this.ruleForm.analysisDatastreams.map(function(v,index){
                    return that.dsList[index].find((item)=>{
                        return item.id === v.ddId;
                    }).dm_name
                });
                let temp = [...this.ruleForm.analysisDatastreams,this.ruleForm.output];
                let resp = await addApp(this.productId,"",1,temp);//this.productId
                if(resp.code==0){
                    if(resp.data){
                        if(resp.data.data[1].length==1){
                            this.linearFlag = false;
                            this.$refs.linear.drawChart1(resp.data);
                        }else if(resp.data.data[1].length==2){
                            this.linearFlag = false;
                            this.$refs.linear.drawChart2(resp.data);
                        }else if(resp.data.data[1].length>2){
                            // this.$refs.linear.innerHTML = '';
                            this.linearFlag = true;
                            this.linearParams = resp.data.data[1];
                        }
                    }
                }else if(resp.code=="error"){
                    return;
                }else{
                    this.$message({
                        message: "生成图表失败！",
                        type: 'error'
                    });
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
