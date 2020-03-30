<template>
  <q-page>
    <div class="row justify-center">
      <div class="col-6">
        <q-card>
          <q-card-section class="center">
            <div class="text-h5 text-center q-my-lg">
              Send email to reset your password
            </div>
            <ForgotUserPassword />
          </q-card-section>
        </q-card>
      </div>
    </div>
  </q-page>
</template>
<script>
import ForgotUserPassword from 'components/ForgotUserPassword'
export default {
  components: { ForgotUserPassword },
  data: () => {
    return {}
  },
  created: function() {
    const url = new URL(location.href)
    if (url.searchParams.has('q')) {
      this.verificationHash = url.searchParams.get('q')
    }
  }
}
</script>
