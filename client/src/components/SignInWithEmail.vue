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
    <q-btn @click="signIn" label="Sign In" no-caps />
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
    signIn() {
      const { email, password } = this
      this.$store.dispatch('signIn', {
        email: email,
        password: password
      })
    }
  }
}
</script>
<style lang="sass" scoped>
.center
  text-align: center
</style>
