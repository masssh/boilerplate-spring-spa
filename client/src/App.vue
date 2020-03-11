<template>
  <v-app id="app">
    <v-app-bar app>
      <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>
      <v-btn to="/dashboard" class="mx-2" icon
        ><font-awesome-icon icon="home" size="lg"
      /></v-btn>
      <v-toolbar-title>{{ title }}</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn
        v-if="userId === null"
        to="/signIn"
        color="primary"
        class="no-text-transform mr-2"
        >Sing In</v-btn
      >
      <v-btn
        v-if="userId === null"
        to="/signUp"
        color="secondary"
        class="no-text-transform"
        >Sing Up</v-btn
      >
      <v-btn icon v-if="userId !== null">
        <font-awesome-icon icon="user" size="lg" />
      </v-btn>
    </v-app-bar>

    <v-navigation-drawer v-model="drawer" absolute temporary>
      <v-list-item>
        <v-list-item-content>Sample</v-list-item-content>
      </v-list-item>
    </v-navigation-drawer>

    <v-content>
      <router-view />
    </v-content>

    <v-footer>
      <v-spacer></v-spacer>
      <div>&copy; {{ new Date().getFullYear() }}</div>
    </v-footer>
  </v-app>
</template>

<script>
import { mapState } from 'vuex'
export default {
  computed: mapState({
    title: (state) => state.title,
    userId: (state) => state.userId
  }),
  data: function() {
    return {
      drawer: false
    }
  }
}
</script>

<style lang="stylus" scoped>
#app
  font-family Avenir, Helvetica, Arial, sans-serif
a
  text-decoration : none
.v-btn
  text-transform : none !important
</style>
