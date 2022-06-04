<template>
  <div class="base" @click="cancel">
    <div class="modal-pane" v-on:click.stop>
      <div class="modalheader">
        <span v-if="getInput">Enter artist name and album title</span>
        <span v-else>Select filling mode</span>
        <button class="btn btn-close" @click="cancel"></button>
      </div>
      <div class="modal-content-pane">
        <div class="input-holder" v-if="getInput">
          <div class="property">
            <label for="artist">Artist name</label>
            <input
              type="text"
              id="title"
              class="inputfield"
              v-model="this.artist"
            />
          </div>
          <div class="property">
            <label for="title">Album title</label>
            <input
              type="text"
              id="title"
              class="inputfield"
              v-model="this.title"
            />
          </div>

          <div class="confirm-group">
            <button class="btn btn-secondary" @click="automatic">Cancel</button>
            <button class="btn btn-primary" @click="submit">Submit</button>
          </div>
        </div>
        <div class="btn-group" v-else>
          <button class="btn btn-primary" @click="automatic">Automatic</button>
          <button class="btn btn-outline-primary" @click="manual">
            Manual
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    record: null,
  },
  data() {
    return {
      getInput: false,
      artist: null,
      title: null,
    };
  },
  methods: {
    cancel() {
      this.getInput = false;
      this.artist = null;
      this.title = null;
      this.$emit("cancel");
    },
    automatic() {
      this.getInput = !this.getInput;
    },
    manual() {
      this.$emit("success");
    },
    submit() {
      fetch(
        "http://localhost:8080/record/auto?artist=" +
          encodeURI(this.artist) +
          "&title=" +
          encodeURI(this.title)
      ).then((res) => {
        if (res.ok) {
          res.json().then((newrec) => {
            this.record.artist = newrec.artist;
            this.record.title = newrec.title;
            this.record.color = newrec.color;
            this.record.bootleg = newrec.bootleg;
            this.record.limited = newrec.limited;
            this.record.releaseYear = newrec.releaseYear;
            this.record.tracks = newrec.tracks;
            this.record.imageType = newrec.imageType;

            this.getInput = false;
            this.artist = null;
            this.title = null;
            this.$emit("success");
          });
        } else {
          if (res.status === 404) {
            alert("No album could be found");
          } else {
            alert("Whoops.. Something went wrong");
          }
        }
      });
    },
  },
};
</script>

<style scoped>
.base {
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
.modal-pane {
  width: 40%;
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
  display: flex;
  flex-direction: column;
  overflow-y: scroll;
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
.btn-close {
  margin-left: auto;
}
.btn-outline-primary:hover {
  background: #f0f0f0;
  color: var(--bs-blue);
}
.input-holder {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}
.property {
  display: flex;
  align-items: center;
  flex-direction: row;
}
.property .inputfield {
  margin-left: auto;
  flex-grow: 1;
}
.property label {
  width: 10rem;
  text-align: right;
  padding-right: 0.7rem;
}
.confirm-group {
  margin-left: auto;
  display: flex;
  gap: 0.5rem;
}
</style>