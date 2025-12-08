import axios, { AxiosResponse } from 'axios';
import { router } from 'expo-router';
import { storage } from '../utils/Storage';

let HOST_URL = '';

export const axiosInstance = axios.create({
  baseURL: `http://${HOST_URL}/api`,
});

export const initializeAxiosInstance = async () => {
  const storedHost = await storage.getHost();
  if (storedHost) {
    HOST_URL = storedHost;
    axiosInstance.defaults.baseURL = `http://${HOST_URL}/api`;
  }
};

(async () => {
  await initializeAxiosInstance();
})();

axiosInstance.interceptors.request.use(
  async (config) => {
    if (!config.url?.endsWith('/login') && !config.url?.endsWith('/refresh-token')) {
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

interface TokenRequest {
  refreshToken: string;
}

const getNewToken = async (tokenRequest: TokenRequest): Promise<AxiosResponse> => {
  return axios.post(`http://${HOST_URL}/api/refresh-token`, tokenRequest);
}

let isRefreshing = false;
let failedQueue: any[] = [];

const processQueue = (error: any, token: string | null = null) => {
  failedQueue.forEach(prom => {
    if (error) {
      prom.reject(error);
    } else {
      prom.resolve(token);
    }
  });

  failedQueue = [];
};

axiosInstance.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    if (error.response?.status === 401 && !originalRequest._retry) {
      if (isRefreshing) {
        return new Promise((resolve, reject) => {
          failedQueue.push({ resolve, reject });
        }).then(token => {
          originalRequest.headers.Authorization = `Bearer ${token}`;
          return axiosInstance(originalRequest);
        }).catch(err => {
          return Promise.reject(err);
        });
      }

      originalRequest._retry = true;
      isRefreshing = true;

      const refreshToken = await storage.getRefreshToken();

      if (!refreshToken) {
        isRefreshing = false;
        await storage.removeToken();
        await storage.removeRefreshToken();
        router.replace('/login');
        return Promise.reject(error);
      }

      try {
        const response = await getNewToken({ refreshToken });

        if (response.data?.token) {
          await storage.setToken(response.data.token);

          axiosInstance.defaults.headers.common['Authorization'] = `Bearer ${response.data.token}`;
          originalRequest.headers.Authorization = `Bearer ${response.data.token}`;

          processQueue(null, response.data.token);
          isRefreshing = false;

          return axiosInstance(originalRequest);
        }

        isRefreshing = false;
        await storage.removeToken();
        await storage.removeRefreshToken();
        router.replace('/login');
        return Promise.reject(error);

      } catch (refreshError) {
        console.error('Error refreshing token:', refreshError);
        isRefreshing = false;
        processQueue(refreshError, null);
        await storage.removeToken();
        await storage.removeRefreshToken();
        router.replace('/login');
        return Promise.reject(refreshError);
      }
    }

    return Promise.reject(error);
  }
);