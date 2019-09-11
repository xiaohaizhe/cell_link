<template>
    <div class="block bgWhite clBody">
        <ul class="el-timeline">
            <li class="el-timeline-item" v-for="(item,index) in data" :key="index">
                <div class="el-timeline-item__tail"></div>
                <div class="el-timeline-item__node el-timeline-item__node--normal el-timeline-item__node--"><!---->
                </div><!---->
                <div class="el-timeline-item__wrapper">
                    <div class="el-timeline-item__timestamp is-top">
                    {{item.created}}
                    </div>
                    <div class="el-timeline-item__content">
                        <div class="el-card is-always-shadow">
                            <div class="el-card__body">
                                <h4>{{item.oplogType}}</h4> <p>{{item.msg}}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </li> 
        </ul>
    </div>
</template>

<script>
    import { opLog } from 'api/log'
    import { mapGetters } from 'vuex'

    export default {
        name: 'opLog',
        data () {
        return {
                data:[]
            }
        },
        components:{
        },
        computed: {
            ...mapGetters([
                'user'
            ])
        },
        mounted(){
            this.opLog()
        },
        methods:{
            async opLog(){
                let resp = await opLog(this.user.userId);
                this.data = resp.data;
            }
        }
    }
</script>
<style scoped>
    
</style>