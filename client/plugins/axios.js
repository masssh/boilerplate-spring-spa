export default function({ store, $axios, redirect }) {
  $axios.onRequest((config) => {
    const accessToken = store.state.auth.accessToken
    if (accessToken) {
      config.headers.Authorization = `Bearer ${accessToken}`
    }
    return config
  })

  $axios.onError((error) => {
    if (error.response.status === 401) {
      store.dispatch('auth/reset')
      redirect('/login')
    }
    if (error.response.status !== 200 && error.response.status !== 404) {
      store.dispatch('notification/setNotify', {
        title: error.response.status,
        text: error.response.data.reason,
        type: 'is-danger',
        duration: 3500
      })
    }
  })
}
