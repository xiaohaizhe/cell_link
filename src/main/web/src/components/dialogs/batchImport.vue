<template>
    <el-dialog
        title="批量导入设备"
        :visible.sync="isVisible" width="40%">
        <div class="upload">
            <div class="flexBtw" style="border-bottom:1px solid;align-items: center;flex-wrap: nowrap;position:relative">
                <span style="color:red">*</span>
                <el-input placeholder="请上传excel地址" v-model="fileName" clearable></el-input>
                <el-input type="file" id="ulFile" style="position:absolute;opacity:0;z-index:999;top:0;width:100%" @change="uploadFile"></el-input>
                <el-button type="primary" class="mgbot-10">上传</el-button>
            </div>
            <div style="margin:2.14rem 0;">
                <span style="color:red">*</span>
                <span class="colorGray font-16">格式参考</span>
                <span class="download" @click="downloadExcel">点击下载excel模板</span>
            </div>
        </div>
        <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="upload">确 定</el-button>
            <el-button @click="isVisible = false">返 回</el-button>
        </span>
    </el-dialog>
</template>

<script>
    import {mapState} from 'vuex'
  
  export default {
        name: 'batchImport',
        data () {
            return{
                file:'',
                fileName:'',
                doUpload:'/api/up/file',
                isVisible:this.dialogVisible
            }
        },
        props:{
            dialogVisible:{
                type:Boolean,
                default:false
            }
        },
        computed:{
            ...mapState([
                'product'
            ])
        },
        watch:{
            dialogVisible(val){
                this.isVisible = this.dialogVisible
            },
            isVisible(val){
                this.$emit('getImpDialogVisible', val)
            }
        },
        methods:{
            async upload(){
                let that = this;
                //loading
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                let formdata = new FormData();
                formdata.append("productId",this.product.id);
                formdata.append("file",this.file);
                new Promise((resolve, reject) => {
                    let requestObj;
                    if (window.XMLHttpRequest) {
                        requestObj = new XMLHttpRequest();
                    } else {
                        requestObj = new ActiveXObject;
                    }
                    requestObj.open('POST', "/dev/api/device/import_excel");
                    requestObj.send(formdata);
                    
                    requestObj.onreadystatechange = () => {
                        loading.close();
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
                        that.isVisible = false;
                    }else{
                        that.$message({
                            message: "上传失败！"+resp.msg,
                            type: 'error'
                        });
                    }
                })
                // let resp = await a();
                // fetch("/dev/api/device/import_excel",{
                //     method:"POST",
                //     headers:{},
                //     body:formdata
                // })
                // let responseJson = await resp.json();
                // if(responseJson.code==0){
                //     const h = this.$createElement;
                //     let msg = "上传成功"+responseJson.data.success+"条，"+"上传失败"+responseJson.data.fail.sum+"条。";
                //     let elements =[]
                //     if(responseJson.data.fail.sum>0){
                //         for(let item of responseJson.data.fail.errNo){
                //             if(item.data.length>0){
                //                 elements.push(h('li', null, '第'+item.data+'条：'+item.msg)) 
                //             }
                            
                //         }
                //     }
                //     this.$msgbox({
                //         title: '提示',
                //         message: h('div', null, [
                //             h('p', null, msg),
                //             h('ul', null, elements)
                //         ]),
                //         showCancelButton: true,
                //         confirmButtonText: '确定',
                //         cancelButtonText: '取消'
                //     })
                //     this.isVisible = false;
                // }else{
                //     this.$message({
                //         message: "上传失败！"+resp.msg,
                //         type: 'error'
                //     });
                // }
                
            },
            uploadFile(val){
                let fileDom = document.getElementById("ulFile");
                this.file = fileDom.files[0];
                this.fileName = this.file.name;
            },
            async downloadExcel(){
                //loading
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                window.URL = window.URL || window.webkitURL;  // Take care of vendor prefixes.
                var xhr = new XMLHttpRequest();
                xhr.open('POST', '/dev/api/device/export_excel', true);
                xhr.responseType = 'blob';

                xhr.onload = function(e) {
                    loading.close();
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
                                a.click();
                                document.body.removeChild(a);
                        }
                    }
                    
                };

                xhr.send();
                // fetch('/dev/api/device/export_excel', {
                //         method: 'POST',
                //         headers: {
                //             'Content-Type': 'application/json',
                //         },
                //         body: '',
                //     })
                //     .then(res => res.blob())
                //     .then(data => {
                //         let blobUrl = window.URL.createObjectURL(data);
                //         this.download(blobUrl);
                //     });
            },
            // download(blobUrl){
            //     const a = document.createElement('a');
            //     a.style.display = 'none';
            //     a.download = 'cell_link_device_model.xls';
            //     a.href = blobUrl;
            //     a.click();
            //     document.body.removeChild(a);
            // }
        }
    }
    </script>

    <style>
        .upload{
            padding:2rem 15%;
        }
        .upload input{
            border: none;
        }
        .download{
            color:#3bbaf0;
            border-bottom:1px solid #3bbaf0;
            margin-left:1.43rem;
            cursor: pointer;
        }
    </style>
    