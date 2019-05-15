<template>
    <div>
        <cl-header headColor="#181818"></cl-header>
        <sub-header title="智能分析" subtitle="相关性热力图-新建" v-on:direct="navDirect"></sub-header>
        <div class="mainContent bg-fff intellAna"  style="padding:2% 5%">
            <v-layout v-for="(item,index) in analysisDatastreams" :key="index" row >
                <v-flex xs12 style="justify-content: center;" class="cl-flex">
                    <span style="flex-shrink:0">参数{{index+1}}：</span>
                    <v-flex xs2>
                        <v-select class="mgR-20" :items="devList" label="设备" v-model="item.devId" item-text="name" item-value="id" @change="devChange($event,index)"></v-select>
                    </v-flex>
                    <v-flex xs2>
                        <v-select class="mgR-20" :items="dsList[index]" label="数据流"  v-model="item.ddId" item-text="dm_name" item-value="id" @change="selectGet($event,index)"></v-select>
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
            <v-layout style="justify-content: center;">
                <el-button type="primary" @click="submit()">确 认</el-button>
                <el-button @click="goBack">返 回</el-button>
            </v-layout>
            <heat-map chartId="heatmaps" ref="heatmaps"></heat-map>
        </div>
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
                productId:0,
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
                    this.labels.splice(index, 1);
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
                        message: "获取数据失败",
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
            selectGet(vId,index){
                let obj = {};
                obj = this.dsList[index].find((item)=>{//这里的selectList就是上面遍历的数据源
                    return item.id === vId;//筛选出匹配数据
                });
                this.labels[index]=obj.dm_name;//我这边的name就是对应label的
                // console.log(obj.id);
            },
            //设备id改变
            devChange(val,index){
                this.getDslist(val,index);
            },
            dateChange(date,index){
                this.analysisDatastreams[index].start =dateFormat(date[0]);
                this.analysisDatastreams[index].end = dateFormat(date[1]);
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
            async submit(){
                let resp = await addApp(this.productId,"",0,this.analysisDatastreams);//this.productId,this.analysisDatastreams
                if(resp.code==0){
                    if(resp.data.data){
                        // let labels = []; 
                        this.$refs.heatmaps.drawChart(this.labels,resp.data.data);
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
