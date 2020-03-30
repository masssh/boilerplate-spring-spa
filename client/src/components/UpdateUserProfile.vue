<template>
  <q-form ref="form" class="q-gutter-md center">
    <q-input
      v-model="userName"
      label="* User Name"
      lazy-rules
      :rules="[(v) => !!v || 'User Name is required']"
    />
    <q-btn @click="update" label="Update" no-caps />
  </q-form>
</template>
<script>
import { mapState } from 'vuex'
export default {
  components: {},
  computed: mapState({
    originalUserName: (state) => state.user.userName
  }),
  data() {
    return {
      userName: null
    }
  },
  created: function() {
    this.userName = this.originalUserName
  },
  methods: {
    async update() {
      try {
        const { userName } = this
        if (this.$refs.form.validate()) {
          await this.$store.dispatch('updateUser', {
            userName: userName
          })
        }
        this.$router.push('/user')
      } catch (error) {
        this.$q.dialog({
          title: 'Alert',
          message: 'Failed to update user profile'
        })
      }
    }
  }
}
</script>
<style lang="sass" scoped>
.center
  text-align: center
</style>
