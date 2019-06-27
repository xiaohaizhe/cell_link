<template>
    <div>
        <cl-header headColor="#181818"></cl-header>
        <sub-header title="智能分析" subtitle="相关性热力图"  detail="新建"  v-on:direct="navDirect"></sub-header>
        <div class="mainContent bg-fff intellAna" style="padding:2% 6%">
            <el-form  :model="ruleForm" ref="ruleForm" label-width="80px" label-position="top" >
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
                        <el-select v-model="item.ddId" placeholder="请选择数据流" style="width:150px;" @change="selectGet($event,index)">
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
            </el-form>
               
                <div style="margin-top:2.14rem;">
                    <el-button type="primary" @click="submitForm('ruleForm')">确 认</el-button>
                    <el-button @click="goBack">返 回</el-button>
                </div>
                <heat-map chartId="heatmaps" ref="heatmaps"></heat-map>
        </div>
        <!-- <div class="mainContent bg-fff intellAna"  style="padding:2% 5%">
            <v-form  ref="ruleForm" v-model="valid">
                <v-layout v-for="(item,index) in analysisDatastreams" :key="index" row >
                    <v-flex xs12 style="justify-content: center;" class="cl-flex">
                        <span style="flex-shrink:0">参数{{index+1}}：</span>
                        <v-flex xs2>
                            <v-select class="mgR-20" :items="devList" label="设备" v-model="item.devId" item-text="name" item-value="id" :rules="devRules" @change="devChange($event,index)"></v-select>
                        </v-flex>
                        <v-flex xs2>
                            <v-select class="mgR-20" :items="dsList[index]" label="数据流"  v-model="item.ddId" item-text="dm_name" item-value="id" :rules="dsRules" @change="selectGet($event,index)"></v-select>
                        </v-flex>
                        <el-date-picker class="mgR-20" v-model="item.time" type="datetimerange" range-separator="至"
                            start-placeholder="开始日期" style="border:none;border-bottom:  1px solid rgba(0,0,0,.42);border-radius: 0;"
                            end-placeholder="结束日期" @change='dateChange($event,index)'> 
                        </el-date-picker>
                        <v-flex xs1>
                            <v-text-field class="mgR-20" label="频率" v-model="item.frequency" type="number" :min="0.5" :max="5" :step="0.5"></v-text-field>
                        </v-flex>
                        <el-button type="danger" icon="el-icon-delete" circle @click="deleteParam(index)" style="padding: 5px;" v-if="index<analysisDatastreams.length-1"></el-button>
                        <el-button type="primary" icon="el-icon-plus" circle @click="addParam()" style="padding: 5px;" v-if="index==analysisDatastreams.length-1"></el-button>
                    </v-flex>
                        
                </v-layout>
            </v-form>
            
            <v-layout style="justify-content: center;">
                <el-button type="primary" @click="submit()">确 认</el-button>
                <el-button @click="goBack">返 回</el-button>
            </v-layout>
            <heat-map chartId="heatmaps" ref="heatmaps"></heat-map>
        </div> -->
    </div>
</template>

<script>
    import headTop from 'components/header/head'
    import subHead from 'components/subHeader/subHeader'
    import heatmapChart from 'components/charts/heatmapChart'
    import {getDevicelist,getDslist,addApp} from 'service/getData'
    import {dateFormat} from 'config/mUtils'

    export default {
        name: 'heatmap',
        data () {
            return {
                productId:"0",
                ruleForm:{
                    analysisDatastreams:[{
                            devId:'',
                            ddId:'',
                            type:1,
                            start:'',
                            end:'',
                            gap:0,
                            frequency:5,
                            time:'',
                        },
                        {
                            devId:'',
                            ddId:'',
                            type:1,
                            start:'',
                            end:'',
                            gap:0,
                            frequency:5,
                            time:'',
                        }
                    ],
                },
                
                // devRules: [
                //     v => !!v || '请选择设备'
                // ],
                // dsRules: [
                //     v => !!v || '请选择数据流'
                // ],
                devList:[],
                dsList:{},
                labels:[]
            }
        },
        computed:{
        },
        components:{
            'cl-header':headTop,
            'sub-header':subHead,
            'heat-map':heatmapChart
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
                    gap:0,
                    key: Date.now()
                });
            },
            //删除参数
            deleteParam(index){
                if(index !== -1 && this.ruleForm.analysisDatastreams.length>2){
                    this.ruleForm.analysisDatastreams.splice(index, 1);
                    this.labels.splice(index, 1);
                }else{
                    this.$alert('参数不能小于2个！', '提示', {
                        confirmButtonText: '确定',
                        callback: action => {
                        }
                    });
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
                        message: "获取数据失败！",
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
                        message: "获取数据失败",
                        type: 'error'
                    });
                }
            },
            navDirect(){
                //加密
                let b = new Buffer(this.productId);
                let s = b.toString('base64');
                let data = encodeURIComponent(s);
                this.$router.push('/myProduct/'+data+'/intellAna')
            },
            selectGet(vId,index){
                let obj = {};
                obj = this.dsList[index].find((item)=>{//这里的selectList就是上面遍历的数据源
                    return item.id === vId;//筛选出匹配数据
                });
                if(obj.dm_name.length>5){
                    this.labels[index]=obj.dm_name.substring(0,4)+'...';
                }else{
                    this.labels[index]=obj.dm_name;//我这边的name就是对应label的
                }
                
                // console.log(obj.id);
            },
            //设备id改变
            devChange(val,index){
                this.getDslist(val,index);
            },
            dateChange(date,index){
                if(date[1].getTime()-date[0].getTime()<=2592000000){
                    this.ruleForm.analysisDatastreams[index].gap = date[1].getTime()-date[0].getTime();
                    this.ruleForm.analysisDatastreams[index].start =dateFormat(date[0]);
                    this.ruleForm.analysisDatastreams[index].end = dateFormat(date[1]);
                }else{
                    this.$alert('请不要选择超过30天的数据！', '提示', {
                        confirmButtonText: '确定',
                        callback: action => {
                        }
                    });
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
                let resp = await addApp(this.productId,"",0,this.ruleForm.analysisDatastreams);//this.productId,this.analysisDatastreams
                if(resp.code==0){
                    if(resp.data.data){
                        // let labels = []; 
                        this.$refs.heatmaps.drawChart(this.labels,resp.data.data);
                    }
                }else if(resp.code=="error"){
                    return;
                }else{
                    this.$message({
                        message: "生成图表失败！"+resp.msg,
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
<style>
  
</style>

