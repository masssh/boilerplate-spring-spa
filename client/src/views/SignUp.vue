<template>
  <v-card width="500" class="mx-auto mt-10">
    <v-card-text>
      <v-container class="signUp">
        <SignUpWithEmail />
      </v-container>
    </v-card-text>
  </v-card>
</template>
<script>
import SignUpWithEmail from '@/components/SignUpWithEmail'

export default {
  components: {
    SignUpWithEmail
  },
  methods: {}
}
</script>
<style lang="stylus" scoped></style>
