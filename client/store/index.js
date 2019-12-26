export const state = () => ({
  isLoggedIn: false
})

export const mutations = {
  login(state, response) {
    state.isLoggedIn = true
    console.log(response)
  }
}

export const actions = {
  async login({ commit }) {
    const response = await this.$axios.$post(
      'http://localhost:8080/api/login',
      { email: 'hello@example.com', password: 12345678 }
    )
    commit('login', response)
  }
}

