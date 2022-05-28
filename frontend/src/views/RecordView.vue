<template>
  <div class="root" v-if="error === null">
    <div class="orderholder">
      <label for="order">Order by</label>
      <select name="order" id="order" v-model="sortby" @change="sort">
        <option value="unsorted">Unsorted</option>
        <option value="artist">Artist</option>
        <option value="title">Title</option>
      </select>
    </div>

    <div class="list" v-if="records.length > 0">
      <RecordItem v-for="record in records" :key="record.id" :record="record" />
    </div>
    <div class="record-list-empty" v-else>
      Such emptiness
    </div>
  </div>
  <div v-else>{{error}}</div>
</template>

<script>
import RecordItem from "../components/RecordItem.vue";

export default {
  components: {
    RecordItem,
  },
  data() {
    return {
      records: [],
      sortby: "unsorted",
      error: null
    };
  },
  created() {
    this.getRecords();
  },
  methods: {
    getRecords() {
      fetch("http://localhost:8080/record")
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
          (a, b) =>
            a.artist.toLowerCase() >
            b.artist.toLowerCase()
        );
      } else if (this.sortby === "title") {
        this.records.sort(
          (a, b) => a.title.toLowerCase() > b.title.toLowerCase()
        );
      } else {
        this.getRecords();
      }
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
  padding: 0 1rem;
}
.orderholder {
  padding: 1em;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}
</style>