<template>
    <div>
        <cl-header headColor="#181818"></cl-header>
        <sub-header title="智能分析" subtitle="线性回归-新建"></sub-header>
        <div class="mainContent bg-fff noBorder">
            <p>输入值</p>
            <div v-for="(item,index) in analysisDatastreams" :key="index">
                参数{{index+1}}：
                <el-select v-model="item.devId" placeholder="请选择设备" style="margin-right:20px;" @change="devChange">
                    <el-option
                    v-for="item in devList"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                    </el-option>
                </el-select>
                <el-select v-model="item.ddId" placeholder="请选择数据流" @visible-change="dsFocus($event,item.devId)">
                    <el-option
                    v-for="item in dsList"
                    :key="item.id"
                    :label="item.dm_name"
                    :value="item.id">
                    </el-option>
                </el-select>
                <el-button type="danger" icon="el-icon-delete" circle @click="deleteParam(index)" style="padding: 5px;" v-if="index<analysisDatastreams.length-1"></el-button>
                <el-button type="primary" icon="el-icon-plus" circle @click="addParam()" style="padding: 5px;" v-if="index==analysisDatastreams.length-1"></el-button>
            </div>
        </div>
    </div>
</template>

<script>
    import headTop from 'components/header/head'
    import subHead from 'components/subHeader/subHeader'
    import {getDevicelist,getDslist} from 'service/getData'

    export default {
        name: 'linear',
        data () {
            return {
                analysisDatastreams:[{
                    devId:'',
                    ddId:''
                    }
                ],
                devList:[],
                dsList:[],
            }
        },
        computed:{
        },
        components:{
            'cl-header':headTop,
            'sub-header':subHead
        },
        mounted(){
            this.getDevicelist();
        },
        methods: {
            //添加参数
            addParam(){
                this.analysisDatastreams.push({
                    devId:'',
                    ddId:'',
                    key: Date.now()
                });
            },
            //
            deleteParam(index){
                if(index !== -1){
                    this.analysisDatastreams.splice(index, 1)
                }
            },
            //获取设备
            async getDevicelist(){
                let resp = await getDevicelist(10);//this.product.id
                if(resp.code==0){
                    this.devList = resp.data;
                }
            },
            //获取数据流
            async getDslist(id){
                let resp = await getDslist(1547795900304);//id
                if(resp.code==0){
                    this.dsList = resp.data;
                }
            },
            //设备id改变
            devChange(val){
                this.getDslist(val);
            },
            //数据流为空，先选择设备
            dsFocus(val,devId){
                if(val && !devId){
                    this.$alert('请先选择设备！', '提示', {
                        confirmButtonText: '确定',
                        callback: action => {
                        }
                    });
                    return false;
                }
            },
        }

    }
</script>

<style>
</style>
