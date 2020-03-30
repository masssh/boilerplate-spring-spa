<template>
  <q-layout view="hHh Lpr lFf">
    <q-header elevated>
      <q-toolbar>
        <!-- Uncomment this block if using drawer menu
        <q-btn
          flat
          round
          icon="menu"
          aria-label="Menu"
          @click="drawer = !drawer"
        />
        <q-drawer v-model="drawer" @mouseleave="drawer = false" overlay>
          <q-list>
            <q-item-label header>
              Links
            </q-item-label>
          </q-list>
        </q-drawer>
        -->

        <q-btn icon="grain" to="/" flat round />

        <q-toolbar-title>
          {{ title }}
        </q-toolbar-title>

        <div>
          <q-btn v-if="!login" label="Sign In" to="/login" no-caps flat />
          <q-btn v-if="!login" label="Sign Up" to="/signUp" no-caps flat />
          <q-btn v-if="login" icon="account_circle" flat round>
            <q-menu
              :offset="[0, 5]"
              transition-show="scale"
              transition-hide="scale"
              square
            >
              <q-list style="min-width: 300px">
                <q-item>
                  <q-item-section avatar>
                    <q-item-label>
                      <q-icon name="account_circle" size="lg" class="q-ma-md" />
                    </q-item-label>
                  </q-item-section>
                  <q-item-section>
                    <q-item-label>{{ userName }}</q-item-label>
                    <q-item-label caption>{{ email }}</q-item-label>
                  </q-item-section>
                </q-item>
                <q-item to="/user" clickable v-close-popup>
                  <q-item-section avatar>
                    <q-icon name="settings" size="sm" />
                  </q-item-section>
                  <q-item-section>Settings</q-item-section>
                </q-item>
                <q-item @click="logout" clickable v-close-popup>
                  <q-item-section avatar>
                    <q-icon name="highlight_off" size="sm" />
                  </q-item-section>
                  <q-item-section>Logout</q-item-section>
                </q-item>
              </q-list>
            </q-menu>
          </q-btn>
        </div>
      </q-toolbar>
    </q-header>

    <q-page-container class="q-ma-md">
      <router-view />
    </q-page-container>

    <q-footer class="q-px-md center"
      >&copy; {{ new Date().getFullYear() }}</q-footer
    >
  </q-layout>
</template>

<script>
import { mapState } from 'vuex'
export default {
  name: 'MainLayout',
  computed: mapState({
    title: (state) => state.title,
    login: (state) => state.user.login,
    userName: (state) => state.user.userName,
    email: (state) => state.user.email
  }),
  data() {
    return {
      drawer: false
    }
  },
  methods: {
    logout() {
      this.$store.dispatch('logout').then(() => this.$router.push('/'))
    }
  }
}
</script>
<style lang="sass" scoped>
.center
  text-align: right
</style>
