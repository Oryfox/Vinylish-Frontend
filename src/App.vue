<script setup>
import { RouterLink } from "vue-router";
import NavigationView from "./views/NavigationView.vue";
import Player from "./components/Player.vue";
</script>

<template>
  <NavigationView :parent="this" />
  <router-view v-slot="{ Component }">
    <transition name="fade" mode="out-in">
      <component :is="Component" ref="current"> </component>
    </transition>
  </router-view>
  <Player />
</template>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
