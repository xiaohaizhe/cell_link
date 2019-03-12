<template>
    <div>   
        <cl-header headColor="#181818"></cl-header>
        <sub-header title="触发器管理" :subtitle="`${triggerData.name}-详情`"></sub-header>
        <div class="mainContent">
            <div class="flexBtw">
                <span class="font-16">基本信息</span>
                <div>
                    <router-link :to="{name:'triggerManage',params:{ data: triggerData ,editVisible:true}}">
                        <i class="editIcon cl-icon"></i>
                    </router-link>
                    <router-link :to="{name:'associatedDev',params:{ data: triggerData,productId:productId}}">
                        <i class="linkIcon cl-icon"></i>
                    </router-link>
                    <i class="delete cl-icon" @click="deleteItem(triggerData.id)"></i>
                </div>
            </div>
            <div class="bg-fff" style="margin-top:20px;">
                <div class="cl-card">
                    <p class="font-18 colorBlack mgbot-10">{{triggerData.name}}</p>
                    <p class="colorGray">数据流名称：{{triggerData.datastreamName}}</p>
                    <p class="colorGray">
                        <span v-if="triggerData.triggerMode==1">URL</span>
                        <span v-if="triggerData.triggerMode==0">邮箱</span>地址：
                        {{triggerData.modeValue}}</p>
                    <p class="colorGray">创建时间：{{triggerData.createTime}}</p>
                </div>
            </div>
            <div class="flexBtw" style="margin:30px 0 20px">
                <span class="font-16">触发情况</span>
                <el-button type="text" class="colorBlack" style="padding:0">打印日志</el-button>
            </div>
            <div class="bg-fff" style="padding:30px 40px">
                <div class="flexBtw" style="margin-bottom:40px">
                    <div class="flex">
                        <el-date-picker v-model="triTime" type="daterange" range-separator="至"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期" @change='dateChange' style="margin-right:20px;" > 
                        </el-date-picker>
                        <el-radio-group v-model="triRadio">
                            <el-radio-button label="0">本月</el-radio-button>
                            <el-radio-button label="1">本周</el-radio-button>
                        </el-radio-group>
                    </div>
                </div>
                <div>
                    <dsChart ref="triggerChart" chartId="triggerChart"></dsChart>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import headTop from 'components/header/head'
    import subHead from 'components/subHeader/subHeader'
    import dsChart from 'components/charts/dsChart'
    import {deleteTrigger} from 'service/getData'

    export default {
        name: 'triggerDetail',
        data () {
            return {
                triggerData:{},
                triTime:'',
                triRadio: '0',
                productId:0
            }
        },
        props:{
        },
        computed:{
        },
        components:{
            'cl-header':headTop,
            'sub-header':subHead,
            'dsChart':dsChart
        },
        mounted(){
            this.triggerData = this.$route.params.data;
            this.productId = this.$route.params.productId;
        },
        methods: {
            dateChange(date){
                let start = dateFormat(date[0]);
                let end  = dateFormat(date[1]);
                // this.getTriIncrement(start,end);
                this.triRadio='';
            },
            //删除事件
            deleteItem(id){
                this.$confirm('删除后，相关资源将会被全部删除，且无法恢复。确定要删除吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteTrigger(id);
                })
            },
            async deleteTrigger(id){
                let resp = await deleteTrigger(id);
                if(resp.code==0){
                    this.$message({
                        type: 'success',
                        message: '删除成功!'
                    });
                    this.$router.push('triggerManage');
                }else{
                    this.$message({
                        type: 'error',
                        message: '删除失败!'
                    });
                }
            }
        }

    }
</script>

<style>
</style>
