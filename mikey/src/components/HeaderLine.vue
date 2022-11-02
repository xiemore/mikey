<!--这是上边栏的复用代码段-->
<template>
  <!--弹出登录表单按钮-->
  <div class="header">
    <div class="home_and_back">
      <a href="javascript:" @click="Before">{{before_page}}</a>|
      <a href="http://localhost:8080">前往主页</a>
    </div>
    <div class="login_stat" v-if="is_login === true">
      <a href="javascript:" @click="UserHomePage">欢迎您</a> |
      <a href="javascript:" @click="logout">注销</a>
    </div>
    <div class="login_stat" v-else>
      <a href="javascript:" @click="UserHomePage">欢迎您</a> |
      <a href="javascript:" @click="login">登录</a>
    </div>
  </div>
</template>

<script>
import router from "@/router";
import {ElMessage} from "element-plus";
import {postRequest} from "@/utils/api";

export default {
  name: "HeaderLine",
  data() {
    return {
      is_login: false,
      before_page: '<返回',
    }
  },
  methods: {
    login(){
      router.push({name: ' login', params: {before_url: '/'}});
    },
    logout(){
      postRequest("/user/logout").then(resp => {
        if (resp.data["code"] === 200){
          ElMessage.info("退出登录成功")
          sessionStorage.clear()
          this.is_login = false
          router.push("/")
        }else{
          ElMessage.info("未知错误")
        }
      })
    },
    Before(){
      router.back();
    },
    UserHomePage() {
      router.push('/home_page')
    }
  },
  created() {
    let that = this;
    if (sessionStorage.getItem("token")!=null){
      that.is_login = true;
    }
  }
}
</script>

<style scoped>
  @import "../style/Header.css";
</style>
