import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default function() {
  const store = new Vuex.Store({
    state: {
      title: null,
      userId: null,
      accessToken: null,
      role: null
    },
    mutations: {
      setTitle(state, payload) {
        state.title = payload.title
      },
      setToken(state, payload) {
        state.userId = payload.userId
        state.accessToken = payload.accessToken
        state.role = payload.role
      }
    },
    actions: {
      async setTitle({ commit }, { title }) {
        commit('setTitle', { title: title })
      },
      async getToken({ commit }) {
        await Vue.prototype.$axios
          .get('/api/token')
          .then(function(response) {
            const { userId, accessToken, role } = response.data
            commit('setToken', {
              userId: userId,
              accessToken: accessToken,
              role: role
            })
          })
          .catch(function() {
            console.debug('No valid session exists')
          })
      },
      async signIn({ commit }, { email, password }) {
        const data = new FormData()
        data.append('email', email)
        data.append('password', password)
        await Vue.prototype.$axios
          .post('/api/login', data)
          .then(function(response) {
            const { userId, accessToken, role } = response.data
            commit('setToken', {
              userId: userId,
              accessToken: accessToken,
              role: role
            })
          })
          .catch(function() {
            console.debug('User not found')
          })
      },
      async signUp({ commit }, { userName, email, password }) {
        await Vue.prototype.$axios
          .post('/api/signUp', {
            userName: userName,
            email: email,
            password: password
          })
          .then(function(response) {
            const { userId, accessToken, role } = response.data
            commit('setToken', {
              userId: userId,
              accessToken: accessToken,
              role: role
            })
          })
          .catch(function() {
            console.log('Could not register user')
          })
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

    modules: {},

    strict: process.env.DEV
  })

  return store
}
