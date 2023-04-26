import { defineStore } from "pinia";
import { useLocalStorage } from "@vueuse/core";

export const useAuthStore = defineStore({
  id: "auth",
  state: () => ({
    token: useLocalStorage("token", "").value,
  }),

  actions: {},
});
