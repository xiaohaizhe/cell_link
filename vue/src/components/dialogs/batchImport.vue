<template>
    <el-dialog
        title="批量导入设备"
        :visible.sync="visible" width="40%">
        <div class="upload">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-position="left" label-width="80px" class="demo-ruleForm">
                <el-form-item label="场景" prop="scenarioId">
                    <el-select v-model="ruleForm.scenarioId" placeholder="请选择场景" @change="changeScene">
                        <el-option
                        v-for="item in scenarios"
                        :key="item.scenarioId"
                        :label="item.scenarioName"
                        :value="item.scenarioId">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="设备组" prop="dgId">
                    <el-select v-model="ruleForm.dgId" placeholder="请选择设备组">
                        <el-option
                        v-for="item in devGroups"
                        :key="item.dgId"
                        :label="item.deviceGroupName"
                        :value="item.dgId">
                        </el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <div class="cl-flex" style="border-bottom:1px solid;align-items: center;flex-wrap: nowrap;position:relative">
                <span style="color:red">*</span>
                <el-input placeholder="请上传excel地址" v-model="fileName" clearable></el-input>
                <input type="file" id="ulFile" style="position:absolute;opacity:0;z-index:999;top:0;bottom:0;width:100%" @change="uploadFile"/>
                <el-button type="primary" class="mgbot-10">上传</el-button>
            </div>
            <div style="margin:2.14rem 0;">
                <span style="color:red">*</span>
                <span class="colorGray font-16">格式参考</span>
                <span class="download" @click="exportDevice">点击下载excel模板</span>
            </div>
        </div>
        <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitForm('ruleForm')">确 定</el-button>
            <el-button @click="visible = false">返 回</el-button>
        </span>
    </el-dialog>
</template>

<script>
    import {mapGetters} from 'vuex'
    import { findListByUser } from 'api/scene'
    import { findListByScenario } from 'api/devGroup'
  
  export default {
        name: 'import',
        data () {
            return{
                scenarios:[],
                devGroups:[],
                ruleForm:{
                    scenarioId:'',
                    dgId:''
                },
                rules: {
                    scenarioId: [
                        { required: true, message: '请选择场景', trigger: 'change' },
                    ],
                    dgId:[
                        { required: true, message: '请选择设备组', trigger: 'change' },
                    ],
                },
                file:'',
                fileName:'',
            }
        },
        props:{
            visible:{
                type:Boolean,
                default:false
            },
            userId:{
                type:Number
            },
            token:{
                type:String
            }
        },
        methods:{
            async findListByUser(){
                let resp = await findListByUser(this.userId);
                this.scenarios = resp.data;
            },
            async changeScene(val){
                this.ruleForm.dgId = '';
                this.findListByScenario(val);
            },
            async findListByScenario(val){
                let resp = await findListByScenario(val);
                this.devGroups = resp.data;
            },
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.upload();
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            async upload(){
                let that = this;
                let formdata = new FormData();
                formdata.append("dgId",this.ruleForm.dgId);
                formdata.append("file",this.file);
                new Promise((resolve, reject) => {
                    let requestObj;
                    if (window.XMLHttpRequest) {
                        requestObj = new XMLHttpRequest();
                    } else {
                        requestObj = new ActiveXObject;
                    }
                    requestObj.open('POST', "/celllink/api/device/import");
                    requestObj.setRequestHeader('authorization', this.token);
                    requestObj.send(formdata);
                    
                    requestObj.onreadystatechange = () => {
                        if (requestObj.readyState == 4) {
                            if (requestObj.status == 200) {
                                let obj = requestObj.response
                                if (typeof obj !== 'object') {
                                    obj = JSON.parse(obj);
                                }
                                resolve(obj)
                            } else {
                                reject(requestObj)
                            }
                        }
                    }
                }).then(function(responseJson){
                    if(responseJson.code==0){
                        const h = that.$createElement;
                        let msg = "上传成功"+responseJson.data.success+"条，"+"上传失败"+responseJson.data.fail.sum+"条。";
                        let elements =[]
                        if(responseJson.data.fail.sum>0){
                            for(let item of responseJson.data.fail.errNo){
                                if(item.data.length>0){
                                    elements.push(h('li', null, '第'+item.data+'条：'+item.msg)) 
                                }
                                
                            }
                        }
                        that.$msgbox({
                            title: '提示',
                            message: h('div', null, [
                                h('p', null, msg),
                                h('ul', null, elements)
                            ]),
                            showCancelButton: true,
                            confirmButtonText: '确定',
                            cancelButtonText: '取消'
                        })
                        that.visible = false;
                    }else{
                        that.$message({
                            message: "上传失败！",
                            type: 'error'
                        });
                    }
                })
                
            },
            uploadFile(val){
                let fileDom = document.getElementById("ulFile");
                this.file = fileDom.files[0];
                this.fileName = this.file.name;
            },
            async exportDevice(){
                window.URL = window.URL || window.webkitURL;  // Take care of vendor prefixes.
                var xhr = new XMLHttpRequest();
                xhr.open('GET', '/celllink/api/device/export_model', true);
                xhr.responseType = 'blob';
                xhr.setRequestHeader('authorization', this.token);
                xhr.onload = function(e) {
                    if (this.status == 200) {
                        var blob = this.response;
                        var URL = window.URL || window.webkitURL;  //兼容处理
                        // for ie 10 and later
                        if (window.navigator.msSaveBlob) {
                            try { 
                                window.navigator.msSaveBlob(blob, 'cell_link_device_model.xls');
                            }
                            catch (e) {
                                console.log(e);
                            }
                        }else{
                                let blobUrl = URL.createObjectURL(blob);
                                const a = document.createElement('a');
                                a.style.display = 'none';
                                a.download = 'cell_link_device_model.xls';
                                a.href = blobUrl;
                                document.body.appendChild(a);
                                a.click();
                                document.body.removeChild(a);
                        }
                    }
                    
                };

                xhr.send();
            },
        }
    }
    </script>

    <style>
        .upload{
            padding:2rem 15%;
        }
        .upload .cl-flex input{
            border: none;
        }
        .download{
            color:#3bbaf0;
            border-bottom:1px solid #3bbaf0;
            margin-left:1.43rem;
            cursor: pointer;
        }
    </style>
    