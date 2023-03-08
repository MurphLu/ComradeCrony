<template>
  <div>
    <div class="recommend" v-if="userInfo && userInfo.nickname">
      <img :src="userInfo.avatar" />
      <div class="content">
        <div class="info">
          <label>{{userInfo.nickname}}</label>
          &nbsp;
          <label>{{userInfo.gender}}</label>
          &nbsp;
          <label>{{userInfo.age}} 岁</label>
          &nbsp;
          <label>{{userInfo.fateValue}}</label>
        </div>
        <div class="tags">
          <label class="tag" v-for="tag in userInfo.tags" :key="tag">{{tag}}</label>
        </div>

      </div>

    </div>
  </div>
</template>

<script>
export default {
  props: {
    userInfo: {
      type: Object,
    }
  }
}
</script>

<style lang="stylus" scoped>
.recommend
  border: 0.1rem solid #eeeeee
  border-radius: 0.5rem
  box-shadow: 0.3rem 0.3rem 0.5rem 0
  height: 5rem
  display: flex
  align-items: center
  justify-items: stretch
  img
    width: 3rem
    height: 3rem
    border-radius: 1.5rem
    margin: 0.5rem
  .content
    padding: 0.5rem
    .info
      font-size: 0.7rem
      display: flex
      justify-content space-between
    .tags
      font-size: 0.5rem
      color: gray
      display: flex
      justify-content flex-start
      width: 100%
      .tag
        margin 0.2rem
        padding 0.1rem 0.2rem
        border-radius: 0.3rem
        background-color: orange
</style>
