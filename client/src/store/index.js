import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default function() {
  const store = new Vuex.Store({
    state: {
      initialized: false,
      title: null,
      userId: null,
      accessToken: null,
      role: null
    },
    mutations: {
      initialized(state) {
        state.initialized = true
      },
      setTitle(state, payload) {
        state.title = payload.title
      },
      setToken(state, payload) {
        state.userId = payload.userId
        state.accessToken = payload.accessToken
        state.role = payload.role
      },
      logout(state) {
        state.userId = null
        state.accessToken = null
        state.role = null
      }
    },
    actions: {
      async setTitle({ commit }, { title }) {
        commit('setTitle', { title: title })
      },
      async initializeToken({ commit }, { onSuccess, onError }) {
        commit('initialized')
        await Vue.prototype.$axios
          .get('/api/token')
          .then(function(response) {
            const { userId, accessToken, role } = response.data
            commit('setToken', {
              userId: userId,
              accessToken: accessToken,
              role: role
            })
            onSuccess(userId)
          })
          .catch(function() {
            console.debug('No valid session exists')
            onError()
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
            console.debug('Could not register user')
          })
      },
      async logout({ commit }, { router }) {
        await Vue.prototype.$axios
          .post('/api/logout')
          .then(function() {
            commit('logout')
            router.push('/')
          })
          .catch(function() {
            console.debug('Could not register user')
            commit('logout')
            router.push('/')
          })
      }
    },

    modules: {},

    strict: process.env.DEV
  })

  return store
}
