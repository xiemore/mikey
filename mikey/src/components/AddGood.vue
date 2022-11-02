<template>
  <el-form :model="Form" :rules="rules" style="width: 50%">
    <el-form-item prop="good_name" label="商品名称">
      <el-input size="default" type="text" auto-complete="off" v-model="Form.good_name"
                placeholder="请输入商品名"></el-input>
    </el-form-item>
<!--    <el-form-item prop="good_type" label="品类">-->
<!--      <el-select v-model="Form.good_type" placeholder="请选择一种品类">-->
<!--        <el-option v-for="types in my_type" :key="types.id" :label="types.type_name" :value="types.id">-->
<!--        </el-option>-->
<!--      </el-select>-->
<!--    </el-form-item>-->
    <el-form-item prop="good_price" label="价格">
      <el-input size="default" type="text" auto-complete="off" v-model="Form.good_price"
                placeholder="请输输入价格"></el-input>
    </el-form-item>
    <el-form-item prop="good_num" label="库存">
      <el-input :disabled="true" size="default" type="text" auto-complete="off" v-model="Form.good_num"
                placeholder="请输入库存量"></el-input>
    </el-form-item>
    <el-form-item prop="good_log" label="描述">
      <el-input size="default" type="textarea" auto-complete="off" v-model="Form.good_log"
                placeholder="请输入描述"></el-input>
    </el-form-item>
    <el-form-item label="上传图片">
      <el-upload
          ref="upload"
          accept="image/png,image/gif,image/jpg,image/jpeg"
          list-type="picture-card"
          :limit=limitNum
          :auto-upload="false"
          :on-exceed="handleExceed"
          :file-list="file_list"
          :before-upload="handleBeforeUpload"
          :on-preview="handlePictureCardPreview"
          :on-remove="handleRemove"
          :on-change="handle_imageChange">
        <i class="el-icon-plus"></i>
      </el-upload>
      <el-dialog v-model:visible="dialogVisible">
        <img width="100%" :src="dialogImageUrl" alt="">
      </el-dialog>
    </el-form-item>
    <el-form-item>
      <el-button size="small" type="primary" @click="uploadFile">上架</el-button>
      <el-button size="small">取消</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import {ElMessage} from "element-plus";
import axios from "axios";
import {postRequest} from "@/utils/api";
import router from "@/router";

export default {
  name: "AddGood",
  data() {
    return{
      total: 0,
      dialogImageUrl: '',
      dialogVisible: false,
      formLabelWidth: '80px',
      limitNum: 5,
      file_list: [],
      username: '',
      Form: {
        good_name: '',
        good_price: '',
        good_num: 1,
        good_log:'',
        good_type: 1
      },
      my_type: [
        {
          id: '',
          type_name: ''
        }
      ],
      rules: {
        pic: [{required: true, message: '请提交图片', trigger: 'blur'}],
        good_name: [{required: true, message: '请输入商品名', trigger: 'blur'}],
        good_price:[{required: true, message: '请输入价格', trigger: 'blur'},
          {pattern: /\d*\.[0-9][0-9]$/, message: '请输入非负二位小数', trigger: 'blur'}],
        good_num: [{required: true, message: '请输入库存量', trigger: 'blur'},
          {pattern: /\d*$/, message: '请输入非负整数', trigger: 'blur'}],
        good_type: [{required: true, message: '请选择商品类型', trigger: 'blur'}],
      },
    }
  },
  methods: {
    init_total(){
      postRequest("/good/selectTotal", JSON.stringify({type: '',
        good_name: ''})).then(resp => {
        if (resp.data["code"] === 200) {
          this.total = resp.data["data"]["total"]
        } else {
          ElMessage.error("未知错误")
        }
      })
    },
    selectAllTypes(){
      postRequest("/type/selectAllType").then(resp => {
        if (resp.data["code"] === 200){
          this.my_type = resp.data["data"]["types"]
        }
      })
    },
    // 上传文件之前的钩子
    handleBeforeUpload(file){
      if(!(file.type === 'image/png' || file.type === 'image/gif' || file.type === 'image/jpg' || file.type === 'image/jpeg')) {
        this.$notify.warning({
          title: '警告',
          message: '请上传格式为image/png, image/gif, image/jpg, image/jpeg的图片'
        })
      }
      let size = file.size / 1024 / 1024 / 2
      if(size > 2) {
        this.$notify.warning({
          title: '警告',
          message: '图片大小必须小于2M'
        })
      }
    },
    //文件超出个数限制时的钩子
    handleExceed() {
      this.$notify.warning({
        title: '警告',
        message: '图片数量至多为3张',
      })
    },
    //文件列表移除文件时的钩子
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handle_imageChange(file, fileList) {
      this.file_list = fileList;
      console.log(fileList)
    },
    // 点击文件列表中已上传的文件时的钩子
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    async uploadFile() {
      const reg1 = new RegExp(/\d*\.[0-9][0-9]$/)
      const reg2 = new RegExp(/\d*$/)
      if ( !(reg1.test(this.Form.good_price)) ){
        ElMessage.error("价格应是二位小数")
        return
      }
      if ( !(reg2.test(this.Form.good_num)) ){
        ElMessage.error("库存应是非负整数")
        return
      }
      let that = this;
      let fileForm = new FormData();
      for (let i = 0; i < that.file_list.length; i++ ){
        fileForm.append("file", that.file_list[i].raw);
      }
      fileForm.append("good_name", that.Form.good_name);
      fileForm.append("good_type", that.Form.good_type);
      fileForm.append("good_price", that.Form.good_price);
      fileForm.append("good_num", that.Form.good_num);
      fileForm.append("good_log", that.Form.good_log);
      await axios.post("http://localhost:8081/good/addGood", fileForm,
          { headers: {'Content-Type': 'multipart/form-data; charset=UTF-8', 'encoding': 'UTF-8',
              'token': sessionStorage.getItem("token")}})
          .then(resp => {
            if (resp.data["code"] === 200){
              ElMessage.info("上架成功")
            } else if (resp.data["code"] === 400){
              ElMessage.info("请上传图片")
            } else {
              ElMessage.error("权限不足!")
            }
          }).catch(function (err){
            console.log(err)
          })
    }
  },
  created() {
    this.selectAllTypes();
    this.init_total();
    if( this.total === 1 ){
      router.back()
    }
  }
}
</script>

<style scoped>

</style>
