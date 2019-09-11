<template>
    <el-dialog
        title="新建设备"
        :visible.sync="isVisible" width="40%">
        <div style="padding:0 10%">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                <el-form-item label="设备名称" prop="deviceName">
                    <el-input v-model="ruleForm.deviceName" placeholder="请输入设备名称"></el-input>
                </el-form-item>
                <el-form-item label="设备坐标" prop="latitude">
                    <el-input v-model="ruleForm.latitude" placeholder="请输入纬度">
                        <template slot="append">纬度</template>
                    </el-input>
                </el-form-item>
                <el-form-item label="" prop="longitude">
                    <el-input v-model="ruleForm.longitude" placeholder="请输入经度">
                        <template slot="append">经度</template>
                    </el-input>
                </el-form-item>
                <el-form-item label="设备描述" prop="description">
                    <el-input type="textarea" v-model="ruleForm.description" maxlength="100" show-word-limit placeholder="请输入设备描述"></el-input>
                </el-form-item>
                <el-form-item class="btnRight">
                    <el-button type="primary" @click="submitForm()">确 定</el-button>
                    <el-button @click="isVisible = false">返 回</el-button>
                </el-form-item>
            </el-form>
        </div>
    </el-dialog>
</template>

<script>
    import {mapGetters} from 'vuex'
    import { updateDev } from 'api/dev'

  export default {
        name: 'editDevice',
        data () {
            return{
                isVisible:this.dialogVisible,
                ruleForm: {
                    deviceName:'',
                    description:'',
                    latitude:'',
                    longitude:''
                },
                rules: {
                    description:[
                        { max: 100, message: '设备描述的最大长度为100', trigger: 'blur' }
                    ],
                    deviceName:[
                        { required: true, message: '请输入设备名称', trigger: 'blur' },
                        { min: 4, max: 10, message: '长度在 4 到 10 个字符', trigger: 'blur' }
                    ],
                    latitude:[
                        { required: true, message: '请输入纬度', trigger: 'blur' },
                        { pattern:  /^(\-|\+)?([0-8]?\d{1}\.\d{0,6}|90\.0{0,6}|[0-8]?\d{1}|90)$/, message: '请输入正确的纬度', trigger: 'blur' }
                    ],
                    longitude:[
                        { required: true, message: '请输入经度', trigger: 'blur' },
                        { pattern: /^(\-|\+)?(((\d|[1-9]\d|1[0-7]\d|0{1,3})\.\d{0,6})|(\d|[1-9]\d|1[0-7]\d|0{1,3})|180\.0{0,6}|180)$/, message: '请输入正确的经度', trigger: 'blur' }
                    ]
                }
            }
        },
        props:{
            dialogVisible:{
                type:Boolean,
                default:false
            }
        },
        watch:{
            dialogVisible(val){
                this.isVisible = this.dialogVisible
            },
            isVisible(val){
                this.$emit('devDialogVisible', val)
            }
        },
        computed:{
            ...mapGetters([
                'activeScene'
            ])
        },
        mounted(){
            this.ruleForm.description = this.activeScene.description;
            this.ruleForm.deviceName = this.activeScene.deviceName;
            this.ruleForm.latitude = this.activeScene.latitude;
            this.ruleForm.longitude = this.activeScene.longitude;
        },
        methods:{
            async submit(){
                let resp = await updateDev({deviceId:this.activeScene.deviceId,...this.ruleForm,devicesn:this.activeScene.devicesn});
                this.$message({
                    message: "修改成功！",
                    type: 'success'
                });
                this.isVisible = false;
                this.$store.dispatch('user/setScene',{deviceId:this.activeScene.deviceId})
            },
            submitForm() {
                if (this.$refs.ruleForm.validate()) {
                    this.submit();
                }else{
                    console.log('error submit!!');
                    return false;
                }
            },
        }
    }
    </script>

    <style>
    </style>
    