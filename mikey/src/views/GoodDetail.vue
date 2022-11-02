<template>
  <HeaderLine></HeaderLine>
  <div class="firstContainer">
    <div class="secondContainer">
      <div class="thirdContainer">
        <div>
          <div v-if="good.good_pics[0]!==''" class="imgGroup">
<!--            <div v-show="show" class="large"-->
<!--                 :style="[{backgroundImage:`url(${get_image(pics_pick)})`},largePosition]">-->
<!--            </div>-->
<!--            <div class="middle" ref="target">-->
<!--              <img  width="320" height="320" :src="get_image(pics_pick)" alt="sorry">-->
<!--              <div v-show="show" class="layer" :style="layerPosition"></div>-->
<!--            </div>-->
            <div>
              <ul class="good_detail_ul">
                <li class="good_detail_li" v-for="pic in good.good_pics" v-bind:key="pic">
                  <img class="smallImg" :src="get_image(pic)" @click="change_pick(pic)" alt="sorry">
                </li>
              </ul>
            </div>
          </div>
        <div class="detailGroup">
          <div class="name">{{good.good_name}}</div>
          <div class="type">{{good.good_type}}</div>
          <div class="log">{{good.good_log}}</div>
          <div class="price_log">
            <el-table height="250px" :data="good.good_price_log">
              <el-table-column prop="date" label="价格变更日期"></el-table-column>
              <el-table-column prop="price" label="价格"></el-table-column>
            </el-table>
          </div>
          <div class="div_price">
            <strong class="price" data-popper-placement="1">
              <em>¥</em>
              <i>{{good.good_price.toFixed(2)}}</i>
            </strong>
          </div>
          <div class="name">库存数量:{{good.good_num}}</div>
          <a style="text-decoration: none; color: #505458" href="javascript:" @click="bus_detail(good.user_name)">
            <div class="name">卖家信息:{{good.user_name}}</div>
          </a>
          <div>
            <el-button :disabled="can_buy" size="default" type="primary" style="width: 30%;" @click="Buy">购买商品</el-button>
          </div>
        </div>
      </div>
      </div>
    </div>
  </div>
  <el-dialog
          v-model="log"
          center>
        <el-form
            :rules="rule"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            :model="Form">
          <el-form-item prop="good_price">
            <el-input label="姓名" size="default" type="text" auto-complete="off" v-model="Form.true_name"
                      placeholder="请输入姓名"></el-input>
          </el-form-item>
          <el-form-item prop="good_num">
            <el-input label="电话号码" size="default" type="text" auto-complete="off" v-model="Form.phone_number"
                      placeholder="请输入电话号码"></el-input>
          </el-form-item>
          <div>
            <div class="group">
              <el-button-group>
                <el-button size="default" type="primary" style="width: 50%;" @click="buy_buy">提交</el-button>
                <el-button size="default" type="primary" style="width: 50%;" @click="cancel">取消</el-button>
              </el-button-group>
            </div>
          </div>
        </el-form>
  </el-dialog>
</template>

<script>
import { useRoute } from "vue-router"
import HeaderLine from "@/components/HeaderLine";
import router from "@/router";
import {postRequest} from "@/utils/api";
import {ElMessage} from "element-plus";
// import {reactive, ref, watch} from "vue";
// import {useMouseInElement} from "@vueuse/core";

export default {
  name: "GoodDetail",
  components: {HeaderLine,},
  data() {
    return {
      log: false,
      good: {
        id: '',
        good_name: '',
        user_name: '',
        good_num: '',
        good_locker: '',
        good_price: 0,
        good_log: '',
        good_pics: ['', '', ''],
        good_type: '',
        good_price_log: [
          {date: ''},
          {price: ''},
        ],
      },
      Form:{
        true_name: '',
        phone_number: '',
      },
      rule: {
        true_name: [{required: true, message: '请输入用户名', trigger: 'blur'}],
        phone_number: [{required: true, message: '请输入电话号码', trigger: 'blur'},
          {pattern: /^1[3-9][0-9]{9}$/, message: '电话号码格式错误', trigger: 'blur'}],
      },
      num: 0,
      can_buy: true,
      pics_pick: 0,
      good_id: '',
      autoplayOptions: {
        delay: 500,
        disableOnInteraction: false,
        loop: false,
        pauseOnMouseEnter: true,
        reverseDirection: true
      },
    }
  },
  methods: {
    change_pick(k){
      this.pics_pick = k;
    },
    initGood(){
      postRequest("/good/findGoodById", this.good_id).then(resp => {
        if (resp.data["code"] === 200){
          this.good = resp.data["data"]["good"];
          this.pics_pick = this.good.good_pics[0];
          this.can_buy = !(this.good.good_locker === '0' && this.good.good_num > 0);
        }
    })
    },
    get_image(url){
      return require('../assets/' + url + '.jpg')
    },
    Buy() {
      // postRequest("/order/createOneOrder", JSON.stringify({good_id: this.good_id})).then(resp => {
      //   if (resp.data["code"] === 200){
      //     ElMessage.info("购买成功");
      //     this.initGood();
      //   } else {
      //     ElMessage.error("权限不足,请先登录");
      //     router.push({name: ' login', params: {before_url: `/good_detail${this.good_id}`}})
      //   }
      // })
      this.log = true;
    },
    cancel() {
      this.log = false;
    },
    buy_buy(){
      postRequest("/want/basicCreateWant", JSON.stringify({true_name: this.Form.true_name,
        phone_number: this.Form.phone_number, id: this.good_id})).then(resp => {
        if (resp.data["code"] === 200){
          ElMessage.info("发送意向完毕");
          this.initGood();
        } else {
          ElMessage.error("权限不足,请先登录");
          router.push({name: ' login', params: {before_url: `/good_detail${this.good_id}`}})
        }
      })
    },
    bus_detail(user_name){
      router.push({name: ' bus_detail', params: {bus_id: user_name}});
    },
    translateLeft() {
      if (this.num === 0) {
        return
      }
      if (this.num <= -4) {
        this.num += 4;
      } else if (this.num === -3) {
        this.num += 3;
      } else if (this.num === -2) {
        this.num += 2;
      } else if (this.num === -1) {
        this.num += 1;
      }
    },
    // 点击右移的按钮
    translateRight() {
      if (this.num === - (this.good.good_pics.length - 4)) {
        return
      }
      if ((this.good.good_pics.length + (this.num - 4)) >= 4) {
        this.num -= 4;
      } else if ((this.good.good_pics.length + (this.num - 4)) === 3) {
        this.num -= 3;
      } else if ((this.good.good_pics.length + (this.num - 4)) === 2) {
        this.num -= 2;
      } else if ((this.good.good_pics.length + (this.num - 4)) === 1) {
        this.num -= 1;
      }
    }
  },
  created(){
    this.good_id = useRoute().params.id;
    this.initGood();
  },
  // setup() {
  //   // 当前预览图的索引
  //   const currIndex = ref(0)
  //
  //   // 1. 是否显示遮罩和大图
  //   const show = ref(false)
  //   // 2. 遮罩的坐标(样式)
  //   const layerPosition = reactive({
  //     left: 0,
  //     top: 0
  //   })
  //   // 3. 大图背景定位(样式)
  //   const largePosition = reactive({
  //     backgroundPositionX: 0,
  //     backgroundPositionY: 0
  //   })
  //   // 4. 使用useMouseInElement得到基于元素左上角的坐标和是否离开元素数据
  //   const target = ref(null)
  //   const { elementX, elementY, isOutside } = useMouseInElement(target)
  //   watch([elementX, elementY, isOutside], () => {
  //     // 5. 根据得到数据设置样式数据和是否显示数据
  //     show.value = !isOutside.value
  //     // 计算坐标
  //     const position = { x: 0, y: 0 }
  //
  //     if (elementX.value< 100) position.x = 0
  //     else if (elementX.value > 300) position.x = 200
  //     else position.x = elementX.value - 100
  //
  //     if (elementY.value< 100) position.y = 0
  //     else if (elementY.value > 300) position.y = 200
  //     else position.y = elementY.value - 100
  //     // 给样式赋值
  //     layerPosition.left = position.x + 'px'
  //     layerPosition.top = position.y+ 'px'
  //     largePosition.backgroundPositionX = -2 * position.x + 'px'
  //     largePosition.backgroundPositionY = -2 * position.y + 'px'
  //   })
  //
  //   return { currIndex, show, layerPosition, largePosition, target }
  // }
}
</script>

<style scoped>
  @import "swiper/swiper.min.css";
  @import "../style/HomePageBody.css";
  @import "../style/GoodDetail.css";
</style>
