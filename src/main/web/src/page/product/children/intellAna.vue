<template>
    <div>
        <router-link :to="{name:'linear',params:{productId:product.id}}">
            <div>linear</div>
        </router-link>
        <router-link :to="{name:'heatmap'}">
            <div>heatmap</div>
        </router-link>
        
    </div>
</template>

<script>
import {mapState} from 'vuex'

export default {
    name: 'intellAna',
    data () {
        return {
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
</style>