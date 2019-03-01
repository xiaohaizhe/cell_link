<template>
    <div>
        dsManage++   {{productId}}
    </div>
</template>

<script>
    import {mapState} from 'vuex'

    export default {
        name: 'dsManage',
        data () {
            return {
            }
        },
        computed:{
        ...mapState([
                    'productId'
                ]),
        },
        methods: {

        }

    }
</script>

<style>
</style>