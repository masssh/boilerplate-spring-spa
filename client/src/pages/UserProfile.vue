<template>
  <q-page>
    <div class="row justify-center">
      <div class="col-6">
        <q-card>
          <q-card-section>
            <div class="text-h5 text-center q-my-lg">Profile</div>
            <q-list>
              <q-item>
                <q-item-section>Email</q-item-section>
                <q-item-section>{{ email }}</q-item-section>
              </q-item>
            </q-list>

            <q-separator spaced />

            <q-list>
              <q-item>
                <q-item-section>User Name</q-item-section>
                <q-item-section>{{ userName }}</q-item-section>
              </q-item>
              <q-item>
                <q-item-section></q-item-section>
                <q-item-section side>
                  <q-btn
                    to="user/profile/edit"
                    class="q-my-lg"
                    label="Edit"
                    no-caps
                  />
                </q-item-section>
              </q-item>

              <q-separator spaced />

              <q-item>
                <q-item-section>Password</q-item-section>
                <q-item-section>********</q-item-section>
              </q-item>
              <q-item>
                <q-item-section></q-item-section>
                <q-item-section side>
                  <q-btn
                    to="user/password/edit"
                    class="q-my-lg"
                    label="Edit"
                    no-caps
                  />
                </q-item-section>
              </q-item>
            </q-list>
          </q-card-section>
        </q-card>
        <q-separator class="q-my-md" />
        <q-card>
          <q-card-section>
            <q-list>
              <q-item>
                <q-item-section>
                  Delete Account
                </q-item-section>
                <q-item-section side>
                  <q-btn
                    @click="deleteUser"
                    color="negative"
                    label="Delete"
                    no-caps
                  />
                </q-item-section>
              </q-item>
            </q-list>
          </q-card-section>
        </q-card>
      </div>
    </div>
  </q-page>
</template>
<script>
import { mapState } from 'vuex'
export default {
  computed: mapState({
    userName: (state) => state.user.userName,
    email: (state) => state.user.email
  }),
  data() {
    return {}
  },
  created: async function() {
    await this.$store.dispatch('getUser')
  },
  methods: {
    deleteUser() {
      this.$store
        .dispatch('deleteUser', { router: this.$router })
        .then(() => this.$router.push('/'))
    }
  }
}
</script>
<style lang="sass" scoped>
.vertical-middle
  margin: 0 auto
</style>
