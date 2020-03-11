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
    path: '/signIn',
    name: 'Sign In',
    component: () =>
      import(/* webpackChunkName: "secret" */ '../views/SignIn.vue'),
    meta: {
      isPublic: true
    }
  },
  {
    path: '/signUp',
    name: 'Sign Up',
    component: () =>
      import(/* webpackChunkName: "secret" */ '../views/SignUp.vue'),
    meta: {
      isPublic: true
    }
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () =>
      import(/* webpackChunkName: "secret" */ '../views/Dashboard.vue')
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
  const params = new URLSearchParams(window.location.search)
  if (params.has('token')) {
    Store.dispatch('token', { encodedToken: params.get('token') })
  }
  if (
    to.matched.some((page) => page.meta.isPublic) ||
    Store.state.accessToken
  ) {
    Store.dispatch('setTitle', { title: to.name })
    next()
  } else {
    Store.dispatch('setTitle', { title: 'Home' })
    next('/')
  }
})

export default router
