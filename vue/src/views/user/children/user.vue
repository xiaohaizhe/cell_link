<template>
    <el-row class="fullHeight ">
        <el-col :span="5" class="mgR-20 bgWhite clBody cl-flex alignCenter directColumn fullHeight ">
            <div class="headIcon mgbot-20">
                <img :src="headIcon"/>
            </div>
            <p class="font-18 font-bold colorDBlue mgbot-20">用户名</p>
            <p class="font-16 colorGray mgbot-10">{{user.name}}</p>
            <p class="font-16 colorGray">{{user.phone}}</p>
        </el-col>
        <el-col :span="18" class="bgWhite clBody fullHeight ">
            <div class="security">
                <p class="justifyBet cl-flex" style="margin:0;">
                    <span class="font-14">
                        登录密码<span class="colorGray font-14">建议您定期更改密码，以保证账号安全</span>
                    </span>
                    <router-link to="/user/editPwd">
                        <el-button type="text" class="font-14">修改密码</el-button>
                    </router-link>
                    
                </p>
                <p class="justifyBet cl-flex">
                    <span class="font-14">
                        绑定手机<span class="colorGray font-14">您已绑定手机：{{user.phone}}</span>
                    </span>
                    <router-link to="/user/bindtel">
                        <el-button type="text" class="font-14">手机换绑</el-button>
                    </router-link>
                </p>
                <p class="justifyBet cl-flex">
                    <span class="font-14">
                        绑定邮箱
                        <span class="colorGray font-14" v-if="!user.email">未绑定</span>
                        <span class="font-14 colorGray" v-if="user.email">您已绑定邮箱：{{user.email}}</span>
                    </span>
                    <span class="cl-flex">
                        <el-button type="text" v-if="!user.email" class="font-14" @click="$router.push('/user/bindEmail/0')">立即绑定</el-button>
                        <el-button type="text" v-if="user.email && user.isVertifyEmail==0" class="font-14" @click="$router.push('/user/bindEmail/0')">邮箱激活</el-button>
                        <el-button type="text" v-if="user.isVertifyEmail==1 || user.email && user.isVertifyEmail==0" class="font-14" @click="$router.push('/user/bindEmail/1')">邮箱换绑</el-button>
                    </span>
                </p>
            </div>
        </el-col>
    </el-row>
</template>

<script>
    import { mapGetters } from 'vuex'
    
    export default {
        name: 'user',
        data () {
        return {
            headIcon:require('assets/headIcon.svg')
            }
        },
        computed: {
            ...mapGetters([
                'user'
            ])
        },
        components:{
        },
        mounted(){
        },
        methods:{
        }
    }
</script>
<style scoped>
    .headIcon{
        background-color: #F1F5F8;
        border-radius: 50%;
        padding: 20px;
    }
    .security{
        padding: 15px 85px;
    }
    .security .colorGray{
        margin-left: 2.14rem;
        line-height: 40px;
    }
    .security .justifyBet{
        align-items: center;
        margin-top: 2.14rem;
        border-bottom: 1px dotted #cccccc;
    }
</style>