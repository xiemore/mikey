<template>
  <div style="margin: auto; width: 1000px">
    <el-carousel style="width: 1000px; margin: auto;" height="500px">
      <el-carousel-item v-for="img in imgList" :key="img">
        <h3><img class="img_1" style="height: 500px; width: 1000px" v-bind:src="img.url" alt="sorry"></h3>
      </el-carousel-item>
    </el-carousel>
  </div>
</template>
<script>
export default {
  name: 'HomeBanner',
  data() {
    return {
      imgList: [
        { url: require('../assets/homeBanner/banner_1.jpg') },
        { url: require('../assets/homeBanner/banner_2.jpg') },
        { url: require('../assets/homeBanner/banner_3.jpg') },
        { url: require('../assets/homeBanner/banner_4.jpg') },
        { url: require('../assets/homeBanner/banner_5.jpg') },
      ]
    }
  },
}
</script>

<style scoped>
/* @import '../style/HomeBanner.css'; */

.home_banner {
  height: 500px;
  position: relative;
  left: 0;
  top: 0;
  z-index: 98;
  text-align: center;
}
.img_1 {
  position: absolute;
  left: 0;
  top: 0;
  margin: auto auto;
  /* 淡入淡出过渡时间 */
}
</style>
