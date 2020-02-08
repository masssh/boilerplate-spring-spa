export const state = () => ({
  isLoggedIn: false
})

export const mutations = {
  login(state, response) {
    state.isLoggedIn = true
  }
}

export const actions = {
  async login({ commit }, { onSuccess, onFailure }) {
    const response = await this.$axios.$post('/api/login', {
      email: '1577935387@example.com',
      password: '12345678'
    })
    if (status === 200) {
      console.log('success')
      commit('login', response)
      this.$router.push(onSuccess)
    } else {
      console.log('fail', onFailure)
      this.$router.push(onFailure)
    }
  }
}
