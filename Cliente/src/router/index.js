import Vue from 'vue'
import VueRouter from 'vue-router'
import ListMovies from '../modules/movies/views/ListMovies.vue'

Vue.use(VueRouter)

const router = new VueRouter({
  mode: 'history',
  base: import.meta.env.BASE_URL,
  routes: [
    {
      path: '/',
      name: 'listMovies',
      component: ListMovies
    }
  ]
})

export default router
