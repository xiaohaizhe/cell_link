<template>
    <div>
        <div class="bgWhite topCard cl-flex justifyBet mgbot-10">
            <div class="detail">
                <p class="font-24 mgbot-20"><span class="mgR-10">{{activeScene.appName}}</span><i class="el-icon-edit colorGray2 point" @click="editApp"></i></p>
                <p class="colorGray font-14">{{activeScene.description}}</p>
            </div>
        </div>
          <draggable class="bgWhite cl-flex flexWrap justifyBet" style="padding:10px" :options="{group:'people',animation:150,ghostClass:'sortable-ghost',chosenClass:'chosenClass',scroll:true,scrollSensitivity:200}"
              v-model="appChartList"
 
              >
            <li v-for="(item, index) in appChartList" class="appChart"
                :key="index">
              <line-chart :chartId="`appLineChart${item.acId}`" :ref="`appLineChart${item.acId}`" :data="item.appDatastreamList" v-if="item.chart==2"></line-chart>
              <bar-chart :chartId="`appBarChart${item.acId}`" :ref="`appBarChart${item.acId}`" :data="item.appDatastreamList" v-if="item.chart==1"></bar-chart>
            </li>
          </draggable>
           <li class="appChart cl-flex alignCenter justifyCenter colorBlue">
                <i class="el-icon-circle-plus-outline font-40 point" @click="addAppChart"></i>
            </li>
        <!-- <ul class="bgWhite cl-flex flexWrap justifyBet" style="padding:10px">
            <li class="appChart">
                <line-chart chartId="appChart" ref="appChart"></line-chart>
                
            </li>
            <li class="appChart">
                <bar-chart chartId="appChart1" ref="appChart1"></bar-chart>
                
            </li>
           
        </ul> -->
    </div>
</template>
<script>
    import { mapGetters } from 'vuex'
    import lineChart from './children/lineChart'
    import barChart from './children/barChart'
    import draggable from 'vuedraggable'
    import {getChartDetail} from 'api/application'

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
            lineChart,barChart,draggable
        },
        mounted(){
            // this.$refs.appChart.drawChart([],'#3BBAF0');
            // this.$refs.appChart1.drawChart([]);
            this.getChartDetail()
        },
        methods:{
            async getChartDetail(){
                let resp = await getChartDetail(this.$route.params.appId)
                this.appChartList = resp.data;
            },
            addAppChart(){
                this.$addAppChart.show({
                    appData:this.activeScene,
                    onOk: () => {
                        
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
            }
        }
    }
</script>
<style>
    .appChart{
        flex-basis: 49%;
        height: 300px;
        border: 1px solid #f2f2f2;
        margin-bottom: 10px
    }
</style>