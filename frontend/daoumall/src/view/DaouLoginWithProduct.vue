<template>
  <h1>로그인 전용 페이지입니다</h1>
  <hr />
  <div
    class="container-fluid vh-100"
    v-if="loginId == 'logout' && loginStatus == 'logout'"
  >
    <div class="" style="margin-top: 200px">
      <div class="rounded d-flex justify-content-center">
        <div class="col-md-4 col-sm-12 shadow-lg p-5 bg-light">
          <div class="text-center">
            <h3 class="text-primary">다우몰 로그인</h3>
          </div>
          <div class="p-4">
            <div class="input-group mb-3">
              <span class="input-group-text bg-primary"
                ><i class="bi bi-person-plus-fill text-white"></i
              ></span>
              <input
                type="text"
                class="form-control"
                placeholder="loginId"
                v-model="customerLoginId"
              />
            </div>
            <div class="input-group mb-3">
              <span class="input-group-text bg-primary"
                ><i class="bi bi-key-fill text-white"></i
              ></span>
              <input
                type="password"
                class="form-control"
                placeholder="password"
                v-model="customerPassword"
              />
            </div>
            <button
              class="btn btn-primary text-center mt-2"
              type="submit"
              @click="daouMallLogin(customerLoginId, customerPassword)"
              :disabled="
                customerLoginId.length == 0 || customerPassword.length == 0
              "
            >
              Login
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div v-else>
    <h2>로그인에 성공했습니다!</h2>
    <input type="button" value="로그아웃" @click="daouMallLogOut" />

    <router-link
      :to="{
        name: 'paymentProcess',
        params: {
          orderPaperDtos: JSON.stringify(beforeOrderPaperDtos),
          orderPaperInfos: JSON.stringify(beforeOrderPaperInfos),
          mallName: mallName,
          loginId: loginId,
        },
      }"
      v-if="
        beforeOrderPaperDtos.length > 0 && loginId != 'logout' && loginStatus != 'logout'
      "
      class="btn btn-outline-warning"
      >이전 상품 구매하기
    </router-link>
  </div>
</template>

<script>
import { computed } from "vue";
import { useStore } from "vuex";
import { useRoute } from "vue-router";

export default {
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

  props: ["params"],

  data() {
    const route = useRoute();
    return {
      customerLoginId: "",
      customerPassword: "",
      successLoginId: "",
      beforeOrderPaperDtos: JSON.parse(route.params.orderPaperDtos), // 결제 내역 정보 [ serialCode ]
      beforeOrderPaperInfos: JSON.parse(route.params.orderPaperInfos), // 결제 내역 정보 [ 사용자에게 보여질 상품 목록 ]
      mallName: route.params.mallName, // 판매점 이름
    };
  },

  methods: {
    daouMallLogin(loginId, password) {
      console.log("로그인을 시도합니다!!", loginId, " 비밀번호 ", password);
      const loginDto = {
        loginId: this.customerLoginId,
        password: this.customerPassword,
      };

      this.axios
        .post("/api/daou/mall/customer/signin", loginDto)
        .then((res) => {
          console.log("로그인에 성공했습니다! ", JSON.stringify(res.data));
          this.loginStatus = res.data.status;
          this.successLoginId = res.data.loginId;
          this.customerLoginId = "";
          this.customerPassword = "";
          this.login(this.successLoginId);
        })
        .catch((error) => {
          console.log("로그인에 실패해습니다. ", error);
        });
    },

    daouMallLogOut() {
      console.log(
        "로그아웃을 진행합니다. 현재 로그인 아이디 ",
        this.successLoginId
      );
      this.logout();
      this.successLoginId = "";
      this.loginStatus = "";
    },
  },
};
</script>

<style></style>
