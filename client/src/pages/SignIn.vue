<template>
  <q-page>
    <div class="row justify-center">
      <div class="col-6">
        <q-card>
          <q-card-section class="center">
            <SignInWithEmail id="signInWithEmail" />
            <q-separator class="q-my-md" />
            <SignInWithGoogle id="signInWithGoogle" />
          </q-card-section>
        </q-card>
      </div>
    </div>
  </q-page>
</template>
<script>
import SignInWithEmail from 'components/SignInWithEmail'
import SignInWithGoogle from 'components/SignInWithGoogle'
export default {
  components: { SignInWithEmail, SignInWithGoogle }
}
</script>
