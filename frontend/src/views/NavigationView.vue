<template>
  <nav>
    <img src="/icon.png" alt="Logo" @click="this.$router.push('/about')"/>
    <!--<RouterLink to="/" class="rl">Home</RouterLink>
    <RouterLink to="/about" class="rl">About</RouterLink>-->
    <div v-if="cookie" class="navb">
      <div class="router-links">
        <div
            @click="this.$router.go(-1)"
            class="rl"
            v-if="
          this.$route.name !== 'records' &&
          this.$route.name !== 'artists' &&
          this.$route.name !== 'logout' &&
          this.$route.name !== 'login'
        "
        >
          <svg
              xmlns="http://www.w3.org/2000/svg"
              width="16"
              height="16"
              fill="currentColor"
              class="bi bi-chevron-left"
              viewBox="0 0 16 16"
          >
            <path
                fill-rule="evenodd"
                d="M11.354 1.646a.5.5 0 0 1 0 .708L5.707 8l5.647 5.646a.5.5 0 0 1-.708.708l-6-6a.5.5 0 0 1 0-.708l6-6a.5.5 0 0 1 .708 0z"
            />
          </svg>
          Back
        </div>

        <RouterLink to="/records" class="rl underline">Records</RouterLink>
        <RouterLink to="/artists" class="rl underline">Artists</RouterLink>
      </div>

      <div class="navbar-buttons">
        <div
          class="navbar-button"
          @click="createNew"
          v-if="this.$route.name === 'records'"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="32"
            height="32"
            fill="currentColor"
            class="bi bi-plus create-button"
            viewBox="0 0 16 16"
          >
            <path
              d="M8 4a.5.5 0 0 1 .5.5v3h3a.5.5 0 0 1 0 1h-3v3a.5.5 0 0 1-1 0v-3h-3a.5.5 0 0 1 0-1h3v-3A.5.5 0 0 1 8 4z"
            />
          </svg>
        </div>

        <div class="navbar-button" @click="this.$router.push('/logout')">
          <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" fill="currentColor" class="bi bi-person-circle user-button" viewBox="0 0 16 16">
            <path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0z"/>
            <path fill-rule="evenodd" d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8zm8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1z"/>
          </svg>
        </div>
      </div>
    </div>
  </nav>
</template>

<script>
import emitter from "tiny-emitter/instance";
export default {
  props: {
    parent: null,
  },
  data() {
    return {
      cookie: null,
    };
  },
  created() {
    this.cookie = this.$cookies.get("token");
    emitter.on("tokenSet", (token) => (this.cookie = token));
    emitter.on("tokenRemove", () => (this.cookie = null));
  },
  methods: {
    createNew() {
      emitter.emit("createNew");
    },
  },
};
</script>

<style scoped>
nav {
  display: flex;
  background: #f0f0f0;
  height: 4rem;
  align-items: center;
}
.navb {
  display: flex;
  width: 100%;
}

.navbar-buttons {
  margin-left: auto;
  margin-right: 1rem;
  display: flex;
  gap: 0.5rem;
}

.navbar-button {
  border-radius: 1rem;
  width: 2rem;
  height: 2rem;
  display: flex;
  align-items: center;
  justify-content: center;
  transition-duration: 0.3s;
}
.create-button {
  transform: rotate(0deg) scale(1, 1);
  transition: transform 400ms;
  cursor: pointer;
}
.navbar-button:hover .create-button {
  transform: rotate(90deg) scale(1.2, 1.2);
}
.user-button {
  transform: scale(1, 1);
  transition: transform 400ms;
  cursor: pointer;
}
.navbar-button:hover .user-button {
  transform: scale(1.2, 1.2);
}

.router-links {
  display: flex;
}
.rl {
  color: var(--text-color);
  text-decoration: none;
  margin-right: 1.4rem;
  transition-duration: 0.3s;
  display: flex;
  align-items: center;
  position: relative;
  font-weight: 500;
  cursor: pointer;
  stroke: var(--text-color);
}
/*.rl::after  {*/
/*  display: inline-block;*/
/*  content: "";*/
/*  position: absolute;*/
/*  width: 2px;*/
/*  height: 20px;*/
/*  right: -0.7rem;*/
/*  border-right: 2px solid var(--text-color);*/
/*}*/
/*.rl:last-child {*/
/*  padding-right: 0;*/
/*}*/
/*.rl:last-child::after {*/
/*  display: none;*/
/*}*/
.underline:hover::after {
  transform: translate3d(0, 0, 0);
  opacity: 1;
}
.underline {
  transition-duration: 0.5s;
  cursor: pointer;
  overflow: hidden;
  position: relative;
}
.underline:after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 0.1em;
  background-color: var(--primary-color);
  opacity: 0;
  transition: opacity 300ms, transform 300ms;
  transform: translate3d(-100%, 0, 0);
}

img {
  width: auto;
  height: 80%;
  margin-right: 2rem;
  margin-left: 0.5rem;
  transform: rotate(0deg);
  transition: transform 400ms;
  cursor: pointer;
}
img:hover {
  transform: rotate(-45deg);
  transition: transform 400ms;
}
</style>