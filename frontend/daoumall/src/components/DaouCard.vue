<template>
  <div class="col-sm-6">
    <div class="card">
      <div class="card-body">
        <h5 class="card-title">상품명 : {{ name }}</h5>
        <h6 class="card-title">상품 가격 : {{ price }}원</h6>
        <p class="card-text">상품 정보 {{ metaInfo }}</p>
        <p>식별 번호 : {{ serialCode }}</p>
        <router-link
          :to="{
            name: 'productInfo',
            query: { productSerialCode: serialCode, testData: 10, mallName: mallName }
          }"
          class="btn btn-primary"
        >
          주문하기</router-link
        >
      </div>
    </div>
  </div>
</template>

<script>
import { useRoute } from "vue-router";

export default {
  name: "mallProducts",

  setup() {
    const route = useRoute();
    console.log("route 쿼리문 " + JSON.stringify(route.query, null, 2));
  },

  props: ["name", "metaInfo", "price", "serialCode", "mallName"],

  data() {
    return {
      count: 0,
    };
  },
};
</script>

<style></style>
