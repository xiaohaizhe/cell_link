<template>
  <div>
      <div class="prodCenter">
          <div class="subtotal">
              <p class="font-24">数据中心</p>
              <div>
                <p>个人产品</p>
                <div class="content">
                    <div>
                        <img :src="userData.img" style="width:22px;height:22px;margin-right: 20px;"/>
                        <div class="number">
                            <p class="font-24">{{userData.total}}</p>
                            <p>{{userData.text}}</p>
                        </div>
                    </div>
                </div>
            </div>
            <div>
                <p>全站总览</p>
                <div class="content">
                    <div v-for="item in prodData" :key="item.text">
                        <img :src="item.img" style="width:22px;height:22px;margin-right: 20px;"/>
                        <div class="number">
                            <p class="font-24">{{item.total}}</p>
                            <p>{{item.text}}</p>
                        </div>
                    </div>
                </div>
            </div>
          </div>
          
      </div>
      

  </div>
</template>

<script>

  export default {
    name: 'prodOverview',
    data () {
        return{
            userData: {
                        img: require('../../assets/prod.png'),
                        total: 0,
                        text: '已拥有产品'
                    } ,
            prodData: [{
                        img: require('../../assets/user.png'),
                        total: 7890982,
                        text: '用户总量'
                    },{
                        img: require('../../assets/device.png'),
                        total: 0,
                        text: '连接设备'
                    },{
                        img: require('../../assets/stream.png'),
                        total: 0,
                        text: '连接数据流'
                    },  
            ],
        }
    } 

  }
</script>

<style>
.prodCenter{
    padding-top: 60px;
    padding-bottom: 50px;
    background-color: #fff;
}
.prodCenter .number p{
    color: #07aaa5;
}
.prodCenter .content{
    display: flex;
    justify-content: space-around;
}
.prodCenter .content>div{
    display: flex;
    align-items: baseline;
    margin: 60px 55px;
}
  
.prodCenter .subtotal{
    width: 62%;
    margin: 0 auto;
    overflow: hidden;
}
.prodCenter .subtotal>div{
    border: solid 1px #cccccc;
    margin-right: 10px;
    float: left;
    margin-top: 30px;
}
.prodCenter .subtotal>div>p{
    margin-left: 30px;
    margin-top: 20px;
    font-size: 16px;
}
</style>

