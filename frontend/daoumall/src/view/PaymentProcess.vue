<template>
  <h1>결제 페이지</h1>
  <!-- <input type="text" placeholder="로그인아이디" v-model="loginId" />{{
    loginId
  }}
  <input
    type="submit"
    value="검색"
    @click="getCustomerInfo"
    :disabled="loginId.length == 0"
  /> -->
  <div class="card" style="border: 2px solid black">
    <h5 class="card-header">{{ mallName }} 주문 정보</h5>
    <div class="card-body" v-for="(data, index) in orderPaperInfos" :key="data">
      <p class="card-text" v-if="data.additionalProductInfos.length > 0">
        <b>{{ index + 1 }}</b
        >. 기본 상품 정보 : {{ data.baseProductName }} 필수 옵션 정보 :
        {{ data.essentialOptionName }}, 추가 옵션 정보 :
        {{ data.additionalProductInfos }} | 개수 :{{ data.count }} , 금액 :
        {{ data.count }} * {{ data.price }} | 총 금액 :{{
          data.count * data.price
        }}
      </p>
      <p v-else>
        기본 상품 정보 : {{ data.baseProductName }} 필수 옵션 정보 :
        {{ data.essentialOptionName }} | 개수 :{{ data.count }} , 금액 :
        {{ data.count }} * {{ data.price }} | 총 금액 :{{ totalPrice }}
      </p>
    </div>
    <p class="card-text">상품 총 금액 : {{ totalPrice }}</p>
  </div>
  <div class="row">
    <div class="col-sm-6">
      <div class="card">
        <div class="card-body" style="border: 2px solid black">
          <h5 class="card-title">보유 쿠폰 목록</h5>
          <button
            type="button"
            class="btn btn-info btn-sm"
            data-bs-toggle="modal"
            data-bs-target="#couponList"
          >
            보유한 쿠폰 수 : {{ canUseCoupons.length }}
          </button>
          <button
            type="button"
            class="btn btn-secondary btn-sm"
            @click="cancelCoupon"
          >
            쿠폰 미사용
          </button>
          <div
            class="modal fade"
            id="couponList"
            tabindex="-1"
            aria-labelledby="exampleModalLabel"
            aria-hidden="true"
          >
            <div class="modal-dialog">
              <div class="modal-content">
                <div class="modal-header">
                  <h5 class="modal-title" id="exampleModalLabel">
                    쿠폰 목록 정보
                  </h5>
                  <button
                    type="button"
                    class="btn-close"
                    data-bs-dismiss="modal"
                    aria-label="Close"
                  ></button>
                </div>
                <div
                  class="modal-body"
                  v-for="(data, index) in canUseCoupons"
                  :key="data"
                >
                  {{ index + 1 }}. 쿠폰 : {{ data.couponCategory }} |
                  할인금액(비율) : {{ data.discountInfo }} <br />
                  생성 날짜 : {{ data.createDate }} | 만료날짜
                  {{ data.expiredDate }} <br />
                  최소 결제 금액(최대 할인 금액) : {{ data.constraint }} 식별
                  번호 : {{ data.serialCode }}
                  <input
                    type="button"
                    value="선택하기"
                    :disabled="
                      data.couponCategory == 'AMOUNT' &&
                      data.constraint > totalPrice
                    "
                    @click="chooseCoupon(index)"
                  />
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
          <div
            v-if="
              choosedCoupon != null && choosedCoupon.couponCategory == 'RATE'
            "
          >
            쿠폰 종류 : 비율 | 쿠폰 할인 비율 :
            {{ choosedCoupon.discountInfo }} | 최대 할인 금액 :
            {{ choosedCoupon.constraint }} |
            <div
              v-if="
                totalPrice * (choosedCoupon.discountInfo / 100) >
                choosedCoupon.constraint
              "
            >
              할인 금액 : {{ choosedCoupon.constraint }}
            </div>
            <div v-else>
              할인 금액 : {{ totalPrice * (choosedCoupon.discountInfo / 100) }}
            </div>
          </div>
          <p
            v-if="
              choosedCoupon != null && choosedCoupon.couponCategory == 'AMOUNT'
            "
          >
            쿠폰 종류 : 금액 | 할인 금액 : {{ choosedCoupon.discountInfo }} |
            최소 구매 금액 : {{ choosedCoupon.constraint }}
          </p>

          <p
            v-if="
              choosedCoupon == null &&
              appliedCoupon !== null &&
              appliedCoupon.couponCATEGORY == 'RATE'
            "
          >
            쿠폰 종류 : 비율 | 쿠폰 할인 금액 :
            {{ appliedCoupon.discountPrice }}
          </p>
          <p
            v-if="
              choosedCoupon == null &&
              appliedCoupon !== null &&
              appliedCoupon.couponCATEGORY == 'AMOUNT'
            "
          >
            쿠폰 종류 : 금액 | 할인 금액 : {{ appliedCoupon.discountPrice }}
          </p>

          <p v-if="appliedCoupon == null">할인 금액 : 0</p>
        </div>
        <div class="card-body" style="border: 2px solid black">
          <h5 class="card-title">사용 포인트 내역</h5>
          사용 가능한 포인트 금액 : {{ canUsePoint }}
          <br />
          사용 포인트:
          <input
            type="number"
            :max="canUsePoint"
            min="0"
            placeholder="point"
            v-model="usePoint"
          />{{ usePoint }}
          <p v-if="usePoint > canUsePoint">
            사용 가능한 포인트는 {{ canUsePoint }}원 입니다.
          </p>
          <input
            type="button"
            class="btn btn-primary btn-sm"
            value="전액 사용"
            @click="useAllPoint"
          />
        </div>
        <div class="card-body" style="border: 2px solid black">
          <h5 class="card-title">결제수단</h5>
          <p class="card-text">
            카드 :
            <input
              type="text"
              placeholder="결제 번호"
              v-model="cardSerialCode"
              :disabled="bankSerialCode.length > 0"
            />
          </p>
          <p class="card-text">
            은행 :
            <input
              type="text"
              placeholder="결제 번호"
              v-model="bankSerialCode"
              :disabled="cardSerialCode.length > 0"
            />
          </p>
        </div>
      </div>
    </div>
    <div class="col-sm-6">
      <div class="card">
        <div class="card-body" style="border: 2px solid black">
          <h5 class="card-title">결제 요약 정보</h5>
          <p>총금액 : {{ totalPrice }}</p>
          <p>할인 금액 : {{ discountAmount }}</p>
          <p>포인트 사용 금액 : {{ usePoint }}</p>
          <p>결제 금액 : {{ calculatePayAmount() }}</p>
          <input
            type="button"
            value="결제하기"
            class="btn btn-primary"
            @click="payProcess"
            :disabled="bankSerialCode.length == 0 && cardSerialCode == 0"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { useRoute } from "vue-router";

export default {
  props: ["params"],

  data() {
    const route = useRoute();
    return {
      orderPaperDtos: JSON.parse(route.params.orderPaperDtos), // 결제 내역 정보 [ serialCode ]
      orderPaperInfos: JSON.parse(route.params.orderPaperInfos), // 결제 내역 정보 [ 사용자에게 보여질 상품 목록 ]
      mallName: route.params.mallName, // 판매점 이름

      loginId: route.params.loginId,

      appliedCoupon: null, // 자동 적용된 쿠폰 정보

      choosedCouponSerialCode: "", // 선택 쿠폰 식별 정보
      discountAmount: 0, // 할인 금액
      canUsePoint: 0, // 사용 가능한 포인트 금액
      usePoint: 0, // 사용할 포인트 금액
      totalPrice: 0, // 상품 총 금액
      payAmount: 0,

      // 결제 정보
      cardSerialCode: "", // 카드
      bankSerialCode: "", // 은행

      // 사용 가능한 쿠폰 리스트
      canUseCoupons: [],
      choosedCoupon: null,
    };
  },
  methods: {
    cancelCoupon() {
      console.log("쿠폰 선택을 취소합니다.")
      this.discountAmount = 0;
      this.choosedCoupon = null;
      this.appliedCoupon = {
        couponCATEGORY : "NONE",
        couponSerialCode : "NONE",
        discountPrice : 0,
        originalPrice : this.originalPrice
      };
      console.log("쿠폰 선택 취소를 완료했습니다.")
    },
    chooseCoupon(index) {
      console.log("자동 적용된 쿠폰을 해제하고 새로운 쿠폰을 선택합니다.");
      this.choosedCoupon = this.canUseCoupons[index];

      this.choosedCouponSerialCode = this.choosedCoupon.serialCode;
      if (this.choosedCoupon.couponCategory == "RATE") {
        this.discountAmount =
          this.totalPrice * (this.choosedCoupon.discountInfo / 100);
        if (
          this.totalPrice * (this.choosedCoupon.discountInfo / 100) >
          this.choosedCoupon.constraint
        ) {
          this.discountAmount = this.choosedCoupon.constraint;
        }
      }
      if (this.choosedCoupon.couponCategory == "AMOUNT") {
        this.discountAmount = this.choosedCoupon.discountInfo;
      }
      console.log("선택한 쿠폰 정보 ", JSON.stringify(this.choosedCoupon));
    },

    getCouponList() {
      console.log("사용 가능한 쿠폰 리스트를 확인합니다.");
      this.axios
        .get("/api/daou/mall/customer/coupon/mall/can-use-coupons", {
          params: {
            customerLoginId: this.loginId,
            mallName: this.mallName
          },
        })
        .then((res) => {
          console.log("사용 가능한 쿠폰 리스트를 확인했습니다");
          this.canUseCoupons = res.data;
        })
        .catch((error) => {
          console.log(
            "사용 가능한 쿠폰 조회에 실패했습니다. 로그인 아이디를 확인해주세요.",
            error
          );
        });
    },

    payProcess() {
      console.log("결제를 진행합니다. 최종 결제!!");

      const createdOrderSerialCode =
        "T-D-PM-O" + Math.floor(Math.random() * 100000);
      // 기본으로 은행 계좌로 진행한다고 고려
      var paymentManagementDto = {
        mallName: this.mallName,
        loginId: this.loginId,
        totalPrice: this.totalPrice,
        discountAmount: this.discountAmount,
        usedPointAmount: this.usePoint,
        couponCategory: "NONE",
        couponSerialCode: "NONE",
        paymentMethod: "BANKACCOUNT",
        paymentSerialCode: this.bankSerialCode,
        orderSerialCode: createdOrderSerialCode,
      };

      // 카드 정보가 들어온 경우
      if (this.cardSerialCode != "") {
        paymentManagementDto.paymentMethod = "CARD";
        paymentManagementDto.paymentSerialCode = this.cardSerialCode;
      }

      console.log(
        "쿠폰 정보 ",
        JSON.stringify(this.appliedCoupon),
      );

      // 자동 쿠폰이 비율 쿠폰인 경우
      if (this.appliedCoupon.couponCATEGORY != null && this.appliedCoupon.couponCATEGORY == "RATE") {
        paymentManagementDto.couponCategory = "RATE";
        paymentManagementDto.couponSerialCode =
          this.appliedCoupon.couponSerialCode;
      }

      // 자동 쿠폰이 금액 쿠폰인 경우
      if (this.appliedCoupon.couponCATEGORY != null && this.appliedCoupon.couponCATEGORY == "AMOUNT") {
        paymentManagementDto.couponCategory = "AMOUNT";
        paymentManagementDto.couponSerialCode =
          this.appliedCoupon.couponSerialCode;
      }

      // 선택한 쿠폰이 비율 쿠폰인 경우
      if (
        this.choosedCoupon != null &&
        this.choosedCoupon.couponCategory == "RATE"
      ) {
        paymentManagementDto.couponCategory = "RATE";
        console.log(
          "선택한 쿠폰이 비율 쿠폰이며 serialCode는 다음과 같습니다.",
          this.choosedCouponSerialCode
        );
        paymentManagementDto.couponSerialCode = this.choosedCouponSerialCode;
      }

      // 선택한 쿠폰이 금액 쿠폰인 경우
      if (
        this.choosedCoupon != null &&
        this.choosedCoupon.couponCategory == "AMOUNT"
      ) {
        paymentManagementDto.couponCategory = "AMOUNT";
        paymentManagementDto.couponSerialCode = this.choosedCouponSerialCode;
      }

      console.log(
        "제공할 결제 내역 정보 ",
        JSON.stringify(paymentManagementDto)
      );

      const paymentManagement = {
        orderPaperDtos: this.orderPaperDtos,
        paymentManagementDto: paymentManagementDto,
      };

      console.log(
        "결제 내역으로 보낼 정보 ",
        JSON.stringify(paymentManagement)
      );
      this.axios
        .post("/api/daou/mall/payment/buy-products", paymentManagement)
        .then((res) => {
          console.log(
            "결제 내역 생성이 완료되었습니다. 결제 내역 정보 : ",
            JSON.stringify(res.data)
          );
          this.$router.push({
            name: "completePayment",
            params: { data: JSON.stringify(res.data) },
          });
        })
        .catch((error) => {
          console.log(
            "결제 내역을 생성하는데 실패 했습니다. 에러 정보 ",
            error
          );
        });

      console.log("결제 내역 생성 완료!!");
    },
    calculatePayAmount() {
      console.log("최종 결제 금액을 계산합니다.");
      this.payAmount = this.totalPrice - this.discountAmount - this.usePoint;
      return this.payAmount;
    },
    useAllPoint() {
      console.log("포인트를 모두 사용합니다.");

      var discountAmount = this.appliedCoupon.discountPrice;
      console.log("할인 금액 ", discountAmount);
      var payAmount = this.totalPrice - discountAmount;
      if (payAmount > this.canUsePoint) {
        this.usePoint = this.canUsePoint;
      } else {
        this.usePoint = payAmount;
      }
      console.log("사용할 포인트 금액 ", this.usePoint);
    },

    calculateTotalPrice() {
      console.log(
        "해당 주문에서 결제할 금액을 계산합니다. [ 주문서 상품 결제금액 ] "
      );
      var totalPrice = 0;
      this.orderPaperInfos.forEach((element) => {
        totalPrice += element.price * element.count;
      });
      console.log("결제 금액 " + totalPrice);
      this.totalPrice = totalPrice;
    },

    getCustomerInfo() {
      console.log(this.loginId, "의 정보를 가져옵니다.");

      // 사용 가능한 포인트 정보 가져오기
      this.axios
        .get("/api/daou/mall/payment/point/can-use", {
          params: {
            customerLoginId: this.loginId,
          },
        })
        .then((res) => {
          console.log("사용할 수 있는 포인트 금액 정보입니다. ", res.data);
          this.canUsePoint = res.data.pointAmount;
        })
        .catch((error) => {
          console.log("포인트 정보를 받지 못했습니다. 에러 정보 : ", error);
        });

      // 적용할 쿠폰 정보 가져오기
      this.axios
        .get("/api/daou/mall/coupon/use/auto-apply-coupon", {
          params: {
            customerLoginId: this.loginId,
            totalPrice: this.totalPrice,
            mallName: this.mallName
          },
        })
        .then((res) => {
          console.log("자동으로 적용할 쿠폰의 정보입니다. ", res.data);
          if (res.data != null) {
            this.appliedCoupon = res.data;
            this.discountAmount = this.appliedCoupon.discountPrice;
          }
        })
        .catch((error) => {
          console.log(error);
        });

      this.getCouponList();

      // 포인트 정보 가져오기
    },

    checkInJsonParams() {
      console.log("기본 출력 ", this.orderPaperDtos);
      console.log("JSON stringfy 출력", JSON.stringify(this.orderPaperDtos));
      console.log(
        "내부 객체 데이터 접근 [기본 상품 코드]",
        this.orderPaperDtos[0].baseProductSerialCode
      );
    },
  },

  created() {
    this.calculateTotalPrice();
    this.getCustomerInfo();
  },
};
</script>

<style>
.alignleft {
  float: left;
}
.alignright {
  float: right;
}
</style>
