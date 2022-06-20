<template>
  <div class="base" v-if="record">
    <div class="left-split">
      <div class="image">
        <img
          :src="baseUrl + 'record/image?id=' + this.record.id"
          alt=""
          class="background coverimage"
          id="background"
          @load="applyBlur"
        />
        <img
          :src="baseUrl + 'record/image?id=' + this.record.id"
          alt=""
          :class="
            'foreground coverimage' +
            (record.imageType === 'DEFAULT' ? ' noborder' : '')
          "
          id="foreground"
          @load="applyBorder"
          @click="selectImage"
        />
        <div class="delete-image-button">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="64"
            height="64"
            fill="currentColor"
            class="bi bi-trash trash-can"
            viewBox="0 0 16 16"
            @click="deleteImage(false)"
          >
            <path
              d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6z"
            />
            <path
              fill-rule="evenodd"
              d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z"
            />
          </svg>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="64"
            height="64"
            fill="currentColor"
            class="bi bi-cloud-download set-default-image"
            viewBox="0 0 16 16"
            @click="deleteImage(true)"
          >
            <path
              d="M4.406 1.342A5.53 5.53 0 0 1 8 0c2.69 0 4.923 2 5.166 4.579C14.758 4.804 16 6.137 16 7.773 16 9.569 14.502 11 12.687 11H10a.5.5 0 0 1 0-1h2.688C13.979 10 15 8.988 15 7.773c0-1.216-1.02-2.228-2.313-2.228h-.5v-.5C12.188 2.825 10.328 1 8 1a4.53 4.53 0 0 0-2.941 1.1c-.757.652-1.153 1.438-1.153 2.055v.448l-.445.049C2.064 4.805 1 5.952 1 7.318 1 8.785 2.23 10 3.781 10H6a.5.5 0 0 1 0 1H3.781C1.708 11 0 9.366 0 7.318c0-1.763 1.266-3.223 2.942-3.593.143-.863.698-1.723 1.464-2.383z"
            />
            <path
              d="M7.646 15.854a.5.5 0 0 0 .708 0l3-3a.5.5 0 0 0-.708-.708L8.5 14.293V5.5a.5.5 0 0 0-1 0v8.793l-2.146-2.147a.5.5 0 0 0-.708.708l3 3z"
            />
          </svg>
        </div>
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
        <PrimaryButton @click="toggleEditModal" v-text="'Edit record'" />
        <SecondaryButton @click="toggleDeleteModal" v-text="'Delete record'" />
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
    <input
      type="file"
      id="upload"
      style="display: none"
      @change="uploadSubmit"
    />
  </div>
  <div v-else>Loading...</div>
</template>

<script>
import EditPopup from "../components/EditPopup.vue";
import DeletePopup from "../components/DeletePopup.vue";
import ES from "../plugins/eventService";
import PrimaryButton from "../components/PrimaryButton.vue";
import SecondaryButton from "../components/SecondaryButton.vue";

export default {
  components: {
    EditPopup,
    DeletePopup,
    PrimaryButton,
    SecondaryButton,
  },
  data() {
    return {
      baseUrl: import.meta.env.VITE_HOST,
      record: null,
      originalRecord: null,
    };
  },
  created() {
    this.getRecord();
  },
  methods: {
    getRecord() {
      ES.getRecord(this.$route.params.id)
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
      this.$router.push("/artists/" + this.record.artist);
    },
    selectImage() {
      document.getElementById("upload").click();
    },
    uploadSubmit() {
      var formdata = new FormData();
      formdata.append("file", document.getElementById("upload").files[0]);
      ES.postRecordImage(this.record.id, formdata).then((res) => {
        if (res.ok) {
          document.getElementById("upload").value = "";
          const elements = document.getElementsByClassName("coverimage");
          for (let index = 0; index < elements.length; index++) {
            const element = elements[index];
            element.src = element.src + "&t=" + new Date().getTime();
          }
          this.applyBlur();
          this.applyBorder();
          document
            .getElementsByClassName("coverimage")[1]
            .classList.remove("noborder");
        } else {
          if (res.status === 415) {
            alert("Please select an image file!");
          }
        }
      });
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
    },
    saveEdit() {
      this.record.creator = null;
      ES.putRecord(this.record)
        .then((response) => {
          if (response.ok) {
            this.toggleEditModal();
          } else {
            alert("Sorry, that did not work. Please try again");
          }
        })
        .catch((error) => console.log(error));
    },
    deleteRecord() {
      ES.deleteRecord(this.record.id).then((response) => {
        if (response.ok) {
          this.$router.push("/records");
        } else {
          alert("Sorry, that did not work. Please try again");
        }
      });
    },
    deleteImage(none) {
      ES.deleteRecordImage(this.record.id, none)
        .then((res) => {
          const elements = document.getElementsByClassName("coverimage");
          for (let index = 0; index < elements.length; index++) {
            const element = elements[index];
            element.src = element.src + "&t=" + new Date().getTime();
          }
          if (!none)
            document
              .getElementsByClassName("coverimage")[1]
              .classList.add("noborder");
          else
            document
              .getElementsByClassName("coverimage")[1]
              .classList.remove("noborder");
          this.applyBlur();
          this.applyBorder();
          if (!res.ok) {
            alert("Sorry, that did not work");
          }
        })
        .catch(() => alert("Sorry, that did not work"));
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
  position: relative;
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
  color: var(--mild-color2);
  transition-duration: 0.5s;
}

.underline:hover::after,
.underline:focus::after {
  transform: translate3d(0, 0, 0);
  opacity: 1;
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
  background-color: var(--primary-color);
  opacity: 0;
  transition: opacity 300ms, transform 300ms;
  transform: translate3d(-100%, 0, 0);
}

.foreground {
  z-index: 1;
  position: relative;
  width: 85%;
  transition: border-radius 2s transform 0.5s;
  border: solid thin rgba(0, 0, 0, 0.2);
}
.foreground-blur {
  border-radius: 1rem;
  transition: border-radius 2s, transform 0.5s;
}
.background {
  z-index: 0;
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  width: 85%;
  height: auto;
  transition: filter 2s, transform 0.5s;

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
  transition: filter 2s, transform 0.5s;
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
.image:hover img {
  transform: translateX(-40%);
  transition: transform 0.5s;
}
.delete-image-button {
  position: absolute;
  top: 0;
  right: 10px;
  bottom: 0;
  width: 50%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  gap: 2rem;
  opacity: 0;
  transition: opacity 300ms;
}
.image:hover .delete-image-button {
  opacity: 1;
  transition: opacity 300ms;
}
.trash-can {
  transform: scale(1, 1);
  transition: transform 0.3s;
}
.trash-can:hover {
  color: var(--primary-color);
  transform: scale(1.1, 1.1);
  transition: transform 0.3s;
}
.set-default-image {
  transform: scale(1, 1);
  transition: transform 0.3s;
}
.set-default-image:hover {
  color: var(--primary-color);
  transform: scale(1.1, 1.1);
  transition: transform 0.3s;
}
.noborder {
  border: none;
}
</style>