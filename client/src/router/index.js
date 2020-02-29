import Vue from 'vue'
import VueRouter from 'vue-router'
import Store from '@/store/index.js'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import(/* webpackChunkName: "home" */ '../views/Home.vue'),
    meta: {
      isPublic: true
    }
  },
  {
    path: '/public',
    name: 'Public',
    component: () =>
      import(/* webpackChunkName: "public" */ '../views/Public.vue'),
    meta: {
      isPublic: true
    }
  },
  {
    path: '/secret',
    name: 'Secret',
    component: () =>
      import(/* webpackChunkName: "secret" */ '../views/Secret.vue')
  },
  {
    path: '/error',
    name: 'Error',
    component: () =>
      import(/* webpackChunkName: "error" */ '../views/Error.vue'),
    meta: {
      isPublic: true
    }
  },
  {
    path: '*',
    component: () =>
      import(/* webpackChunkName: "error" */ '../views/Error.vue'),
    meta: {
      isPublic: true
    }
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {
  if (
    to.matched.some((page) => page.meta.isPublic) ||
    Store.state.accessToken
  ) {
    next()
  } else {
    next('/')
  }
})

export default router
