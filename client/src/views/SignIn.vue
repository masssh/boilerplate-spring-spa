<template>
  <v-card width="500" class="mx-auto mt-10">
    <v-card-text>
      <v-container fluid class="signIn">
        <v-row justify="center">
          <SignInWithEmail />
        </v-row>
        <v-row justify="center">
          <v-divider class="my-3" />
        </v-row>
        <v-row justify="center">
          <SignInWithGoogle />
        </v-row>
      </v-container>
    </v-card-text>
  </v-card>
</template>
<script>
import SignInWithEmail from '@/components/SignInWithEmail'
import SignInWithGoogle from '@/components/SignInWithGoogle'

export default {
  components: {
    SignInWithEmail,
    SignInWithGoogle
  },
  methods: {}
}
</script>
<style lang="stylus" scoped></style>
