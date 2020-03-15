<template>
  <q-form class="q-gutter-md center">
    <q-input
      v-model="email"
      label="* Email"
      lazy-rules
      :rules="[
        (v) => !!v || 'Email is required',
        (v) => /.+@.+\..+/.test(v) || 'E-mail must be valid'
      ]"
    />
    <q-input
      v-model="password"
      label="* Password"
      type="password"
      lazy-rules
      :rules="[(v) => !!v || 'Password is required']"
    />
    <q-btn @click="login" label="Sign In" no-caps />
  </q-form>
</template>
<script>
export default {
  components: {},
  data: function() {
    return {
      email: null,
      password: null
    }
  },
  methods: {
    login() {
      const { email, password } = this
      this.$store.dispatch('login', {
        email: email,
        password: password,
        router: this.$router
      })
    }
  }
}
</script>
<style lang="sass" scoped>
.center
  text-align: center
</style>
