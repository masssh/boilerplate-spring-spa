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
    <q-btn @click="update" label="Update" no-caps />
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
    async update() {
      try {
        const { password } = this
        if (this.$refs.form.validate()) {
          await this.$store.dispatch('updateUser', {
            userName: null,
            password: password
          })
        }
        this.$router.push('/user')
      } catch (error) {
        this.$q.dialog({
          title: 'Alert',
          message: 'Failed to update user password'
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
