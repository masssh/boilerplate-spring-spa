<template>
  <q-page>
    <div class="row justify-center">
      <div class="col-6">
        <q-card>
          <q-card-section class="center">
            <SignUpWithEmail />
          </q-card-section>
        </q-card>
      </div>
    </div>
  </q-page>
</template>
<script>
import SignUpWithEmail from 'components/SignUpWithEmail'
export default {
  components: { SignUpWithEmail }
}
</script>
