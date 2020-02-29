import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    userId: null,
    accessToken: null,
    role: null
  },
  mutations: {
    login(state, payload) {
      state.userId = payload.userId
      state.accessToken = payload.accessToken
      state.role = payload.role
    }
  },
  actions: {
    async login({ commit }, { email, password }) {
      const response = await Vue.prototype.$axios.post('/api/login', {
        email: email,
        password: password
      })
      if (response.status === 200) {
        const { userId, accessToken, role } = response.data
        commit('login', {
          userId: userId,
          accessToken: accessToken,
          role: role
        })
      }
    },
    async test() {
      const response = await Vue.prototype.$axios.get('/api/test')
      console.log(response)
    }
  },
  modules: {}
})
