<template>
  <div @click="cancel">
    <div class="background" v-on:click.stop>
      <div class="btn-holder">
        <button class="btn btn-secondary" @click="cancel">Cancel</button>
        <button class="btn btn-warning" @click="confirm">
          Confirm deletion
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  methods: {
      cancel() {
          this.$emit('cancel')
      },
      confirm() {
          this.$emit('confirm')
      }
  }
}
</script>

<style scoped>
.background {
  background: white;
  padding: 1rem;
  border: solid 1px rgba(0, 0, 0, 0.2);
  border-radius: 1rem;
  box-shadow: 2px 2px 13px 2px rgba(0, 0, 0, 0.2);
}
.btn-holder {
  display: flex;
  gap: 1rem;
}
</style>