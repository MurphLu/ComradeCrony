import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import _ from 'lodash'
import { Indicator, Popup, Button, TabContainer, TabContainerItem, IndexList, IndexSection, Cell, InfiniteScroll } from 'mint-ui'

Vue.config.productionTip = false

Vue.prototype._ = _
Vue.prototype.$indicator = Indicator

Vue.component(Popup.name, Popup)
Vue.component(Button.name, Button)
Vue.component(TabContainer.name, TabContainer)
Vue.component(TabContainerItem.name, TabContainerItem)
Vue.component(IndexList.name, IndexList)
Vue.component(IndexSection.name, IndexSection)
Vue.component(Cell.name, Cell)

Vue.use(InfiniteScroll);


router.beforeResolve((to, from, next) => {
  if(to.path === '/login') {
    next()
    return
  }
  if(store.state.token === '') {
    next({path: '/login'})
  } else {
    next()
  }
})

new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')
