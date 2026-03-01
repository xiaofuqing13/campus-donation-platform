<template>
  <div class="donation-list">
    <!-- 搜索栏 -->
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="物品名称">
          <el-input v-model="searchForm.name" placeholder="请输入物品名称" />
        </el-form-item>
        <el-form-item label="物品类别">
          <el-select v-model="searchForm.category" placeholder="请选择类别">
            <el-option label="全部" value="" />
            <el-option label="衣物" value="衣物" />
            <el-option label="书籍" value="书籍" />
            <el-option label="电子产品" value="电子产品" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
          <el-button type="success" @click="showAddDialog">新增物品</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 物品列表 -->
    <el-card class="list-card">
      <el-table :data="donationItems" stripe v-loading="loading">
        <el-table-column prop="name" label="物品名称" />
        <el-table-column prop="category" label="类别" />
        <el-table-column prop="quantity" label="数量" />
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="scope.row.status === '可用' ? 'success' : 'info'">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间">
          <template #default="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
            <el-button type="success" size="small" @click="handleDonate(scope.row)">捐赠</el-button>
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

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogType === 'add' ? '新增物品' : '编辑物品'"
      v-model="dialogVisible"
      width="500px"
    >
      <el-form :model="itemForm" :rules="rules" ref="itemFormRef" label-width="100px">
        <el-form-item label="物品名称" prop="name">
          <el-input v-model="itemForm.name" />
        </el-form-item>
        <el-form-item label="物品类别" prop="category">
          <el-select v-model="itemForm.category" style="width: 100%">
            <el-option label="衣物" value="衣物" />
            <el-option label="书籍" value="书籍" />
            <el-option label="电子产品" value="电子产品" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="数量" prop="quantity">
          <el-input-number v-model="itemForm.quantity" :min="1" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input type="textarea" v-model="itemForm.description" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>

    <!-- 捐赠对话框 -->
    <el-dialog title="登记捐赠" v-model="donateDialogVisible" width="500px">
      <el-form :model="donateForm" :rules="donateRules" ref="donateFormRef" label-width="100px">
        <el-form-item label="捐赠人" prop="donorName">
          <el-input v-model="donateForm.donorName" />
        </el-form-item>
        <el-form-item label="联系方式" prop="donorContact">
          <el-input v-model="donateForm.donorContact" />
        </el-form-item>
        <el-form-item label="捐赠数量" prop="quantity">
          <el-input-number v-model="donateForm.quantity" :min="1" :max="selectedItem?.quantity || 1" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="donateDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitDonate">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getDonationItems, createDonationItem, updateDonationItem, deleteDonationItem, createDonationRecord } from '@/api/donation'

// 数据
const donationItems = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const dialogVisible = ref(false)
const dialogType = ref('add')
const donateDialogVisible = ref(false)
const selectedItem = ref(null)

// 表单
const itemFormRef = ref(null)
const donateFormRef = ref(null)
const searchForm = reactive({
  name: '',
  category: ''
})

const itemForm = reactive({
  name: '',
  category: '',
  quantity: 1,
  description: ''
})

const donateForm = reactive({
  donorName: '',
  donorContact: '',
  quantity: 1
})

// 表单验证规则
const rules = {
  name: [{ required: true, message: '请输入物品名称', trigger: 'blur' }],
  category: [{ required: true, message: '请选择物品类别', trigger: 'change' }],
  quantity: [{ required: true, message: '请输入数量', trigger: 'blur' }]
}

const donateRules = {
  donorName: [{ required: true, message: '请输入捐赠人姓名', trigger: 'blur' }],
  donorContact: [{ required: true, message: '请输入联系方式', trigger: 'blur' }],
  quantity: [{ required: true, message: '请输入捐赠数量', trigger: 'blur' }]
}

// 方法
const loadData = async () => {
  loading.value = true
  try {
    const data = await getDonationItems({
      page: currentPage.value,
      size: pageSize.value,
      ...searchForm
    })
    donationItems.value = data.content
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
  searchForm.name = ''
  searchForm.category = ''
  handleSearch()
}

const showAddDialog = () => {
  dialogType.value = 'add'
  Object.assign(itemForm, {
    name: '',
    category: '',
    quantity: 1,
    description: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogType.value = 'edit'
  Object.assign(itemForm, row)
  dialogVisible.value = true
}

const handleDonate = (row) => {
  selectedItem.value = row
  Object.assign(donateForm, {
    donorName: '',
    donorContact: '',
    quantity: 1
  })
  donateDialogVisible.value = true
}

const submitForm = async () => {
  if (!itemFormRef.value) return
  
  await itemFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (dialogType.value === 'add') {
          await createDonationItem(itemForm)
          ElMessage.success('添加成功')
        } else {
          await updateDonationItem(itemForm.id, itemForm)
          ElMessage.success('更新成功')
        }
        dialogVisible.value = false
        loadData()
      } catch (error) {
        ElMessage.error('操作失败')
      }
    }
  })
}

const submitDonate = async () => {
  if (!donateFormRef.value) return
  
  await donateFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // 打印请求数据，用于调试
        console.log('提交的捐赠数据:', {
          donationItem: { id: selectedItem.value.id },
          donorName: donateForm.donorName,
          donorContact: donateForm.donorContact,
          quantity: donateForm.quantity
        })

        const response = await createDonationRecord({
          donationItem: { id: selectedItem.value.id },
          donorName: donateForm.donorName,
          donorContact: donateForm.donorContact,
          quantity: donateForm.quantity
        })
        
        console.log('捐赠响应:', response)
        ElMessage.success('捐赠登记成功')
        donateDialogVisible.value = false
        await loadData()
      } catch (error) {
        console.error('捐赠失败:', error)
        ElMessage.error(error.response?.data || '捐赠失败')
      }
    }
  })
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除这个物品吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await deleteDonationItem(row.id)
    ElMessage.success('删除成功')
    await loadData() // 重新加载数据
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
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
.donation-list {
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