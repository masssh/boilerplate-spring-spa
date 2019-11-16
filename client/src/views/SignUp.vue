<template>
  <div class="signup">
    <h1>This is an sign up page</h1>
    <div>
      <label>User Name</label>
      <input type="text" name="userName" value="" v-model="userName" />
    </div>
    <div>
      <label>Password</label>
      <input type="password" name="password" value="" v-model="password" />
    </div>
    <div>
      <label>Email</label>
      <input type="email" name="email" value="" v-model="email" />
    </div>
    <button type="button" @click="onSubmit">Submit</button>
  </div>
</template>

<script>
import { signup } from "@/api";

export default {
  name: "signup",
  components: {},
  data: function() {
    return {
      userName: "",
      password: "",
      email: ""
    };
  },
  created: function() {},
  methods: {
    onSubmit: function() {
      signup({
        userName: this.userName,
        password: this.password,
        email: this.email
      });
    }
  }
};
</script>
