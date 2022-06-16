<template>
  <div class="split">
    <div class="list">
      <ArtistItem
        v-for="artist in artists"
        :key="artist"
        :artist="artist"
        @click="selectedArtist = artist"
      />
    </div>
  </div>
</template>

<script>
import ES from "../plugins/eventService";
import ArtistItem from "../components/ArtistItem.vue";
export default {
  components: {
    ArtistItem,
  },
  data() {
    return {
      artists: [],
      selectedArtist: null,
    };
  },
  created() {
    this.getArtists();
  },
  methods: {
    getArtists() {
      ES.getArtists()
        .then((res) => res.json())
        .then((json) => {
          this.artists = json;
          this.artists.sort((a, b) => a.toLowerCase() > b.toLowerCase());
        });
    },
  },
};
</script>

<style scoped>
.split {
  display: flex;
  max-height: 100%;
  overflow-y: scroll;
}
.list {
  display: flex;
  flex-direction: column;
  width: 30%;
  overflow-y: scroll;
}
</style>