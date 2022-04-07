import { createRouter, createWebHistory } from "vue-router";
import DaouMall from "@/view/DaouMall";
import DaouProduct from "@/view/DaouProduct";
import DaouLogin from "@/view/DaouLogin";
import DaouCoupon from "@/view/DaouCoupon";
import DaouCard from "@/components/DaouCard";
import ProductSpecification from "@/components/ProductSpecification";
import PaymentProcess from "@/view/PaymentProcess";
import MallPaymentManagement from "@/view/MallPaymentManagement";
import CustomerPaymentInfo from "@/view/CustomerPaymentInfo"
import CompletePayment from "@/view/CompletePayment"
import DaouLoginWithProduct from "@/view/DaouLoginWithProduct"

const routes = [
  {
    path: "/", 
    redirect: "/daou-mall",
  },
  {
    path: "/daou-mall",
    component: DaouMall,
  },
  {
    path: "/daou-product",
    component: DaouProduct,
  },
  {
    path: "/daou-login",
    name: "login",
    component: DaouLogin,
  },
  {
    path: "/daou-coupon",
    component: DaouCoupon,
  },
  {
    path: "/daou-product-card",
    component: DaouCard,
  },
  {
    path: "/product-specification",
    name: "productInfo",
    component: ProductSpecification,
    props: (route) => ({ query: route.query }),
  },
  {
    path: "/payment-process",
    name: "paymentProcess",
    component: PaymentProcess,
    params: true,
  },
  {
    path: "/mall-payment-management",
    name: "Mall PaymentManagement",
    component: MallPaymentManagement,
  },
  {
      path: "/customer-info",
      name: "CustomerInfo",
      component: CustomerPaymentInfo
  },
  {
    path: "/complete-payment",
    name: "completePayment",
    component: CompletePayment
  },
  {
    path: "/daou-login-with-product",
    name: "loginWithProduct",
    component: DaouLoginWithProduct
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
