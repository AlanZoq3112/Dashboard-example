import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const router = new VueRouter({
  mode: 'history',
  base: import.meta.env.BASE_URL,
  routes: [
    {
      path: '/',
      name: 'inicio',
      component: () => import('../views/Inicio.vue')
    },
    {
      path: '/inicio',
      name: 'inicio',
      component: () => import('../views/Inicio.vue')
    },
    {
      path: '/peliculas',
      name: 'peliculas',
      component: () => import('../modules/Movies/Views/Peliculas.vue')
    },
    {
      path: '/dragform',
      name: 'dragform',
      component: () => import('../modules/DragAndAnimations/Views/Form.vue')
    }
  ]
})

export default router
