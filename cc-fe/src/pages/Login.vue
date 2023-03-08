<template>
  <div>
    <div class="box">
      <div class="form_field">
        <label>手  机：</label>
        <input v-model="phoneNumber" placeholder="请输入手机号"/>
      </div>
      <div class="form_field">
        <label>验证码：</label>
        <input v-model="smsCode" placeholder="请输入验证码"/>
      </div>
      <div class="form_field">
        <input value="发送验证码" type="button" @click="sendSmsCode"/>
        &nbsp;&nbsp;
        <input value="登 录" type="button" @click="verifySmsCode"/>
      </div>
    </div>
  </div>
</template>

<script>
import { callSSOApi } from '@/assets/js/network_service.js'

export default {
  data() {
    return {
      phoneNumber: '',
      smsCode: ''
    }
  },
  methods: {
    sendSmsCode() {
      if(this.phoneNumber !== null && this.phoneNumber !== '') {
        callSSOApi({
          url: '/sms/send',
          method: 'POST',
          data: {'phone': this.phoneNumber}
        }).then((res) =>{
          console.log(res)
        }).catch((err) => {
          console.log(err)
        })
      }
    },
    verifySmsCode() {
      callSSOApi({
        url: '/user/loginVerification',
        method: 'POST',
        data: {'phone': this.phoneNumber, 'smsCode': this.smsCode}
      }).then((res)=> {
        const data = res.data
        if(data.token !== null) {
          this.$store.commit('tokenChanged', data.token)
          this.$router.push({path: "/home"})
        }
      })
    }
  }
};
</script>

<style lang="stylus" scoped>
.box
  padding-top: .625rem
  width: calc(100% - 1.875rem)
  margin: 0 auto

.form_field
  padding 0.5rem
  position relative
  display flex
  align-items center
  line-height 1.3125rem
  label
    width: 6rem
    text-align: left

  input
    padding-left 0.5rem
    position relative
    appearance none
    border-radius 0.5rem
    // border 0
    outline 0
    width 100%
    font-size .9375rem
    background #ffffff
    flex 1
    // padding 0
    display block
    color #333333
    height 2rem
    color #333333

    border-width: 0.01rem
    border-color: #999999
    &::placeholder
      color #D3D3D3
    &:disabled
      color #C2C2C2
      ~ .form_field
        color #C2C2C2
</style>
