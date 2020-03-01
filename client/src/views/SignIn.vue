<template>
  <div class="signIn">
    <h1><font-awesome-icon icon="user-circle" /> Sign in</h1>
    <SignInWithGoogle />
  </div>
</template>
<script>
import SignInWithGoogle from '@/components/SignInWithGoogle'

export default {
  components: {
    SignInWithGoogle
  },
  methods: {}
}
</script>
