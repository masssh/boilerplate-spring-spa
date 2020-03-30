<template>
  <q-page>
    <div class="row justify-center">
      <div class="col-6">
        <q-card>
          <q-card-section class="center">
            <div class="text-h5 text-center q-my-lg">Reset Password</div>
            <ResetUserPassword />
          </q-card-section>
        </q-card>
      </div>
    </div>
  </q-page>
</template>
<script>
import ResetUserPassword from 'components/ResetUserPassword'
export default {
  components: { ResetUserPassword },
  data: () => {
    return {
      verificationHash: null
    }
  },
  created: function() {
    const url = new URL(location.href)
    if (url.searchParams.has('q')) {
      this.verificationHash = url.searchParams.get('q')
    }
  }
}
</script>
