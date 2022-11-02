<template>
  <div>
    <div>
      <el-form
          :rules="chPwd_rules"
          element-loading-background="rgba(0, 0, 0, 0.8)"
          :model="chPwd_form"
          style="width: 50%">
        <el-form-item prop="password_1" label="原密码">
          <el-input size="default" type="text" auto-complete="off" v-model="chPwd_form.password_1"
                    placeholder="请输入原密码"></el-input>
        </el-form-item>
        <el-form-item prop="password_2" label="新密码">
          <el-input size="default" type="password" auto-complete="off" v-model="chPwd_form.password_2"
                    placeholder="请输入新密码"></el-input>
        </el-form-item>
        <el-form-item prop="password_3" label="重复密码">
          <el-input size="default" type="password" auto-complete="off" v-model="chPwd_form.password_3"
                    placeholder="请重复新密码"></el-input>
        </el-form-item>
        <el-button size="default" type="primary" @click="change_password">确认修改</el-button>
      </el-form>
    </div>
  </div>
</template>

<script>

import {ElMessage} from "element-plus";
import {postRequest} from "@/utils/api";

export default {
  name: "ChangePwd",
  data() {
    return {
      chPwd_rules: {
        password_1: [{required: true, message: '请输入原密码', trigger: 'blur'}],
        password_2: [{required: true, message: '请输入新密码', trigger: 'blur'},
          {pattern: /^.*[A-Z]+.*$/, message: '必须包含大写、小写、数字', trigger: 'blur'},
          {pattern: /^.*[a-z]+.*$/, message: '必须包含大写、小写、数字', trigger: 'blur'},
          {pattern: /^.*[0-9]+.*$/, message: '必须包含大写、小写、数字', trigger: 'blur'},],
        password_3: [{required: true, message: '请重复新密码', trigger: 'blur'}]
      },
      chPwd_form: {
        password_1: '',
        password_2: '',
        password_3: '',
      },
    }
  },
  methods: {
    async change_password() {
      let that = this;
      const reg1 = new RegExp(/^.*[A-Z]+.*$/)
      const reg2 = new RegExp(/^.*[a-z]+.*$/)
      const reg3 = new RegExp(/^.*[0-9]+.*$/)
      if ( !(reg1.test(this.chPwd_form.password2) && reg2.test(this.chPwd_form.password2) && reg3.test(this.chPwd_form.password2)) ){
        ElMessage.error("密码必须包含大写、小写、数字")
        return
      }
      if ( that.form.password_2 !== that.form.password_3 ){
        ElMessage.info("重复密码不一致")
      }
      else {
        const crypto = require("crypto");
        let sha512_1 = crypto.createHash("sha512");
        let sha512_2 = crypto.createHash("sha512");
        let pwd_1 = sha512_1.update(that.form.password_1).digest("hex")
        let pwd_2 = sha512_2.update(that.form.password_2).digest("hex")
        postRequest("/user/change_pwd", JSON.stringify({"pwd1": pwd_1, "pwd2": pwd_2})).then(resp => {
          if (resp.data["code"] === 200)
            ElMessage.info("修改密码成功")
          else
            ElMessage.info("旧密码错误!")
        })
      }
    }
  },
}
</script>

<style scoped>

</style>
