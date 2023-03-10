<template>
  <div>
    timeline
    <ul v-infinite-scroll="loadMore"
        infinite-scroll-disabled="loading">
      <li v-for="feed in feedList" :key="feed.id">
        <feed-card :feedInfo="feed"/>
      </li>
    </ul>

  </div>
</template>

<script>
import { TimelineLoader } from "@/assets/js/request_loader.js"
import FeedCard from '../components/timeline/FeedCard.vue'

export default {
  components:{
    FeedCard,
  },
  data() {
    return {
      feedList: [],
      currentPage: 1,
      pageSize: 10
    }
  },
  mounted() {
    this.loadFeedList()
  },
  methods: {
    loadFeedList(){
      TimelineLoader.loadTimeline(this.currentPage, this.pageSize)
      .then((res) => {
        const list = res && res.data && res.data.items
        if(list !== null) {
          this.feedList = list
        }
        console.log(res)
      }).catch((err) => {
        console.log(err)
      })
    },
    loadMore(){}
  }
}
</script>

<style  lang="stylus" scoped>
ul
  list-style: none
  padding-left: 0
  li
    padding-bottom: 0.5rem
</style>
