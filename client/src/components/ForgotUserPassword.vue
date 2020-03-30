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
    <q-btn @click="reset" label="Reset Password" no-caps />
  </q-form>
</template>
<script>
export default {
  components: {},
  data: function() {
    return {
      email: null
    }
  },
  methods: {
    async reset() {
      const { email } = this
      if (this.$refs.form.validate()) {
        try {
          await this.$store.dispatch('forgotPassword', {
            email: email
          })
        } catch (error) {
          this.$q.dialog({
            title: 'Alert',
            message: 'Failed to request password reset'
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
