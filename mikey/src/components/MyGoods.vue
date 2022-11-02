<template>
  <!--商品表单-->
<!--  <el-dialog-->
<!--      v-model="log"-->
<!--      center>-->
<!--    <el-form-->
<!--        :rules="rules"-->
<!--        element-loading-background="rgba(0, 0, 0, 0.8)"-->
<!--        :model="Form">-->
<!--      <el-form-item prop="good_price">-->
<!--        <el-input size="default" type="text" auto-complete="off" v-model="Form.good_price"-->
<!--                  placeholder="请输入新价格"></el-input>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="good_num">-->
<!--        <el-input size="default" type="text" auto-complete="off" v-model="Form.good_num"-->
<!--                  placeholder="请输入库存量"></el-input>-->
<!--      </el-form-item>-->
<!--      <div>-->
<!--        <div class="group">-->
<!--          <el-button-group>-->
<!--            <el-button size="default" type="primary" style="width: 50%;" @click="update">提交</el-button>-->
<!--            <el-button size="default" type="primary" style="width: 50%;" @click="cancel">取消</el-button>-->
<!--          </el-button-group>-->
<!--        </div>-->
<!--      </div>-->
<!--    </el-form>-->
<!--  </el-dialog>-->
  <!--商品列表-->
  <div class="this_firstContainer">
    <div class="this_secondContainer">
      <div>
        <el-button-group>
          <el-button @click="show_now">查看当前商品</el-button>
          <el-button @click="show_end">查看历史商品</el-button>
        </el-button-group>
      </div>
      <el-table v-if="showType === 'create'" :data="goods">
        <el-table-column
            label="商品名称">
          <template v-slot="scope">
            <a style="text-decoration: none; color: #505458" href="javascript:" @click="detail(scope.row.good_id,)">
              {{scope.row.good_name}}</a>
          </template>
        </el-table-column>
        <el-table-column
            label="库存数量">
          <template v-slot="scope">
            <i>{{scope.row.good_num}}</i>
          </template>
        </el-table-column>
        <el-table-column
            label="意向购买人数">
          <template v-slot="scope">
            <i>{{scope.row.good_wants}}</i>
          </template>
        </el-table-column>
        <el-table-column
            label="商品价格">
          <template v-slot="scope">
            <em>¥</em>
            <i>{{parseFloat(scope.row.good_price).toFixed(2)}}</i>
          </template>
        </el-table-column>
        <el-table-column
              label="操作">
          <template v-slot="scope">
            <el-button-group>
<!--              <el-button @click="change_good(scope.row.id)">编辑</el-button>-->
              <el-button v-if="scope.row.good_locker === '0'" @click="lock_good(scope.row.id)">冻结</el-button>
              <el-button v-else @click="unlock_good(scope.row.id)">解冻</el-button>
              <el-button @click="delete_good(scope.row.id)">下架</el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>
      <el-table v-else :data="goods">
        <el-table-column
            label="商品名称">
          <template v-slot="scope">
            <a style="text-decoration: none; color: #505458" href="javascript:" @click="detail(scope.row.good_id,)">
              {{scope.row.good_name}}</a>
          </template>
        </el-table-column>
        <el-table-column
            label="库存数量">
          <template v-slot="scope">
            <i>{{scope.row.good_num}}</i>
          </template>
        </el-table-column>
        <el-table-column
            label="意向购买人数">
          <template v-slot="scope">
            <i>{{scope.row.good_wants}}</i>
          </template>
        </el-table-column>
        <el-table-column
            label="商品价格">
          <template v-slot="scope">
            <em>¥</em>
            <i>{{parseFloat(scope.row.good_price).toFixed(2)}}</i>
          </template>
        </el-table-column>
      </el-table>
      <div class="pageContainer">
        <el-pagination
            background
            @current-change="currentChange"
            @size-change="sizeChange"
            layout="sizes, prev, pager, next, jumper, ->, total, slot"
            :total="total">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
import router from "@/router";
import {postRequest} from "@/utils/api";
import {ElMessage} from "element-plus";

export default {
  name: "MyGoods",
  data() {
    return{
      showType: 'create',
      goods: [{
        id: '',
        good_name: '',
        user_name: '',
        good_num: '',
        good_wants: '',
        good_locker: '',
        good_price: 0,
        good_log: '',
        good_pics: ['','',''],
        good_type: '',
        good_price_log: [
          {date: ''},
          {price: ''},
        ],
      }],
      page: 1,
      size: 10,
      total: 0,
      choose_change_id: 0,
      log: false,
      Form: {
        good_price: '',
        good_num: ''
      },
      rules: {
        good_price: [{required: true, message: '请输入新价格', trigger: 'blur'},
          {pattern: /\d*\.[0-9][0-9]$/, message: '请输入非负二位小数', trigger: 'blur'}],
        good_num: [{required: true, message: '请输入库存量', trigger: 'blur'},
          {pattern: /\d*$/, message: '请输入非负整数', trigger: 'blur'}]
      },
    }
  },
  methods: {
    change_good(id){
      this.log = true
      this.choose_change_id = id
    },
    show_now(){
      this.showType = 'create'
      this.init_good('1')
    },
    show_end(){
      this.showType = 'done'
      this.init_good('0')
    },
    cancel(){
      this.log = false
    },
    detail(id){
      router.push({name: ' good_detail', params: {id: id}});
    },
    init_good(type){
      postRequest("/good/selectAllGoodsByUser", JSON.stringify({page: this.page,
        size: this.size, type: type})).then(resp => {
        if (resp.data["code"] === 200 ){
          this.goods = resp.data["data"]["goods"]
          console.log(this.goods)
        } else {
          ElMessage.error("未知错误")
        }
      })
      this.getTotal(type)
    },
    sizeChange(currentSize) {
      this.size = currentSize;
      this.init_good('1');
      this.init_good('0');
    },
    currentChange(currentPage) {
      this.page = currentPage;
      this.init_good('1');
      this.init_good('0');
    },
    getTotal(type){
      postRequest("/good/selectTotalByUser", JSON.stringify({type: type})).then(resp => {
        if (resp.data["code"] === 200) {
          this.total = resp.data["data"]["total"]
        } else {
          ElMessage.error("未知错误")
        }
      })
    },
    lock_good(id){
      postRequest("/good/lock_good", id).then(resp => {
        if(resp.data["code"] === 200){
          ElMessage.info("操作成功")
          this.init_good('1')
        } else {
          ElMessage.info("权限不足")
        }
      })
    },
    unlock_good(id){
      postRequest("/good/unlock_good", id).then(resp => {
        if(resp.data["code"] === 200){
          ElMessage.info("操作成功")
          this.init_good('1')
        } else {
          ElMessage.info("权限不足")
        }
      })
    },
    delete_good(id){
      postRequest("/good/delete_good_by_id", id).then(resp => {
        if(resp.data["code"] === 200){
          ElMessage.info("操作成功")
          this.init_good('1');
        } else {
          ElMessage.info("权限不足")
        }
      })
    },
    update(){
      const reg1 = new RegExp(/\d*\.[0-9][0-9]$/)
      const reg2 = new RegExp(/\d*$/)
      if ( !(reg1.test(this.Form.good_price))){
        ElMessage.error("商品价格应2位小数")
        return
      }
      if ( !(reg2.test(this.Form.good_num))){
        ElMessage.error("商品库存应为正整数")
        return
      }
      postRequest("/good/change_good", JSON.stringify({id: this.choose_change_id,
        good_num: this.Form.good_num, good_price: this.Form.good_price})).then(resp => {
        if(resp.data["code"] === 200){
          ElMessage.info("操作成功")
          this.init_good();
        } else if (resp.data["code"] === 510 ){
          ElMessage.error("数据库错误")
        } else {
          ElMessage.info("权限不足")
        }
      })
      this.init_good('1')
    }
  },
  created() {
    this.init_good('1')
  }
}
</script>

<style scoped>
  @import "../style/UserPageBody.css";
</style>
