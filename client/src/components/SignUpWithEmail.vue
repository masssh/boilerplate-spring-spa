<template>
  <v-container fluid class="signUpWithEmail">
    <v-form ref="form">
      <v-row>
        <v-text-field
          v-model="userName"
          label="User Name"
          :rules="userNameRules"
          :validate-on-blur="true"
        ></v-text-field>
      </v-row>
      <v-row>
        <v-text-field
          v-model="email"
          label="Email"
          :rules="emailRules"
          :validate-on-blur="true"
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
      <v-row>
        <v-text-field
          v-model="passConfirmation"
          label="Confirm Password"
          type="password"
          :rules="passConfirmationRules"
          :validate-on-blur="true"
        ></v-text-field>
      </v-row>
      <v-row justify="center">
        <v-btn color="primary" @click="signUp">Sing Up</v-btn>
      </v-row>
    </v-form>
  </v-container>
</template>
<script>
export default {
  components: {},
  data: function() {
    return {
      userName: null,
      userNameRules: [(v) => !!v || 'User Name is required'],
      email: null,
      emailRules: [
        (v) => !!v || 'Email is required',
        (v) => /.+@.+\..+/.test(v) || 'E-mail must be valid'
      ],
      pass: null,
      passRules: [(v) => !!v || 'Password is required'],
      passConfirmation: null,
      passConfirmationRules: [
        (v) => !!v || 'Password is required',
        (v) => this.pass === v || 'Password not matched'
      ]
    }
  },
  methods: {
    signUp() {
      const { userName, email, pass } = this
      if (this.$refs.form.validate()) {
        this.$store.dispatch('signUp', {
          userName: userName,
          email: email,
          password: pass
        })
      }
    }
  }
}
</script>
<style lang="stylus" scoped></style>
