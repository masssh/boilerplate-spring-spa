<template>
  <q-form ref="form" class="q-gutter-md center">
    <q-input
      v-model="userName"
      label="* User Name"
      lazy-rules
      :rules="[(v) => !!v || 'User Name is required']"
    />
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
    <q-input
      v-model="passwordConfirm"
      label="* Confirm Password"
      type="password"
      lazy-rules
      :rules="[
        (v) => !!v || 'Password is required',
        (v) => this.password === v || 'Password not matched'
      ]"
    />
    <q-btn @click="signUp" label="Sign Up" no-caps />
  </q-form>
</template>
<script>
export default {
  components: {},
  data: function() {
    return {
      userName: null,
      email: null,
      password: null,
      passwordConfirm: null
    }
  },
  methods: {
    signUp() {
      const { userName, email, password } = this
      if (this.$refs.form.validate()) {
        this.$store.dispatch('signUp', {
          userName: userName,
          email: email,
          password: password,
          router: this.$router
        })
      }
    }
  }
}
</script>
<style lang="sass" scoped>
.center
  text-align: center
</style>
