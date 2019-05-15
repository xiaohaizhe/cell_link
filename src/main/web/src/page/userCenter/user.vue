<template>
    <div>
        <cl-header headColor="#181818"></cl-header>
        <sub-header title="个人中心"></sub-header>
        <div class="mainContent">
            <div class="personalInfo cl-flex">
                <div class="headIcon"></div>
                <div>
                    <p>用户名<span>{{user.userName}}</span></p>
                    <p>用户ID<span>{{user.userId}}</span></p>
                    <p>手机号<span>{{phoned}}</span></p>
                </div>
            </div>
            <div class="security bg-fff">
                <p class="flexBtw" style="margin:0;">
                    <span>
                        登录密码<span class="colorGray">建议您定期更改密码，以保证账号安全</span>
                    </span>
                    <router-link to="/editpsw">
                        <el-button type="text">修改密码</el-button>
                    </router-link>
                    
                </p>
                <p class="flexBtw">
                    <span>
                        绑定手机<span class="colorGray">您已绑定手机：{{phoned}}</span>
                    </span>
                    <router-link to="/bindtel">
                        <el-button type="text">手机换绑</el-button>
                    </router-link>
                </p>
                <p class="flexBtw">
                    <span>
                        绑定邮箱<span class="colorGray" v-if="user.isvertifyemail==0">未绑定</span><span class="colorGray" v-if="user.isvertifyemail==1">已绑定</span>
                    </span>
                    <router-link to="/bindEmail">
                        <el-button type="text" v-if="user.isvertifyemail==0">立即绑定</el-button>
                    </router-link>
                </p>
            </div>
        </div>
    </div>
</template>

<script>
    import headTop from 'components/header/head'
    import subHead from 'components/subHeader/subHeader'
    import {mapState} from 'vuex'

    export default {
        name: 'user',
        data () {
            return {
            }
        },
        components:{
            'cl-header':headTop,
            'sub-header':subHead
        },
        computed:{
            ...mapState([
                'user'
            ]),
            phoned: function(){
                return this.user.phone.substr(0,3) + '****' + this.user.phone.substr(7,4) || '';
            }
        },
        methods: {
        }

    }
</script>

<style>
    .personalInfo{
        height: 200px;
        padding-left: 90px;
	    background-color: #07aaa5;
    }
    .personalInfo p{
        margin: 10px 0;
        color: white;
    }
    .personalInfo span{
        margin-left: 1.43rem;
    }
    .security{
        padding: 90px;
    }
    .security .colorGray{
        margin-left: 2.14rem;
        line-height: 40px;
    }
    .security .flexBtw{
        align-items: center;
        margin-top: 2.14rem;
        border-bottom: 1px dotted #cccccc;
    }
</style>
