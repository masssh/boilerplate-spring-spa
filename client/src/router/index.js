import Vue from 'vue'
import VueRouter from 'vue-router'
import routes from './routes'

Vue.use(VueRouter)

export default function({ store }) {
  const router = new VueRouter({
    scrollBehavior: () => ({ x: 0, y: 0 }),
    routes,
    mode: process.env.VUE_ROUTER_MODE,
    base: process.env.VUE_ROUTER_BASE
  })

  router.beforeEach((to, from, next) => {
    const validate = () => {
      const { login } = store.state.user
      if (to.path === '/' && login) {
        next({ path: '/dashboard' })
        return
      }
      if (to.meta.public || login) {
        store.dispatch('setTitle', { title: to.name })
        next()
        return
      }
      store.dispatch('setTitle', { title: 'Home' })
      next('/')
    }
    const onSuccess = () => {
      if (to.path === '/') {
        next('/dashboard')
      } else {
        validate()
      }
    }

    const { initialized } = store.state
    if (!initialized) {
      store.dispatch('initializeToken', {
        onSuccess: onSuccess,
        onError: validate
      })
      return
    }
    validate()
  })

  return router
}
