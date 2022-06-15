<template>
  <div style="display: flex; justify-content: center">
    <div class="base">
      <input
        type="text"
        v-model="username"
        placeholder="Email"
        @keyup.enter="login"
        @keydown="resetMessage"
      />
      <input
        type="password"
        v-model="password"
        placeholder="Password"
        @keyup.enter="login"
        @keydown="resetMessage"
      />
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
      username: "",
      password: "",
      message: null,
    };
  },
  methods: {
    login() {
      this.message = null;
      if (this.username !== "" && this.password !== "") {
        ES.login({
          email: this.username,
          password: this.password,
        }).then((res) => {
          if (res.ok) {
            res.text().then((token) => {
              this.$cookies.set("token", token);
              emitter.emit("tokenSet", token);
              this.username = "";
              this.password = "";
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
      } else {
        this.message = "Email and Password must contain a value!";
      }
    },
    register() {
      this.message = null;
      if (this.username !== "" && this.password !== "") {
        ES.register({
          email: this.username,
          password: this.password,
        }).then((res) => {
          if (res.ok) {
            this.username = "";
            this.password = "";
            this.message = "Account created";
          } else {
            alert("That did not work");
          }
        });
      } else {
        this.message = "Email and Password must contain a value!";
      }
    },
    resetMessage() {
      this.message = null;
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
.btn-primary {
  background: var(--bright-color);
  border-color: var(--bright-color);
}
.btn-primary:focus {
  box-shadow: 1px 1px 10px 1px var(--bright-color);
}
.btn-outline-primary {
  border-color: var(--bright-color);
  color: var(--bright-color);
}
.btn-outline-primary:hover {
  background: #f0f0f0;
}
.btn-outline-primary:focus {
  box-shadow: 1px 1px 10px 1px var(--bright-color);
}
</style>