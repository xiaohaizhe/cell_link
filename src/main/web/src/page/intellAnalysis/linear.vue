<template>
    <div>
        <cl-header headColor="#181818"></cl-header>
        <sub-header title="智能分析" subtitle="线性回归-新建"></sub-header>
        <div class="mainContent bg-fff noBorder">
            <div style="margin:30px auto;width: 90%;"> 
                <p class="font-16">输入值</p>
                <div v-for="(item,index) in analysisDatastreams" :key="index" style="margin:15px 0;">
                    参数{{index+1}}：
                    <el-select v-model="item.devId" placeholder="请选择设备" style="margin-right:20px;width:150px" @change="devChange($event,index)">
                        <el-option
                        v-for="item in devList"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                        </el-option>
                    </el-select>
                    <el-select v-model="item.ddId" placeholder="请选择数据流" @visible-change="dsFocus($event,item.devId)" style="margin-right:20px;width:150px">
                        <el-option
                        v-for="item in dsList[index]"
                        :key="item.id"
                        :label="item.dm_name"
                        :value="item.id">
                        </el-option>
                    </el-select>
                    <div style="display:inline-block;margin-right:20px;">
                        <p>时间段</p>
                        <el-date-picker v-model="item.time" type="datetimerange" range-separator="至"
                            start-placeholder="开始日期" style="border: none;border-bottom: 1px solid;border-radius: 0;"
                            end-placeholder="结束日期" @change='dateChange($event,index)'> 
                        </el-date-picker>
                    </div>
                    <div style="display:inline-block;margin-right:10px;">
                        <p>频率</p>
                        <el-input-number v-model="item.frequency" controls-position="right" :min="0.5" :max="5" :step="0.5" style="width:90px"></el-input-number>
                    </div>
                    <el-button type="danger" icon="el-icon-delete" circle @click="deleteParam(index)" style="padding: 5px;" v-if="index<analysisDatastreams.length-1"></el-button>
                    <el-button type="primary" icon="el-icon-plus" circle @click="addParam()" style="padding: 5px;" v-if="index==analysisDatastreams.length-1"></el-button>
                </div>
                <p class="font-16" style="margin-top:30px;">输出值</p>
                <div style="margin:15px 0;">
                    参数1：
                    <el-select v-model="output.devId" placeholder="请选择设备" style="margin-right:20px;width:150px" @change="devChange($event,-1)">
                        <el-option
                        v-for="item in devList"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                        </el-option>
                    </el-select>
                    <el-select v-model="output.ddId" placeholder="请选择数据流" @visible-change="dsFocus($event,output.devId)" style="margin-right:20px;width:150px">
                        <el-option
                        v-for="item in dsList[-1]"
                        :key="item.id"
                        :label="item.dm_name"
                        :value="item.id">
                        </el-option>
                    </el-select>
                    <div style="display:inline-block;margin-right:20px;">
                        <p>时间段</p>
                        <el-date-picker v-model="output.time" type="datetimerange" range-separator="至"
                            start-placeholder="开始日期" style="border: none;border-bottom: 1px solid;border-radius: 0;"
                            end-placeholder="结束日期" @change='dateChange($event,-1)'> 
                        </el-date-picker>
                    </div>
                    <div style="display:inline-block;margin-right:10px;">
                        <p>频率</p>
                        <el-input-number v-model="output.frequency" controls-position="right" :min="0.5" :max="5" :step="0.5" style="width:90px"></el-input-number>
                    </div>
                </div>
                <div style="margin-top:30px;">
                    <el-button type="primary" @click="submit()">确 认</el-button>
                    <el-button @click="goBack">返 回</el-button>
                </div>
                <linear-chart ref="linear"></linear-chart>
                <div class="flex" v-if="linearFlag">
                    <table border="1" cellspacing="0" cellpadding="15" style="border-color:#ebeef5;margin:30px auto">
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
        </div>
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
                this.analysisDatastreams.push({
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
                    this.analysisDatastreams.splice(index, 1);
                }
            },
            //设备id改变
            devChange(val,index){
                if(index>-1){
                    this.analysisDatastreams[index].ddId = '';
                }else{
                    this.output.ddId = '';
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
                    this.analysisDatastreams[index].start = dateFormat(date[0]);
                    this.analysisDatastreams[index].end = dateFormat(date[1]);
                }else{
                    this.output.start = dateFormat(date[0]);
                    this.output.end = dateFormat(date[1]);
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
                        this.analysisDatastreams[index].devId = '';
                        this.analysisDatastreams[index].ddId = '';
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
            async submit(){
                let that = this;
                this.dsParams = this.analysisDatastreams.map(function(v,index){
                    return that.dsList[index].find((item)=>{
                        return item.id === v.ddId;
                    }).dm_name
                });
                let temp = [...this.analysisDatastreams,this.output];
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

<style>
</style>
