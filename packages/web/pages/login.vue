<template>login....</template>

<script lang="ts" setup>
import { useAuthStore } from "~/store/user";

const appConfig = useAppConfig();

const headers = useRequestHeaders(["token"]);
headers.token = useAuthStore().token;
const { data } = await useFetch(appConfig.baseUrl + "/login", {
  method: "POST",
  headers,
});

console.log(data);
</script>

<style scoped></style>
