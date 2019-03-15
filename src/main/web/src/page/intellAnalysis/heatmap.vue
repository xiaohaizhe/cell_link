<template>
    <div>
        <cl-header headColor="#181818"></cl-header>
        <sub-header title="智能分析" subtitle="相关性热力图-新建"></sub-header>
        <div class="mainContent bg-fff noBorder">
            <div style="margin:30px auto;width: 85%;">
                <div v-for="(item,index) in analysisDatastreams" :key="index" style="margin:15px 0;">
                    参数{{index+1}}：
                    <el-select v-model="item.devId" placeholder="请选择设备" style="margin-right:20px;width:150px" @change="devChange">
                        <el-option
                        v-for="item in devList"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                        </el-option>
                    </el-select>
                    <el-select v-model="item.ddId" placeholder="请选择数据流" style="width:150px;" @visible-change="dsFocus($event,item.devId)">
                        <el-option
                        v-for="item in dsList"
                        :key="item.id"
                        :label="item.dm_name"
                        :value="item.id">
                        </el-option>
                    </el-select>
                    <div style="display:inline-block">
                        <p>时间段</p>
                        <el-date-picker v-model="item.time" type="daterange" range-separator="至"
                            start-placeholder="开始日期" style="border: none;border-bottom: 1px solid;border-radius: 0;"
                            end-placeholder="结束日期" @change='dateChange($event,1)'> 
                        </el-date-picker>
                    </div>
                    <div style="display:inline-block">
                        <p>频率</p>
                        <el-input-number v-model="item.frequency" controls-position="right" :min="0.5" :max="5" :step="0.5" style="width:90px"></el-input-number>
                    </div>
                    <el-button type="danger" icon="el-icon-delete" circle @click="deleteParam(index)" style="padding: 5px;" v-if="index<analysisDatastreams.length-1"></el-button>
                    <el-button type="primary" icon="el-icon-plus" circle @click="addParam()" style="padding: 5px;" v-if="index==analysisDatastreams.length-1"></el-button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import headTop from 'components/header/head'
    import subHead from 'components/subHeader/subHeader'
    import {getDevicelist,getDslist} from 'service/getData'

    export default {
        name: 'heatmap',
        data () {
            return {
                analysisDatastreams:[{
                      devId:'',
                      ddId:'',
                      time:'',
                      frequency:5
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
                    time:'',
                    frequency:5,
                    key: Date.now()
                });
            },
            //删除参数
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
