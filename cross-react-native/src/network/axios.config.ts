import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';
import {apiURL} from 'app.json';

// Create an instance of axios
const axiosInstance = axios.create({
  baseURL: apiURL,
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true,
});

// Add a request interceptor
axiosInstance.interceptors.request.use(
  async config => {
    config.headers['User-Agent'] = '_';
    const cookie = await AsyncStorage.getItem('cookie');
    if (cookie) {
      config.headers.set('Cookie', cookie ?? '');
    }
    console.log({config});
    return config;
  },
  error => {
    return Promise.reject(error);
  },
);

// Add a response interceptor (optional)
axiosInstance.interceptors.response.use(
  response => response,
  error => {
    return Promise.reject(error);
  },
);

export default axiosInstance;
