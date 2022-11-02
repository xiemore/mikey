<template>
  <HeaderLine></HeaderLine>
  <!--商品列表-->
  <div class="firstContainer">
    <HomeBanner></HomeBanner>
    <div class="secondContainer">
      <div class="sidebar">
        <el-table :data="type_list">
          <el-table-column>
            <template v-slot="scope">
              <a href="javascript:" @click="search_by_type(scope.row.type_name)">
                {{scope.row.type_name}}</a>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="thirdContainer">
        <div>
<!--          <div class="myInput" >-->
<!--            <el-input class="myText" type="text" auto-complete="off" v-model="search_Form.search_name" placeholder="输入关键字搜索"></el-input>-->
<!--            <el-button class="myB" size="default" type="primary" @click="selectAllGoods">搜索</el-button>-->
<!--          </div>-->
          <ul v-if="goods.length !== 0">
              <li class="liType" v-for="good in goods" v-bind:key="good.id">
                <a href="javascript:" @click="good_detail(good.id)">
                  <div><img v-if="good.good_pics[0]!==''" width="240" height="240" :src="getImg(good.good_pics[0])" alt="sorry"></div>
                  <div class="div_name">{{good.good_name}}</div>
                  <div class="div_price">
                    <strong class="price" data-popper-placement="1">
                      <em>¥</em>
                      <i>{{good.good_price.toFixed(2)}}</i>
                    </strong>
                  </div>
                  <div class="div_name">存货:{{good.good_num}}</div>
                </a>
                <a href="javascript:" @click="user_detail(good.user_name)">
                  <div class="div_name">商家信息:{{good.user_name}}</div>
                </a>
              </li>
            </ul>
          <div v-else>抱歉，没有上架中的商品哦</div>
        </div>
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
  </div>
</template>


<script>
  import HeaderLine from "@/components/HeaderLine";
  import HomeBanner from "@/components/HomeBanner";
  import {postRequest} from "@/utils/api";
  import {ElMessage} from "element-plus";
  import router from "@/router";
  export default {
    name: "HomePage",
    components: {HeaderLine, HomeBanner, },
    data() {
      return {
        goods: [
          {
            id: '',
            good_name: '',
            user_name: '',
            good_num: '',
            good_price: 0,
            good_pics: ['','',''],
          }
        ],
        //分页参数
        total: 0,
        page: 1,
        size: 10,
        search_Form: {
          search_name: ''
        },
        type_list:[
          {
            id: '',
            type_name: ''
          }
        ],
        search_type: '',
      }
    },
    methods: {
      selectAllTypes() {
        postRequest("/type/selectAllType").then(resp => {
          if (resp.data["code"] === 200) {
            this.type_list = resp.data["data"]["types"]
          }
        })
      },
      selectTotal(){
        postRequest("/good/selectTotal", JSON.stringify({type: this.search_type,
          good_name: this.search_Form.search_name})).then(resp => {
          if (resp.data["code"] === 200) {
            this.total = resp.data["data"]["total"]
          } else {
            ElMessage.error("未知错误")
          }
        })
      },
      selectAllGoods() {
        postRequest("/good/selectAllGoods", JSON.stringify({type: this.search_type,
          good_name: this.search_Form.search_name, size: this.size, page: this.page})).then(resp => {
          if (resp.data["code"] === 200) {
            this.goods = resp.data["data"]["goods"]
          } else {
            ElMessage.error("未知错误")
          }
        })
        this.selectTotal()
      },
      search_by_type(type){
        this.search_type = type
        if ( type === "全部" )
          this.search_type = ''
        this.selectAllGoods()
      },
      good_detail(id) {
        router.push({name: ' good_detail', params: {id: id}});
      },
      user_detail(user_name) {
        router.push({name: ' user_detail', params: {user_name: user_name}});
      },
      getImg(url) {
        return require('../assets/' + url + '.jpg');
      },
      sizeChange(currentSize) {
        this.size = currentSize;
        this.selectAllGoods();
      },
      currentChange(currentPage) {
        this.page = currentPage;
        this.selectAllGoods();
      },
    },
    created() {
      this.selectAllGoods();
      this.selectAllTypes();
    }
  }
</script>

<style scoped>
  @import "../style/HomePageBody.css";
  @import "../style/HomePage.css";
</style>
