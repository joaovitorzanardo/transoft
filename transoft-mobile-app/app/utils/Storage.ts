import AsyncStorage from '@react-native-async-storage/async-storage';

const TOKEN_KEY = '@transoft_token';
const REFRESH_TOKEN_KEY = '@transoft_refresh_token';
const HOST_KEY = '@transoft_host';

export const storage = {
  async setToken(token: string) {
    try {
      await AsyncStorage.setItem(TOKEN_KEY, token);
    } catch (error) {
      console.error('Error saving token:', error);
    }
  },

  async getToken() {
    try {
      return await AsyncStorage.getItem(TOKEN_KEY);
    } catch (error) {
      console.error('Error getting token:', error);
      return null;
    }
  },

  async removeToken() {
    try {
      await AsyncStorage.removeItem(TOKEN_KEY);
    } catch (error) {
      console.error('Error removing token:', error);
    }
  },

  async setRefreshToken(refreshToken: string) {
    try {
      await AsyncStorage.setItem(REFRESH_TOKEN_KEY, refreshToken);
    } catch (error) {
      console.error('Error saving refresh token:', error);
    }
  },

  async getRefreshToken() {
    try {
      return await AsyncStorage.getItem(REFRESH_TOKEN_KEY);
    } catch (error) {
      console.error('Error getting refresh token:', error);
      return null;
    }
  },

  async removeRefreshToken() {
    try {
      await AsyncStorage.removeItem(REFRESH_TOKEN_KEY);
    } catch (error) {
      console.error('Error removing refresh token:', error);
    }
  },

  async getHost() {
    try {
      return await AsyncStorage.getItem(HOST_KEY);
    } catch (error) {
      console.error('Error getting host:', error);
      return null;
    }
  },

  async setHost(host: string) {
    try {
      await AsyncStorage.setItem(HOST_KEY, host);
    } catch (error) {
      console.error('Error saving host:', error);
    }
  }
};