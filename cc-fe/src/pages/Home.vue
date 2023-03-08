<template>
  <div>
    <div class="box">
      <div class="cell_content">
        <div class="user_info">
          <img class="avator" :src="avator" />
          <label class="nick_name">{{nickName}}</label>
        </div>
      </div>
      <div class="tool_box">
        <ul>
          <li v-for="item in this.toolList" :key="item.name">
            <tool-cell :title="item.name" @click="item.onClick"></tool-cell>
          </li>
        </ul>
      </div>
    </div>

  </div>
</template>

<script>
import { callApi } from '@/assets/js/network_service.js'
import ToolCell from '../components/home/ToolCell.vue'
export default {
  components: {
  ToolCell
  },
  data() {
    return {
      avator: '',
      nickName: '',
      toolList: [
        {name: "todayBest", onClick: this.enterRecommend},
        {name: "timeLine", onClick: this.enterTimeline}
      ]
    }
  },
  methods: {
    loadUserInfo() {
      callApi({
        url: '/userInfo',
        method: 'GET',
        data: {},
      }).then((res)=>{
        const data = res.data
        if(data === null) { return }
        this.nickName = data && data.nickName
        this.avator = data && data.logo
        console.log(res)
      }).catch((err)=> {
        console.log(err)
      })
    },
    enterRecommend() {
      this.$router.push({path: '/recommend'})
    },
    enterTimeline() {
      this.$router.push({path: '/timeline'})
    }
  },
  mounted() {
    this.loadUserInfo()
  }
}
</script>

<style lang="stylus" scoped>
.cell_content
  border 0.01rem solid lightgray
  border-left: 0rem
  border-right: 0rem
  // display: block
  // border-width: 0.1rem
  // border-color: #000000
.box
  padding-top: .625rem
  width: calc(100% - 1.875rem)
  margin: 0 auto
  .user_info
    display flex
    align-items: center
    height: 5rem
    width 100%
    background-color: #FFFFFF
    .avator
      margin-left: 1rem
      width 3rem
      height 3rem
      border-radius: 1.5rem

    .nick_name
      margin-left: 1rem
      font-size: 1rem
.tool_box
  margin-top: 1rem

ul
  list-style: none
  padding: 0
  margin: 0.0625rem 0 0 0
  background-color #FAFAFC
</style>
