<template>
  <h1>판매점의 판매 상품 내역입니다.</h1>
  <hr />
  <ul class="nav nav-pills nav-fill">
    <li class="nav-item" v-for="mall in mallList" :key="mall">
      <input
        type="button"
        class="nav-item nav-link"
        :value="mall.mallName"
        @click="searchMallPaymentManagements(mall.mallName)"
      />
    </li>
  </ul>
  <input
    v-if="mallPaymentManagements.length > 0"
    type="button"
    value="전체 리스트"
    @click="searchMallPaymentManagements(mallName)"
  />

  <input
    v-if="mallPaymentManagements.length > 0"
    type="button"
    value="취소 리스트"
    @click="searchCanceledMallPaymentManagements"
  />
  <input
    v-if="mallPaymentManagements.length > 0"
    type="button"
    value="확정 리스트"
    @click="searchCompletedsearchMallPaymentManagements"
  />
  <table class="table">
    <thead>
      <tr>
        <th scope="col">#</th>
        <th scope="col">총 상품 금액</th>
        <th scope="col">할인 금액</th>
        <th scope="col">포인트 사용 내역</th>
        <th scope="col">결제 금액</th>
        <th scope="col">결제 날짜</th>
        <th scope="col">사용자</th>
        <th scope="col">쿠폰 번호</th>
        <th scope="col">주문 번호</th>
        <th scope="col">주문 상태</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(data, index) in mallPaymentManagements" :key="data">
        <th scope="row">{{ index + 1 }}</th>
        <td
          :class="{
            'table-danger': data.orderStatus === 'CANCEL',
            'table-primary': data.orderStatus === 'COMPLETE',
          }"
        >
          {{ data.totalPrice }}
        </td>
        <td
          :class="{
            'table-danger': data.orderStatus === 'CANCEL',
            'table-primary': data.orderStatus === 'COMPLETE',
          }"
        >
          {{ data.discountAmount }}
        </td>
        <td
          :class="{
            'table-danger': data.orderStatus === 'CANCEL',
            'table-primary': data.orderStatus === 'COMPLETE',
          }"
        >
          {{ data.usedPointAmount }}
        </td>
        <td
          :class="{
            'table-danger': data.orderStatus === 'CANCEL',
            'table-primary': data.orderStatus === 'COMPLETE',
          }"
        >
          {{ data.payAmount }}
        </td>
        <td
          :class="{
            'table-danger': data.orderStatus === 'CANCEL',
            'table-primary': data.orderStatus === 'COMPLETE',
          }"
        >
          {{ data.createDate }}
        </td>
        <td
          :class="{
            'table-danger': data.orderStatus === 'CANCEL',
            'table-primary': data.orderStatus === 'COMPLETE',
          }"
        >
          {{ data.loginId }}
        </td>
        <td
          :class="{
            'table-danger': data.orderStatus === 'CANCEL',
            'table-primary': data.orderStatus === 'COMPLETE',
          }"
        >
          {{ data.couponSerialCode }}
        </td>
        <td
          :class="{
            'table-danger': data.orderStatus === 'CANCEL',
            'table-primary': data.orderStatus === 'COMPLETE',
          }"
        >
          {{ data.orderSerialCode }}
        </td>
        <td
          :class="{
            'table-danger': data.orderStatus === 'CANCEL',
            'table-primary': data.orderStatus === 'COMPLETE',
          }"
        >
          {{ data.orderStatus }}
        </td>
      </tr>
    </tbody>
  </table>
</template>

<script>
export default {
  data() {
    return {
      mallList: [],
      mallName: "", // 판매점 이름

      mallPaymentManagements: [], // 판매점 판매 내역
    };
  },

  methods: {
    searchMallPaymentManagements(mallName) {
      this.mallName = mallName;
      if (this.mallName == "") {
        alert("판매점 이름을 입력해주세요.");
      }
      console.log(this.mallName, "의 판매 내역을 조회합니다.");

      this.axios
        .get("/api/daou/mall/search/payment-managements", {
          params: {
            mallName: this.mallName,
          },
        })
        .then((res) => {
          console.log("판매점의 판매 내역 ", res.data);
          this.mallPaymentManagements = res.data;
        })
        .catch((error) => {
          console.log("판매점의 판매 내역 검색에 실패했습니다. " + error);
        });
    },

    searchCanceledMallPaymentManagements() {
      console.log(this.mallName, "의 취소 목록을 조회합니다.");
      this.axios
        .get("/api/daou/mall/search/payment-managements/canceled", {
          params: {
            mallName: this.mallName,
          },
        })
        .then((res) => {
          console.log("판매점의 판매 내역 ", res.data);
          this.mallPaymentManagements = res.data;
        })
        .catch((error) => {
          console.log("판매점의 판매 내역 검색에 실패했습니다. " + error);
        });
    },

    searchCompletedsearchMallPaymentManagements() {
      console.log(this.mallName, "의 구매 확정 목록을 조회합니다.");
      this.axios
        .get("/api/daou/mall/search/payment-managements/completed", {
          params: {
            mallName: this.mallName,
          },
        })
        .then((res) => {
          console.log("판매점의 판매 내역 ", res.data);
          this.mallPaymentManagements = res.data;
        })
        .catch((error) => {
          console.log("판매점의 판매 내역 검색에 실패했습니다. " + error);
        });
    },
    getMallList() {
      console.log("판매점 목록을 가져옵니다.");
      this.axios
        .get("/api/daou/mall/list")
        .then((res) => {
          console.log(
            "판매점 목록을 성공적으로 가져왔습니다.",
            JSON.stringify(res.data)
          );
          this.mallList = res.data;
        })
        .catch((error) => {
          console.log("판매점 목록을 가져오지 못했습니다. ", error);
        });
    },
  },
  created() {
    this.getMallList();
  },
};
</script>

<style></style>
