import axios from 'axios';

export const axiosInstance = axios.create({
    baseURL: 'http://localhost:8081/api',
});

axiosInstance.interceptors.request.use(
  async (config) => {
    const token = sessionStorage.getItem("apiToken");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);