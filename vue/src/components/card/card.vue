<template>
    <el-row class="clCard">
        <ul class="cl-flex">
            <li :class="item.class" class="devTotal cl-flex directColumn justifyCenter" v-for="item in totalData" :key="item.name" >
                <count-to :start-val="0" :end-val="item.value" :duration="2600" class="colorWhite font-40 font-bold"/>
                <p class="colorGray2 font-16">{{item.name}}</p>
            </li>
        </ul>
    </el-row>
    
</template>

<script>
    import CountTo from 'vue-count-to'
    export default {
        name: 'clCard',
        data () {
            return {
                totalData:[{
                        name:'产品数量',
                        value:20000,
                        class:'productNum'
                    },{
                        name:'应用数量',
                        value:20000,
                        class:"appNum"
                    }
                ]
            }
        },
        components:{
            CountTo
        },
    }
</script>
<style>
    .clCard .devTotal{
        background-size: cover;
        width: 100%;
        height: 120px;
        border-radius: 4px;
        padding-left: 65px;
        flex:1;
        margin-right:1.43rem;
    }
    .clCard .devTotal:last-child{
      margin-right: 0;
    }
    .productNum{
        background: url('../../assets/productNum.png') no-repeat;   
    }
    .appNum{
        background: url('../../assets/appNum.png') no-repeat;
    }
</style>