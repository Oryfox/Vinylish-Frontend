<template>
  <div class="base" @click="navigateTo">
    <img :src="baseUrl + 'record/image?id=' + this.record.id" alt="Cover" />
    <b>{{ this.record.title }}</b>
    <span>{{ this.record.artist }}</span>
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
  methods: {
    navigateTo() {
      this.$router.push("/records/" + this.record.id);
    },
  },
};
</script>

<style scoped>
.base {
  display: flex;
  flex-direction: column;
  width: 16rem;
  transition-duration: 0.3s;
  cursor: pointer;
}
.base:hover {
  filter: brightness(1.15);
  transition-duration: 0.3s;
}
.base:hover img {
  box-shadow: 2px 2px 7px 1px rgba(0, 0, 0, 0.2);
  transition-duration: 0.3s;
}
img {
  border-radius: 0.5rem;
  border: solid thin rgba(0, 0, 0, 0.2);
  margin-bottom: 0.5rem;
  transition-duration: 0.3s;
  max-width: 16rem;
  max-height: auto;
}
span {
  color: var(--mild-color2);
}
</style>