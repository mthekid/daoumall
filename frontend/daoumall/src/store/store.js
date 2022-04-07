import { createStore } from "vuex";

export const store = createStore({
  state: {
    loginId: "logout",
    status: "logout",
  },

  mutations: {
    login(state, loginId) {
      state.loginId = loginId;
      state.status = "login";
    },
    logout(state) {
      state.loginId = "logout";
      state.status = "logout";
    },
  },
  actions: {},
});
