<template>
  <nav v-if="innerWidth >= 900">
    <img src="/icon.png" alt="Logo" @click="this.$router.push('/about')" />
    <!--<RouterLink to="/" class="rl">Home</RouterLink>
    <RouterLink to="/about" class="rl">About</RouterLink>-->
    <div v-if="cookie" class="navb">
      <div class="router-links">
        <div
          @click="this.$router.go(-1)"
          class="rl underline"
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

        <div class="navbar-button" @click="userClick" id="userIcon">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="22"
            height="22"
            fill="currentColor"
            class="bi bi-person-circle user-button"
            viewBox="0 0 16 16"
          >
            <path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0z" />
            <path
              fill-rule="evenodd"
              d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8zm8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1z"
            />
          </svg>
        </div>
      </div>
    </div>
    <div class="popup-wrapper" v-show="showUserPopup" @click="userClick">
      <div class="userPopup" id="userPopup" v-if="user" v-on:click.stop>
        <h4 v-if="user.displayName">Hey, {{ user.displayName }}!</h4>
        <h4 v-else>Hello there!</h4>

        <div class="userPopupButtonWrapper">
          <div class="popup-button profile-button" @click="goToProfile">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="50"
              height="50"
              fill="currentColor"
              class="bi bi-person-badge"
              viewBox="0 0 16 16"
            >
              <path
                d="M6.5 2a.5.5 0 0 0 0 1h3a.5.5 0 0 0 0-1h-3zM11 8a3 3 0 1 1-6 0 3 3 0 0 1 6 0z"
              />
              <path
                d="M4.5 0A2.5 2.5 0 0 0 2 2.5V14a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2V2.5A2.5 2.5 0 0 0 11.5 0h-7zM3 2.5A1.5 1.5 0 0 1 4.5 1h7A1.5 1.5 0 0 1 13 2.5v10.795a4.2 4.2 0 0 0-.776-.492C11.392 12.387 10.063 12 8 12s-3.392.387-4.224.803a4.2 4.2 0 0 0-.776.492V2.5z"
              />
            </svg>
          </div>
          <div class="popup-button settings-button" @click="goToSettings">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="50"
              height="50"
              fill="currentColor"
              class="bi bi-gear-wide-connected"
              viewBox="0 0 16 16"
            >
              <path
                d="M7.068.727c.243-.97 1.62-.97 1.864 0l.071.286a.96.96 0 0 0 1.622.434l.205-.211c.695-.719 1.888-.03 1.613.931l-.08.284a.96.96 0 0 0 1.187 1.187l.283-.081c.96-.275 1.65.918.931 1.613l-.211.205a.96.96 0 0 0 .434 1.622l.286.071c.97.243.97 1.62 0 1.864l-.286.071a.96.96 0 0 0-.434 1.622l.211.205c.719.695.03 1.888-.931 1.613l-.284-.08a.96.96 0 0 0-1.187 1.187l.081.283c.275.96-.918 1.65-1.613.931l-.205-.211a.96.96 0 0 0-1.622.434l-.071.286c-.243.97-1.62.97-1.864 0l-.071-.286a.96.96 0 0 0-1.622-.434l-.205.211c-.695.719-1.888.03-1.613-.931l.08-.284a.96.96 0 0 0-1.186-1.187l-.284.081c-.96.275-1.65-.918-.931-1.613l.211-.205a.96.96 0 0 0-.434-1.622l-.286-.071c-.97-.243-.97-1.62 0-1.864l.286-.071a.96.96 0 0 0 .434-1.622l-.211-.205c-.719-.695-.03-1.888.931-1.613l.284.08a.96.96 0 0 0 1.187-1.186l-.081-.284c-.275-.96.918-1.65 1.613-.931l.205.211a.96.96 0 0 0 1.622-.434l.071-.286zM12.973 8.5H8.25l-2.834 3.779A4.998 4.998 0 0 0 12.973 8.5zm0-1a4.998 4.998 0 0 0-7.557-3.779l2.834 3.78h4.723zM5.048 3.967c-.03.021-.058.043-.087.065l.087-.065zm-.431.355A4.984 4.984 0 0 0 3.002 8c0 1.455.622 2.765 1.615 3.678L7.375 8 4.617 4.322zm.344 7.646.087.065-.087-.065z"
              />
            </svg>
          </div>
        </div>

        <PrimaryButton
          class="logout-button"
          @click="logout"
          v-text="'Logout'"
        />
      </div>
    </div>
  </nav>
  <nav
    v-else-if="cookie && innerWidth <= 900"
    style="display: flex; padding: 1rem"
  >
    <PrimaryButton @click="logout" v-text="'Logout'" style="flex: 1" />
  </nav>
</template>

<script>
import emitter from "tiny-emitter/instance";
import ES from "../plugins/eventService";
import PrimaryButton from "../components/PrimaryButton.vue";
export default {
  components: {
    PrimaryButton,
  },
  props: {
    parent: null,
  },
  data() {
    return {
      cookie: null,
      showUserPopup: false,
      user: null,
      innerWidth: window.innerWidth,
    };
  },
  created() {
    this.cookie = this.$cookies.get("token");
    emitter.on("tokenSet", (token) => (this.cookie = token));
    emitter.on("tokenRemove", () => (this.cookie = null));
  },
  mounted() {
    window.addEventListener("resize", this.onResize);
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.onResize);
  },
  methods: {
    createNew() {
      emitter.emit("createNew");
    },
    userClick() {
      ES.getUser()
        .then((res) => res.json())
        .then((json) => (this.user = json));
      this.showUserPopup = !this.showUserPopup;
    },
    goToProfile() {
      this.showUserPopup = false;
      this.$router.push("/profile");
    },
    goToSettings() {
      this.showUserPopup = false;
      this.$router.push("/settings");
    },
    logout() {
      this.user = null;
      this.showUserPopup = false;
      this.$router.push("/logout");
    },
    onResize() {
      this.innerWidth = window.innerWidth;
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
.userPopup {
  width: 20rem;
  height: 15.5rem;
  background: #fefefe;
  position: absolute;
  right: 18px;
  /* top: 4rem; */
  filter: drop-shadow(1px 1px 10px rgba(0, 0, 0, 0.4));
  border-top-left-radius: 0.5rem;
  border-bottom-left-radius: 0.5rem;
  border-bottom-right-radius: 0.5rem;

  display: flex;
  flex-direction: column;
  padding: 1rem;
}
.userPopup::after {
  content: "";
  z-index: 5;
  position: absolute;
  top: -10px;
  right: 4px;
  width: 20px;
  height: 20px;
  background: #fefefe;
  transform: rotate(45deg);
}
.popup-wrapper {
  position: fixed;
  top: 4rem;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 10;
}
.logout-button {
  margin-top: auto;
  transition-duration: 0.3s;
}
.logout-button:hover {
  box-shadow: 1px 1px 10px 1px var(--primary-color);
  transition-duration: 0.3s;
}
.userPopupButtonWrapper {
  display: flex;
  flex: 1;
  padding-bottom: 0.7rem;
  gap: 0.7rem;
}
.popup-button {
  border: solid thin rgba(0, 0, 0, 0.2);
  flex: 1;
  height: 100%;
  border-radius: 0.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  cursor: pointer;
}
.popup-button::after {
  position: absolute;
  left: 0;
  bottom: 3px;
  right: 0;
  text-align: center;
}
.popup-button svg {
  transform: translateY(0px);
  transition: transform 300ms;
}
.popup-button:hover svg {
  transform: translateY(-13px);
  transition: transform 500ms;
}
.profile-button::after {
  content: "Profile";
}
.settings-button::after {
  content: "Settings";
}
</style>