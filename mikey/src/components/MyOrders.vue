<template>
  <div class="firstContainer">
    <div class="secondContainer">
      <div>
        <el-button-group >
          <el-button @click="show_now">查看当前订单</el-button>
          <el-button @click="show_end">查看历史订单</el-button>
        </el-button-group>
      </div>
      <el-table v-if="showType === 'create'" :data="wants">
        <el-table-column
            label="商品名称">
          <template v-slot="scope">
            <a style="text-decoration: none; color: #505458" href="javascript:" @click="detail(scope.row.good_id,
            'good')">{{scope.row.good_name}}</a>
          </template>
        </el-table-column>
        <el-table-column
            prop="true_name"
            label="交易人用户名">
        </el-table-column>
        <el-table-column
            prop="phone_number"
            label="交易人联系方式">
        </el-table-column>
        <el-table-column
            label="操作">
          <template v-slot="scope">
            <el-button-group>
              <el-button @click="cancel(scope.row.good_id, scope.row.id)">取消订单</el-button>
              <el-button v-if="userType === '商家'" @click="complete(scope.row.good_id, scope.row.id)">完成订单</el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>
      <el-table v-else :data="wants">
        <el-table-column
            label="商品名称">
          <template v-slot="scope">
            <a style="text-decoration: none; color: #505458" href="javascript:" @click="detail(scope.row.good_id,
            'good')">{{scope.row.good_name}}</a>
          </template>
        </el-table-column>
        <el-table-column
            prop="true_name"
            label="交易人用户名">
        </el-table-column>
        <el-table-column
            prop="phone_number"
            label="交易人联系方式">
        </el-table-column>
        <el-table-column
            prop="is_complete"
            label="订单状态(1代表已取消,2代表已完成)">
        </el-table-column>
      </el-table>
      <div class="pageContainer">
        <el-pagination
            background
            @current-change="currentChange"
            @size-change="sizeChange"
            :current-page="page"
            :page-size="size"
            :total="total"
            :page-sizes="[10, 20, 30, 40]"
            layout="sizes, prev, pager, next, jumper, ->, total, slot">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
import {postRequest} from "@/utils/api";
import {ElMessage} from "element-plus";
import router from "@/router";

export default {
  name: "MyOrders",
  data() {
    return {
      wants:[{
        id: '',
        good_name: '',
        true_name: '',
        phone_number: '',
        good_id: '',
        is_chosen: '',
        is_complete: '',
      }],
      username: '',
      userType: '',
      showType: 'create',
      page: 1,
      size: 10,
      total: 0,
    }
  },
  methods: {
    init_orders_now(){
      postRequest("/want/lookOrNow", JSON.stringify({page: this.page, size:this.size})).then(resp => {
        if (resp.data["code"] === 200){
          this.wants = resp.data["data"]["wants"]
        } else {
          ElMessage.info("权限不足")
        }
      })
      this.init_total_created()
    },
    init_orders_done(){
      postRequest("/want/lookOrDone", JSON.stringify({page: this.page, size:this.size})).then(resp => {
        if (resp.data["code"] === 200){
          this.wants = resp.data["data"]["wants"]
        } else {
          ElMessage.info("权限不足")
        }
      })
      this.init_total_done()
    },
    init_type() {
      this.userType = sessionStorage.getItem("type").toString()
    },
    init_total_created(){
      postRequest("/want/lookOrNowTotal").then(resp => {
        if (resp.data["code"] === 200) {
          this.total = resp.data["data"]["total"]
        } else {
          ElMessage.error("未知错误")
        }
      })
    },
    init_total_done(){
      postRequest("/want/lookOrDoneTotal").then(resp => {
        if (resp.data["code"] === 200) {
          this.total = resp.data["data"]["total"]
        } else {
          ElMessage.error("未知错误")
        }
      })
    },
    sizeChange(currentSize) {
      this.size = currentSize;
      if (this.showType === 'create')
        this.init_orders_now()
      else
        this.init_orders_done()
    },
    currentChange(currentPage) {
      this.page = currentPage;
      if (this.showType === 'create')
        this.init_orders_now()
      else
        this.init_orders_done()
    },
    show_now(){
      this.showType = 'create'
      this.init_orders_now()
    },
    show_end(){
      this.showType = 'done'
      this.init_orders_done()
    },
    detail(id){
      router.push({name: ' good_detail', params: {id: id}});
    },
    cancel(good_id, order_id){
      postRequest("/want/basicFailWant", JSON.stringify({"id": order_id, "good_id": good_id})).then(resp => {
        if (resp.data["code"] === 200){
          ElMessage.info("操作成功")
          this.init_orders_now()
        } else {
          ElMessage.info("权限不足")
        }
      })

    },
    complete(good_id, order_id){
      postRequest("/want/basicCompleteWant", JSON.stringify({"id": order_id, "good_id": good_id})).then(resp => {
        if (resp.data["code"] === 200){
          ElMessage.info("操作成功")
          this.init_orders_now()
        } else {
          ElMessage.info("权限不足")
        }
      })

    },
  },
  created() {
    this.init_orders_now()
    this.init_type()
  }
}
</script>

<style scoped>
  @import "../style/UserPageBody.css";
</style>
