<template>
  <div class="base" v-if="record">
    <div class="left-split">
      <div class="image">
        <img
          :src="
            'http://localhost:8080/record/image?id=' +
            this.record.id +
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
            'http://localhost:8080/record/image?id=' +
            this.record.id +
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
          <tr v-if="this.record.releaseYear">
            <td>Release year:</td>
            <td>{{ this.record.releaseYear }}</td>
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
        <b>{{ this.record.title }}</b>
      </span>
      <span class="artist underline" @click="goToArtist">{{
        this.record.artist
      }}</span>

      <div
        class="tracks"
        v-if="this.record.tracks && this.record.tracks.length > 0"
      >
        <div class="track" v-for="item in this.record.tracks" :key="item.rank">
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

    <EditPopup
      class="edit-modal disabled"
      id="edit-modal"
      action="Editing"
      :record="this.record"
      @save="saveEdit"
      @cancel="cancelEdit"
    />
    <DeletePopup
      class="delete-modal disabled"
      id="delete-modal"
      @cancel="toggleDeleteModal"
      @confirm="deleteRecord"
    />
  </div>
  <div v-else>Loading...</div>
</template>

<script>
import EditPopup from "../components/EditPopup.vue"
import DeletePopup from "../components/DeletePopup.vue"
export default {
  components: {
    EditPopup,
    DeletePopup
  },
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
      this.$router.push("/artist/" + this.record.artist);
    },
    goToYoutubeSearch(song) {
      window
        .open(
          "https://youtube.com/results?search_query=" +
            encodeURIComponent(this.record.artist) +
            "+" +
            encodeURIComponent(song),
          "_blank"
        )
        .focus();
    },
    toggleEditModal() {
      if (!document.getElementById("edit-modal").classList.toggle("disabled")) {
        this.originalRecord = JSON.parse(JSON.stringify(this.record));
      }
    },
    toggleDeleteModal() {
      document.getElementById("delete-modal").classList.toggle("disabled");
    },
    cancelEdit() {
      this.record = this.originalRecord;
      this.toggleEditModal();
      this.applyBlur();
      this.applyBorder();
    },
    saveEdit() {
      console.log(JSON.stringify(this.record))
      fetch("http://localhost:8080/record", {
        method: "PUT",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(this.record)
      }).then(response => {
        if (response.ok) {
          this.toggleEditModal();
        } else {
          alert("Sorry, that did not work. Please try again")
        }
      }).catch(error => console.log(error));
    },
    deleteRecord() {
      fetch("http://localhost:8080/record?id=" + this.record.id, {
        method: "DELETE"
      }).then(response => {
        if (response.ok) {
          this.$router.push("/records");
        } else {
          alert("Sorry, that did not work. Please try again");
        }
      })
    }
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
.button-holder {
  padding-top: 1rem;
  margin-left: 1%;
  width: 85%;
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
.delete-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 10;
  backdrop-filter: blur(5px);
  -webkit-backdrop-filter: blur(5px);
}
.disabled {
  display: none;
}
</style>