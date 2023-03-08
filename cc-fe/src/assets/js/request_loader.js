import { callApi } from "./network_service";

const RecommendLoader = {
  async loadTodayBest() {
    const result = await callApi({
      url: "/cc/todayBest",
      method: "get"
    })
    return result
  },
  async loadRecommendList(page, pageSize) {
    const result = await callApi({
      url: "/cc/recommendList",
      method: "get",
      params: {
        page: page,
        pagesize: pageSize
      }
    })
    return result
  }
}

const TimelineLoader = {
  async loadTimeline(page, pageSize) {
    const result = await callApi({
      url: "/feed",
      method: "get",
      params: {
        page: page,
        pageSize: pageSize
      }
    })
    return result
  }
}

export {
  RecommendLoader,
  TimelineLoader
}
