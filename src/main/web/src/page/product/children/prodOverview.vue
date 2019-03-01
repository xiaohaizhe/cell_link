<template>
    <div>
        <p class="font-16" style="margin-bottom:20px;">产品概要分析</p>
        <ul class="ovAnalysis flexBtw">
            <li v-for="item in ovAnaData" :key="item.name" class="bg-fff" 
                :class="{active : active==item.className}" @click="handleClick(item)">
                <div :class="item.className"></div>
                <p style="margin: 15px 0 5px;">{{item.name}}</p>
                <p>{{item.total}}</p>
            </li>
        </ul>
        <p class="font-16" style="margin:30px 0;">设备趋势分析</p>
        <!-- <div> -->
            <!-- <div>
                <el-radio-group v-model="radio3">
                    <el-radio-button label="本月"></el-radio-button>
                    <el-radio-button label="本周"></el-radio-button>
                </el-radio-group>
                <el-date-picker v-model="time" type="daterange" range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期" @change='dateChange($event,props.row.id)'> 
                </el-date-picker>
            </div>
            <div>
                <dsChart ref="deviceChart" chartId="deviceChart"></dsChart>
            </div>
        </div>
        <div>
            <div>
                <el-radio-group v-model="radio3">
                    <el-radio-button label="本月"></el-radio-button>
                    <el-radio-button label="本周"></el-radio-button>
                </el-radio-group>
                <el-date-picker v-model="time" type="daterange" range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期" @change='dateChange($event,props.row.id)'> 
                </el-date-picker>
            </div>
            <div>
                <dsChart ref="deviceChart" chartId="deviceChart"></dsChart>
            </div>
        </div>
        <div>
            <div>
                <el-radio-group v-model="radio3">
                    <el-radio-button label="本月"></el-radio-button>
                    <el-radio-button label="本周"></el-radio-button>
                </el-radio-group>
                <el-date-picker v-model="time" type="daterange" range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期" @change='dateChange($event,props.row.id)'> 
                </el-date-picker>
            </div>
            <div>
                <dsChart ref="triggerChart" chartId="triggerChart"></dsChart>
            </div>
        </div> -->
    </div>
</template>

<script>
    import {getProductOverview,getDevIncrement} from 'service/getData'
    import dsChart from 'components/charts/dsChart'

    export default {
        name: 'prodOverview',
        data () {
            return {
                radio3: '本月',
                active:'0',
                ovAnaData:[{
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
                    name:'创建相关性热力图',
                    total:0,
                    id:'intellAna'
                },{
                    className:'linear',
                    name:'创建线性回归数',
                    total:0,
                    id:'intellAna'
                }]
            }
        },
        props:{
            prodId:{
                type:Number
            }
        },
        components:{
            'dsChart':dsChart
        },
        mounted(){
            this.getProductOverview();
        },
        methods: {
            async getProductOverview(){
                let resp = await getProductOverview(this.prodId);
                if(resp.data.device_sum!=0){
                    this.ovAnaData[1].total = resp.data.device_datastream_sum;   //device_sum=0,device_datastream_sum=0
                }
                this.ovAnaData[0].total = resp.data.device_sum;
                this.ovAnaData[2].total = resp.data.application_sum;
                this.ovAnaData[3].total = resp.data.trigger_sum;
                this.ovAnaData[4].total = resp.data.correlation_analyse_sum;
                this.ovAnaData[5].total = resp.data.linear_analyse_sum;
            },
            async getDevIncrement(){
                let resp = await getDevIncrement(product_id,start,end);
            },
            handleClick(item){
                this.active = item.name;
                this.$router.push({name:item.id});
                this.$emit('changeNav', item.id)
            }

        }

    }
</script>

<style>
</style>
