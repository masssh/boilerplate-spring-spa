<template>
  <div class="home">
    <h1>This is home page</h1>
    <button @click="test">test</button>
  </div>
</template>
<script>
export default {
  components: {},
  methods: {
    test() {
      this.$store.dispatch('test')
    }
  }
}
</script>
