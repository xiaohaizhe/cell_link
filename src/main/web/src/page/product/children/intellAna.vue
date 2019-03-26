<template>
    <div class="flex intellAna">
        <div class="wid50 bg-fff">
            <router-link :to="{name:'heatmap',params:{productId:product.id}}">
                <img :src="heatmap.img" style="width:100%;height:300px;"/>
                <p class="center colorBlack" style="margin:15px 0;">{{heatmap.text}}</p>
            </router-link>
        </div>
        <div class="wid50 bg-fff" style="margin-left:20px;">
            <router-link :to="{name:'linear',params:{productId:product.id}}">
                <img :src="linear.img" style="width:100%;height:300px;"/>
                <p class="center colorBlack" style="margin:15px 0;">{{linear.text}}</p>
            </router-link>
        </div>
    </div>
</template>

<script>
import {mapState} from 'vuex'

export default {
    name: 'intellAna',
    data () {
        return {
            heatmap:{
                img: require('assets/heatmappic.png'),
                text:'相关性热力图'
            },
            linear:{
                img: require('assets/linearpic.png'),
                text:'线性回归'
            }
        }
    },
    computed:{
        ...mapState([
            'product'
        ])
    },
    methods: {

    }

}
</script>

<style>
    .intellAna .bg-fff{
        cursor: pointer;
        box-shadow: 0px 2px 7px 0px rgba(71, 85, 88, 0.45);
    }
</style>