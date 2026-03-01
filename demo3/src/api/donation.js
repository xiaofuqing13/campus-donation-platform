import axios from 'axios'

const API_URL = 'http://localhost:8080/api'

// 添加请求拦截器
axios.interceptors.request.use(config => {
  config.withCredentials = true;
  return config;
});

// 添加响应拦截器
axios.interceptors.response.use(
  response => response.data,
  error => {
    console.error('API请求失败:', error);
    throw error;
  }
);

// 物品相关接口
export const getDonationItems = (params) => {
  return axios.get(`${API_URL}/items`, { params });
}

export const createDonationItem = (item) => {
  return axios.post(`${API_URL}/items`, item);
}

export const updateDonationItem = (id, item) => {
  return axios.put(`${API_URL}/items/${id}`, item);
}

export const deleteDonationItem = (id) => {
  return axios.delete(`${API_URL}/items/${id}`);
}

// 捐赠记录相关接口
export const getDonationRecords = (params) => {
  return axios.get(`${API_URL}/records`, { params });
}

export const createDonationRecord = (record) => {
  console.log('发送捐赠请求:', record)
  return axios.post(`${API_URL}/records`, record, {
    headers: {
      'Content-Type': 'application/json'
    }
  });
}

// 统计相关接口
export const getStatistics = () => {
  return axios.get(`${API_URL}/statistics`);
} 