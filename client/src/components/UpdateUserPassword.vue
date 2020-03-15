<template>
  <q-form ref="form" class="q-gutter-md center">
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
    <q-btn @click="signUp" label="Update" no-caps />
  </q-form>
</template>
<script>
export default {
  components: {},
  data: function() {
    return {
      password: null,
      passwordConfirm: null
    }
  },
  methods: {
    signUp() {
      const { password } = this
      if (this.$refs.form.validate()) {
        this.$store.dispatch('updateUser', {
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
