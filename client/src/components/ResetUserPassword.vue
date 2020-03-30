<template>
  <q-form ref="form" class="q-gutter-md center">
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
    <q-btn @click="update" label="Reset Password" no-caps />
  </q-form>
</template>
<script>
export default {
  components: {},
  data: function() {
    return {
      email: null,
      password: null,
      passwordConfirm: null,
      verificationHash: null
    }
  },
  created: function() {
    const url = new URL(location.href)
    if (url.searchParams.has('q')) {
      this.verificationHash = url.searchParams.get('q')
    }
  },
  methods: {
    async update() {
      const { email, password, verificationHash } = this
      if (this.$refs.form.validate()) {
        try {
          await this.$store.dispatch('resetPassword', {
            email: email,
            password: password,
            verificationHash: verificationHash
          })
          this.$router.push('/')
        } catch (error) {
          this.$q.dialog({
            title: 'Alert',
            message: 'Failed to reset email'
          })
        }
      }
    }
  }
}
</script>
<style lang="sass" scoped>
.center
  text-align: center
</style>
