import axios from 'axios'
import store from '@/store'

export default {
  install(Vue) {
    const _axios = axios.create({
      withCredentials: true
    })
    _axios.interceptors.request.use((config) => {
      const { userId, accessToken } = store.state
      const authorizatoin = btoa(
        JSON.stringify({ userId: userId, accessToken: accessToken })
      )
      config.headers['Authorization'] = authorizatoin
      return config
    })
    Vue.prototype.$axios = _axios
  }
}
