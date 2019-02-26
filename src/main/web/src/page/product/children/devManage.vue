<template>
    <div>
        <div class="notice center bg-fff">
            <p class="font-16">设备接入</p>
            <p>在接入设备时，请将以下注册码写入到设备中，只用于设备注册</p>
            <p class="flex" style="justify-content:center;">{{code}}
                <i class="copy" @click="copy"></i>
            </p>
        </div>
        <div>
            <div class="searchArea">
                <p style="margin-bottom:30px;">设备数量： 6个<span style="margin-left:30px;">设备接入协议： HTTP</span></p>
                <div class="flexBtw">
                    <el-input placeholder="输入设备ID或者设备名称后按回车键"  v-model="devKey" @keyup.enter.native="changeDevKey()" 
                        clearable style="width:320px;height:36px;"></el-input>
                    <div>
                        <router-link to="/addProduct">
                            <el-button type="primary">+新建设备</el-button>
                        </router-link>
                        <el-button>批量导入设备</el-button>
                        <el-button>导出设备信息</el-button>
                    </div>
                </div>
                
            </div>
            <dev-table :keywords='devKey' :productId='productId' ref="child"></dev-table>
        </div>
    </div>
</template>

<script>
import devTable from 'components/tables/devTable'

export default {
    name: 'devManage',
    data () {
        return {
            devKey:'',
            code:'asdasdad'
        }
    },
    computed:{
    },
    components:{
        'dev-table':devTable
    },
    methods: {
        copy(){
            var Url2 = document.getElementById("inviteCode").innerText;
            var oInput = document.createElement("input");
            oInput.value = Url2;
            document.body.appendChild(oInput);
            oInput.select(); // 选择对象
            document.execCommand("Copy"); // 执行浏览器复制命令
            oInput.className = "oInput";
            oInput.style.display = "none";
            msg("复制成功");
        }
    }

}
</script>

<style>
    .notice{
        padding: 10px 0;
        box-shadow: 0px 2px 7px 0px 
		rgba(71, 85, 88, 0.45);
    }
    .notice.center p{
        margin: 10px 0;
    }
    .searchArea{
        background-color: #fff;
        padding: 30px 40px 25px;
        border: 1px solid #cccccc;
        border-bottom: none;
    }
</style>