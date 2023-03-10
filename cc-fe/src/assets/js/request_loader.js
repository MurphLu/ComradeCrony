import { callApi } from "./network_service";

const API = {
  async post({url, data}) {
    return await callApi({
      url, method: "POST", data
    })
  },
  async get({url, params}) {
    return await callApi({
      url, method: "GET", params
    })
  },
  async delete({url, data}) {
    return await callApi({
      url, method: "delete", data
    })
  },

}

const RecommendLoader = {
  async loadTodayBest() {
    return await API.get({url: "/cc/todayBest"})
  },
  async loadRecommendList(page, pageSize) {
    return await API.get({
      url: "/cc/recommendList",
      params: {
        page: page,
        pagesize: pageSize
      }
  })
  }
}

const TimelineLoader = {
  async loadTimeline(page, pageSize) {
    return await API.get({
      url: "/feed",
      params: {
        page: page,
        pageSize: pageSize
      }})
  },
  async addComment(type, publishId, publishUserId, comment) {
    return await API.post({
      url: "/feed/comment",
      method: "post",
      data: {
        commentType: type,
        publishId: publishId,
        publishUserId: publishUserId,
        content: comment
      }
    })
  },
  async removeComment(type, publishId, publishUserId, commentId) {
    return await API.delete({
      url: "/feed/comment",
      data: {
        commentType: type,
        publishId: publishId,
        publishUserId: publishUserId,
        commentId: commentId
      }
    })
  },
  async getCommentCount(type, publishId) {
    return await API.get({
      url: "/feed/comment/cont",
      params: {
        commentType: type,
        publishId: publishId,
      }
    })
  }
}

export {
  RecommendLoader,
  TimelineLoader
}
