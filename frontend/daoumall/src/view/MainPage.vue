<template>
  <h1>다우몰의 공식 홈페이지입니다. [기본 페이지]</h1>
  <hr/>
  <p>vuex의 로그인 {{loginId}} | vuex의 로그인 상태 {{loginStatus}}</p>
  <input type="button" @click="testVuex" value="로그인 정보 가져오기">
  <input type="button" @click="testLoginVuex" value="로그인">
  <p>해당 컴포넌트에서 받은 사용자 로그인 아이디{{customerLoginId}}</p>
  <!-- <p>사용자 로그인 아이디 {{getLoginId}} | 로그인 상태 {{getLoginStatus}}</p> -->
</template>

<script>
import {computed} from 'vue';
import {useStore} from "vuex";

export default {
  name: 'MainPage',

  setup() {
    const store = useStore();

    let loginId = computed( function() {
      return store.state.loginId
    });

    let loginStatus = computed( function() {
      return store.state.status
    })

    function testVuex(loginId) {
      store.commit('login', loginId);
    }

    return {
      loginId,
      loginStatus,
      testVuex
    }
  },

  data() {
    return {
      customerLoginId: "",
    }
  },

  methods: {

    testLoginVuex() {
      console.log("로그인을 시도합니다.")
      this.testVuex("test tomboy");
    },

    testLogoutVuex() {
      console.log("로그아웃을 시도합니다.")
    }
  }
}
</script>

<style>

</style>