<template>
    <div>
        <div class="bgWhite topCard cl-flex justifyBet">
            <div class="detail">
                <p class="font-24 mgbot-20"><span class="mgR-10">{{activeScene.scenarioName}}</span><i class="el-icon-edit colorGray2 point" ></i></p>
                <p class="colorGray font-14">{{activeScene.description}}</p>
            </div>
            <div>
                <el-button type="warning" class="clButton" icon="el-icon-delete"  @click="deleteItem()">删除场景</el-button>
                <el-button type="primary" class="clButton">添加设备组</el-button>
                <el-button type="primary" class="clButton">添加触发器</el-button>
                <el-button type="primary" class="clButton">添加应用</el-button>
            </div>
        </div>
    </div>
</template>

<script>
    import { mapGetters } from 'vuex'
    export default {
        name: 'dgDetail',
        data () {
        return {

            }
        },
        components:{
        },
        computed: {
            ...mapGetters([
                'activeScene'
            ])
        },
        mounted(){
        },
        methods:{
        }
    }
</script>
<style>
</style>