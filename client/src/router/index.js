import Vue from 'vue'
import VueRouter from 'vue-router'
import routes from './routes'

Vue.use(VueRouter)

export default function({ store }) {
  const router = new VueRouter({
    scrollBehavior: () => ({ x: 0, y: 0 }),
    routes,
    mode: process.env.VUE_ROUTER_MODE,
    base: '/web/'
  })

  router.beforeEach((to, from, next) => {
    async function validate() {
      const { login } = store.state.user
      if (to.path === '/' && login) {
        next({ path: '/dashboard' })
        return
      }
      if (to.meta.public || login) {
        store.commit('setTitle', { title: to.name })
        next()
        return
      }
      store.commit('setTitle', { title: 'Home' })
      next('/')
    }
    async function initialize(store, validate) {
      try {
        store.commit('initialized')
        await store.dispatch('getToken')
        await store.dispatch('getUser')
        if (to.path === '/') {
          next('/dashboard')
        } else {
          validate()
        }
      } catch (error) {
        validate()
      }
    }
    const { initialized } = store.state
    if (!initialized) {
      initialize(store, validate)
      return
    }
    validate()
  })

  return router
}
