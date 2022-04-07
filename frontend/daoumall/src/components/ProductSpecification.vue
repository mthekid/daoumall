<template>
  <h1>상품 상세 페이지 입니다.</h1>
  <!-- {{ loginId }}, {{ loginStatus }} -->
  <h3>
    판매점 이름: {{ mallName }} | 상품 식별 번호 : 상품 식별 정보:
    {{ productSerialCode }}
  </h3>
  <hr />
  <div class="card">
    <div class="card-header">상품 정보</div>
    <div class="card-body">
      <h5 class="card-title">
        상품 이름 : {{ productRes.name }}, 상품 가격 : {{ productRes.price }}
      </h5>
      <p class="card-text">{{ productRes.metaInfo }}</p>

      <select
        class="form-select"
        v-model="selectedEssentialOption"
        style="border: 3px solid SteelBlue"
      >
        <option
          :key="i"
          v-for="(essentialOption, i) in essentialOptions"
          :value="essentialOption"
        >
          필수 옵션{{ i + 1 }} : 필수옵션 이름: {{ essentialOption.name }}, 필수
          옵션 정보 : {{ essentialOption.metaInfo }}, 필수 옵션 가격 :
          {{ essentialOption.price }}
        </option>
      </select>

      <h5 class="card-title">선택된 필수 옵션</h5>
      <p class="card-text" v-if="selectedEssentialOption !== null">
        이름 : {{ selectedEssentialOption.name }} <br />
        설명 : {{ selectedEssentialOption.metaInfo }} <br />
        가격 : {{ selectedEssentialOption.price }}
      </p>

      <h5 class="card-title">추가 옵션 정보 목록</h5>
      <div
        v-for="additionalProduct in additionalProducts"
        :key="additionalProduct"
      >
        <p class="card-text">
          <input
            type="checkbox"
            v-model="selectedAdditionalProducts"
            :value="additionalProduct"
          />
          추가 상품 이름 : {{ additionalProduct.additionalProduct.name }} 추가
          상품 가격 : {{ additionalProduct.additionalProduct.price }} 추가 상품
          정보 :{{ additionalProduct.additionalProduct.metaInfo }}
        </p>
      </div>
      <!-- <p class="card-text" v-if="selectedAdditionalProducts.length > 0">
        선택한 추가상품들 : {{ selectedAdditionalProducts }}
      </p> -->
      <br />
      <div v-if="selectedEssentialOption !== null">
        <h5>가격 정보</h5>
        <p class="card-text" v-if="selectedAdditionalProducts.length > 0">
          기본 금액: {{ productRes.price }}, 필수 옵션 금액:
          {{ selectedEssentialOption.price }}
          추가 상품 총 금액 : {{ additionalProductTotalPrice() }} 총 금액:
          {{
            productRes.price +
            selectedEssentialOption.price +
            additionalProductTotalPrice()
          }}
        </p>
        <p class="card-text" v-else>
          기본 금액: {{ productRes.price }}, 필수 옵션 금액:
          {{ selectedEssentialOption.price }} 총 금액:
          {{ productRes.price + selectedEssentialOption.price }}
        </p>
      </div>
      <input
        v-if="selectedEssentialOption !== null"
        type="button"
        class="btn btn-outline-primary"
        @click="createOrderPaper"
        value="추가하기"
      />
    </div>
  </div>
  <div class="card">
    <div class="card-header">선택한 상품 목록</div>
    <div class="card-body">
      <div>주문 정보</div>
      <div v-for="(data, index) in orderPaperInfos" :key="data">
        {{ index + 1 }} : UI 주문서 정보: 상품명{{ data.baseProductName }} [필수
        옵션 : {{ data.essentialOptionName }}] [추가 상품:
        {{ data.additionalProductInfos }}] | 개수 : {{ data.count }} | 가격 :
        {{ computeOrderPrice(index) }}
        <input
          type="number"
          class="btn btn-outline-primary btn-sm"
          @click="updateOrderCount(index, data.count)"
          v-model="data.count"
          min="1"
        />
        <input
          type="button"
          class="btn btn-outline-success btn-sm"
          @click="removeOrderPaper"
          value="제거하기"
        />
      </div>
      <!-- <div v-for="(data, index) in orderPapers" :key="data">
        {{ index + 1 }} : 결제 주문서 정보: {{ data }} :
      </div> -->
      <h4 class="card-title">총 결제 금액 : {{ computeTotalPrice() }} </h4>

      <div
        class="alert alert-Info"
        v-if="orderPapers.length > 0 && loginId == 'logout' && loginStatus == 'logout'"
      >
        <strong>Info!</strong> 구매를 위해선 로그인이 필요합니다.
        <router-link :to="{
          name: 'loginWithProduct',
          params: {
            orderPaperDtos: JSON.stringify(orderPapers),
            orderPaperInfos: JSON.stringify(orderPaperInfos),
            mallName: mallName,
          },
        }" class="alert-link"
          >로그인 페이지로 이동하기</router-link
        >
      </div>

      <router-link
        :to="{
          name: 'paymentProcess',
          params: {
            orderPaperDtos: JSON.stringify(orderPapers),
            orderPaperInfos: JSON.stringify(orderPaperInfos),
            mallName: mallName,
            loginId: loginId,
          },
        }"
        v-if="
          orderPapers.length > 0 &&
          loginId != 'logout' &&
          loginStatus != 'logout'
        "
        class="btn btn-outline-warning"
        >구매하기
      </router-link>
    </div>
  </div>
</template>

<script>
// import { computed } from "vue";
// import { useStore } from "vuex";

import { computed } from "vue";
import { useStore } from "vuex";

export default {
  setup() {
    const store = useStore();

    let loginId = computed(function () {
      return store.state.loginId;
    });

    let loginStatus = computed(function () {
      return store.state.status;
    });

    function checkLogin() {
      let loginId = computed(function () {
        return store.state.loginId;
      });

      if (loginId.value == "logout") {
        alert("로그인이 필요합니다!");
      }
    }

    return {
      loginId,
      loginStatus,
      checkLogin,
    };
  },

  props: ["query"],

  data() {
    return {
      customerLoginId: "",
      routeTestData: {
        testInfo: "Hello",
      },
      productSerialCode: this.query.productSerialCode,
      mallName: this.query.mallName,
      // 기본 상품 정보
      productRes: {
        name: "",
        price: 0,
        serialCode: "",
        metaInfo: "",
      },

      // 조회용
      essentialOptions: [], // 상품이 가진 필수 옵션 정보들
      additionalProducts: [], // 상품이 가진 추가 상품 정보들

      // 사용자 선택
      selectedEssentialOption: null, // 선택된 필수 옵션
      selectedAdditionalProducts: [], // 선택한 추가상품 정보들
      selectedAdditionalProductSerialCodes: [],
      selectedAdditionalProductInfos: [],

      // 결제 내역으로 넘길 주문서 정보
      orderPapers: [],
      orderPaper: {
        baseProductSerialCode: "",
        essentialOptionSerialCode: "",
        additionalProductSerialCodes: [],
        count: 1,
        price: 0,
      },

      //화면에 보여질 주문서 정보
      orderPaperInfos: [],
      orderPaperInfo: {
        baseProductName: "",
        essentialOptionName: "",
        additionalProductInfos: [],
        count: 1,
        price: 0,
      },

      // 추가 상품 식별 정보들
      additionalProductSerialCode: "",
      additionalProductInfos: [],

      // 개수
      orderPaperCount: 1,

      // 결제 화면으로 넘길 정보
      orderPaperDtos: [],

      // 전체 결제 금액.
      totalPrice: 0,
    };
  },

  methods: {
    navigateToLogin() {
      console.log("로그인 창으로 이동시킵니다.");
    },
    buyAllOrderPaper() {
      this.orderPapers.forEach((element) => {
        this.orderPaperDtos.push(element);
      });
      console.log(
        "최종적으로 구매해야 하는 주문 정보들 " +
          JSON.stringify(this.orderPaperDtos, null, 2)
      );
    },

    updateOrderCount(index, count) {
      console.log(
        "주문서 정보의 개수를 수정합니다. 기존 개수 : " +
          this.orderPaperInfos[index].count
      );
      this.orderPaperInfos[index].count = count;
      this.orderPapers[index].count = count;
      console.log("수정한 개수 : " + this.orderPaperInfos[index].count);
    },

    removeOrderPaper(index) {
      console.log(index + " 주문서를 제거합니다.");
      this.orderPaperInfos.splice(index, 1);
      this.orderPapers.splice(index, 1);
    },

    checkOrderPapers() {
      console.log(
        "주문서 장바구니 조회 " + JSON.stringify(this.orderPapers, null, 2)
      );
    },

    createOrderPaper() {
      console.log("구매 상품을 위한 주문서를 생성합니다.");
      console.log("선택한 상품 식별 정보 " + this.productSerialCode);
      console.log(
        "선택한 필수 옵션 정보 " + this.selectedEssentialOption.serialCode
      );
      this.orderPaper.price += this.productRes.price;
      this.orderPaperInfo.price += this.productRes.price;
      this.orderPaper.price += this.selectedEssentialOption.price;
      this.orderPaperInfo.price += this.selectedEssentialOption.price;

      this.selectedAdditionalProducts.forEach((element) => {
        this.selectedAdditionalProductInfos.push(
          element.additionalProduct.name
        );
        this.selectedAdditionalProductSerialCodes.push(
          element.additionalProduct.serialCode
        );
        // 금액 생성
        this.orderPaper.price += element.additionalProduct.price;
        this.orderPaperInfo.price += element.additionalProduct.price;
      });
      console.log(
        "선택한 추가 상품 옵션 정보 " +
          this.selectedAdditionalProductSerialCodes
      );

      this.orderPaper.baseProductSerialCode = this.productSerialCode;
      this.orderPaper.essentialOptionSerialCode =
        this.selectedEssentialOption.serialCode;
      this.orderPaper.additionalProductSerialCodes =
        this.selectedAdditionalProductSerialCodes;

      console.log("주문서 정보 " + JSON.stringify(this.orderPaper, null, 2));
      const orderPaper = {
        baseProductSerialCode: this.orderPaper.baseProductSerialCode,
        essentialOptionSerialCode: this.orderPaper.essentialOptionSerialCode,
        additionalProductSerialCodes:
          this.orderPaper.additionalProductSerialCodes,
        count: 1,
        price: this.orderPaper.price,
      };
      console.log(
        "결제 내역으로 보내질 주문서 정보 " +
          JSON.stringify(this.orderPaper, null, 2)
      );
      this.orderPapers.push(orderPaper);

      const orderPaperInfo = {
        baseProductName: this.productRes.name,
        essentialOptionName: this.selectedEssentialOption.name,
        additionalProductInfos: this.selectedAdditionalProductInfos,
        price: this.orderPaperInfo.price,
        count: 1,
      };
      console.log(
        "UI 화면으로 보여질 주문서 정보 " +
          JSON.stringify(this.orderPaperInfo, null, 2)
      );
      this.orderPaperInfos.push(orderPaperInfo);

      // 주문 정보 비우기
      this.orderPaper.baseProductSerialCode = "";
      this.orderPaper.essentialOptionSerialCode = "";
      this.orderPaper.additionalProductSerialCodes = [];
      this.selectedAdditionalProducts = [];

      // 가격 비우기
      this.orderPaperInfo.price = 0;
      this.orderPaper.price = 0;
      // 필수옵션 & 추가상품 선택 비우기
      this.selectedEssentialOption = null;
      this.selectedAdditionalProductSerialCodes = [];
      this.selectedAdditionalProductInfos = [];
    },

    fetchProductInfo(dataInputed) {
      this.axios
        .get("/api/daou/mall/search/product", {
          params: {
            productSerialCode: dataInputed,
          },
        })
        .then((res) => {
          this.productRes = res.data.productRes;
          this.essentialOptions = res.data.essentialOptions;
          this.additionalProducts = res.data.additionalProducts;
        })
        .catch((error) => {
          console.log(error);
        });
    },

    computeOrderPrice(index) {
      console.log("인덱스 정보 " + index);
      console.log(
        "상품 가격 정보 " +
          this.orderPaperInfos[index].price +
          " , 개수 " +
          this.orderPaperInfos[index].count
      );
      var orderPrice = this.orderPaperInfos[index].price;
      var count = this.orderPaperInfos[index].count;
      var computedOrderPrice = orderPrice * count;

      return computedOrderPrice;
    },

    computeTotalPrice() {
      console.log(
        "전체 결제 금액을 계산합니다. 주문서 정보 " +
          JSON.stringify(this.orderPaperInfos, null, 2)
      );
      var totalPrice = 0;

      this.orderPaperInfos.forEach((element) => {
        console.log("주문서 정보 " + JSON.stringify(element, null, 2));
        totalPrice += element.count * element.price;
      });

      this.totalPrice = totalPrice;
      return this.totalPrice;
    },

    additionalProductTotalPrice() {
      var totalPrice = 0;
      this.selectedAdditionalProducts.forEach((element) => {
        totalPrice += element.additionalProduct.price;
      });
      return totalPrice;
    },
  },

  mounted() {
    this.fetchProductInfo(this.productSerialCode);
  },
};
</script>

<style></style>
