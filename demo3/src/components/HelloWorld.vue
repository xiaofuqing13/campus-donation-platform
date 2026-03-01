<template>
  <div class="hello">
    <h1>{{ msg }}</h1>
  </div>
</template>

<script setup>
defineProps({
  msg: {
    type: String,
    default: '欢迎使用校园公益捐赠系统'
  }
})
</script>

<style scoped>
.hello {
  text-align: center;
  margin: 20px 0;
}
</style> 