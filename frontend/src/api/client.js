import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('placify_token') || localStorage.getItem('preptrack_token');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    if (config.data instanceof FormData) {
      delete config.headers['Content-Type'];
    }
    return config;
  },
  (error) => Promise.reject(error)
);

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      // Don't auto-redirect on login/register attempt failures
      const url = error.config.url;
      if (!url.includes('/auth/login') && !url.includes('/auth/register')) {
        localStorage.removeItem('preptrack_token');
        localStorage.removeItem('preptrack_user');
        localStorage.removeItem('placify_token');
        localStorage.removeItem('placify_user');
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

export default api;
