<template>
  <div>
    <div class="dataCenter">
      <p class="font-30 center">全站数据中心</p>
      <p class="center" style="margin-top:15px;">高质量、高可靠的数据预览，全方位了解您的平台数据</p>
      <div class="content">
        <div v-for="item in globalData" :key="item.text">
          <img :src="item.img" style="width:32px;height:32px;margin-right: 20px;"/>
          <div class="number">
            <p class="font-48">{{item.total}}</p>
            <p class="font-16">{{item.text}}</p>
          </div>
        </div>
      </div>
    </div>
    <div class="scenarios" :class='activeName'>
      <div>
        <ul class="sLeft"> 
          <li v-for="item in scenarios" :name="item.name" :class="{active : activeName == item.name }"
           @click="handleClick(item.name)" :key="item.name"><span>{{item.text}}</span></li>
        </ul>
        <div class="sRight" v-for="item in scenarios" :key="item.name" v-show="activeName == item.name">
          <p class="font-30">应用场景</p>
          <p class="font-24">{{item.english}}</p>
          <p class="font-16">{{item.text}}</p>
          <p style="margin-top:40px;margin-bottom:20px">{{item.detail}}</p>
          <p v-for="exam in item.example" :key="exam">{{exam}}</p>
        </div>
      </div>
    </div>
    <div class="infos center">
      <p class="font-30" style="margin-bottom:15px">丰富可靠的智能感知平台</p>
      <p>聚集多项创新技术，为各项业务提供强大数据支持</p>
      <div class="content">
        <div v-for="item in infos" :key="item.text" style="">
          <img :src="item.img" style="width:70px;height:70px;margin-bottom:40px"/>
          <p class="font-20">{{item.text}}</p>
          <p class="colorGray">{{item.detail}}</p>
        </div>
      </div>
      
    </div>
  </div>
</template>

<script>
  import { getGlobalData } from 'service/getData'

  export default {
    name: 'index',
    data () {
      return {
        activeName: 'platform',
        globalData:[{
          img: require('assets/user.png'),
          total: 0,
          text: '用户总量'
        },{
          img: require('assets/device.png'),
          total: 0,
          text: '连接设备'
        },{
          img: require('assets/stream.png'),
          total: 0,
          text: '连接数据流'
        }],
        scenarios:[{
          name: "platform",
          text: "警用监管平台",
          english: "Police supervision platform",
          detail: "接入区域中的环境传感器设备，监控管理区域中的车流量、人流量、交通信号、环境的温湿度、水电气接入、楼宇通风排水等等方面数据，对于故障或警情，及时的发现和精确的排障。",
          example: ["例如：某辖区某楼宇发生火情，通过温度、烟感、可燃气体浓度传感器数据迅速感知，精确定位火情发生位置，自动触发报警。楼宇内的传感器数据将给出当前火势范围及蔓延趋势，指导楼内人员正确撤离。同时根据当前车流量、人流量数据，发现火情周边的关键交通节点，安排警力迅速前往，进行管制和疏导，防止出现大规模事件。通过道路交通的数据，分析出最佳的路线，提供给消防部门迅速到达火场，同时提供区域的水、电、气实时情况，助力消防人员开展灭火。"]
        },{
          name: "plant",
          text: "农业种植",
          english: "Agricultural cultivation",
          detail: "智能感知平台将传感器收集的土壤湿度、天气、肥料使用情况、生长状况和GIS信息数据搜集并进行机器学习和分析；使用者可利用可视应用程序的来指导种植、灌溉庄稼。",
          example: [
            "· 为农场经营者提供科学、定时定量的指导，从而降低种植、灌溉成本；",
            "· 种植、灌溉策略更高效、更有效，从而提高了作物产量；",
            "· 及时解决作物产量问题，避免影响收入。"
          ]
        },{
          name: "industry",
          text: "工业生产",
          english: "Industrial production",
          detail: "通过传感器对产品质量指标(如：黏度、硬度、表面光洁度、成分、颜色及味道等)进行快速直接测量，联合判断指标间的内在变化关系，精准调整生产手段。",
          example: ["例如：在零件的生产线中，元件在某一步骤中进行精密打磨，通过传感器：识别打磨过程的动作波动；识别元件该步骤后的打磨精度；识别磨具刀头的磨损程度：综合几项数据给出磨具刀头需要更换的最佳时间提示，避免了过早更换刀头带来的成本浪费，也预防了过晚更换刀头而导致生产出次品。"]
        },{
          name: "more",
          text: "敬请期待更多"
        }],
        infos:[
          {
            img: require('assets/connect.png'),
            text: "开放、便捷的设备连接",
            detail: "智能感知平台提供可扩展、安全、可嵌入并部署的通信，支持在主流网络拓扑结构及通信情景中连接传感设备和装置"
          },{
            img: require('assets/workspace.png'),
            text: "快速创建协作工作区",
            detail: "智能感知平台为开发人员和业务人员提供快速创建协作的应用程序、分析工具和便于搜索的UI,帮助更快地解决问题，获取机会"
          },{
            img: require('assets/option.png'),
            text: "灵活的部署选项",
            detail: "按市场需求部署：云部署、内部部署和嵌入式部署，满足不同场景的需求"
          },{
            img: require('assets/time.png'),
            text: "基于模型的开发节省时间成本",
            detail: "智能感知平台的“无代码”开发支持创建你的环境中物的服务、储存、时间、协作和关系的模型。这将实现高效率和高重用率，将开发速度提高5-10倍"
          },{
            img: require('assets/run.png'),
            text: "事件驱动的执行",
            detail: "事件驱动的执行方式可提供对大规模设备的需求。智能感知平台还支持与大数据分析系统双向连接"
          },{
            img: require('assets/calculate.png'),
            text: "无处不在的计算",
            detail: "提供低延迟、低成本、高可用、易扩展的计算服务和AI能力，实现物的实时决策和自主协作，将物联网真正推向智联网"
          }
        ]
      }
    },
    mounted() {
      this.getData();
    },
    computed: {
    },
    methods: {
      async getData(){
        let resp = await getGlobalData();
        this.globalData[0].total = resp.data.user_sum;
        this.globalData[1].total = resp.data.device_sum;
        this.globalData[2].total = resp.data.device_datastream_sum;
      },
      handleClick(name) {
          if(name == "more"){
            return false;
          }
          this.activeName = name;
      }
    }

  }
</script>

<style>
  .dataCenter , .infos{
    padding-top: 60px;
    padding-bottom: 80px;
    background-color: #fff;
  }
  .dataCenter .content{
    display: flex;
    justify-content: space-around;
    margin-top: 80px;
    padding: 0 8%;
  }
  .dataCenter .content>div{
    display: flex;
    align-items: baseline;
  }
  .dataCenter .number p{
    color: #07aaa5;
  }
  .scenarios .sLeft li {
    text-align: left;
    line-height: 110px;
    color: #fff;
    cursor: pointer;
    margin: 0 30px;
    border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  }
  .scenarios .sLeft li.active{
    background-color: #3bbaf0;
    margin: 0;
    padding: 0px 42px;
  }
  .scenarios>div{
    padding-left: 17%;
    display: flex;
    align-items: center;
    background-color: rgba(25, 32, 38, 0.85);
  }
  .scenarios .sLeft {
    width: 18%;
    background-color: rgba(255, 255, 255, 0.1);
    box-shadow: 0px 2px 8px 0px rgba(71, 85, 88, 0.75);
  }
  .scenarios{
    background-position: center;
    background-size: cover;
    min-height: 300px;
  }
 
  .scenarios .sRight{
    padding-left: 80px;
    color: #fff;
    max-width: 70%;
  }
  .infos .content{
    display: flex;
    flex-wrap: wrap;
    padding: 0 5%;
    margin-top: 90px;
    justify-content: space-around;
  }
  .infos .content>div{
    flex-basis: 21%;
    margin: 50px;
  }
  .infos .content .colorGray{
    margin-top: 20px;
  }
</style>
