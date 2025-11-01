import axios from 'axios';
import { storage } from '../utils/Storage';

export const axiosInstance = axios.create({
    baseURL: 'http://192.168.0.16:8081/api',
});

axiosInstance.interceptors.request.use(
    async (config) => {
      if (!config.url?.endsWith('/login')) {
        const token = await storage.getToken();
        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }
      }
      return config;
    },
    (error) => {
      return Promise.reject(error);
    }
  );