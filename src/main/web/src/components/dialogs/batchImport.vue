<template>
    <el-dialog
        title="批量导入设备"
        :visible.sync="isVisible" width="40%">
        <div class="upload">
            <div class="flexBtw" style="border-bottom:1px solid;align-items: center;">
                <span style="color:red">*</span>
                <el-input placeholder="请上传excel地址" v-model="file" clearable></el-input>
                <el-input type="file" id="ulFile" style="position:absolute;opacity:0;z-index:999" @change="uploadFile"></el-input>
                <el-button type="primary" >上传</el-button>
            </div>
            <div style="margin:30px 0;">
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
    import {importExcel,exportExcel} from 'service/getData'
  
  export default {
        name: 'batchImport',
        data () {
            return{
                file:'',
                doUpload:'/api/up/file',
                isVisible:this.dialogVisible,
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
                let resp = await importExcel(45,this.file);//this.product.id
                if(resp.code==0){
                   this.$message({
                        message: "上传成功！",
                        type: 'success'
                    });
                }else{
                    this.$message({
                        message: "上传失败！",
                        type: 'error'
                    });
                }
            },
            uploadFile(){
                let that = this;
                let fileDom = document.getElementById("ulFile");
                var reader = new FileReader();
                reader.onload = function(e)
                {
                    debugger
                    that.file = e.target.result;
                }
                var file = fileDom.files[0];
                reader.readAsDataURL(file);
            },
            async downloadExcel(){
            //     exportExcel('1')
            //     .then(response => response.blob())
            //     .then(blob => {
            //         var url = window.URL.createObjectURL(blob);
            //         var a = document.createElement('a');
            //         a.href = url;
            //         a.download = "filename.xlsx";
            //         a.click();                    
            //     }).catch( err => {
            //                 debugger
            // 　　})
                let resp = await exportExcel();
                resp.blob().then((blob) => {
                    const a = window.document.createElement('a');
                    const downUrl = window.URL.createObjectURL(blob);// 获取 blob 本地文件连接 (blob 为纯二进制对象，不能够直接保存到磁盘上)
                    const filename = response.headers.get('Content-Disposition').split('filename=')[1].split('.');
                    a.href = downUrl;
                    a.download = `${decodeURI(filename[0])}.${filename[1]}`;
                    a.click();
                    window.URL.revokeObjectURL(downUrl);
                    });
                // if(resp.code==0){
                //     this.$message({
                //         message: "模板下载成功！",
                //         type: 'success'
                //     });
                // }
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
    