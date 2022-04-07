<template>
  <div id="app" class="container">
    <ul class="nav nav-pills nav-fill">
      <li class="nav-item" v-for="mall in mallList" :key="mall">
        <input
          type="button"
          class="nav-item nav-link"
          :value="mall.mallName"
          @click="fetchCouponData(mall.mallName)"
        />
      </li>
    </ul>

    <div class="card">
      <div class="card-header">쿠폰 생성 내역</div>
      <div class="card-body">
        <b>금액 쿠폰 생성 내역</b>
        <li v-for="amountCoupon in amountCoupons" :key="amountCoupon">
          쿠폰 정보: {{ amountCoupon.couponInfo }}, 만료 날짜:
          {{ amountCoupon.expiredDate }}, 식별 번호:
          {{ amountCoupon.serialCode }},
        </li>
        <br /><b>할인 쿠폰 생성 내역</b>
        <li v-for="rateCoupon in rateCoupons" :key="rateCoupon">
          쿠폰 정보: {{ rateCoupon.couponInfo }}, 만료 날짜:
          {{ rateCoupon.expiredDate }}, 식별 번호: {{ rateCoupon.serialCode }},
        </li>
      </div>
      <div class="card-header">다우몰 쿠폰 발행</div>
      <div class="card-body">
        <h4>금액 쿠폰 생성하기</h4>
        <div class="input-group input-group">
          <input
            type="text"
            ref="get_id"
            class="form-control ml-2"
            :placeholder="mallName"
            v-model="mallName"
          />
          <input
            type="number"
            ref="get_id"
            class="form-control ml-2"
            placeholder="할인금액"
            v-model="discountAmount"
          />
          <input
            type="number"
            ref="get_id"
            class="form-control ml-2"
            placeholder="최소 금액"
            v-model="limitPrice"
          />
          <div class="input-group-append">
            <p class="h4">Expire Date:</p>
          </div>
          <input
            type="date"
            ref="get_id"
            class="form-control ml-2"
            v-model="amountExpiredDate"
          />
          <div class="input-group mb-2">
            <input
              type="text"
              ref="get_id"
              class="form-control ml-2"
              placeholder="쿠폰 내용"
              v-model="amountCouponInfo"
            />{{ amountCouponInfo }}
          </div>
          <div class="col-md-12 text-center">
            <button class="btn btn-sm btn-primary" @click="createAmountCoupon">
              금액 쿠폰 생성하기
            </button>
          </div>
          <div class="card-body">
            <div class="form-group">
              <input
                type="text"
                class="form-control"
                ref="post_title"
                placeholder="사용자 이름"
                v-model="amountCouponOfferCustomer"
              />
            </div>
            <div class="form-group">
              <input
                type="text"
                class="form-control"
                ref="post_description"
                placeholder="쿠폰 serialCode"
                v-model="OfferedAmountCouponSerialCode"
              />
            </div>
            <button class="btn btn-sm btn-primary" @click="offerAmountCoupon">
              금액 쿠폰 발행하기
            </button>
          </div>
          <div class="card-body">
            할인 금액 : {{ discountAmount }} 최소 구매 금액 : {{ limitPrice }}
            <br />
            만료 기한 : {{ amountExpiredDate }} <br />
            금액 쿠폰 정보 : {{ amountCouponInfo }}
          </div>
          <hr />
        </div>
        <h4>비율 할인 쿠폰 발행하기</h4>
        <div class="input-group input-group">
          <input
            type="text"
            ref="get_id"
            class="form-control ml-2"
            :placeholder="mallName"
            v-model="mallName"
          />
          <input
            type="number"
            ref="get_id"
            class="form-control ml-2"
            placeholder="할인율"
            v-model="discountRate"
          />
          <input
            type="number"
            ref="get_id"
            class="form-control ml-2"
            placeholder="최대 할인 금액"
            v-model="upperBoundPrice"
          />
          <div class="input-group-append">
            <p class="h4">Expire Date:</p>
          </div>
          <input
            type="date"
            ref="get_id"
            class="form-control ml-2"
            v-model="rateExpiredDate"
          />
          <div class="input-group mb-2">
            <input
              type="text"
              ref="get_id"
              class="form-control ml-2"
              placeholder="쿠폰 내용"
              v-model="rateCouponInfo"
            />
          </div>
          <div class="col-md-12 text-center">
            <button class="btn btn-sm btn-primary" @click="createRateCoupon">
              비율 할인 쿠폰 생성하기
            </button>
          </div>
          <div class="card-body">
            <div class="form-group">
              <input
                type="text"
                class="form-control"
                ref="post_title"
                placeholder="사용자 이름"
                v-model="rateCouponOfferCustomer"
              />
            </div>
            <div class="form-group">
              <input
                type="text"
                class="form-control"
                ref="post_description"
                placeholder="비율 쿠폰 serialCode"
                v-model="OfferedRateCouponSerialCode"
              />
            </div>
            <hr />
            <button class="btn btn-sm btn-primary" @click="offerRateCoupon">
              비율 할인 쿠폰 발행하기
            </button>
          </div>
          <div class="card-body">
            할인 비율 : {{ discountRate }} 최대 할인 금액 :
            {{ upperBoundPrice }}
            <br />
            만료 기한 : {{ rateExpiredDate }} <br />
            할인 쿠폰 정보 : {{ rateCouponInfo }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      mallName: "",
      mallList: [],

      // 생성 금액 쿠폰 정보
      amountCoupon: null,
      amountCouponInfo: "",
      discountAmount: 0,
      amountExpiredDate: "",
      limitPrice: "",
      amountCouponSerialCode: "",

      // 생성 할인 쿠폰 정보
      rateCouponInfo: "",
      discountRate: 0,
      rateExpiredDate: "",
      upperBoundPrice: 0,
      rateCouponSerialCode: "",

      // 생성된 금액 쿠폰 내역
      amountCoupons: [],
      // 생성된 할인 쿠폰 내역
      rateCoupons: [],

      // 발행 쿠폰 정보 [ 금액 - 비율 순]
      amountCouponOfferCustomer: "",
      OfferedAmountCouponSerialCode: "",
      rateCouponOfferCustomer: "",
      OfferedRateCouponSerialCode: "",
    };
  },

  methods: {
    fetchCouponData(mallName) {
      console.log("판매점 쿠폰 정보를 패치합니다.");
      this.mallName = mallName;
      this.fetchAmountCoupons(mallName);
      this.fetchRateCoupons(mallName);
    },
    offerAmountCoupon() {
      console.log("사용자에게 금액 쿠폰을 발행합니다.");
      var offeredAmountCouponInfo = {
        customerLoginId: this.amountCouponOfferCustomer,
        couponSerialCode: this.OfferedAmountCouponSerialCode,
        mallName: this.mallName
      };
      this.axios
        .post("/api/daou/mall/coupon/offer-amount", offeredAmountCouponInfo)
        .then((res) => {
          console.log(
            "금액 쿠폰이 발행되었습니다. ",
            JSON.stringify(res.data, null, 2)
          );
        })
        .catch((error) => {
          console.log(error);
        });
    },

    offerRateCoupon() {
      console.log("사용자에게 할인 쿠폰을 발행합니다.");
      var offeredRateCouponInfo = {
        customerLoginId: this.rateCouponOfferCustomer,
        couponSerialCode: this.OfferedRateCouponSerialCode,
        mallName: this.mallName
      };
      this.axios
        .post("/api/daou/mall/coupon/offer-rate", offeredRateCouponInfo)
        .then((res) => {
          console.log(
            "비율 쿠폰이 발행되었습니다. ",
            JSON.stringify(res.data, null, 2)
          );
        })
        .catch((error) => {
          console.log(error);
        });
    },

    createAmountCoupon() {
      console.log("만료 날짜 " + this.amountExpiredDate);
      var createdSerialCode = "T-D-A-C" + Math.floor(Math.random() * 100000);
      var amountCoupon = {
        mallName: this.mallName,
        couponInfo: this.amountCouponInfo,
        discountAmount: this.discountAmount,
        expiredDate: this.amountExpiredDate,
        limitPrice: this.limitPrice,
        serialCode: createdSerialCode,
      };
      this.axios
        .post("/api/daou/mall/coupon/amount", amountCoupon)
        .then((res) => {
          var coupon = {
            mallName: res.data.mallName,
            couponInfo: res.data.couponInfo,
            expiredDate: res.data.expiredDate,
            serialCode: res.data.serialCode,
          };
          this.amountCoupons.push(coupon);
          console.log(res);
        })
        .catch((err) => {
          console.log(err);
        });
    },

    createRateCoupon() {
      console.log("비율 쿠폰 만료 날짜 " + this.rateExpiredDate);
      var createdSerialCode = "T-D-R-C" + Math.floor(Math.random() * 100000);
      var rateCoupon = {
        mallName: this.mallName,
        couponInfo: this.rateCouponInfo,
        rate: this.discountRate,
        expiredDate: this.rateExpiredDate,
        upperBoundPrice: this.upperBoundPrice,
        serialCode: createdSerialCode,
      };

      this.axios
        .post("/api/daou/mall/coupon/rate", rateCoupon)
        .then((res) => {
          var coupon = {
            mallName: res.data.mallName,
            couponInfo: res.data.couponInfo,
            expiredDate: res.data.expiredDate,
            serialCode: res.data.serialCode,
          };
          this.rateCoupons.push(coupon);
          console.log("비율 쿠폰 생성 완료 " + res.data);
        })
        .catch((err) => {
          console.log(err);
        });
    },

    fetchAmountCoupons(mallName) {
      console.log("금액 쿠폰을 가져옵니다.");
      this.axios
        .get("/api/daou/mall/search/amount-coupons", {
          params: {
            mallName: mallName,
          },
        })
        .then((res) => {
          console.log(
            "금액 쿠폰을 가져왔습니다." + JSON.stringify(res.data, null, 2)
          );
          this.amountCoupons = res.data;
        })
        .catch((error) => {
          console.log(error);
        });
    },

    fetchRateCoupons(mallName) {
      console.log("할인 쿠폰을 가져옵니다.");
      this.axios
        .get("/api/daou/mall/search/rate-coupons", {
          params: {
            mallName: mallName,
          },
        })
        .then((res) => {
          console.log(
            "비율 쿠폰을 가져왔습니다." + JSON.stringify(res.data, null, 2)
          );
          this.rateCoupons = res.data;
        })
        .catch((error) => {
          console.log(error);
        });
    },
    getMallList() {
      console.log("판매점 목록을 가져옵니다.");
      this.axios
        .get("/api/daou/mall/list")
        .then((res) => {
          console.log("판매점 목록을 성공적으로 가져왔습니다.");
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
