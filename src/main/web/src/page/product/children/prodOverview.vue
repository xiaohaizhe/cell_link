<template>
    <div>
        <p class="font-16 mgbot-20">产品概要分析</p>
        <ul class="ovAnalysis flexBtw">
            <li v-for="item in ovAnaData" :key="item.name" class="bg-fff" 
                :class="{active : active==item.className}" @click="handleClick(item)">
                <div :class="item.className"></div>
                <p style="margin: 15px 0 5px;">{{item.name}}</p>
                <p>{{item.total}}</p>
            </li>
        </ul>
        <p class="font-16" style="margin:2.14rem 0;">设备趋势分析</p>
        <div class="bg-fff" style="padding:2.14rem 40px">
            <div class="flexBtw" style="margin-bottom:40px">
                <div class="cl-flex">
                    <el-radio-group v-model="devRadio" style="margin-right:1.43rem;" @change="devChange">
                        <el-radio-button label="0">本月</el-radio-button>
                        <el-radio-button label="1">本周</el-radio-button>
                    </el-radio-group>
                    <el-date-picker v-model="devTime" type="daterange" range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期" @change='dateChange($event,0)'> 
                    </el-date-picker>
                </div>
                <router-link :to="{name:'devManage',query:{addVisible:true}}">
                    <el-button @click="addDev">+添加设备</el-button>
                </router-link>
                
            </div>
            <div>
                <dsChart ref="deviceChart" chartId="deviceChart"></dsChart>
            </div>
        </div>
        <p class="font-16" style="margin:2.14rem 0;">数据点上传趋势</p>
        <div class="bg-fff" style="padding:2.14rem 40px">
            <div class="cl-flex" style="margin-bottom:40px">
                <el-radio-group v-model="dsmRadio" style="margin-right:1.43rem;" @change="dsmChange">
                    <el-radio-button label="0">本月</el-radio-button>
                    <el-radio-button label="1">本周</el-radio-button>
                </el-radio-group>
                <el-date-picker v-model="dsmTime" type="daterange" range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期" @change='dateChange($event,1)'> 
                </el-date-picker>
            </div>
            <div>
                <dsChart ref="dsmChart" chartId="dsmChart"></dsChart>
            </div>
        </div>
        <p class="font-16" style="margin:2.14rem 0;">触发信息数</p>
        <div class="bg-fff" style="padding:2.14rem 40px">
            <div class="cl-flex" style="margin-bottom:40px">
                <el-radio-group v-model="triRadio" style="margin-right:1.43rem;" @change="triChange">
                    <el-radio-button label="0">本月</el-radio-button>
                    <el-radio-button label="1">本周</el-radio-button>
                </el-radio-group>
                <el-date-picker v-model="triTime" type="daterange" range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期" @change='dateChange($event,2)'> 
                </el-date-picker>
            </div>
            <div>
                <dsChart ref="triggerChart" chartId="triggerChart"></dsChart>
            </div>
        </div>
    </div>
</template>

<script>
    import {getProductOverview,getDevIncrement,getTriggerIncrement,getDsmIncrement} from 'service/getData'
    import {showMonthFirstDay,showMonthLastDay,getDay,dateFormat} from 'config/mUtils'
    import dsChart from 'components/charts/dsChart'
    import {mapState} from 'vuex'

    export default {
        name: 'prodOverview',
        data () {
            return {
                active: '0',
                ovAnaData: [{
                    className:'connected',
                    name:'已连接设备',
                    total:0,
                    id:'devManage'
                },{
                    className:'datastream',
                    name:'在线数据流',
                    total:0,
                    id:'dsManage'
                },{
                    className:'execute',
                    name:'应用运行数',
                    total:0,
                    id:'appManage'
                },{
                    className:'trigger',
                    name:'触发器运行数',
                    total:0,
                    id:'triggerManage'
                },{
                    className:'heatmap',
                    name:'相关性热力图',
                    total:'',
                    id:'intellAna'
                },{
                    className:'linear',
                    name:'线性回归',
                    total:'',
                    id:'intellAna'
                }],
                devRadio: '0',
                triRadio: '0',
                dsmRadio: '0',
                devTime: '',
                triTime:'',
                dsmTime:'',
                thisMonth:[],
                thisWeek:[]
            }
        },
        // props:{
        //     prodId:{
        //         type:Number
        //     }
        // },
        components:{
            'dsChart':dsChart
        },
        computed:{
            ...mapState([
                'product'
            ])
        },
        mounted(){
            this.getTime();
            this.getProductOverview();
            this.getDevIncrement();
            this.getDsmIncrement();
            this.getTriggerIncrement();
        },
        methods: {
            async getProductOverview(){
                let resp = await getProductOverview(this.product.id);
                if(resp.code==0){
                    if(resp.data){
                        if(resp.data.device_sum!=0){
                            this.ovAnaData[1].total = resp.data.device_datastream_sum;   //device_sum=0,device_datastream_sum=0
                        }
                        
                    }
                    this.ovAnaData[0].total = resp.data.device_sum;
                    this.ovAnaData[2].total = resp.data.application_sum;
                    this.ovAnaData[3].total = resp.data.trigger_sum;
                    // this.ovAnaData[4].total = resp.data.correlation_analyse_sum;
                    // this.ovAnaData[5].total = resp.data.linear_analyse_sum;
                }else if(resp.code=="error"){
                    return;
                }else{
                    this.$message({
                        message: "获取数据失败",
                        type: 'error'
                    });
                }
                
            },
            async getDsmIncrement(start=this.thisMonth[0],end=this.thisMonth[1]){
                let resp = await getDsmIncrement(this.product.id,start,end);
                if(resp.code==0){
                    this.$refs.dsmChart.drawChart(resp.data);
                }else if(resp.code=="error"){
                    return;
                }else{
                    this.$message({
                        message: "获取数据失败",
                        type: 'error'
                    });
                }
            },
            async getDevIncrement(start=this.thisMonth[0],end=this.thisMonth[1]){
                let resp = await getDevIncrement(this.product.id,start,end);
                if(resp.code==0){
                    this.$refs.deviceChart.drawChart(resp.data);
                }else if(resp.code=="error"){
                    return;
                }else{
                    this.$message({
                        message: "获取数据失败",
                        type: 'error'
                    });
                }
            },
            async getTriggerIncrement(start=this.thisMonth[0],end=this.thisMonth[1]){
                let resp = await getTriggerIncrement(this.product.id,start,end);
                if(resp.code==0){
                    this.$refs.triggerChart.drawChart(resp.data);
                }else if(resp.code=="error"){
                    return;
                }else{
                    this.$message({
                        message: "获取数据失败",
                        type: 'error'
                    });
                }
            },
            getTime(){
                this.thisMonth[0] = showMonthFirstDay();
                this.thisMonth[1] = showMonthLastDay();
                let d = new Date().getDay()||7;
                this.thisWeek[0] = getDay(1-d);
                this.thisWeek[1] = getDay(7-d);
            },
            dateChange(date,flag){
                let start = dateFormat(date[0]);
                let end  = dateFormat(date[1]);
                if(flag==0){
                    this.getDevIncrement(start,end);
                    this.devRadio='';
                }else if(flag==1){
                    this.getDsmIncrement(start,end);
                    this.dsmRadio='';
                }else if(flag==2){
                    this.getTriggerIncrement(start,end);
                    this.triRadio='';
                }
            },
            handleClick(item){
                this.active = item.name;
                this.$store.commit('SAVE_TAB', item.id);
                this.$router.push({name:item.id});
            },
            devChange(val){
                if(val=="1"){
                    this.getDevIncrement(this.thisWeek[0],this.thisWeek[1])
                }else{
                    //本月
                    this.getDevIncrement()
                }
                this.devTime='';
            },
            dsmChange(val){
                if(val=="1"){
                    this.getDsmIncrement(this.thisWeek[0],this.thisWeek[1])
                }else{
                    //本月
                    this.getDsmIncrement()
                }
                this.dsmTime='';
            },
            triChange(val){
                if(val=="1"){
                    this.getTriggerIncrement(this.thisWeek[0],this.thisWeek[1])
                }else{
                    //本月
                    this.getTriggerIncrement()
                }
                this.triTime='';
            },
            addDev(){
                this.$store.commit('SAVE_TAB', 'devManage');
            }

        }

    }
</script>

<style>
</style>
