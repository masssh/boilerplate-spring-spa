const excludePages = ['index', 'public']

export default function({ store, route }) {
  console.log('process middleware...')
  if (!store.isLoggedIn && !excludePages.includes(route.name)) {
  }
  store.app.router.beforeEach((to, from, next) => {
    // if (!excludePages.includes(route.name) && !store.state.isLoggedIn) {
    //   store.dispatch('login', { onSuccess: route.path, onFailure: '/' })
    // }
    console.log(to, from)
    if (store.state.isLoggedIn || excludePages.includes(to.name)) {
      next()
    } else if (!excludePages.includes(to.name) && !store.state.isLoggedIn) {
      store.dispatch('login', { onSuccess: to.path, onFailure: from.path })
    }
  })
}
