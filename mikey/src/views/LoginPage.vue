<template>
  <!--登录表单-->
  <el-form
      class="loginContainer"
      :rules="login_rules"
      element-loading-background="rgba(0, 0, 0, 0.8)"
      :model="Form">
    <h3 class="loginTitle">购物平台登录</h3>
    <el-form-item prop="username" label="用户名">
      <el-input size="default" type="text" auto-complete="off" v-model="Form.username"
                placeholder="请输入用户名"></el-input>
    </el-form-item>
    <el-form-item prop="password" label="密码">
      <el-input size="default" type="password" auto-complete="off" v-model="Form.password"
                placeholder="请输入密码"></el-input>
    </el-form-item>
    <div>
      <div class="signButton">
        <el-button @click="show_sign">没有账号,注册</el-button>
      </div>
      <div class="button_right">
        <el-button class="loginButton" size="default" type="primary" @click="LoginWithType">登录</el-button>
      </div>
    </div>
  </el-form>
  <!--注册表单，使用dialog并添加监听-->
  <el-dialog
      class="signForm"
      v-model="sign_dialog_view"
      center>
    <el-form
        element-loading-background="rgba(0, 0, 0, 0.8)"
        :rules="sign_rule"
        :model="sign_Form">
      <h3 class="loginTitle">购物平台注册</h3>
      <span>
      <el-form-item prop="user_name" label="用户名">
        <el-input size="default" type="text" auto-complete="off" v-model.lazy="sign_Form.user_name"
                  placeholder="请输入用户名"></el-input>
        </el-form-item>
        {{tip}}
      </span>
      <el-form-item prop="password1" label="密码">
        <el-input size="default" type="password" auto-complete="off" v-model="sign_Form.password1"
                  placeholder="请输入密码"></el-input>
      </el-form-item>
      <el-form-item prop="password2" label="重复密码">
        <el-input size="default" type="password" auto-complete="off" v-model="sign_Form.password2"
                  placeholder="请输入重复密码"></el-input>
      </el-form-item>
      <el-form-item prop="phone_number" label="联系电话">
        <el-input size="default" type="text" auto-complete="off" v-model="sign_Form.phone_number"
                  placeholder="请输入联系电话"></el-input>
      </el-form-item>
      <el-form-item prop="ni_name" label="昵称">
        <el-input size="default" type="text" auto-complete="off" v-model="sign_Form.ni_name"
                  placeholder="请输入昵称"></el-input>
      </el-form-item>
      <div>
        <el-button-group class="loginButton">
          <el-button size="default" :disabled="can_sign" type="primary" @click="SignupCus">注册成为客户</el-button>
          <el-button size="default" :disabled="can_sign" type="primary" @click="SignupBus">注册成为商家</el-button>
        </el-button-group>
      </div>
    </el-form>
  </el-dialog>
</template>

<script>
import {postRequest} from "@/utils/api";
import {ElMessage} from "element-plus";
import router from "@/router";
import {useRoute} from "vue-router";
import {watch} from "vue";

export default {
  name: "LoginPage",
  data() {
    return {
      sign_dialog_view: false,
      Form: {
        username: '',
        password: ''
      },
      login_rules: {
        username: [{required: true, message: '请输入用户名', trigger: 'blur'}],
        password: [{required: true, message: '请输入密码', trigger: 'blur'}]
      },
      to_url: '',
      tip: '',
      can_sign: false,
      sign_Form: {
        user_name: '',
        password1: '',
        password2: '',
        phone_number: '',
        ni_name: '',
      },
      sign_rule: {
        user_name: [{required: true, message: '请输入用户名', trigger: 'blur'}],
        password1: [{required: true, message: '请输入密码', trigger: 'blur'},
          {pattern: /^.*[A-Z]+.*$/, message: '必须包含大写、小写、数字', trigger: 'blur'},
          {pattern: /^.*[a-z]+.*$/, message: '必须包含大写、小写、数字', trigger: 'blur'},
          {pattern: /^.*[0-9]+.*$/, message: '必须包含大写、小写、数字', trigger: 'blur'},
        ],
        password2: [{required: true, message: '请输入重复密码', trigger: 'blur'}],
        phone_number: [{required: true, message: '请输入电话号码', trigger: 'blur'},
          {pattern: /^1[3-9][0-9]{9}$/, message: '电话号码格式错误', trigger: 'blur'}],
        ni_name: [{required: true, message: '请输入昵称', trigger: 'blur'}]
      },
    }
  },
  methods:{
    show_sign(){
      this.sign_dialog_view = true;
    },
    LoginWithType(){
      let that = this;
      const crypto = require("crypto");
      const sha512 = crypto.createHash("sha512");
      let pwd1 = sha512.update(this.Form.password);
      pwd1 = pwd1.digest("hex");
      postRequest("/user/login", JSON.stringify({"user_name": that.Form.username, "pwd": pwd1}))
          .then(resp => {
            if (resp.data["code"] === 200){
              sessionStorage.clear()
              sessionStorage.setItem("token", resp.data["data"]["user"])
              sessionStorage.setItem("type", resp.data["data"]["type"])
              ElMessage.info("登陆成功")
              router.push(this.to_url)
            }
            else {
              ElMessage.error("登录失败!")
            }
          })
    },
    //客户注册
    SignupCus() {
      const crypto = require("crypto");
      const sha512 = crypto.createHash("sha512");
      const reg1 = new RegExp(/^.*[A-Z]+.*$/)
      const reg2 = new RegExp(/^.*[a-z]+.*$/)
      const reg3 = new RegExp(/^.*[0-9]+.*$/)
      const reg4 = new RegExp(/^1[3-9][0-9]{9}$/)
      if ( !(reg1.test(this.sign_Form.password1) && reg2.test(this.sign_Form.password1) && reg3.test(this.sign_Form.password1)) ){
        ElMessage.error("密码必须包含大写、小写、数字")
        return
      }
      if ( !(reg4.test(this.sign_Form.phone_number))){
        ElMessage.error("电话号码格式错误")
        return
      }
      if (this.sign_Form.password1 !== this.sign_Form.password2) {
        ElMessage.info("密码不一致")
      } else {
        let pwd1 = sha512.update(this.sign_Form.password1);
        pwd1 = pwd1.digest("hex");
        postRequest("/user/signCus", JSON.stringify({
          "user_name": this.sign_Form.user_name,
          "pwd": pwd1, "ni_name": this.sign_Form.ni_name, "phone_number": this.sign_Form.phone_number,
        })).then(resp => {
          if (resp.data["code"] === 200) {
            this.sign_dialog_view = false;
            ElMessage.info("注册成功");
          } else {
            ElMessage.error("注册失败,用户名已存在")
          }
        })
      }
    },
    //商家注册
    SignupBus() {
      const crypto = require("crypto");
      const sha512 = crypto.createHash("sha512");
      if ( this.sign_Form.password1 === '' || this.sign_Form.phone_number === ''){
        ElMessage.error("请填写必要信息")
      }
      const reg1 = new RegExp(/^.*[A-Z]+.*$/)
      const reg2 = new RegExp(/^.*[a-z]+.*$/)
      const reg3 = new RegExp(/^.*[0-9]+.*$/)
      const reg4 = new RegExp(/^1[3-9][0-9]{9}$/)
      if ( !(reg1.test(this.sign_Form.password1) && reg2.test(this.sign_Form.password1) && reg3.test(this.sign_Form.password1)) ){
        ElMessage.error("密码必须包含大写、小写、数字")
        return
      }
      if ( !(reg4.test(this.sign_Form.phone_number))){
        ElMessage.error("电话号码格式错误")
        return
      }
      if (this.sign_Form.password1 !== this.sign_Form.password2) {
        ElMessage.info("密码不一致")
      } else {
        let pwd1 = sha512.update(this.sign_Form.password1);
        pwd1 = pwd1.digest("hex");
        postRequest("/user/signBus", JSON.stringify({
          "user_name": this.sign_Form.user_name,
          "pwd": pwd1, "ni_name": this.sign_Form.ni_name, "phone_number": this.sign_Form.phone_number,
        })).then(resp => {
          if (resp.data["code"] === 200) {
            this.sign_dialog_view = false;
            ElMessage.info("注册成功");
          } else {
            ElMessage.error("注册失败,用户名已存在")
          }
        })
      }
    }
  },
  mounted(){
    //监听用户名是否存在
    let that = this;
    watch(() => that.sign_Form.user_name, (newVal) => {
      postRequest("/user/check_user_name", JSON.stringify({"user_name": newVal})).then(resp=>{
        if (resp.data["code"] === 200){
          this.tip = "用户名可用"
          this.can_sign = false;
        } else {
          this.tip = "用户名已存在"
          this.can_sign = true;
        }
      })
    }, {
      deep: true,
      immediate: true
    })
  },
  created() {
    this.to_url = useRoute().params.before_url;
  }

}
</script>

<style scoped>
  @import "../style/LoginPage.css";
</style>
