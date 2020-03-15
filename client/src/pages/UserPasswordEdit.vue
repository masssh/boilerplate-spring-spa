<template>
  <q-page>
    <div class="row justify-center">
      <div class="col-6">
        <q-card>
          <q-card-section class="center">
            <UpdateUserPassword />
          </q-card-section>
        </q-card>
      </div>
    </div>
  </q-page>
</template>
<script>
import UpdateUserPassword from 'components/UpdateUserPassword'
export default {
  components: { UpdateUserPassword }
}
</script>
