<template>
  <h1>다우몰</h1>
  <hr />
  <ul class="nav nav-pills nav-fill">
    <li class="nav-item" v-for="mall in mallList" :key="mall">
      <input type="button" class="nav-item nav-link" :value="mall.mallName" @click="fetchProductsData(mall.mallName)"/>
    </li>
  </ul>
  <div class="row">
    <DaouCard
      v-for="productInfo in mallProductInfos"
      :key="productInfo"
      :name="productInfo.name"
      :price="productInfo.price"
      :metaInfo="productInfo.metaInfo"
      :serialCode="productInfo.serialCode"
      :mallName="mallName"
    ></DaouCard>
  </div>
</template>

<script>
import DaouCard from "@/components/DaouCard.vue";

export default {
  components: {
    DaouCard,
  },

  data() {
    return {
      mallProductInfos: [],
      mallName: "",

      mallList: [],
    };
  },

  methods: {
    fetchProductsData(mallName) {
      this.mallName = mallName;
      this.axios
        // .get("api/daou/mall/search/products?mallName="+mallName) // 직접 붙이는 방식
        .get("api/daou/mall/search/products", {
          params: {
            mallName: mallName,
          },
        })
        .then((res) => {
          this.mallProductInfos = res.data;
          console.log(
            "상품 목록 조회 반환 데이터 " + JSON.stringify(res.data, null, 2)
          );
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
          console.log("판매점 목록 : ", JSON.stringify(res.data));
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
