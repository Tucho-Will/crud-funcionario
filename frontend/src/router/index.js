import Vue from 'vue'
import Router from 'vue-router'
import funcionario from '../modules/funcionario/crud_funcionario'

Vue.use(Router)
export default new Router({
  base: '/',
  routes: [
    {
      path: '/',
      redirect: 'crud'
    },
    {
      path: '/crud',
      name: 'crud',
      component: funcionario
    }
  ]
})
