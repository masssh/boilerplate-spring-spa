<template>
  <v-layout column justify-center align-center>
    <v-flex xs12 sm8 md6>
      <v-card-title class="headline">
        This is welcome page
      </v-card-title>
      <button @click="login">Login</button>
    </v-flex>
  </v-layout>
</template>

<script>
export default {
  components: {},
  methods: {
    login(e) {
      this.$store.dispatch('login')
    }
  }
}
</script>
