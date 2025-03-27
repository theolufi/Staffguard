import api from './api';

export const login = async (credentials) => {
  const response = await api.post('/login', credentials);
  return response.data;
};

export const register = async (userData) => {
  const response = await api.post('/api/auth/register', {
    ...userData,
    roles: userData.roles || ['EMPLOYEE']
  });
  return response.data;
};

export const logout = async () => {
  await api.post('/logout');
};

export const getCurrentUser = async () => {
  try {
    const response = await api.get('/api/auth/me');
    return response.data;
  } catch (error) {
    return null;
  }
};