<template>
  <div class="signInWithEmail">
    <div class="email">
      <label for="email">Email</label>
      <input type="email" name="email" v-model="email" />
    </div>
    <div class="password">
      <label for="password">Password</label>
      <input type="password" name="password" v-model="password" />
    </div>
    <button @click="login">Sign In</button>
  </div>
</template>
<script>
export default {
  components: {},
  data: function() {
    return {
      email: 'aaa',
      password: null
    }
  },
  methods: {
    login() {
      const { email, password } = this
      this.$store.dispatch('login', {
        email: email,
        password: password
      })
    },
    test() {
      this.$store.dispatch('test')
    }
  }
}
</script>
