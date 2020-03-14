<template>
  <div class="center">
    <p>or</p>
    <a href="http://localhost:8080/oauth2/authorization/google">
      <q-img :src="src" class="google"></q-img>
    </a>
  </div>
</template>
<script>
export default {
  data: function() {
    return {
      src: 'statics/btn_google_signin_light_normal_web.png'
    }
  }
}
</script>
<style lang="sass" scoped>
.center
  text-align: center
.google
  width: 30%
  height: 30%
</style>
