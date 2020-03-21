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
      async setTitle({ commit }, { title }) {
        commit('setTitle', { title: title })
      },
      async initializeToken({ commit, dispatch }, { onSuccess, onError }) {
        commit('initialized')
        dispatch('getToken', { onSuccess: onSuccess, onError: onError })
      },
      async getToken({ commit, dispatch }, { onSuccess, onError }) {
        await Vue.prototype.$axios
          .get('/api/token')
          .then(function(response) {
            const { userHash, accessToken, role } = response.data
            commit('login', {
              userHash: userHash,
              accessToken: accessToken,
              role: role
            })
            dispatch('getUser')
            onSuccess()
          })
          .catch(function() {
            console.debug('No valid session exists')
            onError()
          })
      },
      async login({ dispatch }, { email, password, router }) {
        const data = new FormData()
        data.append('email', email)
        data.append('password', password)
        await Vue.prototype.$axios
          .post('/api/login', data)
          .then(function() {
            dispatch('getToken', {
              onSuccess: () => {
                router.push('/dashboard')
              },
              onError: () => {}
            })
          })
          .catch(function() {
            console.debug('User not found')
          })
      },
      async signUp({ dispatch }, { userName, email, password, router }) {
        await Vue.prototype.$axios
          .post('/api/signUp', {
            userName: userName,
            email: email,
            password: password
          })
          .then(function() {
            dispatch('login', {
              email: email,
              password: password,
              router: router
            })
          })
          .catch(function() {
            console.debug('Could not register user')
          })
      },
      async logout({ commit }) {
        await Vue.prototype.$axios
          .post('/api/logout')
          .then(function() {
            commit('logout')
          })
          .catch(function() {
            console.debug('Could not register user')
            commit('logout')
          })
      },
      async getUser({ commit }) {
        await Vue.prototype.$axios
          .get('/api/user')
          .then(function(response) {
            const { userName, email } = response.data
            commit('setUser', { userName: userName, email: email })
          })
          .catch(function() {
            console.debug('Could not register user')
          })
      },
      async updateUser({ commit }, { userName, password }) {
        await Vue.prototype.$axios
          .put('/api/user', { userName: userName, password: password })
          .then(function(response) {
            const { userName, email } = response.data
            commit('setUser', { userName: userName, email: email })
          })
          .catch(function() {
            console.debug('Could not register user')
          })
      },
      async deleteUser({ commit }) {
        await Vue.prototype.$axios
          .delete('/api/user')
          .then(function() {
            commit('logout')
          })
          .catch(function() {
            console.debug('Could not delete user')
          })
      }
    },

    modules: {},

    strict: process.env.DEV
  })

  return store
}
