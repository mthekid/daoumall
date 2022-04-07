<template>
  <h1>결제가 완료되었습니다.</h1>
  <hr />
  <div class="card">
    <div class="card-header">결제 정보</div>
    <div class="card-body">
      <h5 class="card-title">이용한 판매점 : {{ inputData.mallName }}</h5>
      <div class="card-text">
        상품 총 금액: {{ inputData.totalPrice }}
        <div v-if="inputData.couponCategory != 'NONE'">
          사용한 쿠폰 종류: {{ inputData.couponCategory }} | 할인 금액:
          {{ inputData.discountAmount }} | 쿠폰 번호:
          {{ inputData.couponInfo.serialCode }}
        </div>
        사용한 포인트 금액 : {{ inputData.usedPointAmount }} | 결제 수단
        {{ inputData.paymentMethod }} | 결제 금액 :
        {{ inputData.payInfo.totalPrice }} | 잔액 :
        {{ inputData.payInfo.remainPrice }}
        <div v-for="(data, index) in inputData.buyProductsInfoDTOs" :key="data">
          <hr />
          <h5>{{ index + 1 }}. 상품 정보 | 개수 : {{ data.count }}</h5>
          상품 이름 : {{ data.productName }} | 상품 가격 :
          {{ data.productPrice }} | 필수 옵션 정보 :
          {{ data.essentialOptionName }} | 필수 옵션 가격 :
          {{ data.essentialOptionPrice }} <br />
          <h5 v-if="data.buyAdditionalProductsInfoDtos.length > 0">추가 상품 정보</h5>
          <div
            v-for="(
              addtionalProductData, index
            ) in data.buyAdditionalProductsInfoDtos"
            :key="addtionalProductData"
          >
            <b>{{ index + 1 }}.</b>
            추가상품명 : {{ addtionalProductData.additionalProductName }} |
            추가가격 : {{ addtionalProductData.additionalProductPrice }}
          </div>
        </div>
      </div>
    </div>
    <router-link :to="{
          name: 'CustomerInfo'
        }">결제 내역 보기</router-link>
  </div>
</template>

<script>
import { useRoute } from "vue-router";

export default {
  setup() {
    const route = useRoute();
    console.log(route.params);
    console.log("테스트 + 결제 정보 ", route.params.data);
    console.log(
      "테스트 + 결제 정보 [JSON으로 돌리기]",
      JSON.parse(route.params.data)
    );
  },

  props: ["params"],

  data() {
    const route = useRoute();
    return {
      inputData: JSON.parse(route.params.data),
    };
  },
};
</script>

<style></style>
