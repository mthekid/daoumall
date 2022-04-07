<template>
  <!-- <router-link to="/main-page">Main</router-link> | -->
  <router-link to="/daou-mall">판매점</router-link> |
  <router-link to="/daou-product">상품</router-link> |
  <router-link to="/daou-coupon">쿠폰</router-link> |
  <router-link to="/mall-payment-management">판매내역[판매자]</router-link> |
  <router-link v-if="loginId != 'logout' && loginStatus != 'logout'" to="/customer-info">사용자 정보</router-link> |
  <router-link
    v-if="loginId == 'logout' && loginStatus == 'logout'"
    to="/daou-login"
    >로그인</router-link
  >
  <a v-if="loginId != 'logout' && loginStatus != 'logout'" href="#" @click="customerLogout">로그아웃</a>
  <!-- <router-link to="/daou-product-card">Card Component</router-link> -->
  <router-view />
</template>

<script>
import { computed } from "vue";
import { useStore } from "vuex";

export default {
  name: "App",

  setup() {
    const store = useStore();

    let loginId = computed(function () {
      return store.state.loginId;
    });

    let loginStatus = computed(function () {
      return store.state.status;
    });

    function login(loginId) {
      store.commit("login", loginId);
    }

    function logout() {
      store.commit("logout", logout);
    }
    return {
      loginId,
      loginStatus,
      login,
      logout,
    };
  },

  methods: {
    customerLogout() {
      this.logout();
    }
  }
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
