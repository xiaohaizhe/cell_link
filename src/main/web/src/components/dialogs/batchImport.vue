<template>
    <el-dialog
        title="批量导入设备"
        :visible.sync="isVisible" width="40%">
        <div class="upload">
            <div class="flexBtw" style="border-bottom:1px solid;align-items: center;">
                <span style="color:red">*</span>
                <el-input placeholder="请上传excel地址" v-model="fileName" clearable></el-input>
                <el-input type="file" id="ulFile" style="position:absolute;opacity:0;z-index:999" @change="uploadFile"></el-input>
                <el-button type="primary" >上传</el-button>
            </div>
            <div style="margin:30px 0;">
                <span style="color:red">*</span>
                <span class="colorGray font-16">格式参考</span>
                <!-- <a :href="downLoadSrc" download class="downLoad-btn">点击下载excel模板</a> -->

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
                isVisible:this.dialogVisible,
                downLoadSrc:'http://10.0.91.100:30018//api/device/export_excel'
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
                let formdata = new FormData();
                formdata.append("productId",45);
                formdata.append("file",this.file);
                let resp = await fetch("/dev/api/device/import_excel",{
                    method:"POST",
                    headers:{},
                    body:formdata
                })
                let responseJson = await resp.json();
                if(responseJson.code==0){
                    this.$message({
                        message: "上传成功！",
                        type: 'success'
                    });
                    this.isVisible = false;
                }else{
                    this.$message({
                        message: "上传失败！",
                        type: 'error'
                    });
                }
                
            },
            uploadFile(val){
                let fileDom = document.getElementById("ulFile");
                this.file = fileDom.files[0];
                this.fileName = this.file.name;
            },
            async downloadExcel(){
                fetch('/dev/api/device/export_excel', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        body: '',
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
            }
        }
    }
    </script>

    <style>
        .upload{
            padding:0 15%;
        }
        .upload input{
            border: none;
        }
        .download{
            color:#3bbaf0;
            border-bottom:1px solid #3bbaf0;
            margin-left:20px;
            cursor: pointer;
        }
    </style>
    