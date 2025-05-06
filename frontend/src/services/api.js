import axios from 'axios';
const API_URL = process.env.REACT_APP_API_URL || 'https://localhost:8443';

const api = axios.create({ baseURL: API_URL });

api.interceptors.request.use(cfg => {
    const token = localStorage.getItem('jwt');
    if (token) cfg.headers['Authorization'] = 'Bearer ' + token;
    return cfg;
});

export default api;
