import Vue from 'vue'
import 'material-design-icons/iconfont/material-icons.css'
import Vuetify from 'vuetify'
import 'vuetify/dist/vuetify.min.css'
import '../static/css/font.css'
import api from './utils/backend-api'
import Vuex from 'vuex'
import App from './App'
import router from './router'

Vue.use(Vuetify, {
  theme:
  {
    primary: '#0091EA',
    secondary: '#94f62f',
    accent: '#00897B',
    error: '#f44336',
    warning: '#ffeb3b',
    info: '#2196f3',
    success: '#4caf50'
  }
})

Vue.use(Vuex)
Vue.config.productionTip = false
Vue.prototype.api = api

// eslint-disable-next-line
new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
