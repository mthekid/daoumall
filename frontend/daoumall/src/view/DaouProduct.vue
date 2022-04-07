<template>
  <h2>상품 생성과 관련된 테스트 API 페이지 입니다.</h2>
  <hr />
  <button v-if="externalApiFlag" @click="testApi">외부API호출(GET)</button>
  <label v-if="internalApiFlag" for="mallName"
    ><b>판매점 이름: </b></label
  >
  <input v-if="internalApiFlag" type="text" v-model="mallName" />

  <input type="button" v-if="internalApiFlag" @click="fetchShopProducts(mallName)" value ="검색" />

  <div style="border: 5px solid Teal; float: left; width: 100%">
    <h3>판매점에서 판매하는 상품 정보</h3>
    <div style="border: 2px solid LightSeaGreen; float: left; width: 55%">
      <h5><b>기본 상품 목록</b></h5>
      <li v-for="productInfo in productsInfos" :key="productInfo">
        상품명: {{ productInfo.name }}, 가격: {{ productInfo.price }}, 상품
        정보: {{ productInfo.metaInfo }}, 상품 식별번호 :
        {{ productInfo.serialCode }}
      </li>
      <h5><b>추가 상품 목록</b></h5>
      <li
        v-for="additionalProduct in additionalproductsInfos"
        :key="additionalProduct"
      >
        상품명: {{ additionalProduct.additionalProduct.name }}, 가격:
        {{ additionalProduct.additionalProduct.price }}, 상품 정보:
        {{ additionalProduct.additionalProduct.metaInfo }}, 상품 식별번호 :
        {{ additionalProduct.additionalProduct.serialCode }}
      </li>
    </div>
    <div style="border: 2px solid LightSeaGreen; float: left; width: 45%">
      <input
        type="text"
        placeholder="serialCode"
        v-model="targetProductSerialCode"
      />
      <input
        class="btn btn-outline-secondary btn-sm"
        @click="searchProductDetails"
        type="submit"
        value="검색하기"
        :disabled="targetProductSerialCode.length == 0"
      />
      <p><b>상품과 연결된 추가 상품 정보</b></p>
      <li
        v-for="additionalProductInfo in additionalProductInfos"
        :key="additionalProductInfo"
      >
        상품명: {{ additionalProductInfo.additionalProduct.name }}, 가격:{{
          additionalProductInfo.additionalProduct.price
        }}, 상품정보:
        {{ additionalProductInfo.additionalProduct.metaInfo }} 식별번호:
        {{ additionalProductInfo.additionalProduct.serialCode }}
      </li>
      <br />
      <p><b>상품의 필수옵션 정보</b></p>
      <li
        v-for="essentialOptionInfo in essentialOptionInfos"
        :key="essentialOptionInfo"
      >
        상품명: {{ essentialOptionInfo.name }}, 가격:{{
          essentialOptionInfo.price
        }}, 상품 정보:{{ essentialOptionInfo.metaInfo }}, 필수옵션 식별번호:
        {{ essentialOptionInfo.serialCode }}
      </li>
    </div>
  </div>
  <hr />
  <div style="border: 3px solid SteelBlue; float: left; width: 30%">
    <h4><b>추가 상품</b> 생성</h4>
    <input
      type="text"
      name="productName"
      v-model="additionalProductInfo.name"
      class="form-control ml-2"
      placeholder="addtional product name"
    />
    <input
      type="number"
      name="price"
      v-model="additionalProductInfo.price"
      class="form-control ml-2"
      placeholder="price"
    />
    <input
      type="text"
      name="metaInfo"
      class="form-control ml-2"
      placeholder="metaInfo"
      v-model="additionalProductInfo.metaInfo"
    />
    <input
      class="btn btn-primary btn-sm"
      @click="createAdditionalProduct"
      type="submit"
      value="생성"
      :disabled="mallName.length == 0"
    />
    <button
      type="button"
      class="btn btn-warning btn-sm"
      @Click="clearAdditionalProduct"
    >
      비우기
    </button>
  </div>

  <div style="border: 3px solid DimGray; float: left; width: 40%">
    <h4>상품 - 필수 옵션 생성</h4>
    <input
      type="text"
      name="productName"
      v-model="baseProductName"
      class="form-control ml-2"
      placeholder="baseProductName"
    />


    <input
      type="number"
      name="price"
      v-model="baseProductPrice"
      class="form-control ml-2"
      placeholder="baseProductPrice"
    />

    <input
      type="text"
      name="metaInfo"
      v-model="baseProductMetaInfo"
      class="form-control ml-2"
      placeholder="baseProductMetaInfo"
    />

    <div class="form-group">
      <input
        @click="addEssentialOption"
        type="submit"
        class="btn btn-sm btn-secondary"
        value="필수 옵션 추가"
      />
      <button
        type="button"
        class="btn btn-warning btn-sm"
        @Click="clearProductBase"
      >
        비우기
      </button>
    </div>

    <div class="essentialOptions">
      <div
        class="form-row"
        v-for="(essentialOption, index) in essentialOptions"
        :key="index"
      >
        <hr />
        필수옵션[{{ index + 1 }}]

        <input
          v-model="essentialOption.name"
          :name="`essentialOptions[${index}].name`"
          type="text"
          placeholder="name"
          class="form-control ml-2"
        />

        <input
          v-model="essentialOption.price"
          :name="`essentialOptions[${index}].price`"
          type="number"
          placeholder="price"
          class="form-control ml-2"
        />

        <input
          v-model="essentialOption.metaInfo"
          :name="`essentialOptions[${index}].metaInfo`"
          type="text"
          placeholder="metaInfo"
          class="form-control ml-2"
        />
      </div>
    </div>
    <input
      @click="createBaseProductWithEssentialOption"
      type="submit"
      value="생성"
      class="btn btn-primary btn-sm"
      :disabled="mallName.length == 0"
    />
  </div>

  <div style="border: 3px solid gold; float: left; width: 30%">
    <h4>상품 - 추가 상품 연결</h4>
    <input
      type="text"
      name="productName"
      v-model="productSerialCode"
      placeholder="base product serialCode"
      class="form-control ml-2"
    />
    
    <input
      type="text"
      name="price"
      v-model="additionalProductSerialCode"
      placeholder="additional product serialCode"
      class="form-control ml-2"
    />
    <input
      @click="connectBaseProductWithEssentialoption"
      type="submit"
      value="연결"
      class="btn btn-primary btn-sm"
    />
  </div>
</template>

<script>
export default {
  data() {
    return {
      mallName: "", // 판매점 이름
      productsInfos: null, // 상품 정보 [ array로 처리 ]
      externalApiFlag: false,
      internalApiFlag: true,
      // 생성할 추가상품 정보
      additionalProductInfo: {
        name: "",
        price: "",
        metaInfo: "",
        serialCode: "",
      },

      // 생성 상품 이름
      baseProductName: "",
      baseProductPrice: "",
      baseProductMetaInfo: "",

      // 필수옵션 정보
      essentialOption: {
        name: "",
        price: "",
        metaInfo: "",
        serialCode: "",
      },
      // 필수옵션 정보 array
      essentialOptions: [],

      // 연결할 추가상품 serialCode
      additionalProductSerialCode: "",
      // 연결할 상품 serialCode
      productSerialCode: "",

      // 검색 상품 시리얼 코드
      targetProductSerialCode: "",

      // 검색 추가 상품 정보
      additionalProductInfos: null,

      // 검색 필수 상품 정보
      essentialOptionInfos: null,

      // 검색 추가 상품 정보
      additionalproductsInfos: [],
    };
  },

  methods: {
    fetchShopProducts(mallName) {
      console.log("검색한 판매점: ", mallName, "으로 상품-필수 옵션 상품을 조회합니다.")
      this.fetchProductsData(mallName);
      this.fetchAdditionalProductsData(mallName);
      console.log("검색한 판매점 조회를 완료합니다.")
    },

    fetchProductsData(mallName) {
      console.log(mallName, "의 기본 상품 정보들을 가져옵니다.");
      this.axios
        .get("api/daou/mall/search/products", {
          params: {
            mallName: mallName,
          },
        })
        .then((res) => {
          this.productsInfos = res.data;
          console.log(res.status);
          console.log(res.data);
        })
        .catch((error) => {
          console.log(error);
        });
    },

    fetchAdditionalProductsData(mallName) {
      console.log(mallName, "의 추가 상품 정보들을 가져옵니다.");
      this.axios
        .get("api/daou/mall/search/additional-products", {
          params: {
            mallName: mallName,
          },
        })
        .then((res) => {
          console.log("추가상품을 조회합니다.");
          this.additionalproductsInfos = res.data;
          console.log(res.data);
        })
        .catch((error) => {
          console.log(error);
        });
    },

    createAdditionalProduct() {
      console.log("추가 상품을 생성합니다.");
      var createdSerialCode = "T-D-AP-" + Math.floor(Math.random() * 100000);
      var data = {
        mallName: this.mallName,
        additionalProduct: {
          name: this.additionalProductInfo.name,
          price: this.additionalProductInfo.price,
          metaInfo: this.additionalProductInfo.metaInfo,
          serialCode: createdSerialCode,
        },
      };
      console.log("추가 상품 정보 ", JSON.stringify(data))
      this.axios
        .post("/api/daou/mall/additional-product", data)
        .then((res) => {
          console.log("생성된 시리얼 코드 " + createdSerialCode);
          console.log(res);
          const additionalProductInfo = {
            additionalProduct: {
              name: res.data.additionalProduct.name,
              price: res.data.additionalProduct.price,
              metaInfo: res.data.additionalProduct.metaInfo,
              serialCode: res.data.additionalProduct.serialCode,
            },
          };
          console.log("추가 상품 " ,JSON.stringify(additionalProductInfo) + "를 추가합니다.");
          this.additionalproductsInfos.push(additionalProductInfo);
          this.clearAdditionalProduct();
        })
        .catch((err) => {
          console.log(err);
        });
    },

    createBaseProductWithEssentialOption() {
      console.log("상품-필수 옵션을 생성합니다.");
      var createdSerialCode = "T-D-P-" + Math.floor(Math.random() * 100000);
      var data = {
        mallName: this.mallName,
        productName: this.baseProductName,
        price: this.baseProductPrice,
        metaInfo: this.baseProductMetaInfo,
        serialCode: createdSerialCode,
        essentialOptions: this.essentialOptions,
      };
      console.log(
        "생성될 필수 옵션 정보" + JSON.stringify(this.essentialOptions, null, 2)
      );
      console.log(
        "상품 - 필수 옵션을 생성합니다." + JSON.stringify(data, null, 2)
      );
      this.axios
        .post("/api/daou/mall/product-essential-option", data)
        .then((res) => {
          console.log("생성될 serialCode ", createdSerialCode);
          const productInfo = {
            name: res.data.productResDTO.name,
            price: res.data.productResDTO.price,
            metaInfo: res.data.productResDTO.metaInfo,
            serialCode: res.data.productResDTO.serialCode,
          };
          this.productsInfos.push(productInfo);
          console.log(res);
          this.clearProductBase();
        })
        .catch((err) => {
          console.log(err);
        });
    },

    connectBaseProductWithEssentialoption() {
      console.log("상품 - 추가상품을 연결합니다.");
      var data = {
        productSerialCode: this.productSerialCode,
        additionalProductSerialCode: this.additionalProductSerialCode,
      };
      this.axios
        .post("/api/daou/mall/additional-with-product", data)
        .then((res) => {
          console.log("상품을 추가 상품과 연결합니다. 결과 : ", JSON.stringify(res.data));
          this.productSerialCode = "";
          this.additionalProductSerialCode = "";
        })
        .catch((err) => {
          console.log(err);
        });
    },

    addEssentialOption() {
      var createdSerialCode = "T-D-OP-" + Math.floor(Math.random() * 100000);
      console.log(
        "필수 옵션을 추가합니다. 생성된 식별 번호 : ",createdSerialCode
      );
      this.essentialOptions.push({
        name: "",
        price: 0,
        metaInfo: "",
        serialCode: createdSerialCode,
      });
      console.log(
        "필수 옵션 정보 " + JSON.stringify(this.essentialOptions, null, 2)
      );
    },

    searchProductDetails() {
      console.log(
        this.targetProductSerialCode, "의 필수 옵션과 추가 상품을 조회합니다."
      );
      this.axios
        .get("/api/daou/mall/search/product", {
          params: {
            productSerialCode: this.targetProductSerialCode,
          },
        })
        .then((res) => {
          console.log(res.data);
          this.additionalProductInfos = res.data.additionalProducts;
          this.essentialOptionInfos = res.data.essentialOptions;
        })
        .catch((error) => {
          console.log(error);
        });
    },

    clearAdditionalProduct() {
      console.log("추가상품의 정보를 비웁니다.");
      this.additionalProductInfo.name = "";
      this.additionalProductInfo.price = 0;
      this.additionalProductInfo.metaInfo = "";
    },

    clearProductBase() {
      console.log("기본 상품(필수 옵션 포함)의 정보를 비웁니다.");
      this.baseProductName = "";
      this.baseProductPrice = 0;
      this.baseProductMetaInfo = "";
      this.essentialOptions = [];
    },
  },
};
</script>

<style></style>
