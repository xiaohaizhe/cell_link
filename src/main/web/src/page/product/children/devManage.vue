<template>
    <div>
        <div class="notice center bg-fff">
            <p class="font-16">设备接入</p>
            <p>在接入设备时，请将以下注册码写入到设备中，只用于设备注册</p>
            <p class="cl-flex" style="justify-content:center;">{{product.registrationCode}}
                <i class="copy" v-clipboard:copy="product.registrationCode"
                    v-clipboard:success="onCopy"
                    v-clipboard:error="onError"></i>
            </p>
        </div>
        <div>
            <div class="searchArea">
                <p style="margin-bottom:2.14rem;">设备数量： {{deviceNumber}}个<span style="margin-left:2.14rem;">设备接入协议： {{protocolType}}</span></p>
                <div class="flexBtw">
                    <el-input placeholder="输入设备ID或者设备名称后按回车键"  v-model="devKey" @keyup.enter.native="changeDevKey()" 
                        clearable style="width:320px;height:36px;" @clear="clearKey()"></el-input>
                    <div>
                        <el-button type="primary" @click="addDevice">+新建设备</el-button>
                        <el-button @click="batchImport">批量导入设备</el-button>
                        <el-button @click="exportDevice">导出设备信息</el-button>
                    </div>
                </div>
                
            </div>
            <dev-table :keywords='devKey' :productId='product.id-0' :isAdmin='false' ref="child" @deviceNum='deviceNum'></dev-table>
        </div>
        <add-device :dialogVisible="addVisible" v-if='addVisible' @getAddDialogVisible="setAddVisible"></add-device>
        <batch-import :dialogVisible="importVisible" v-if='importVisible' @getImpDialogVisible="setImpVisible"></batch-import>
    </div>
</template>

<script>
    import devTable from 'components/tables/devTable'
    import addDevice from 'components/dialogs/addDevice'
    import batchImport from 'components/dialogs/batchImport'
    import {mapState} from 'vuex'

    export default {
        name: 'devManage',
        data () {
            return {
                devKey:'',
                deviceNumber:0,
                addVisible:false,
                importVisible:false,
            }
        },
        computed:{
            ...mapState([
                'product'
            ])
        },
        props:{
            protocolType:{
                type:String
            }
        },
        components:{
            'dev-table':devTable,
            'add-device':addDevice,
            'batch-import':batchImport
        },
        mounted(){
            this.addVisible = this.$route.query.addVisible;
        },
        methods: {
            //导出设备信息
            async exportDevice(){
                fetch('/dev/api/device/export_device?product_id='+this.product.id, {
                        method: 'GET',
                        headers: {
                            'Content-Type': 'application/json',
                        }
                    })
                    .then(res => res.blob())
                    .then(data => {
                        let blobUrl = window.URL.createObjectURL(data);
                        this.download(blobUrl);
                    });
            },
            download(blobUrl){
                const a = document.createElement('a');
                a.style.display = 'none';
                a.download = 'cell_link_device_model.xls';
                a.href = blobUrl;
                a.click();
                // document.body.removeChild(a);
            },
            clearKey(){
                this.$refs.child.queryDevice();
            },
            //devKey改变触发表格刷新
            changeDevKey(){
                this.$refs.child.queryDevice();
            },
            deviceNum(val){
                this.deviceNumber = val;
            },
            //添加设备
            addDevice(){
                this.addVisible = true;
            },
            //批量导入
            batchImport(){
                this.importVisible = true;
            },
            //复制到剪贴板成功
            onCopy(e){
                this.$message({
                    type: 'success',
                    message: '复制成功!'
                });
            },
            //复制到剪贴板失败
            onError(e){
                this.$message({
                    type: 'error',
                    message: '复制失败!'
                });
            },
            //弹出新建
            setAddVisible(val){
                this.addVisible = val;
                this.changeDevKey();
            },
            setImpVisible(val){
                this.importVisible = val;
                this.changeDevKey();
            }
        }

    }
</script>

<style>
    .notice{
        padding: 10px 0;
        box-shadow: 0px 2px 7px 0px 
        rgba(71, 85, 88, 0.45);
        margin-bottom: 2.14rem;
    }
    .notice.center p{
        margin: 10px 0;
    }
    .searchArea{
        background-color: #fff;
        padding: 2.14rem 40px 25px;
        border: 1px solid #cccccc;
        border-bottom: none;
    }
</style>