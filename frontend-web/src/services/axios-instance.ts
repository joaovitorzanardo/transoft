import axios from 'axios';

const API_URL = import.meta.env?.VITE_API_URL ||
  'http://localhost:8081';

export const axiosInstance = axios.create({
  baseURL: `${API_URL}/api`,
  withCredentials: true
});

axiosInstance.interceptors.request.use(
  async (config) => {
    config.headers['X-Client-Type'] = 'web';
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export const redirectToLogin = () => {
  window.location.href = '/login';
};

axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      redirectToLogin();
    }
    return Promise.reject(error);
  }
);