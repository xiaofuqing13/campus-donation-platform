<template>
  <div class="statistics">
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card class="chart-card">
          <div class="chart-title">物品类别统计</div>
          <div ref="pieChart" style="height: 400px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <div class="chart-title">近7天捐赠趋势</div>
          <div ref="lineChart" style="height: 400px"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import { getStatistics } from '@/api/donation'

const pieChart = ref(null)
const lineChart = ref(null)
let pieChartInstance = null
let lineChartInstance = null

const initCharts = () => {
  pieChartInstance = echarts.init(pieChart.value)
  lineChartInstance = echarts.init(lineChart.value)
}

const loadData = async () => {
  try {
    const data = await getStatistics()
    console.log('统计数据:', data)
    
    // 饼图数据
    pieChartInstance.setOption({
      title: {
        text: '物品类别分布',
        left: 'center'
      },
      tooltip: {
        trigger: 'item',
        formatter: '{b}: {c} ({d}%)'
      },
      series: [{
        type: 'pie',
        radius: '60%',
        data: data.categoryStats || [],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }]
    })

    // 折线图数据
    const trendData = data.trendStats || []
    console.log('趋势数据:', trendData)
    
    lineChartInstance.setOption({
      title: {
        text: '近7天捐赠趋势',
        left: 'center'
      },
      tooltip: {
        trigger: 'axis',
        formatter: '{b}: {c} 件'
      },
      xAxis: {
        type: 'category',
        data: trendData.map(item => item.date),
        axisLabel: {
          rotate: 30,
          interval: 0
        }
      },
      yAxis: {
        type: 'value',
        name: '捐赠数量',
        minInterval: 1
      },
      series: [{
        name: '捐赠数量',
        data: trendData.map(item => item.value),
        type: 'line',
        smooth: true,
        symbolSize: 8,
        itemStyle: {
          color: '#409EFF'
        },
        areaStyle: {
          color: {
            type: 'linear',
            x: 0,
            y: 0,
            x2: 0,
            y2: 1,
            colorStops: [{
              offset: 0,
              color: 'rgba(64,158,255,0.3)'
            }, {
              offset: 1,
              color: 'rgba(64,158,255,0)'
            }]
          }
        }
      }]
    })
  } catch (error) {
    console.error('加载统计数据失败:', error)
  }
}

onMounted(() => {
  initCharts()
  loadData()
  
  // 监听窗口大小变化，重绘图表
  window.addEventListener('resize', () => {
    pieChartInstance?.resize()
    lineChartInstance?.resize()
  })
})
</script>

<style scoped>
.statistics {
  padding: 20px;
}

.chart-card {
  margin-bottom: 20px;
}

.chart-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 20px;
  text-align: center;
}
</style> 