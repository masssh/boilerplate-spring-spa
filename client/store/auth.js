const env = process.env.NODE_ENV || 'production'

export const state = () => ({
  accessToken: null
})

export const mutations = {
  setToken(state, data) {
    state.accessToken = data
  },
  resetToken(state) {
    state.accessToken = null
  }
}

export const actions = {
  getLogin({ commit }, accessToken) {
    commit('setToken', accessToken)
  },
  async login({ commit }, data) {
    try {
      const accessToken = await this.$axios.$post('/api/login', data)
      commit('setToken', accessToken)

      if (process.client) {
        this.$cookies.set('auth', accessToken, {
          maxAge: 60 * 60 * 24,
          secure: env === 'production'
        })
      } else {
        this.$cookies.remove('auth')
      }

      return Promise.resolve()
    } catch (error) {
      return Promise.reject(error)
    }
  },
  reset({ commit }) {
    commit('resetToken')
    this.$cookies.removeAll()
    return Promise.resolve()
  }
}
