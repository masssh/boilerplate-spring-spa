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
          <q-btn v-if="!userId" label="Sign In" to="/signIn" no-caps flat />
          <q-btn v-if="!userId" label="Sign Up" to="/signUp" no-caps flat />
          <q-btn v-if="userId" icon="account_circle" flat round>
            <q-menu
              :offset="[0, 5]"
              transition-show="scale"
              transition-hide="scale"
              square
            >
              <q-list style="min-width: 300px">
                <q-item to="/account" clickable v-close-popup>
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
    userId: (state) => state.userId
  }),
  data() {
    return {
      drawer: false
    }
  },
  methods: {
    logout() {
      this.$store.dispatch('logout', { router: this.$router })
    }
  }
}
</script>
<style lang="sass" scoped>
.center
  text-align: right
</style>
