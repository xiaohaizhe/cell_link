<template>
    <div class="bgWhite fullWidth clBody">
        <div class="searchArea mgbot-20">
            <el-input style="width:250px" class="search mgbot-15"
                placeholder="关键词搜索"
                v-model="keyword">
                <el-button slot="append" icon="el-icon-search"></el-button>
            </el-input>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'orderLog',
        data () {
        return {
                keyword:'',
                formInline: {
                    user: '',
                    region: ''
                },
            }
        },
        components:{
        },
        mounted(){
        },
        methods:{
        }
    }
</script>
<style>
</style>