import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

function pushStateIntoSession(key, val) {
  sessionStorage.setItem(key, val)
}

function pullStateFromSession(key) {
  return sessionStorage.getItem(key)
}

const state = {
  token: pullStateFromSession("token"),
}
const mutations = {
  tokenChanged(state, payload){
    // state.token = payload
    pushStateIntoSession("token", payload)
  }
}
const actions = {}
const getters = {}

export default new Vuex.Store({
  state,
  mutations,
  actions,
  getters
})
