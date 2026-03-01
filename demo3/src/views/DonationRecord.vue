<template>
  <div class="donation-record">
    <!-- 搜索栏 -->
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="捐赠人">
          <el-input v-model="searchForm.donorName" placeholder="请输入捐赠人姓名" />
        </el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 记录列表 -->
    <el-card class="list-card">
      <el-table :data="records" stripe v-loading="loading">
        <el-table-column prop="donationItem.name" label="物品名称" />
        <el-table-column prop="donorName" label="捐赠人" />
        <el-table-column prop="donorContact" label="联系方式" />
        <el-table-column prop="quantity" label="数量" />
        <el-table-column prop="donationTime" label="捐赠时间">
          <template #default="scope">
            {{ formatDate(scope.row.donationTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="scope.row.status === '已登记' ? 'success' : 'info'">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { getDonationRecords } from '@/api/donation'

// 数据
const records = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 搜索表单
const searchForm = reactive({
  donorName: '',
  dateRange: []
})

// 方法
const loadData = async () => {
  loading.value = true
  try {
    const [startDate, endDate] = searchForm.dateRange || []
    const data = await getDonationRecords({
      page: currentPage.value,
      size: pageSize.value,
      donorName: searchForm.donorName,
      startDate,
      endDate
    })
    records.value = data.content
    total.value = data.totalElements
  } catch (error) {
    ElMessage.error('加载数据失败')
  }
  loading.value = false
}

const handleSearch = () => {
  currentPage.value = 1
  loadData()
}

const resetSearch = () => {
  searchForm.donorName = ''
  searchForm.dateRange = []
  handleSearch()
}

const handleSizeChange = (val) => {
  pageSize.value = val
  loadData()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  loadData()
}

const formatDate = (date) => {
  if (!date) return '-'
  try {
    const dateObj = new Date(date)
    if (isNaN(dateObj.getTime())) return '-'
    return dateObj.toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    })
  } catch (e) {
    return '-'
  }
}

// 生命周期
onMounted(() => {
  loadData()
})
</script>

<style scoped>
.donation-record {
  padding: 20px;
}

.search-card {
  margin-bottom: 20px;
}

.list-card {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style> 