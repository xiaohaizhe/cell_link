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
                let resp = await importExcel(this.file,this.product.id);
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
                let fileDom = document.getElementById("ulFile");
                let files = fileDom.files;       
                debugger 
            },
            downloadExcel(){
                exportExcel().then().catch( err => {
                    debugger
            　　})
                // let resp = await exportExcel();
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
    