<template>
    <div>
        <cl-header headColor="#181818"></cl-header>
        <sub-header title="智能分析" subtitle="相关性热力图-新建"></sub-header>
        <div class="mainContent bg-fff noBorder">
            <div style="margin:30px auto;width: 85%;">
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
                    <el-select v-model="item.ddId" placeholder="请选择数据流" style="width:150px;margin-right:20px;" @visible-change="dsFocus($event,item.devId)">
                        <el-option
                        v-for="item in dsList"
                        :key="item.id"
                        :label="item.dm_name"
                        :value="item.id">
                        </el-option>
                    </el-select>
                    <div style="display:inline-block;margin-right:20px;">
                        <p>时间段</p>
                        <el-date-picker v-model="item.time" type="daterange" range-separator="至"
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
                <div style="margin-top:30px;">
                    <el-button type="primary" @click="submit()">确 认</el-button>
                    <el-button @click="goBack">返 回</el-button>
                </div>
                <heat-map chartId="heatmaps" ref="triggerChart"></heat-map>
            </div>
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
                dsList:[]
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
                    this.analysisDatastreams.splice(index, 1)
                }
            },
            //获取设备
            async getDevicelist(){
                let resp = await getDevicelist(10);//this.product.id
                if(resp.code==0){
                    this.devList = resp.data;
                }
            },
            //获取数据流
            async getDslist(id,index){
                let resp = await getDslist(id);//id
                if(resp.data.length>0){
                    this.dsList = resp.data;
                }else{
                    this.dsList = [];
                    this.analysisDatastreams[index].devId = '';
                    this.$alert('该设备下没有数据流，请重新选择！', '提示', {
                        confirmButtonText: '确定',
                        callback: action => {
                        }
                    });
                }
            },
            //设备id改变
            devChange(val,index){
                this.analysisDatastreams[index].ddId = '';
                this.getDslist(val,index);
            },
            dateChange(date,index){
                // let start = dateFormat(date[0]);
                // let end  = dateFormat(date[1]);
                this.analysisDatastreams[index].start = dateFormat(date[0]);
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
                let resp = await addApp(10,"",0,[
    {
      "ddId":1548985548907,
      "type":1,
      "start":"2019-02-01 09:45:10",
      "end":"2019-02-01 09:46:10",
      "frequency":10
    },
    {
      "ddId":1548985548907,
      "type":1,
      "start":"2019-02-01 09:46:10",
      "end":"2019-02-01 09:47:10",
      "frequency":10
    }
  
  ]);//this.productId,this.analysisDatastreams
                if(resp.code==0){
                    // if(resp.data.result){
                        let labels = []; 
                        this.$refs.triggerChart.drawChart(labels,resp.data.result);//resp.data.result
                    // }
                    
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
