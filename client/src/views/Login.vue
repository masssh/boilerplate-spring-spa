<template>
  <div class="login">
    <h1>This is an login page</h1>
    <div>
      <label>Email</label>
      <input type="email" name="email" value="" v-model="email" />
    </div>
    <div>
      <label>Password</label>
      <input type="password" name="password" value="" v-model="password" />
    </div>
    <button type="button" @click="onSubmit">Submit</button>
  </div>
</template>

<script>
import { login } from "@/api";

export default {
  name: "login",
  components: {},
  data: function() {
    return {
      email: "",
      password: ""
    };
  },
  created: function() {},
  methods: {
    onSubmit: function() {
      login({
        email: this.email,
        password: this.password
      });
    }
  }
};
</script>
