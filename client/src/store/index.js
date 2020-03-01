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
    setToken(state, payload) {
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
        commit('setToken', {
          userId: userId,
          accessToken: accessToken,
          role: role
        })
      }
    },
    async token({ commit }, { encodedToken }) {
      const { userId, accessToken, role } = JSON.parse(atob(encodedToken))
      commit('setToken', {
        userId: userId,
        accessToken: accessToken,
        role: role
      })
    }
  },
  modules: {}
})
