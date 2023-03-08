<template>
  <div>
    timeline
  </div>
</template>

<script>
import { TimelineLoader } from "@/assets/js/request_loader.js"
export default {
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
        console.log(res)
      }).catch((err) => {
        console.log(err)
      })
    }
  }
}
</script>

<style>

</style>
