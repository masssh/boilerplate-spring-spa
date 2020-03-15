<template>
  <q-page>
    <div class="row justify-center">
      <div class="col-6">
        <q-card>
          <q-card-section class="center">
            <div class="text-h5 text-center q-my-lg">Edit Profile</div>
            <UpdateUserProfile />
          </q-card-section>
        </q-card>
      </div>
    </div>
  </q-page>
</template>
<script>
import UpdateUserProfile from 'components/UpdateUserProfile'
export default {
  components: { UpdateUserProfile }
}
</script>
