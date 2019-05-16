<template>
    <div>   
        <cl-header headColor="#181818"></cl-header>
        <sub-header title="触发器管理" :subtitle="`${triggerData.name}-详情`" v-on:direct="navDirect"></sub-header>
        <div class="mainContent">
            <div class="flexBtw">
                <span class="font-16">基本信息</span>
                <div>
                    <!-- <router-link :to="{name:'triggerManage',params:{ data: triggerData ,editVisible:true}}"> -->
                    <i class="editIcon cl-icon" @click="goAddress('triggerManage',true)"></i>
                    <!-- </router-link> -->
                    <!-- <router-link :to="{name:'associatedDev',params:{ data: triggerData,productId:productId}}"> -->
                        <i class="linkIcon cl-icon" @click="goAddress('associatedDev',false)"></i>
                    <!-- </router-link> -->
                    <i class="delete cl-icon" @click="deleteItem(triggerData.id)"></i>
                </div>
            </div>
            <div class="bg-fff" style="margin-top:1.43rem;">
                <div class="cl-card">
                    <p class="font-18 colorBlack mgbot-10">{{triggerData.name}}</p>
                    <p class="colorGray">数据流名称：{{triggerData.datastreamName}}</p>
                    <p class="colorGray">
                        <span v-if="triggerData.triggerMode==1">URL</span>
                        <span v-if="triggerData.triggerMode==0">邮箱</span>地址：
                        {{triggerData.modeValue}}</p>
                    <p class="colorGray">创建时间：{{triggerData.createTime}}</p>
                </div>
            </div>
            <div class="flexBtw" style="margin:2.14rem 0 1.43rem">
                <span class="font-16">触发情况</span>
                <!-- <el-button type="text" class="colorBlack" style="padding:0">打印日志</el-button> -->
            </div>
            <div class="bg-fff" style="padding:2.14rem 40px">
                <div class="cl-flex" style="margin-bottom:40px">
                    <el-radio-group v-model="triRadio" style="margin-right:1.43rem;" @change="triChange">
                        <el-radio-button label="0">本月</el-radio-button>
                        <el-radio-button label="1">本周</el-radio-button>
                    </el-radio-group>
                    <el-date-picker v-model="triTime" type="daterange" range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期" @change='dateChange($event)'> 
                    </el-date-picker>
                </div>
                <div>
                    <dsChart ref="triggerChart" chartId="triggerChart"></dsChart>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import headTop from 'components/header/head'
    import subHead from 'components/subHeader/subHeader'
    import dsChart from 'components/charts/dsChart'
    import {deleteTrigger,getTriggerChart} from 'service/getData'
    import {showMonthFirstDay,showMonthLastDay,getDay,dateFormat} from 'config/mUtils'

    export default {
        name: 'triggerDetail',
        data () {
            return {
                triggerData:{},
                triTime:'',
                triRadio: '0',
                thisMonth:[],
                thisWeek:[]
            }
        },
        props:{
        },
        computed:{
        },
        components:{
            'cl-header':headTop,
            'sub-header':subHead,
            'dsChart':dsChart
        },
        mounted(){
            //解密
            let x = new Buffer(decodeURIComponent(this.$route.params.trigger), 'base64')
            let y = x.toString('utf8');
            this.triggerData = JSON.parse(y);
            this.getTime();
            this.getTriggerChart();
        },
        methods: {
            async getTriggerChart(start=this.thisMonth[0],end=this.thisMonth[1]){
                let resp = await getTriggerChart(this.triggerData.id,start,end);
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
            dateChange(date){
                let start = dateFormat(date[0]);
                let end  = dateFormat(date[1]);
                this.getTriggerChart(start,end);
                this.triRadio='';
            },
            triChange(val){
                if(val=="1"){
                    this.getTriggerChart(this.thisWeek[0],this.thisWeek[1])
                }else{
                    //本月
                    this.getTriggerChart()
                }
                this.triTime='';
            },
            //删除事件
            deleteItem(id){
                this.$confirm('删除后，相关资源将会被全部删除，且无法恢复。确定要删除吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteTrigger(id);
                })
            },
            async deleteTrigger(id){
                let resp = await deleteTrigger(id);
                if(resp.code==0){
                    this.$message({
                        type: 'success',
                        message: '删除成功!'
                    });
                    this.goAddress('triggerManage',false);
                }else if(resp.code=="error"){
                    return;
                }else{
                    this.$message({
                        type: 'error',
                        message: '删除失败!'
                    });
                }
            },
            navDirect(){
                //加密
                let b = new Buffer(JSON.stringify(this.triggerData.productId));
                let s = b.toString('base64');
                let data = encodeURIComponent(s);
                this.$router.push('/myProduct/'+data+'/triggerManage')
            },
            goAddress(url,flag){
                //加密
                let b = new Buffer(JSON.stringify(this.triggerData.productId));
                let s = b.toString('base64');
                let data = encodeURIComponent(s);
                if(flag){
                    this.$router.push({name:url,params:{productId:data,data:this.triggerData,editVisible:true}})
                }else{
                    this.$router.push({name:url,params:{productId:data,data:this.triggerData}})
                }
                
            }  
        }

    }
</script>

<style>
</style>
