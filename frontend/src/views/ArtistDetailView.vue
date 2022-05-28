<template>
  <div v-if="artist">{{ artist }}</div>
</template>

<script>
export default {
  data() {
    return {
      artist: null,
    };
  },
  created() {
    this.getArtist();
  },
  methods: {
    getArtist() {
      fetch("http://localhost:8080/artist?id=" + this.$route.params.name)
        .then((res) => res.json())
        .then((data) => (this.artist = data))
        .catch((error) => console.log(error));
    },
  },
};
</script>

<style>
</style>