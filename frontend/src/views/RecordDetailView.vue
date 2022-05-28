<template>
  <div class="base" v-if="record">
    <div class="left-split">
      <div class="image">
        <img
          :src="
            'http://localhost:8080/album/image?id=' +
            this.record.album.id +
            '&time=' +
            Date.now()
          "
          alt=""
          class="background"
          id="background"
          @load="applyBlur"
        />
        <img
          :src="
            'http://localhost:8080/album/image?id=' +
            this.record.album.id +
            '&time=' +
            Date.now()
          "
          alt=""
          class="foreground"
          id="foreground"
          @load="applyBorder"
        />
      </div>

      <div>
        <table class="infopanel">
          <tr v-if="this.record.album.releaseYear">
            <td>Release year:</td>
            <td>{{ this.record.album.releaseYear }}</td>
          </tr>
          <tr>
            <td>Vinyl color:</td>
            <td v-if="this.record.color">{{ this.record.color }}</td>
            <td v-else>Black</td>
          </tr>
          <tr>
            <td>Limited:</td>
            <td v-if="this.record.limited">Yes</td>
            <td v-else>No</td>
          </tr>
          <tr>
            <td>Bootleg:</td>
            <td v-if="this.record.bootleg">Yes</td>
            <td v-else>No</td>
          </tr>
        </table>
      </div>

      <div class="btn-group button-holder">
        <button class="btn btn-warning" @click="toggleEditModal">
          Edit record
        </button>
        <button class="btn btn-danger" @click="toggleDeleteModal">
          Delete record
        </button>
      </div>
    </div>
    <div class="right-split">
      <span class="title">
        <b>{{ this.record.album.title }}</b>
      </span>
      <span class="artist underline" @click="goToArtist">{{
        this.record.album.artist.name
      }}</span>

      <div
        class="tracks"
        v-if="this.record.album.tracks && this.record.album.tracks.length > 0"
      >
        <div
          class="track"
          v-for="item in this.record.album.tracks"
          :key="item.rank"
        >
          <span class="track-rank">
            <b>{{ item.rank }}.</b>
          </span>
          <span
            class="track-title underline"
            @click="goToYoutubeSearch(item.title)"
            >{{ item.title }}</span
          >
        </div>
      </div>
    </div>

    <div class="edit-modal disabled" id="edit-modal">
      <div class="modal-pane">
        <div class="modalheader">
          <span
            >Editing
            <span class="highlighted">{{ this.record.album.title }}</span> by
            <span class="highlighted">{{
              this.record.album.artist.name
            }}</span></span
          >
          <button
            class="btn-close close-modal-button"
            @click="toggleEditModal"
          />
        </div>

        <div class="modal-content-pane">
          <div class="property-list">
            <div class="property">
              <label for="title">Album title</label>
              <input type="text" id="title" v-model="this.record.album.title" />
            </div>
            <div class="property">
              <label for="title">Artist name</label>
              <input
                type="text"
                id="title"
                v-model="this.record.album.artist.name"
              />
            </div>
            <div class="property">
              <label for="title">Album release year</label>
              <input
                type="number"
                id="title"
                v-model="this.record.album.releaseYear"
              />
            </div>
            <div class="property">
              <label for="title">Vinyl color</label>
              <input type="text" id="title" v-model="this.record.color" />
            </div>
            <div class="property">
              <label for="title">Limited</label>
              <input type="checkbox" id="title" v-model="this.record.limited" />
            </div>
            <div class="property">
              <label for="title">Bootleg</label>
              <input type="checkbox" id="title" v-model="this.record.bootleg" />
            </div>
          </div>

          <div class="track-list">
            Tracklist
          </div>

          <div class="control-buttons">
            <button class="btn btn-secondary">Cancel</button>
            <button class="btn btn-primary">Save changes</button>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div v-else>Loading...</div>
</template>

<script>
export default {
  data() {
    return {
      record: null,
      originalRecord: null
    };
  },
  created() {
    this.getRecord();
  },
  methods: {
    getRecord() {
      fetch("http://localhost:8080/record?id=" + this.$route.params.id)
        .then((res) => res.json())
        .then((data) => (this.record = data))
        .catch((error) => console.log(error));
    },
    applyBlur() {
      document.getElementById("background").classList.toggle("background-blur");
    },
    applyBorder() {
      document.getElementById("foreground").classList.toggle("foreground-blur");
    },
    goToArtist() {
      this.$router.push("/artist/" + this.record.album.artist.id);
    },
    goToYoutubeSearch(song) {
      window
        .open(
          "https://youtube.com/results?search_query=" +
            encodeURIComponent(this.record.album.artist.name) +
            "+" +
            encodeURIComponent(song),
          "_blank"
        )
        .focus();
    },
    toggleEditModal() {
      if (!document.getElementById("edit-modal").classList.toggle("disabled")) {
        this.originalRecord = this.record;
      }
    },
    toggleDeleteModal() {
      document.getElementById("delete-modal").classList.toggle("disabled");
    },
  },
};
</script>

<style scoped>
.base {
  padding: 2rem;
  display: flex;
}
.image {
  width: 100%;
}
.left-split {
  width: 30%;
}
.right-split {
  width: 70%;
  max-width: 70%;
  display: flex;
  flex-direction: column;
  /* border-radius: 1rem;
  box-shadow: 2px 2px 7px 3px rgba(0, 0, 0, 0.2);
  padding: 0.5rem; */
}

.title {
  font-size: 2rem;
}
.artist {
  color: darkslategray;
  transition-duration: 0.5s;
}

.underline:hover::after,
.underline:focus::after {
  transform: translate3d(0, 0, 0);
}
.underline {
  transition-duration: 0.5s;
  cursor: pointer;
  overflow: hidden;
  position: relative;
  display: block;
  align-self: flex-start;
}
.underline:after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 0.1em;
  background-color: darkslategray;
  opacity: 0;
  transition: opacity 300ms, transform 300ms;
  opacity: 1;
  transform: translate3d(-100%, 0, 0);
}

.foreground {
  z-index: 1;
  position: relative;
  width: 85%;
  transition-duration: 2s;
}
.foreground-blur {
  border-radius: 1rem;
  transition-duration: 2s;
}
.background {
  z-index: 0;
  position: absolute;
  width: 25%;
  height: auto;
  transition-duration: 2s;

  backface-visibility: hidden;
  transform: translate3d(0, 0, 0);
  -webkit-backface-visibility: hidden;
  -moz-backface-visibility: hidden;
  -webkit-transform: translate3d(0, 0, 0);
  -moz-transform: translate3d(0, 0, 0);
}
.background-blur {
  filter: blur(25px);
  -webkit-filter: blur(25px);
  -moz-filter: blur(25px);
  -o-filter: blur(25px);
  -ms-filter: blur(25px);
  transition-duration: 2s;
}
.infopanel {
  margin-top: 2rem;
  margin-left: 1%;
  width: 85%;
}
.tracks {
  margin-top: 1.5rem;
}
.track {
  display: flex;
  gap: 0.6rem;
}
.track-rank {
  width: 1.2rem;
}
.edit-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  justify-content: center;
  z-index: 10;
  backdrop-filter: blur(5px);
  -webkit-backdrop-filter: blur(5px);
}
.disabled {
  display: none;
}
.button-holder {
  padding-top: 1rem;
  margin-left: 1%;
  width: 85%;
}
.modal-pane {
  width: 80%;
  margin: 2.5rem 0;
  box-shadow: 2px 2px 13px 2px rgba(0, 0, 0, 0.2);
  border-radius: 1rem;
  background-color: white;
  display: flex;
  flex-direction: column;
}
.modal-content-pane {
  padding: 1rem;
  position: relative;
  flex-grow: 1;
}
.modalheader {
  background: #f0f0f0;
  border-top-left-radius: 1rem;
  border-top-right-radius: 1rem;
  display: flex;
  padding: 0 1rem;
  align-items: center;
  height: 4rem;
  font-size: 24px;
}
.highlighted {
  color: maroon;
}
.close-modal-button {
  margin-left: auto;
}
.property-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}
.property {
  display: flex;
  align-items: center;
}
.property input {
  margin-left: auto;
  flex-grow: 1;
}
.property label {
  width: 10rem;
  text-align: right;
  padding-right: 0.7rem;
}
.track-list {
  display: flex;
  flex-direction: column;
  border-style: solid;
  border-width: 1px;
  border-radius: 0.5rem;
  border-color: rgba(0, 0, 0, 0.2);
  box-shadow: 1px 1px 5px rgba(0, 0, 0, 0.2);
  padding: 0.5rem;

  margin-top: 1rem;
}
.control-buttons {
  display: flex;
  position: absolute;
  bottom: 1rem;
  right: 1rem;
  gap: 1rem;
}
</style>