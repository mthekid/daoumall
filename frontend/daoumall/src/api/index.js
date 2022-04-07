import axios from "axios";

function fetchAmountCouponDatas() {
    console.log("외부에서 함수 호출하기")
  return this.axios.get("/api/daou/mall/search/amount-coupons", {
    params: {
      mallName: "testMall",
    },
  });
}

function fetchExternalApi() {
    console.log("외부 API 호출하기")
    return axios.get("https://jsonplaceholder.typicode.com/todos/1")
}

function testExportFunction() {
    // alert('Hello')
    console.log("외부 함수 호출하기")
}

export default {
    fetchAmountCouponDatas,
}

export {
    testExportFunction,
    fetchExternalApi
}