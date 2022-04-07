<template>
  <h1>사용자[{{customerLoginId}}] 정보</h1>
  <hr />
  <h3>사용 가능한 쿠폰 내역</h3>
  <table class="table">
    <thead>
      <tr>
        <th scope="col">#</th>
        <th scope="col">판매점</th>
        <th scope="col">쿠폰 식별 번호</th>
        <th scope="col">할인 금액(비율)</th>
        <th scope="col">최소 결제금액(최대 할인금액)</th>
        <th scope="col">쿠폰 종류</th>
        <th scope="col">생성 날짜</th>
        <th scope="col">만료 날짜</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(data, index) in customerCoupons" :key="data">
        <th scope="row">{{ index + 1 }}</th>
        <td>
          {{ data.mallName }}
        </td>
        <td>
          {{ data.serialCode }}
        </td>
        <td>
          {{ data.discountInfo }}
        </td>
        <td>
          {{ data.constraint }}
        </td>
        <td>
          {{ data.couponCategory }}
        </td>
        <td>
          {{ data.createDate }}
        </td>
        <td>
          {{ data.expiredDate }}
        </td>
      </tr>
    </tbody>
  </table>
  <h3>사용한 쿠폰 내역</h3>
  <table class="table">
    <thead>
      <tr>
        <th scope="col">#</th>
        <th scope="col">쿠폰 식별 번호</th>
        <th scope="col">할인 금액(비율)</th>
        <th scope="col">최소 결제금액(최대 할인금액)</th>
        <th scope="col">쿠폰 종류</th>
        <th scope="col">생성 날짜</th>
        <th scope="col">사용 날짜</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(data, index) in customerUsedCoupons" :key="data">
        <th scope="row">{{ index + 1 }}</th>
        <td>
          {{ data.serialCode }}
        </td>
        <td>
          {{ data.discountInfo }}
        </td>
        <td>
          {{ data.constraint }}
        </td>
        <td>
          {{ data.couponCategory }}
        </td>
        <td>
          {{ data.createDate }}
        </td>
        <td>
          {{ data.usedDate }}
        </td>
      </tr>
    </tbody>
  </table>

  <hr />
  <h3>포인트 적립 내역</h3>
  <table class="table">
    <thead>
      <tr>
        <th scope="col">#</th>
        <th scope="col">적립 주문 번호</th>
        <th scope="col">포인트 적립금</th>
        <th scope="col">남은 포인트 금액</th>
        <th scope="col">생성 날짜</th>
        <th scope="col">만료 날짜</th>
        <th scope="col">사용 유무</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(data, index) in customerPoints" :key="data">
        <th scope="row">{{ index + 1 }}</th>
        <td>
          {{ data.serialOrderInfo }}
        </td>
        <td>
          {{ data.initialPointAmount }}
        </td>
        <td>
          {{ data.remainPointAmount }}
        </td>
        <td>
          {{ data.createDate }}
        </td>
        <td>
          {{ data.expiredDateTime }}
        </td>
        <td>
          {{ data.isUsed }}
        </td>
      </tr>
    </tbody>
  </table>
  <h5 v-if="canUsePoint > 0">사용 가능한 포인트 금액 {{ canUsePoint }}</h5>

  <hr />
  <h3>상품 구매 내역</h3>
  <table class="table">
    <thead>
      <tr>
        <th scope="col">#</th>
        <th scope="col">주문 번호</th>
        <th scope="col">판매점 이름</th>
        <th scope="col">상품 금액</th>
        <th scope="col">할인 금액</th>
        <th scope="col">포인트 사용 내역</th>
        <th scope="col">결제 금액</th>
        <th scope="col">쿠폰 종류</th>
        <th scope="col">쿠폰 번호</th>
        <th scope="col">결제 수단</th>
        <th scope="col">결제 코드</th>
        <th scope="col">결제 시간</th>
        <th scope="col">주문 상태</th>
        <th scope="col">구매정보</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(data, index) in customerPaymentManagements" :key="data">
        <th scope="row">{{ index + 1 }}</th>
        <td>
          {{ data.orderSerialCode }}
        </td>
        <td>
          {{ data.mallName }}
        </td>
        <td>
          {{ data.totalPrice }}
        </td>
        <td>
          {{ data.usedPointAmount }}
        </td>
        <td>
          {{ data.discountAmount }}
        </td>
        <td>
          {{ data.payPrice }}
        </td>
        <td>
          {{ data.couponCategory }}
        </td>
        <td>
          {{ data.couponSerialCode }}
        </td>
        <td>
          {{ data.paymentMethod }}
        </td>
        <td>
          {{ data.paymentSerialCode }}
        </td>
        <td>
          {{ data.createDate }}
        </td>
        <td>
          <div v-if="data.orderStatus === 'PROGRESS'">
            <input
              class="btn btn-primary btn-sm"
              type="button"
              value="주문 확정"
              @click="completePayment(data.orderSerialCode)"
            />
            <input
              class="btn btn-danger btn-sm"
              type="button"
              value="주문 취소"
              @click="cancelPayment(data.orderSerialCode)"
            />
          </div>
          <div v-if="data.orderStatus != 'PROGRESS'">
            {{ data.orderStatus }}
          </div>
        </td>
        <td>
          <button
            type="button"
            class="btn btn-info btn-sm"
            data-bs-toggle="modal"
            data-bs-target="#orderSpecification"
            @click="getOrderSpecification(data.orderSerialCode)"
          >
            상세보기
          </button>
        </td>
      </tr>
    </tbody>
  </table>

  <!-- Modal -->
  <div
    class="modal fade"
    id="orderSpecification"
    tabindex="-1"
    aria-labelledby="exampleModalLabel"
    aria-hidden="true"
  >
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="exampleModalLabel">주문 정보</h5>
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
          ></button>
        </div>
        <div
          class="modal-body"
          v-for="(orderPaper) in orderPapers"
          :key="orderPaper"
        >
          <h5>기본 상품</h5> 
          구매 개수: {{orderPaper.count}} <br />
          기본 제품 정보: {{ orderPaper.productRESDto.name }} |
          가격: {{ orderPaper.productRESDto.price }} <br />
          <h5>필수 옵션</h5>
          {{ orderPaper.essentialOptionRESDto.name }}, 필수 옵션 가격:
          {{ orderPaper.essentialOptionRESDto.price }}
          <br />
          <h5>추가 구매 상품 내역</h5>
          <div
            v-for="(data, index) in orderPaper.additionalProductOrderPaperDtos"
            :key="data"
          >
            {{ index + 1 }}. 추가상품 이름 : {{ data.name }}, 가격 :
            {{ data.price }}
          </div>
        </div>
        <div class="modal-footer">
          <button
            type="button"
            class="btn btn-secondary"
            data-bs-dismiss="modal"
          >
            Close
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {computed} from 'vue';
import {useStore} from "vuex";

export default {

    setup() {
    const store = useStore();

    let customerLoginId = computed( function() {
      return store.state.loginId
    });

    let globalLoginStatus = computed( function() {
      return store.state.status
    })

    return {
      customerLoginId,
      globalLoginStatus,
    }
  },

  data() {
    return {
      loginId: "",

      // 사용자 결제 내역
      customerPaymentManagements: [],

      // 사용자가 소유한 포인트 내역
      customerPoints: [],
      canUsePoint: 0,

      // 사용자가 보유한 쿠폰 내역
      customerCoupons: [],
      customerUsedCoupons: [],

      // 주문 상세 정보
      orderPapers: [],
    };
  },

  methods: {
    getOrderSpecification(orderSerialCode) {
      console.log(orderSerialCode, " 의 상세 제품 정보를 가져옵니다. ");
      this.axios
        .get("/api/daou/mall/payment/order-specification", {
          params: {
            orderSerialCode: orderSerialCode,
          },
        })
        .then((res) => {
          console.log("상세 제품 정보를 가져왔습니다.");
          this.orderPapers = res.data;
        })
        .catch((error) => {
          console.log("상세 제품 정보를 가져오는데 실패했습니다.", error);
        });
    },

    completePayment(orderSerialCode) {
      console.log(
        "사용자",
        this.loginId,
        "의 구매 내역중 주문 번호 ",
        orderSerialCode,
        "를 구매 확정을 시도합니다."
      );
      var updatePaymentInfo = {
        loginId: this.loginId,
        paymentManagementSerialCode: orderSerialCode,
      };
      this.axios
        .post("/api/daou/mall/payment/complete", updatePaymentInfo)
        .then((res) => {
          console.log(
            "구매 확정을 완료했습니다. 포인트 적립 내역 : ",
            res.data.pointAmount
          );
        })
        .catch((error) => {
          console.log("구매 확정에 실패했습니다. ", error);
        });
    },

    cancelPayment(orderSerialCode) {
      console.log(
        "사용자",
        this.loginId,
        "의 구매 내역중 주문 번호 ",
        orderSerialCode,
        "를 구매 취소를 시도합니다."
      );
      var updatePaymentInfo = {
        loginId: this.loginId,
        paymentManagementSerialCode: orderSerialCode,
      };
      this.axios
        .post("/api/daou/mall/payment/cancel", updatePaymentInfo)
        .then((res) => {
          console.log(
            "구매 취소를 완료했습니다. 금액 환불 내역 : ",
            res.data.returnedPayAmount
          );
        })
        .catch((error) => {
          console.log("구매 확정에 실패했습니다. ", error);
        });
    },

    getCustomerInfo(customerLoginId) {
      this.loginId = customerLoginId;
      if (this.loginId == "") alert("사용자 아이디를 입력해주세요.");

      console.log("사용자: ", this.loginId, "의 정보를 가져옵니다.");

      console.log("사용 가능한 쿠폰을 가져옵니다.");
      this.axios
        .get("/api/daou/mall/customer/coupon/can-use-coupons", {
          params: {
            customerLoginId: this.loginId,
          },
        })
        .then((res) => {
          console.log("사용자의 쿠폰 내역을 성공적으로 조회했습니다. ");
          this.customerCoupons = res.data;
        })
        .catch((error) => {
          console.log("사용자의 쿠폰 내역을 가져오지 못했습니다.", error);
        });

      console.log("사용한 쿠폰을 가져옵니다.");
      this.axios
        .get("/api/daou/mall/customer/coupon/used-coupons", {
          params: {
            customerLoginId: this.loginId,
          },
        })
        .then((res) => {
          console.log("사용한 쿠폰 정보들을 가져왔습니다.");
          this.customerUsedCoupons = res.data;
        })
        .catch((error) => {
          console.log("사용한 쿠폰 내역을 가져오지 못했습니다. ", error);
        });

      console.log("결제 내역을 가져옵니다.");
      this.axios
        .get("/api/daou/mall/payment/history", {
          params: {
            customerLoginId: this.loginId,
          },
        })
        .then((res) => {
          console.log("사용자의 결제 내역을 성공적으로 조회했습니다.");
          this.customerPaymentManagements = res.data;
        })
        .catch((error) => {
          console.log("결제 내역을 가져오지 못했습니다. ", error);
        });

      console.log("사용 가능한 포인트 금액을 가져옵니다. ");
      this.axios
        .get("/api/daou/mall/payment/point/can-use", {
          params: {
            customerLoginId: this.loginId,
          },
        })
        .then((res) => {
          console.log("사용자가 사용 할 수 있는 포인트 금액을 조회했습니다.");
          this.canUsePoint = res.data.pointAmount;
        })
        .catch((error) => {
          console.log(
            "사용자의 사용 가능 포인트 내역을 가져오지 못했습니다. ",
            error
          );
        });

      console.log("포인트 내역을 가져옵니다.");
      this.axios
        .get("/api/daou/mall/payment/point/history", {
          params: {
            customerLoginId: this.loginId,
          },
        })
        .then((res) => {
          console.log("사용자의 포인트 내역을 성공적으로 조회했습니다. 결과 ");
          this.customerPoints = res.data;
        })
        .catch((error) => {
          console.log("포인트 내역을 가져오지 못했습니다.", error);
        });
    },
  },

  created() {
    this.getCustomerInfo(this.customerLoginId)
  }
};
</script>

<style></style>
