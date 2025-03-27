import axios from 'axios';

const api = axios.create({
  baseURL: 'https://localhost:8443',
  withCredentials: true,
});

// Add CSRF token to all requests
api.interceptors.request.use(config => {
  const csrfToken = document.cookie
    .split('; ')
    .find(row => row.startsWith('XSRF-TOKEN='))
    ?.split('=')[1];

  if (csrfToken) {
    config.headers['X-XSRF-TOKEN'] = csrfToken;
  }

  return config;
});

export default api;