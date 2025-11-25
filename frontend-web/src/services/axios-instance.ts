import axios from 'axios';

export const axiosInstance = axios.create({
    baseURL: 'http://localhost:8081/api',
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