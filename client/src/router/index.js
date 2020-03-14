import Vue from 'vue'
import VueRouter from 'vue-router'

import routes from './routes'

Vue.use(VueRouter)

export default function({ store }) {
  const Router = new VueRouter({
    scrollBehavior: () => ({ x: 0, y: 0 }),
    routes,
    mode: process.env.VUE_ROUTER_MODE,
    base: process.env.VUE_ROUTER_BASE
  })
  Router.beforeEach((to, from, next) => {
    const params = new URLSearchParams(window.location.search)
    if (params.has('token')) {
      store.dispatch('token', { encodedToken: params.get('token') })
    }
    if (
      to.matched.some((page) => page.meta.public) ||
      store.state.accessToken
    ) {
      store.dispatch('setTitle', { title: to.name })
      next()
    } else {
      store.dispatch('setTitle', { title: 'Home' })
      next('/')
    }
  })

  return Router
}
