import { useAuthStore } from "~/store/user";

export default defineNuxtRouteMiddleware((to, from) => {
  if (process.server) return;
  if (!isAuthenticated()) {
    if ("/login" !== to.path) {
      return navigateTo("/login");
    }
  } else if ("/dashboard" !== to.path) {
    return navigateTo("/dashboard", { external: true });
  }
});

const isAuthenticated = (): boolean => {
  const authStore = useAuthStore();

  return authStore.token !== null && authStore.token !== "";
};
