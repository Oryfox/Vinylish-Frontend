<template>
  <div class="root" v-if="error === null">
    <div class="orderholder">
      <label for="order">Order by</label>
      <select name="order" id="order" v-model="sortby" @change="sort">
        <option value="unsorted">Unsorted</option>
        <option value="artist">Artist</option>
        <option value="title">Title</option>
      </select>

      <div class="searchField">
        <label for="searchField"
          ><svg
            xmlns="http://www.w3.org/2000/svg"
            width="16"
            height="16"
            fill="currentColor"
            class="bi bi-search"
            viewBox="0 0 16 16"
          >
            <path
              d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001c.03.04.062.078.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1.007 1.007 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0z"
            /></svg
        ></label>
        <input
          id="searchField"
          type="text"
          v-model="search"
          placeholder="Search..."
        />
      </div>

      <div class="toggleGrid">
        <b>List view</b>
        <label class="switch">
          <input type="checkbox" v-model="grid" @change="gridsettings" />
          <span :class="grid ? 'slider-active round' : 'slider round'"></span>
        </label>
        <b>Grid view</b>
      </div>
    </div>

    <div v-if="records.length > 0">
      <div class="grid" v-if="grid">
        <GridItem
          v-for="record in searchRecords"
          :key="record.id"
          :record="record"
        />
      </div>
      <div class="list" v-else>
        <RecordItem
          v-for="record in searchRecords"
          :key="record.id"
          :record="record"
        />
      </div>
    </div>

    <div class="record-list-empty" v-else>
      <span style="margin-right: 0.4rem">Such emptiness</span>
      <svg
        xmlns="http://www.w3.org/2000/svg"
        width="16"
        height="16"
        fill="currentColor"
        class="bi bi-emoji-frown"
        viewBox="0 0 16 16"
      >
        <path
          d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z"
        />
        <path
          d="M4.285 12.433a.5.5 0 0 0 .683-.183A3.498 3.498 0 0 1 8 10.5c1.295 0 2.426.703 3.032 1.75a.5.5 0 0 0 .866-.5A4.498 4.498 0 0 0 8 9.5a4.5 4.5 0 0 0-3.898 2.25.5.5 0 0 0 .183.683zM7 6.5C7 7.328 6.552 8 6 8s-1-.672-1-1.5S5.448 5 6 5s1 .672 1 1.5zm4 0c0 .828-.448 1.5-1 1.5s-1-.672-1-1.5S9.448 5 10 5s1 .672 1 1.5z"
        />
      </svg>
    </div>

    <EditPopup
      class="new-modal disabled"
      id="new-modal"
      action="Creating"
      :record="this.newRecord"
      @save="saveNew"
      @cancel="cancelNew"
    />

    <CreationSelect
      id="creation-select"
      @cancel="toggleNewRecordModal"
      @success="success"
      :record="this.newRecord"
      v-if="selectVisible"
    />
  </div>
  <div v-else>{{ error }}</div>
</template>

<script>
import RecordItem from "../components/RecordItem.vue";
import EditPopup from "../components/EditPopup.vue";
import CreationSelect from "../components/CreationSelect.vue";
import ES from "../plugins/eventService";
import emitter from "tiny-emitter/instance";
import GridItem from "../components/GridItem.vue";
export default {
  components: {
    RecordItem,
    EditPopup,
    CreationSelect,
    GridItem,
  },
  data() {
    return {
      records: [],
      sortby: "unsorted",
      error: null,
      newRecord: null,
      selectVisible: false,
      grid: false,
      search: "",
    };
  },
  created() {
    emitter.on("createNew", () => {
      this.toggleNewRecordModal();
    });
    this.getRecords();
    this.grid = this.$cookies.get("grid") === "Yes";
  },
  computed: {
    searchRecords() {
      return this.records.filter(
        (record) =>
          record.title.toLowerCase().includes(this.search.toLowerCase()) ||
          record.artist.toLowerCase().includes(this.search.toLowerCase())
      );
    },
  },
  methods: {
    getRecords() {
      ES.getRecords()
        .then((response) => response.json())
        .then((data) => {
          this.records = data;
        })
        .catch((error) => {
          console.log(error);
          this.error = "Could not establish connection to server";
        });
    },
    sort() {
      if (this.sortby === "artist") {
        this.records.sort(
          (a, b) => a.artist.toLowerCase() > b.artist.toLowerCase()
        );
      } else if (this.sortby === "title") {
        this.records.sort(
          (a, b) => a.title.toLowerCase() > b.title.toLowerCase()
        );
      } else {
        this.getRecords();
      }
    },
    toggleNewRecordModal() {
      this.selectVisible = !this.selectVisible;
      if (this.selectVisible) {
        this.newRecord = {
          artist: null,
          title: null,
          tracks: [
            {
              rank: 1,
              title: null,
            },
          ],
          releaseYear: null,
          imageType: "NONE",
          color: null,
          limited: false,
          bootleg: false,
        };
      }
    },
    toggleEditRecordModal() {
      document.getElementById("new-modal").classList.toggle("disabled");
    },
    saveNew() {
      ES.postRecord(this.newRecord).then((response) => {
        if (response.ok) {
          this.newRecord = null;
          this.toggleEditRecordModal();
          response.json().then((r) => this.records.push(r));
        } else {
          alert("Sorry that didn't work");
        }
      });
    },
    cancelNew() {
      this.toggleEditRecordModal();
      this.newRecord = null;
    },
    success() {
      this.toggleNewRecordModal();
      this.toggleEditRecordModal();
    },
    gridsettings() {
      this.$cookies.set("grid", this.grid === false ? "No" : "Yes");
    },
  },
};
</script>

<style scoped>
.root {
  display: flex;
  flex-direction: column;
}
.list {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  padding: 0 1rem 1rem 1rem;
}
.grid {
  display: inline-flex;
  flex-wrap: wrap;
  padding: 0 1rem 1rem 1rem;
  gap: 1.5rem;
  flex: 0 1;
  justify-content: center;
}
.orderholder {
  padding: 1em;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}
.record-list-empty {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20vh;
  color: rgba(112, 88, 64, 0.338);
}
.new-modal {
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
.searchField {
  margin-left: auto;
  display: flex;
  gap: 0.5rem;
  align-items: center;
}
.searchField label {
  color: gray;
}
.toggleGrid {
  display: flex;
  align-items: center;
  gap: 8px;
}
.switch {
  position: relative;
  display: inline-block;
  width: 60px;
  height: 34px;
}
.switch input {
  opacity: 0;
  width: 0;
  height: 0;
}
.slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: var(--primary-color);
  -webkit-transition: 0.4s;
  transition: 0.4s;
}
.slider:before {
  position: absolute;
  content: "";
  height: 26px;
  width: 26px;
  left: 4px;
  bottom: 4px;
  background-color: white;
  -webkit-transition: 0.4s;
  transition: 0.4s;
}
.slider-active {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: var(--text-color);
  -webkit-transition: 0.4s;
  transition: 0.4s;
}
.slider-active:before {
  position: absolute;
  content: "";
  height: 26px;
  width: 26px;
  left: 4px;
  bottom: 4px;
  background-color: white;
  -webkit-transition: 0.4s;
  transition: 0.4s;
}
input:checked + .slider:before {
  -webkit-transform: translateX(26px);
  -ms-transform: translateX(26px);
  transform: translateX(26px);
}
.slider.round {
  border-radius: 34px;
}
.slider.round:before {
  border-radius: 50%;
}

input:checked + .slider-active:before {
  -webkit-transform: translateX(26px);
  -ms-transform: translateX(26px);
  transform: translateX(26px);
}
.slider-active.round {
  border-radius: 34px;
}
.slider-active.round:before {
  border-radius: 50%;
}
</style>