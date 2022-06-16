<template>
  <div class="base" v-if="user">
    <svg
      xmlns="http://www.w3.org/2000/svg"
      width="200"
      height="200"
      fill="currentColor"
      class="bi bi-person-circle pic"
      viewBox="0 0 16 16"
      v-if="user.imageType == 'DEFAULT'"
    >
      <path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0z" />
      <path
        fill-rule="evenodd"
        d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8zm8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1z"
      />
    </svg>
    <img
      :src="baseUrl + 'user/image?token=' + this.$cookies.get('token')"
      alt="Your Image"
      class="pic"
      v-else
    />
    <div class="name" v-if="user.displayName">{{ user.displayName }}</div>
    <div class="empty-name name" v-else>
      User
      <svg
        xmlns="http://www.w3.org/2000/svg"
        width="24"
        height="24"
        fill="currentColor"
        class="bi bi-pencil-square edit-button"
        viewBox="0 0 16 16"
      >
        <path
          d="M15.502 1.94a.5.5 0 0 1 0 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 0 1 .707 0l1.293 1.293zm-1.75 2.456-2-2L4.939 9.21a.5.5 0 0 0-.121.196l-.805 2.414a.25.25 0 0 0 .316.316l2.414-.805a.5.5 0 0 0 .196-.12l6.813-6.814z"
        />
        <path
          fill-rule="evenodd"
          d="M1 13.5A1.5 1.5 0 0 0 2.5 15h11a1.5 1.5 0 0 0 1.5-1.5v-6a.5.5 0 0 0-1 0v6a.5.5 0 0 1-.5.5h-11a.5.5 0 0 1-.5-.5v-11a.5.5 0 0 1 .5-.5H9a.5.5 0 0 0 0-1H2.5A1.5 1.5 0 0 0 1 2.5v11z"
        />
      </svg>
    </div>
  </div>
</template>

<script>
import ES from "../plugins/eventService";
export default {
  data() {
    return {
      user: null,
    };
  },
  created() {
    this.getUser();
  },
  methods: {
    getUser() {
      ES.getUser()
        .then((res) => res.json())
        .then((json) => {
          this.user = json;
        });
    },
    anim() {
      document.getElementById("svg").classList.toggle("pic-visible");
    },
  },
};
</script>

<style scoped>
.base {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 3rem 0;
  gap: 1.5rem;
}
.pic {
  animation: 1s ease 0s 1 fade;
  filter: drop-shadow(1px 10px 5px rgba(0, 0, 0, 0.2));
  -webkit-filter: drop-shadow(1px 10px 5px rgba(0, 0, 0, 0.2));
  -webkit-backface-visibility: hidden;
  -moz-backface-visibility: hidden;
  -webkit-transform: translate3d(0, 0, 0);
  -moz-transform: translate3d(0, 0, 0);
}
.name {
  font-weight: 700;
  font-size: 28px;
}
.empty-name {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}
.edit-button {
  transition-duration: 500ms;
  cursor: pointer;
}
.edit-button:hover {
  transform: scale(1.1, 1.1);
  transition-duration: 500ms;
}

@keyframes fade {
  0% {
    transform: translateY(-20px);
    opacity: 0;
  }
  100% {
    transform: translateY(0);
    opacity: 1;
  }
}
</style>