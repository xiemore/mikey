<template>
  <el-form
      class="loginContainer"
      element-loading-background="rgba(0, 0, 0, 0.8)"
      :model="Form">
    <h3 class="loginTitle">购物平台登录</h3>
    <el-form-item prop="user_name" label="用户名">
      <el-input size="default" type="text" auto-complete="off" v-model="Form.user_name"
                placeholder="请输入用户名"></el-input>
    </el-form-item>
    <el-form-item prop="Message" label="密码">
      <el-input size="default" type="text" auto-complete="off" v-model="Form.Message"
                placeholder="请输入密码"></el-input>
    </el-form-item>
    <div>
      <div class="button_right">
        <el-button class="loginButton" size="default" type="primary" @click="send">登录</el-button>
      </div>
    </div>
  </el-form>
</template>

<script>
export default {
  name: "MyChat",
  data() {
    return {
      ws: null,
      Form: {
        username: '',
        Message: '',
      },
      wsUrl: "ws://localhost:8081/chat/one_to_one",
      token: '',
    }
  },
  methods: {
    send(){
      this.wsSendMessage()
    },
    closeWebsocket(){
      this.ws.close();
    },
    initWebsocket(){
      if (typeof(WebSocket) == 'function' ) {
        this.ws = new WebSocket(this.wsUrl + '/' + this.token + '/' + 'ss')
        this.ws.onopen = this.wsOnopen
        this.ws.onclose = this.wsOnclose
        this.ws.onerror = this.wsOnerror
        this.ws.onmessage = this.wsOnMessage
      }
      else{
        console.log("不支持")
      }
    },
    wsOnopen(){
      console.log("连接完毕")
    },
    wsOnclose(){
      console.log("连接关闭")
    },
    wsOnMessage(data){
      console.log(data)
    },
    wsOnerror(){
      console.log("错误")
    },
    wsSendMessage(){
      console.log(this.ws.readyState)
      if(this.ws.readyState === 1) {
        this.ws.send(JSON.stringify({user_name: this.Form.user_name, message: this.Form.Message}));
      }
    }
  },
  created() {
    this.token = sessionStorage.getItem("token")
    this.initWebsocket()
    window.addEventListener('beforeunload', e => this.closeWebsocket(e))
  },
  beforeUnmount() {
    //卸载事件
    window.removeEventListener('beforeunload', e => this.closeWebsocket(e))
  }
}
</script>

<style scoped>

</style>
