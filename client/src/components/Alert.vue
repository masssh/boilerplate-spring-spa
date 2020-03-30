<template>
  <q-dialog v-model="value" ref="dialog">
    <q-card>
      <q-card-section>
        <div class="text-h6">{{ title }}</div>
      </q-card-section>

      <q-card-section class="q-pt-none">
        {{ message }}
      </q-card-section>

      <q-card-actions align="right">
        <q-btn flat label="OK" color="primary" v-close-popup />
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>
<script>
export default {
  components: {},
  props: ['title', 'message'],
  data: function() {
    return {
      value: false
    }
  },
  methods: {
    show() {
      this.$refs.dialog.show()
    }
  }
}
</script>
<style lang="sass" scoped></style>
