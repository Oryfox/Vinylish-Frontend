<template>
  <div class="base">
    <img :src="baseUrl + 'record/image?id=' + record.id" alt="Cover Image" />
    <b>{{ record.title }}</b>
    <span>{{ record.artist }}</span>
  </div>
</template>

<script>
export default {
  props: {
    record: null,
  },
  data() {
    return {
      baseUrl: import.meta.env.VITE_HOST,
    };
  },
};
</script>

<style scoped>
.base {
  display: flex;
  flex-direction: column;
  padding: 1rem;
}
img {
  border-radius: 0.5rem;
  border: solid thin rgba(0, 0, 0, 0.2);
  width: 100%;
  height: auto;
}
</style>