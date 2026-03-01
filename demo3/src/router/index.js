import { createRouter, createWebHistory } from 'vue-router'
import DonationList from '../views/DonationList.vue'
import DonationRecords from '../views/DonationRecords.vue'
import DonationStatistics from '../views/DonationStatistics.vue'

const routes = [
  {
    path: '/',
    name: 'DonationList',
    component: DonationList
  },
  {
    path: '/records',
    name: 'DonationRecords',
    component: DonationRecords
  },
  {
    path: '/statistics',
    name: 'DonationStatistics',
    component: DonationStatistics
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router 