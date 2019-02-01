<template>
    <div>
        <cl-header headColor="#181818"></cl-header>
        <sub-header title="个人中心"></sub-header>
        <div class="mainContent">
            <div class="personalInfo">
                <div class="headIcon"></div>
                <div>
                    <p>用户名</p>
                    <p>用户ID</p>
                    <p>手机号</p>
                </div>
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
                test:{
                    "msg":"成功",
                    "code":0,
                    "data":{
                        "createTime":"2019-01-02 14:25:23",
                        "defaultKey":"74be16979710d4c4e7c6647856088456",
                        "id":12,
                        "islogin":1,
                        "isvalid":1,
                        "isvertifyemail":0,
                        "isvertifyphone":1,
                        "name":"test1",
                        "phone":"15605162862",
                        "pwd":"81dc9bdb52d04dc20036dbd8313ed055",
                        "type":0
                    }
                }
            }
        },
        components:{
            'cl-header':headTop,
            'sub-header':subHead
        },
        computed:{
            ...mapState([
                'userName',
            ]),
        },
        methods: {

        }

    }
</script>

<style>
    .personalInfo{
        height: 200px;
	    background-color: #07aaa5;
    }
</style>
