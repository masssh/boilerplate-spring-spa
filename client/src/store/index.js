import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default function() {
  const store = new Vuex.Store({
    state: {
      initialized: false,
      title: null,
      user: {
        login: false,
        userHash: null,
        accessToken: null,
        role: null,
        userName: null,
        email: null
      }
    },
    mutations: {
      initialized(state) {
        state.initialized = true
      },
      setTitle(state, { title }) {
        state.title = title
      },
      login(state, { userHash, accessToken, role }) {
        state.user.login = true
        state.user.userHash = userHash
        state.user.accessToken = accessToken
        state.user.role = role
      },
      logout(state) {
        state.user.login = false
        state.user.userHash = null
        state.user.accessToken = null
        state.user.role = null
      },
      setUser(state, { userName, email }) {
        state.user.userName = userName
        state.user.email = email
      }
    },
    actions: {
      async getToken({ commit }) {
        const response = await Vue.prototype.$axios.get('/api/token')
        const { userHash, accessToken, role } = response.data
        commit('login', {
          userHash: userHash,
          accessToken: accessToken,
          role: role
        })
      },
      async login(_, { email, password }) {
        const data = new FormData()
        data.append('email', email)
        data.append('password', password)
        await Vue.prototype.$axios.post('/api/login', data)
      },
      async signUp(_, { userName, email, password }) {
        await Vue.prototype.$axios.post('/api/signUp', {
          userName: userName,
          email: email,
          password: password
        })
      },
      async logout({ commit }) {
        await Vue.prototype.$axios.post('/api/logout')
        commit('logout')
      },
      async getUser({ commit }) {
        const response = await Vue.prototype.$axios.get('/api/user')
        const { userName, email } = response.data
        commit('setUser', { userName: userName, email: email })
      },
      async updateUser(_, { userName, password }) {
        await Vue.prototype.$axios.put('/api/user', {
          userName: userName,
          password: password
        })
      },
      async forgotPassword(_, { email }) {
        await Vue.prototype.$axios.post('/api/password/forgot', {
          email: email
        })
      },
      async resetPassword({ commit }, { email, password, verificationHash }) {
        await Vue.prototype.$axios.post('/api/password/reset', {
          email: email,
          password: password,
          verificationHash: verificationHash
        })
        commit('logout')
      },
      async deleteUser({ commit }) {
        await Vue.prototype.$axios.delete('/api/user')
        commit('logout')
      }
    },

    modules: {},

    strict: process.env.DEV
  })

  return store
}
