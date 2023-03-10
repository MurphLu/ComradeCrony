<template>
  <div>
    <div class="feed_content">
      <div class="user_content">
        <img :src="feedInfo.avatar"/>
        <label>{{feedInfo.nickname}}</label>
      </div>
      <div class="feed_info">
        <div class="text_content">
          <label>{{feedInfo.textContent}}</label>
        </div>
        <div class="iamge_content"></div>
      </div>
      <div class="action_buttons">
        <div @click="addComment"><label>comment</label></div>
        <div  @click="like"><label>liked {{feedInfo.likeCount}}</label></div>
        <div @click="favorite"><label>favorite {{feedInfo.loveCount}}</label></div>
      </div>
      <div class="add_comment" v-show="showCommentArea">
        <input v-model="comment"/>
        <button>comment</button>
      </div>
      <div class="comments">
      </div>
    </div>
  </div>
</template>

<script>
import { TimelineLoader } from "@/assets/js/request_loader.js"

export default {
  props: {
    feedInfo: {
      type: Object,
    }
  },
  data(){
    return {
      comment: '',
      showCommentArea: false
    }
  },
  methods: {
    addComment(){},
    like(){

      console.log("like clicked")
    },
    favorite(){
      TimelineLoader.addComment(3, this.feedInfo.id, this.feedInfo.userId, null)
      .then((value) => {
        console.log(value)
      }).catch((err) => {
        console.log(err)
      })
      TimelineLoader.getCommentCount(3, this.feedInfo.id)
      .then((value) => {
        console.log(value)
      }).catch((err) => {
        console.log(err)
      })
    }
  }
}
</script>

<style lang="stylus" scoped>
.feed_content
  display: flex
  padding: 0.5rem
  flex-direction: column
  background-color: #F5F5F5
  .user_content
    height: 3rem
    display: flex
    align-items: center
    img
      width: 2rem
      height: 2rem
      margin: 0rem 0.4rem 0rem 0.4rem
      border-radius: 1rem
  .feed_info
    display: flex
    flex-direction: column
    .text_content
      padding: 0rem 0.5rem
      text-align: left
    .image_content
      padding: 0.5rem 0.5rem
  .action_buttons
    display: flex
    margin-top: 1rem
    // justify-content: space-between
    div
      border 0.1rem solid #FEDF53
      flex-grow: 1
      font-size: 0.7rem
.add_comment
  height 2rem
  margin-top 0.5rem
  display: flex
  flex-direction: row
  justify-content: space-between
  input
    width: 70%
    border 0
    border-radius: 0.3rem
  button
    width 25%
</style>
