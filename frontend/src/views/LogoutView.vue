<template>
  <div>Logging out</div>
</template>

<script>
import ES from "../plugins/eventService";
import emitter from "tiny-emitter/instance";
export default {
  created() {
    ES.logout().then((res) => {
      if (res.ok) {
        this.$cookies.remove("token");
        emitter.emit("tokenRemove");
        this.$router.push("/");
      }
    });
  },
};
</script>

<style>
</style>