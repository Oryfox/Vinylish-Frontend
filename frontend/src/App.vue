<script setup>
import {RouterLink} from "vue-router";</script>

<template>
  <nav>
    <img src="/icon.png" alt="Logo" />
    <!--<RouterLink to="/" class="rl">Home</RouterLink>
    <RouterLink to="/about" class="rl">About</RouterLink>-->
    <div @click="this.$router.go(-1)" class="back-button" v-if="this.$route.name !== 'records' && this.$route.name !== 'artists'"><svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-chevron-left" viewBox="0 0 16 16">
  <path fill-rule="evenodd" d="M11.354 1.646a.5.5 0 0 1 0 .708L5.707 8l5.647 5.646a.5.5 0 0 1-.708.708l-6-6a.5.5 0 0 1 0-.708l6-6a.5.5 0 0 1 .708 0z"/>
</svg></div>
    <RouterLink to="/records" class="rl">Records</RouterLink>
    <RouterLink to="/artists" class="rl">Artists</RouterLink>

    <div class="addButton" @click="this.$refs.current.createNew()" v-if="this.$route.name === 'records'">
      <svg
        xmlns="http://www.w3.org/2000/svg"
        width="22"
        height="22"
        fill="currentColor"
        class="bi bi-plus-circle"
        viewBox="0 0 16 16"
      >
        <path
          d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z"
        />
        <path
          d="M8 4a.5.5 0 0 1 .5.5v3h3a.5.5 0 0 1 0 1h-3v3a.5.5 0 0 1-1 0v-3h-3a.5.5 0 0 1 0-1h3v-3A.5.5 0 0 1 8 4z"
        />
      </svg>
    </div>
  </nav>
  <router-view v-slot="{ Component }">
    <transition name="fade" mode="out-in">
      <component :is="Component" ref="current"> </component>
    </transition>
  </router-view>
</template>

<style scoped>
nav {
  display: flex;
  background: #f0f0f0;
  height: 4rem;
  align-items: center;
}

.addButton {
  margin-left: auto;
  margin-right: 1rem;
  border-radius: 1rem;
  width: 2rem;
  height: 2rem;
  display: flex;
  align-items: center;
  justify-content: center;
  transition-duration: 0.3s;
}
.addButton:hover {
  background: white;
  transition-duration: 0.3s;
}

.rl {
  text-decoration: none;
  border-radius: 1rem;
  color: #5f00a2;
  margin-right: 2rem;
  padding: 0.5rem 1rem;
  transition-duration: .3s;
}
.rl:hover {
  transition-duration: .3s;
  background: white;
}

.back-button {
  text-decoration: none;
  border-radius: 1rem;
  color: #5f00a2;
  margin-right: 2rem;
  display: flex;
  align-items: center;
  justify-content: center;
  transition-duration: .3s;
  width: 2.5rem;
  height: 2.5rem;
  cursor: pointer;
}
.back-button:hover {
  transition-duration: .3s;
  background: white;
}

img {
  width: auto;
  height: 80%;
  margin-right: 2rem;
  margin-left: 0.5rem;
}
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
