<template>
    <div>
        <router-link :to="{name:'linear'}">
            <div>linear</div>
        </router-link>
        <router-link :to="{name:'heatmap'}">
            <div>heatmap</div>
        </router-link>
        
    </div>
</template>

<script>
export default {
    name: 'intellAna',
    data () {
        return {
        }
    },
    computed:{
    },
    methods: {

    }

}
</script>

<style>
</style>