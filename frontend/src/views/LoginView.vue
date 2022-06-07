<template>
  <div style="display: flex; justify-content: center">
    <div class="base">
      <input type="text" v-model="username" placeholder="Email" />
      <input type="password" v-model="password" placeholder="Password" />
      <span class="message">{{ message }}</span>

      <div class="btn-group">
        <button class="btn btn-outline-primary" @click="register">
          Register
        </button>
        <button class="btn btn-primary" @click="login">Login</button>
      </div>
    </div>
  </div>
</template>

<script>
import ES from "../plugins/eventService";
import emitter from "tiny-emitter/instance";
export default {
  data() {
    return {
      username: null,
      password: null,
      message: null,
    };
  },
  methods: {
    login() {
      this.message = null;
      ES.login({
        email: this.username,
        password: this.password,
      }).then((res) => {
        if (res.ok) {
          res.text().then((token) => {
            this.$cookies.set("token", token);
            emitter.emit("tokenSet", token);
            this.username = null;
            this.password = null;
            this.$router.push("/");
          });
        } else {
          if (res.status === 401) {
            this.message = "Either email or password is wrong";
          } else {
            alert("That did not work");
          }
        }
      });
    },
    register() {
      this.message = null;
      ES.register({
        email: this.username,
        password: this.password,
      }).then((res) => {
        if (res.ok) {
          this.username = null;
          this.password = null;
          this.message = "Account created";
        } else {
          alert("That did not work");
        }
      });
    },
  },
};
</script>

<style scoped>
.base {
  display: flex;
  flex-direction: column;
  width: 40%;
  gap: 1rem;
  margin-top: 3rem;
  border: solid 1px rgba(0, 0, 0, 0.2);
  border-radius: 1rem;
  padding: 1rem;
}
.message {
  color: red;
  font-size: 0.8rem;
}
.btn-outline-primary:hover {
  background: #f0f0f0;
  color: var(--bs-blue);
}
</style>