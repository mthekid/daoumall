안녕하세요. 
비즈 웹 개발팀 인턴사원 문현준입니다.

<br />

## Description

인턴사원-기술 과제 : <b>쇼핑몰 상품 주문 프로세스 설계 및 구현</b>에 대해서 소개합니다. <br />
다우몰에서는 다양한 상품을 판매합니다. <br />
고객은 높은 결제 금액을 가질 수록 높은 등급을 가지며 등급별 포인트 적립 혜택을 받을 수 있습니다. 
<br /> 다양한 쿠폰(금액, 할인율 적용)을 제공하고 있으니 다우몰을 많이 이용해주시길 바랍니다.

<hr/>

### 과제 핵심 요구사항

#### 속성 정의

* 고객(Customer) : 상품 구매자 [로그인 필요]
* 상품(Product) : 옵션 별 상품. 추가 상품이 존재.
* 쿠폰(Coupon) : 특정 금액 할인. 주문 가격 비례 할인
* 포인트(Point) : 현금처럼 자유롭게 사용 가능. 지정된 비율로 적립[만료일 존재]
* 결제수단(Payment) : 카드 & 계좌이체
* 주문서(PaymentManagement) : 상품 정보, 할인 및 포인트, 결제수단 선택 후 최종 결제 진행
* 주문 완료 : 최종 금액에 대한 주문 처리 [ 주문 번호 생성 및 이력 관리 ]
* 주문 취소 : 주문완료 후 환불처리 [결제금액, 쿠폰, 포인트 되돌리기 ]

#### subdomain-A : mall & product [back : 03.23 완료. front : 04.05 완료] 

* 다우몰에 입점한 판매점은 상품을 등록할 수 있다.  
    * 상품 등록은 필수 옵션과 함께 등록해야 한다. [필수]
    * 추가 상품은 별도로 등록할 수 있다. [선택사항]
<br />


#### core domain : payment & coupon & points [back : 03.28 완료. front : 04.05 완료]
* 다우몰 사용자에게 다우 서비스에서 제공하는 서비스 이용권을 판매. [핵심 서비스]
    * 판매점 별로 쿠폰 제공 [금액 할인 & 비율 할인]
    * 포인트 서비스 제공 [등급별 포인트 적립]
    * 포인트 사용 가능
<br />

#### subdomain-B : order history & canceled order history [back :  04.01 완료 . front : 04.05 완료]
* 주문내역 관리 및 취소 서비스 제공
<br />
<hr/>

### 개발 고려 사항

#### 백엔드
* Programming Language : kotlin(1.6.10)
* java-version : 11(zulu)
* spring-boot : 2.6.4
* JPA : 1.6.10
* DB : MySQL
<br />

#### 프론트엔드 [front-end/daoumall 참고]
* vue.js(v3.0+)


<hr/>

### 소스코드 관리

* 제공된 BitBucket에 소스 코드 이력 관리

##### git flow
* master - development - feature/{sub-feature}
    * master: 최종 product 소스 코드
    * development: 개발용 소스 코드
    * feature/{sub-feaure}: 요구사항 도메인내 세부 기능 진행
    * pull request 시점 : feature 완료 후 development 반영
        * pull request 리뷰어: 비즈 웹 개발팀
<br />
<hr/>

## 프로젝트 타임라인

#### 1주차(2022.03.14 ~ 18) : 요구사항 분석 및 데이터베이스 설계 + 코틀린 & JPA 학습
* ERD 설계 진행
* Kotlin 기반 JPA 및 스프링 부트 학습 진행
* 테스트 작성 및 ERD 구현 진행
* bitbucket 사용 및 개발 진행
<br />

#### 2주차(2022.03.21 ~ 25) : 중간 발표 : 설계 리뷰. & JPA 구현 & ERD 설계 완료 
* 중간 발표 준비
* Kotlin JPA 기반 설계 구현 - 60~70% 
* 비즈니스 로직(쿠폰, 포인트, 결제) 구현
<br />

#### 3주차(2022.03.28 ~ 4.01) : REST API 진행 및 프론트 구현
* 타임리프 혹은 Vue.js 기반 프론트엔드 서비스 준비
* 최종 발표 준비(구현 + 설계)
<br />

#### 4주차(2022.04.04 ~ 4.05) : 최종 구현 완료 및 최종 발표 준비. 
* 최종 발표 준비 및 프론트엔드 개선
<br />

#### 4주차[최종 발표 이후] 2022.04.06 ~ 08 : 최종 발표 및 회고 시간
* 최종 발표 준비 및 회고
<br />

#### 중요한 이벤트

* 2022.03.23(수) : 중간 발표 : "설계 리뷰 및 피드백"
* 2022.04.06(수) : 최종 발표 : "최종 결과물 시연"
<br />
<hr />

# 요구사항 분석 & 도메인 설계 & API 명세

## 전체 ERD
<b>2022_04_05</b>
![전체_ERD설계도](https://user-images.githubusercontent.com/31848154/161711425-71f297a9-3fd7-435c-a1e3-57d226c350fd.jpg)


## daouMall subDomain-A : 판매점 등록 및 상품 등록(필수옵션, 추가상품, 주문서) 
* branch : feature/product -> development branch
<br />

#### 요구사항 분석
##### use case

1. 판매점을 등록할 수 있다. 
2. 입점한 판매점에 상품을 등록할 수 있다. 
   * 조건 : 반드시 필수 옵션과 등록해야 한다.
3. 입점한 판매점에서 판매할 상품과 함께 추가로 판매하고자 하는 상품을 등록할 수 있다.
4. 결제 내역(주문번호)에는 [ 상품(필수옵션) + 추가상품 ] 정보가 기록된다.
<br />

#### 도메인 설계(ERD)

##### 상품 도메인
![판매점-상품-정보](https://user-images.githubusercontent.com/31848154/161711522-77f17dc2-136b-4aea-a4ab-386d055fd178.jpg)

##### 결제정보 도메인
![결제관리-주문서정보-상품-추가상품](https://user-images.githubusercontent.com/31848154/161711560-96bdd028-af3f-44fd-bd3c-25e6cc742f36.png)


#### API 명세(REST API & 예시 & JSON 입출력)

##### REST API 정보
| 이름                 | 요청 METHOD | 요청 URI | 설명                           |
|--------------------|-----------|----------------|------------------------------|
| 판매점 생성             | POST      | http://localhost:8084/daou/mall/create | 판매점을 생성합니다.                  |
| 판매점에 상품(필수 옵션) 생성  | POST      | http://localhost:8084/daou/mall/product-essential-option | 필수옵션과 함께 상품을 판매점에 등록합니다.     |
| 판매점에 추가 상품 생성      | POST      | http://localhost:8084/api/daou/mall/additional-product | 판매점에 새로운 추가상품을 등록합니다         |
| 상품 - 추가상품 연결       | POST      | http://localhost:8084/api/daou/mall/additional-with-product | 판매점에서 판매중인 상품과 추가상품을 연결합니다   |
| 판매점 판매 상품 목록 조회    | GET       | http://localhost:8084/api/daou/mall/search/products | 판매점에서 판매중인 상품들을 조회합니다.       |
| 판매점 판매 상품 세부 정보 조회 | GET       | http://localhost:8084/api/daou/mall/search/product? | 판매점에서 판매중인 상품의 세부 정보를 조회합니다. |


### REST API Example

<details markdown="1">
  <summary> 판매점 등록 및 상품 등록 REST API Example 보기</summary>


#### 판매점 추가
JSON 입력 정보<br />

```json
{
  "mallName": "testMall",
  "adminName": "adminName"
}
```
<br />
JSON 출력 정보

```json
{
  "mallName": "testMall",
  "status": "OK"
}

```

#### 판매점에 필수 옵션 상품 추가
JSON 입력 정보 
```json
{
  "mallName": "testMall",
  "productName": "testProduct",
  "price": 7777,
  "serialCode": "T-T1",
  "metaInfo": "테스트용 상품 등록",
  "essentialOptions": [
    {
      "name": "Test Essential-1",
      "price": 500,
      "metaInfo": "Test Essential-1"
    },
    {
      "name": "Test Essential-2",
      "price": 1000,
      "metaInfo": "Test Essential-2"
    },
    {
      "name": "Test Essential-3",
      "price": 500,
      "metaInfo": "Test Essential-1"
    }
  ]
}

```
<br />

JSON 출력 정보
```json
{
  "productResDTO": {
    "name": "testProduct",
    "price": 7777,
    "metaInfo": "테스트용 상품 등록"
  },
  "essentialOptionDTOs": [
    {
      "name": "Test Essential-1",
      "price": 500,
      "metaInfo": "Test Essential-1"
    },
    {
      "name": "Test Essential-3",
      "price": 500,
      "metaInfo": "Test Essential-1"
    },
    {
      "name": "Test Essential-2",
      "price": 1000,
      "metaInfo": "Test Essential-2"
    }
  ]
}

``` 

<br />

#### 판매점에 추가 상품 추가
JSON 입력 정보
```json
{
    "mallName": "testMall",
    "additionalProduct": {
        "name" : "test-addtional-product",
        "price" : 8000,
        "serialCode" : "TA-1",
        "metaInfo" : "테스트 추가 상품입니다."
    }
}
``` 
JSON 출력 정보
```json
{
    "mallName": "test-addtional-product",
    "additionalProduct": {
        "name": "test-addtional-product",
        "price": 8000,
        "serialCode": "",
        "metaInfo": "테스트 추가 상품입니다."
    }
}
``` 

#### 상품 - 추가상품 연결
JSON 입력 정보
```json
{
    "productSerialCode" : "T-T1",
    "additionalProductSerialCode" : "TA-1"
}
``` 
JSON 출력 정보
```json
{
    "productName": "testProduct",
    "additionalProductName": "test-addtional-product"
}
``` 

#### 판매점 판매 상품들 조회
curl 입력 정보

```
curl --location --request GET 'http://localhost:8084/api/daou/mall/search/products?mallName=testMall'
```

JSON 출력 정보
```json
[
    {
        "name": "testProduct",
        "price": 7777,
        "metaInfo": "테스트용 상품 등록"
    },
    {
        "name": "testProduct B",
        "price": 1111111,
        "metaInfo": "테스트용 두번째 상품 등록"
    }
]
``` 

#### 판매점 판매 상품 세부 정보 조회
curl 입력 정보
```
curl --location --request GET 'http://localhost:8084/api/daou/mall/search/product?productSerialCode=T-T1'
``` 
JSON 출력 정보
```json
{
    "productRes": {
        "name": "testProduct",
        "price": 7777,
        "metaInfo": "테스트용 상품 등록"
    },
    "additionalProducts": [
        {
            "mallName": "test-addtional-product",
            "additionalProduct": {
                "name": "test-addtional-product",
                "price": 8000,
                "serialCode": "",
                "metaInfo": "테스트 추가 상품입니다."
            }
        },
        {
            "mallName": "test-addtional-productB",
            "additionalProduct": {
                "name": "test-addtional-productB",
                "price": 12000,
                "serialCode": "",
                "metaInfo": "테스트 추가 상품2입니다."
            }
        }
    ],
    "essentialOptions": [
        {
            "name": "Test Essential-1",
            "price": 500,
            "metaInfo": "Test Essential-1"
        },
        {
            "name": "Test Essential-2",
            "price": 1000,
            "metaInfo": "Test Essential-2"
        },
        {
            "name": "Test Essential-3",
            "price": 500,
            "metaInfo": "Test Essential-1"
        }
    ]
}
``` 

</details> 
<hr />

## daouMall core domain-A : 쿠폰 등록 & 사용 및 결제 [2022.03.23 ~ 2022.03.28 완료]
* 쿠폰 & 결제 도메인 : branch : feature/coupon -> development branch
<br />

#### 요구사항 분석
##### use case : 쿠폰 등록 & 쿠폰 사용 & 결제

1. 상품몰 별로 쿠폰을 관리한다.
2. 상품몰에서는 쿠폰을 사용자에게 전달할 수 있다.
3. 쿠폰은 비율, 금액 쿠폰으로 2종류의 쿠폰이 존재한다.
   * 같은 금액으로 할인되는 경우 만료기한이 짧은 순으로 쿠폰이 사용된다.
4. 주문취소시 사용한 쿠폰은 사용 -> 미사용으로 전환된다.

#### 도메인 설계(ERD) [ 쿠폰 ]
![고객-쿠폰도메인](https://user-images.githubusercontent.com/31848154/161711906-ec76ede9-f431-4047-8b4a-31381e9d0610.jpg)

#### API 명세(REST API & 예시 & JSON 입출력)

##### REST API 정보
| 이름            | 요청 METHOD | 요청 URI | 설명                             |
|---------------|-----------|----------------|--------------------------------|
| 판매점 금액 쿠폰 등록  | POST      | http://localhost:8084/api/daou/mall/coupon/amount | 판매점에 금액 쿠폰을 등록합니다.             |
| 판매점 비율 쿠폰 등록  | POST      | http://localhost:8084/api/daou/mall/coupon/rate | 판매점에 비율 쿠폰을 등록합니다.             |
| 고객에게 금액 쿠폰을 발행 | POST      |    http://localhost:8084/api/daou/mall/coupon/offer-amount    | 고객에게 금액 쿠폰을 발행합니다              |
| 고객에게 비율 쿠폰을 발행 | POST      |  http://localhost:8084/api/daou/mall/coupon/offer-rate      | 고객에게 비율 쿠폰을 발행합니다.             |
| 고객이 사용 가능한 쿠폰 조회 | GET       |  http://localhost:8084/api/daou/mall/coupon/find/can-use-coupons      | 고객이 사용할 수 있는 쿠폰들을 조회합니다.       |
| 고객이 사용한 쿠폰 조회 | GET       | http://localhost:8084/api/daou/mall/coupon/find/used-coupons      | 고객이 사용헀던 쿠폰 이력들을 조회합니다.        |
| 상품에 효율적인 쿠폰 자동 적용 | GET       | http://localhost:8084/api/daou/mall/coupon/use/auto-apply-coupon     | 고객이 고른 상품 가격에 적합한 쿠폰을 자동적용합니다. |


### REST API Example

<details markdown="2">
    <summary> 쿠폰 등록 및 사용 REST API Example 보기</summary>


#### 판매점 금액 쿠폰 등록
JSON 입력 정보<br />
```json
{
    "mallName" : "testMall",
    "couponInfo" : "금액 쿠폰A",
    "discountAmount" : 10000,
    "expiredDt" : "2022-04-15",
    "limitPrice" : 20000,
    "serialCode" : "T-T-A1"
}
```

JSON 출력 정보
```json
{
    "mallName": "testMall",
    "couponInfo": "금액 쿠폰A",
    "expiredDt": "2022-04-15"
}
```

#### 판매점 비율 쿠폰 등록
JSON 입력 정보<br />
```json
{
    "mallName" : "testMall",
    "couponInfo" : "테스트용 비율 할인 쿠폰입니다.",
    "rate" : 5,
    "expiredDt" : "2022-04-15",
    "upperBoundPrice" : 60000,
    "serialCode" : "T-T-R1"
}
```

JSON 출력 정보
```json
{
    "mallName": "testMall",
    "couponInfo": "테스트용 비율 할인 쿠폰입니다.",
    "expiredDt": "2022-04-15"
}
```

#### 고객에게 금액 쿠폰 등록
JSON 입력 정보<br />
```json
{
    "customerLoginId": "test5730",
    "couponSerialCode": "T-T-A1"
}
```

JSON 출력 정보
```json
{
    "customerLoginId": "test5730",
    "couponSerialCode": "T-T-A1"
}
```

#### 고객에게 비율 쿠폰 등록
JSON 입력 정보<br />
```json
{
    "customerLoginId": "test5730",
    "couponSerialCode": "T-T-R1"
}
```

JSON 출력 정보
```json
{
    "customerLoginId": "test5730",
    "couponSerialCode": "T-T-R1"
}
```
#### 고객이 사용할 수 있는 쿠폰 조회
curl 입력 정보
```
curl --location --request GET 'http://localhost:8084/api/daou/mall/coupon/find/can-use-coupons?customerLoginId=test5730 --data-raw '
```
JSON 출력 정보
```json
[
    {
        "discountInfo": 10000,
        "expiredDt": "2022-04-15",
        "createDt": "2022-03-25",
        "constraint": 20000,
        "mallName": "testMall",
        "serialCode": "T-T-A1",
        "couponCategory": "AMOUNT"
    }
]
```

#### 고객이 사용한 쿠폰 조회
curl 입력 정보
```
curl --location --request GET 'http://localhost:8084/api/daou/mall/coupon/find/used-coupons?customerLoginId=test5730'
```
JSON 출력 정보
```json
[]
```

#### 상품에 효율적인 쿠폰 자동 적용
curl 입력 정보
```
curl --location --request GET 'http://localhost:8084/api/daou/mall/coupon/use/auto-apply-coupon?customerLoginId=test5730&totalPrice=75000'
```
JSON 출력 정보
```json
{
    "originalPrice": 75000,
    "couponSerialCode": "can't use",
    "couponCATEGORY": "RATE",
    "discountPrice": 0
}
```

</details>
<br />
<hr />

## daouMall core domain-B : 포인트 적립 및 포인트 사용 
* 쿠폰 & 결제 도메인 : branch : feature/point -> development branch
  <br />

#### 요구사항 분석
##### use case : 포인트 적립 및 포인트 사용

1. 포인트는 주문관리에 있는 "진행중"상태의 주문을 "구매 확정"으로 전환할 때 생성된다.
2. 포인트는 "남은 금액"으로 관리되며 남은 금액에 대한 사용 내역은 포인트를 사용한 주문내역의 주문 고유 번호로 관리된다.
  * 주문 취소시 주문 번호에 배정된 포인트 사용 내역을 통해 포인트를 되돌린다.
3. 포인트는 만료기한이 짧은 순으로 사용된다.

#### 도메인 설계(ERD) [ 포인트 적립 및 포인트 사용 ]
![고객-포인트-결제관리](https://user-images.githubusercontent.com/31848154/161712291-7e63c1ad-ad70-4025-9254-4a271daf6544.jpg)


#### API 명세(REST API & 예시 & JSON 입출력)

##### REST API 정보
| 이름               | 요청 METHOD | 요청 URI                                                                       | 설명                      |
|------------------|-----------|------------------------------------------------------------------------------|-------------------------|
| 결제 내역 확정         | POST      | http://localhost:8084/api/daou/mall/payment/complete                                 | 결제 내역을 확정하여 포인트가 생성됩니다. |
| 사용 가능한 포인트 금액 조회 | GET       | http://localhost:8084/api/daou/mall/payment/point/can-use      | 사용 가능한 포인트 금액을 조회합니다.   |
| 포인트 이력 조회        | GET       | http://localhost:8084/api/daou/mall/payment/point/history| 포인트의 이력을 조회합니다.         |

### REST API Example

#### 결제 내역 확정
json 입력 정보
``` json
{
    "loginId" : "test5730",
    "paymentManagementSerialCode" : "T-T-O1"
}
```
```json
{
    "customerLoginId": "test5730",
    "pointAmount": 3500,
    "createDate": "2022-03-29"
}
```

#### 사용 가능한 포인트 금액 조회
curl 입력 정보
```curl
curl --location --request GET 'http://localhost:8084/api/daou/mall/payment/point/can-use?customerLoginId=test5730'
```
```json
{
    "customerLoginId": "test5730",
    "pointAmount": 3500,
    "createDate": "2022-03-29"
}
```

#### 포인트 내역 조회
```curl
curl --location --request GET 'http://localhost:8084/api/daou/mall/payment/point/history?customerLoginId=test5730'
```
```json
[
    {
        "initialPointAmount": 3500,
        "remainPointAmount": 3500,
        "createDate": "2022-03-29",
        "expiredDateTime": "2032-03-29T13:56:23",
        "serialOrderInfo": "T-T-O1",
        "isUsed": "NO"
    }
]
```

## daouMall core domain-C : 결제 진행 및 주문 번호 생성

#### 요구사항 분석
##### use case : 주문서 결제 진행.

1. 주문번호로 구매한 상품 내역을 관리한다.
   * 1..* : 추가상품 관계
   * 1..* : 상품 관계
2. 주문서에는 상품의 필수옵션 정보와 구매한 상품의 정보를 기록한다.
3. count를 통해 구매한 주문상품 옵션과 추가 상품  옵션의 개수를 관리한다.

#### 도메인 설계(ERD) [ 결제 진행 및 주문 번호 생성 ]
![결제내역-포인트](https://user-images.githubusercontent.com/31848154/161712711-58702e7b-cd91-4660-9dc7-5c2099733532.jpg)

#### API 명세(REST API & 예시 & JSON 입출력)

##### REST API 정보
|이름| 요청 METHOD| 요청 URI | 설명  |
|-------|-----|--------|-----|
| 결제 내역 생성         | POST      | http://localhost:8084/api/daou/mall/payment/buy-products                     | 결제 내역을 생성합니다            |
| 결제 내역 조회         | GET       | http://localhost:8084/api/daou/mall/payment/history?customerLoginId=test5730 | 결제 내역을 조회합니다.           |

### REST API Example

#### 결제 내역 생성
json 입력 정보
```json
{
    "orderPaperDtos" : [
        {
            "baseProductSerialCode": "T-T-P1",
            "essentialOptionSerialCode": "T-T-P-E1",
            "additionalProductSerialCodes": ["T-T-AP-1", "T-T-AP-2"],
            "count" : 1
        },
        {
            "baseProductSerialCode": "T-T-P1",
            "essentialOptionSerialCode": "T-T-P-E1",
            "additionalProductSerialCodes": ["T-T-AP-1"],
            "count" : 1
        }
    ],
    "paymentManagementDto" : {
        "loginId": "test5730",
        "totalPrice" : 70000,
        "discountAmount" : 0,
        "usedPointAmount" : 0,
        "orderSerialCode" : "T-T-O1",
        "couponCategory" : "NONE",
        "couponSerialCode" : "NONE",
        "paymentMethod" : "CARD",
        "paymentSerialCode" : "t_qwer1234"
    }
}
```

json 출력 정보
```json
{
    "totalPrice": 70000,
    "couponCategory": "NONE",
    "discountAmount": 0,
    "usedPointAmount": 0,
    "paymentMethod": "CARD",
    "customerLoginId": "test5730",
    "payInfo": {
        "company": "테스트용 카드 회사",
        "totalPrice": 70000,
        "remainPrice": 2930000,
        "comment": "카드 & 계좌 & 쿠폰 테스트 아이디의 테스트용 카드 회사의 회사에서 70000금액 결제가 완료되었습니다. 남은 금액 : 2930000"
    },
    "couponInfo": {
        "discountInfo": 0,
        "createDate": null,
        "constraint": 0,
        "serialCode": "none",
        "couponCategory": "NONE",
        "usedDate": null
    },
    "buyProductsInfoDTOs": [
        {
            "productName": "testProduct",
            "productPrice": 7777,
            "essentialOptionName": "Test Essential-1",
            "essentialOptionPrice": 500,
            "buyAdditionalProductsInfoDtos": [
                {
                    "additionalProductName": "test-addtional-productB",
                    "additionalProductPrice": 12000
                },
                {
                    "additionalProductName": "test-addtional-product",
                    "additionalProductPrice": 8000
                }
            ],
            "count": 1
        },
        {
            "productName": "testProduct",
            "productPrice": 7777,
            "essentialOptionName": "Test Essential-1",
            "essentialOptionPrice": 500,
            "buyAdditionalProductsInfoDtos": [
                {
                    "additionalProductName": "test-addtional-product",
                    "additionalProductPrice": 8000
                }
            ],
            "count": 1
        }
    ]
}
```

<hr />

## subdomain-B : order history & canceled order history
* branch : feature/payment-management -> development branch
<br />

#### 요구사항 분석
##### use case-1 : 주문 내역 관리하기
* 고객의 결제 내역을 주문 내역에서 확인할 수 있다.
  * 고객은 주문의 상태를 확인할 수 있다. [진행, 주문 확정, 주문 취소] 
  * 주문 진행(PROGRESS)이 기본 상태이다.
  * 주문 확정(COMPLETE)시 포인트가 적립된다.
  * 주문 취소(CANCEL)시 포인트와 쿠폰 사용을 되돌린다.

##### use case-2 : 주문 완료 & 취소
* 주문 완료 이벤트
   * 주문 완료를 처리한 경우 절대 금액 + 사용자의 등급에 맞는 포인트 적립이 진행된다.
   * 주문 완료로 처리된 포인트만이 사용 가능하다.
* 주문 취소 이벤트
  * 결제 금액을 되돌린다.
  * 사용한 포인트를 원래대로 복귀시킨다.
  * 사용한 쿠폰을 원래대로 복귀시킨다.

<br />

#### API 명세(REST API & 예시 & JSON 입출력)

##### REST API 정보
| 이름                  | 요청 METHOD | 요청 URI                                                           | 설명                          |
|---------------------|-----------|------------------------------------------------------------------|-----------------------------|
| 사용자 결제 내역 조회        | GET       | http://localhost:8084/api/daou/mall/payment/history              | 사용자의 전체 결제 내역을 조회합니다.       |
| 사용자의 결제 취소내역 조회     | GET       | http://localhost:8084/api/daou/mall/payment/history/cancel       | 사용자의 결제 내역중 취소된 내역을 조회합니다.  |
| 사용자의 결제 확정내역 조회     | GET       | http://localhost:8084/api/daou/mall/payment/history/complete     | 사용자의 결제 내역 중 확정된 내역을 조회합니다. |
| 사용자의 결제 내역 제품 내역 조회 | GET       | http://localhost:8084/api/daou/mall/payment/order-specification  | 구매한 제품의 내역을 조회합니다. |

### REST API Example

#### 사용자 결제 내역 조회
curl입력
```curl
curl --location --request GET 'http://localhost:8084/api/daou/mall/payment/history?customerLoginId=test5730'
```

json 출력
```json
[
    {
        "mallName": "testMall",
        "loginId": "Can't response",
        "totalPrice": 70000,
        "usedPointAmount": 0,
        "orderSerialCode": "T-T-O1",
        "couponCategory": "NONE",
        "couponSerialCode": "NONE",
        "paymentMethod": "CARD",
        "paymentSerialCode": "t_qwer1234",
        "orderStatus": "COMPLETE",
        "discountAmount": 0,
        "payPrice": 70000,
        "createDate": "2022-04-05T13:16:47"
    }
]
```

#### 사용자 결제 취소내역 조회
curl 입력
```curl
curl --location --request GET 'http://localhost:8084/api/daou/mall/payment/history/cancel?customerLoginId=test5730'
```

json 출력
```json
[
    {
        "mallName": "",
        "loginId": "Can't response",
        "totalPrice": 1113111,
        "usedPointAmount": 0,
        "orderSerialCode": "T-D-PM-O6448",
        "couponCategory": "RATE",
        "couponSerialCode": "T-T-R1",
        "paymentMethod": "CARD",
        "paymentSerialCode": "t_qwer1234",
        "orderStatus": "CANCEL",
        "discountAmount": 50000,
        "payPrice": 1063111,
        "createDate": "2022-04-05T14:55:25"
    }
]
```

#### 사용자 결제 확정내역 조회
curl 입력
```curl
curl --location --request GET 'http://localhost:8084/api/daou/mall/payment/history/complete?customerLoginId=test5730'
```

json 출력
```json
[
    {
        "mallName": "testMall",
        "loginId": "Can't response",
        "totalPrice": 70000,
        "usedPointAmount": 0,
        "orderSerialCode": "T-T-O1",
        "couponCategory": "NONE",
        "couponSerialCode": "NONE",
        "paymentMethod": "CARD",
        "paymentSerialCode": "t_qwer1234",
        "orderStatus": "COMPLETE",
        "discountAmount": 0,
        "payPrice": 70000,
        "createDate": "2022-04-05T13:16:47"
    }
]
```
#### 사용자 결제 내역 제품 내역 조회
curl 입력
```curl
curl --location --request GET 'http://localhost:8084/api/daou/mall/payment/order-specification?orderSerialCode=T-T-O1'
```

json 출력
```json
[
    {
        "productRESDto": {
            "name": "testProduct",
            "price": 7777
        },
        "essentialOptionRESDto": {
            "name": "Test Essential-1",
            "price": 500
        },
        "additionalProductOrderPaperDtos": [
            {
                "name": "test-addtional-product",
                "price": 8000
            },
            {
                "name": "test-addtional-productB",
                "price": 12000
            }
        ]
    },
    {
        "productRESDto": {
            "name": "testProduct",
            "price": 7777
        },
        "essentialOptionRESDto": {
            "name": "Test Essential-1",
            "price": 500
        },
        "additionalProductOrderPaperDtos": [
            {
                "name": "test-addtional-product",
                "price": 8000
            }
        ]
    }
]
```