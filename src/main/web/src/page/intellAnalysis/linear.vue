<template>
    <div>
        <cl-header headColor="#181818"></cl-header>
        <sub-header title="智能分析" subtitle="线性回归-新建"></sub-header>
        <div class="mainContent bg-fff noBorder">
            <div style="margin:30px auto;width: 50%;"> 
                <p class="font-16">输入值</p>
                <div v-for="(item,index) in analysisDatastreams" :key="index" style="margin:15px 0;">
                    参数{{index+1}}：
                    <el-select v-model="item.devId" placeholder="请选择设备" style="margin-right:20px;">
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
                <p class="font-16" style="margin-top:30px;">输出值</p>
                <div style="margin:15px 0;">
                    参数：
                    <el-select v-model="output.devId" placeholder="请选择设备" style="margin-right:20px;">
                        <el-option
                        v-for="item in devList"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                        </el-option>
                    </el-select>
                    <el-select v-model="output.ddId" placeholder="请选择数据流" @visible-change="dsFocus($event,output.devId)">
                        <el-option
                        v-for="item in dsList"
                        :key="item.id"
                        :label="item.dm_name"
                        :value="item.id">
                        </el-option>
                    </el-select>
                </div>
                <div style="margin-top:30px;">
                    <el-button type="primary" @click="submit()">确 认</el-button>
                    <el-button @click="goBack">返 回</el-button>
                </div>
                
            </div>
        </div>
    </div>
</template>

<script>
    import headTop from 'components/header/head'
    import subHead from 'components/subHeader/subHeader'
    import {getDevicelist,getDslist,addApp} from 'service/getData'

    export default {
        name: 'linear',
        data () {
            return {
                productId:0,
                analysisDatastreams:[{
                        devId:'',
                        ddId:'',
                        type:1
                    }
                ],
                output:{
                    devId:'',
                    ddId:'',
                    type:0
                },
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
            this.productId = this.$route.params.productId;
            this.getDevicelist();
        },
        methods: {
            //添加参数
            addParam(){
                this.analysisDatastreams.push({
                    devId:'',
                    ddId:'',
                    type:1,
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
            //数据流为空，先选择设备
            dsFocus(val,devId){
                if(val && !devId){
                    this.$alert('请先选择设备！', '提示', {
                        confirmButtonText: '确定',
                        callback: action => {
                        }
                    });
                    return false;
                }else{
                    this.getDslist(devId);
                }
            },
            async submit(){
                let temp = [...this.analysisDatastreams,this.output];
                let resp = await addApp(this.productId,"",1,temp);
                if(resp.code==0){
                    this.$message({
                        message: "添加成功！",
                        type: 'success'
                    });
                    this.$router.push('/myProduct/intellAna');
                }else{
                    this.$message({
                        message: "添加失败！",
                        type: 'error'
                    });
                }
            },
            //返回事件
            goBack () {
                window.history.length > 1
                    ? this.$router.go(-1)
                    : this.$router.push('/')
                }
        }

    }
</script>

<style>
</style>
