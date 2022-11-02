<template>
  <HeaderLine></HeaderLine>
  <div class="this_firstContainer">
    <router-view class="this_secondContainer" />
  </div>
  <div class="user_page_sidebar">
<!--    <el-table :data="action_list">-->
<!--      <el-table-column>-->
<!--        <template v-slot="scope">-->
<!--          <a style="text-decoration: none; color: #505458" href="javascript:" @click="push_url(scope.row.url_name)">-->
<!--            {{scope.row.action_name}}</a>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--    </el-table>-->
    <table>
      <tr>
        <td>
          <a style="text-decoration: none; color: #505458" href="javascript:" @click="push_url('/home_page/change_pwd')">
            修改密码</a>
        </td>
      </tr>
      <tr>
        <td>
          <a style="text-decoration: none; color: #505458" href="javascript:" @click="push_url('/home_page/my_goods')">
            我的商品</a>
        </td>
      </tr>
      <tr>
        <td>
          <a style="text-decoration: none; color: #505458" href="javascript:" @click="push_url('/home_page/my_wants')">
            查看意向</a>
        </td>
      </tr>
      <tr>
        <td>
          <a style="text-decoration: none; color: #505458" href="javascript:" @click="push_url('/home_page/my_base_order')">
            查看订单</a>
        </td>
      </tr>
      <tr v-if="total === 0">
        <td>
          <a style="text-decoration: none; color: #505458" href="javascript:" @click="push_url('/home_page/add_good')">
            上架商品</a>
        </td>
      </tr>
    </table>
  </div>
</template>
<script>
import HeaderLine from "@/components/HeaderLine";
import router from "@/router";
import {ElMessage} from "element-plus";
import {postRequest} from "@/utils/api";
export default {
  name: "UserHomePage",
  components: {HeaderLine},
  data() {
    return{
      total: 0,
      action_list: [
        {
          id: 0,
          url_name: '',
          action_name: '',
        }
      ]
    }
  },
  methods: {
    push_url(url){
      router.push(url)
    },
    init_total(){
      postRequest("/good/selectTotal", JSON.stringify({type: '',
        good_name: ''})).then(resp => {
        if (resp.data["code"] === 200) {
          this.total = resp.data["data"]["total"]
        } else {
          ElMessage.error("未知错误")
        }
      })
    }
  },
  created() {
    postRequest("/user/selectActionsByUser").then(resp => {
      if (resp.data["code"] === 200){
        this.action_list = resp.data["data"]["actions"];
      } else {
        ElMessage.error("权限不足,请先登录");
        router.push({name: ' login', params: {before_url: '/home_page'}})
      }
      router.push("/home_page/hello");
    })
    this.init_total()
  }
}
</script>

<style scoped>
  @import "../style/UserPageBody.css";
</style>
