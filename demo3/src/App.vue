<template>
  <el-config-provider :locale="zhCn">
    <el-container class="app-container">
      <el-header>
        <el-menu mode="horizontal" router :default-active="$route.path">
          <el-menu-item index="/">捐赠物品管理</el-menu-item>
          <el-menu-item index="/records">捐赠记录</el-menu-item>
          <el-menu-item index="/statistics">数据统计</el-menu-item>
        </el-menu>
      </el-header>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-config-provider>
</template>

<script setup>
import { ElConfigProvider } from 'element-plus'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
</script>

<style>
.app-container {
  min-height: 100vh;
}

.el-header {
  padding: 0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12);
}

.el-main {
  background-color: #f5f7fa;
}
</style> 