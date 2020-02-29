<template>
  <div class="home">
    <h1>This is home page</h1>
    <button @click="login">Login</button>
    <button @click="test">test</button>
  </div>
</template>
<script>
export default {
  components: {},
  methods: {
    login() {
      this.$store.dispatch('login', {
        email: '1577935387@example.com',
        password: '12345678'
      })
    },
    test() {
      this.$store.dispatch('test')
    }
  }
}
</script>
