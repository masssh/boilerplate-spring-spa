<template>
  <v-container fluid class="signInWithEmail">
    <v-form ref="form">
      <v-row>
        <v-text-field
          v-model="email"
          label="Email"
          :rules="emailRules"
          :validate-on-blur="true"
          ur="true"
        ></v-text-field>
      </v-row>
      <v-row>
        <v-text-field
          v-model="pass"
          label="Password"
          type="password"
          :rules="passRules"
          :validate-on-blur="true"
        ></v-text-field>
      </v-row>
      <v-row justify="center">
        <v-btn color="primary" class="no-text-transform" @click="login"
          >Sign in with Email</v-btn
        >
      </v-row>
    </v-form>
  </v-container>
</template>
<script>
export default {
  components: {},
  data: function() {
    return {
      email: null,
      emailRules: [
        (v) => !!v || 'Email is required',
        (v) => /.+@.+\..+/.test(v) || 'E-mail must be valid'
      ],
      pass: null,
      passRules: [(v) => !!v || 'Password is required']
    }
  },
  methods: {
    login() {
      const { email, pass } = this
      this.$store.dispatch('login', {
        email: email,
        password: pass
      })
    }
  }
}
</script>
<style lang="stylus" scoped>
.no-text-transform
  text-transform : none
</style>
