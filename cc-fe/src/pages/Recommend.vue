<template>
  <div>
    <recommend-card :userInfo="this.todayBest" />
    <div class="recommend_list">
      <label>推荐列表</label>
      <ul
        v-infinite-scroll="loadMore"
        infinite-scroll-disabled="loading"
      >
        <li v-for="item in recommendList" :key="item.id">
          <recommend-card :userInfo="item" />
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import { RecommendLoader } from '@/assets/js/request_loader.js'
import RecommendCard from '../components/recommend/RecommendCard.vue'

export default {
  components: {
    RecommendCard
  },
  data() {
    return {
      todayBest: {},
      recommendList: [],
      currentPage: 1,
      pageSize: 10,
      loading: false
    }
  },
  methods: {
    loadTodayBest() {
      RecommendLoader.loadTodayBest()
      .then((res) => {
        this.todayBest = res.data
        console.log(res)
      }).catch((err) => {
        console.log(err)
      })
    },
    loadList() {
      RecommendLoader.loadRecommendList(this.currentPage, this.pageSize)
      .then((res) => {
        const items = res && res.data && res.data.items
        if(items) {
          this.recommendList = items
        }
        console.log(res)
      }).catch((err) => {
        console.log(err)
      })
    },
    loadMore(){}
  },
  mounted() {
    this.loadTodayBest()
    this.loadList()
  }
}
</script>

<style lang="stylus" scoped>
.recommend_list
  margin-top: 1rem
ul
  list-style: none
  padding-left: 0
  li
    padding-bottom: 0.5rem
</style>
