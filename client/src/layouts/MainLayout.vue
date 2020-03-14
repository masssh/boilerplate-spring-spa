<template>
  <q-layout view="hHh Lpr lFf">
    <q-header elevated>
      <q-toolbar>
        <q-btn
          flat
          round
          icon="menu"
          aria-label="Menu"
          @click="drawer = !drawer"
        />

        <q-toolbar-title>
          {{ title }}
        </q-toolbar-title>

        <div>
          <q-btn label="Sign In" to="/signIn" no-caps flat />
          <q-btn label="Sign Up" to="/signUp" no-caps flat />
        </div>
      </q-toolbar>
    </q-header>

    <q-drawer v-model="drawer" @mouseleave="drawer = false" overlay>
      <q-list>
        <q-item-label header>
          Links
        </q-item-label>
      </q-list>
    </q-drawer>

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
    title: (state) => state.title
  }),
  data() {
    return {
      drawer: false
    }
  }
}
</script>
<style lang="sass" scoped>
.center
  text-align: right
</style>
