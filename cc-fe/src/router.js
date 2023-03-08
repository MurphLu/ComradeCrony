import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/home',
      name: 'home',
      component: () => import('@/pages/Home.vue')
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/pages/Login.vue')
    },
    {
      path: '/recommend',
      name: 'recommend',
      component: () => import('@/pages/Recommend.vue')
    },
    {
      path: '/timeline',
      name: 'timeline',
      component: () => import('@/pages/Timeline.vue')
    }
  ]
})
