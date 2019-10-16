<template>
    <div>
        <div class="bgWhite topCard cl-flex justifyBet mgbot-10">
            <div class="detail">
                <p class="font-24 mgbot-20"><span class="mgR-10">{{activeScene.appName}}</span><i class="el-icon-edit colorGray2 point" @click="editApp"></i></p>
                <p class="colorGray font-14">{{activeScene.description}}</p>
            </div>
            <div>
                <el-button type="warning" class="clButton"  @click="save()">保存布局</el-button>
                <el-button type="primary" class="clButton" @click="publish()">发布</el-button>
            </div>
        </div>
        <div class="bgWhite" style="padding:10px" >
            <draggable class="cl-flex flexWrap justifyBet" :options="{group:'people',animation:150, draggable:'.dragChart',scroll:true,scrollSensitivity:200}"
              v-model="appChartList" 
              >
                <li v-for="(item) in appChartList" class="appChart dragChart"
                    :key="item.acId">
                    <p class="cl-flex justifyEnd" style="background-color: #f2f2f2;padding: 10px 30px;">
                        <i class="el-icon-edit colorGray font-18  point mgR-10" @click="editAppChart(item)"></i>
                        <i class="el-icon-delete colorGray font-18 point" @click="deleteItem(item.acId)"></i>
                    </p>
                    <line-chart :chartId="`appLineChart${item.acId}`" :ref="`appLineChart${item.acId}`" :data="item.appDatastreamList" v-if="item.chart==2"></line-chart>
                    <bar-chart :chartId="`appBarChart${item.acId}`" :ref="`appBarChart${item.acId}`" :data="item.appDatastreamList"  v-if="item.chart==1"></bar-chart>
                </li>
                <li class="appChart cl-flex alignCenter justifyCenter colorBlue" style="width:49%">
                      <i class="el-icon-circle-plus-outline font-40 point" @click="addAppChart"></i>
                </li>
          </draggable>
        </div>
    </div>
</template>
<script>
    import { mapGetters } from 'vuex'
    import lineChart from './children/lineChart'
    import barChart from './children/barChart'
    import draggable from 'vuedraggable'
    import {getChartDetail,chartLoca,deleteChart} from 'api/application'

    export default {
        name: 'index',
        data () {
            return {
                appChartList:[]
            }
        },
        computed: {
            ...mapGetters([
               'activeScene'
            ])
        },
        components:{
            lineChart,barChart,
            draggable
        },
        mounted(){
            // this.$refs.appChart.drawChart([],'#3BBAF0');
            // this.$refs.appChart1.drawChart([]);
            this.getChartDetail()
        },
        methods:{
            async getChartDetail(){
                let resp = await getChartDetail(this.$route.params.appId)
                this.appChartList = resp.data.sort(function(a,b){
                    return a.sequenceNumber - b.sequenceNumber
                })
            },
            addAppChart(){
                this.$addAppChart.show({
                    appData:this.activeScene,
                    onOk: () => {
                        this.getChartDetail()
                    },
                });
            },
            editAppChart(item){
                this.$editAppChart.show({
                    appData:{...item,scenarioId:this.activeScene.scenarioId},
                    onOk: () => {
                        this.getChartDetail()
                    },
                });
            },
            editApp(){
                this.$editApp.show({
                    appData:this.activeScene,
                    onOk: () => {
                        this.$store.dispatch('user/setScene',{appId:this.activeScene.appId});
                        this.getAppList();
                    },
                });
            },
            deleteItem(val){
                this.$confirm('删除应用图表后，相关数据将会被全部删除，且无法恢复。确定要删除应用图表吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteChart(val)
                })
            },
            async deleteChart(val){
                let resp = await deleteChart(val);
                this.$message({
                    message: "删除成功！",
                    type: 'success'
                });
                this.getChartDetail()
            },
            async save(){
                let temp = [];
                temp = this.appChartList.map((val,index)=>{
                    return {acId:val.acId,sequenceNumber:index+1}
                })
                let resp = await chartLoca(temp)
                this.$message({
                    message: "保存布局成功！",
                    type: 'success'
                });
            },
            publish(){
                this.$router.push('/publish/'+this.activeScene.appId)
            }
        }
    }
</script>
<style>
    .appChart{
        flex-basis: 49%;
        height: 400px;
        border: 1px solid #f2f2f2;
        margin-bottom: 10px
    }
</style>