import { createContext, useContext, useState, useEffect } from 'react';
import axios from 'axios';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  // Use http://localhost:8443 if your backend is running on HTTP.
  const api = axios.create({
    baseURL: process.env.REACT_APP_API_URL || 'https://localhost:8443',
    withCredentials: true, // Ensure cookies are sent
  });

  const login = async (credentials) => {
    try {
      const response = await api.post('/api/auth/login', credentials);
      console.log("Login successful:", response.data);
      setUser(response.data);
      return response.data; // Return user data for redirection
    } catch (error) {
      console.error("Login error:", error.response?.data || error.message);
      throw error.response?.data || "Login failed";
    }
  };

  const register = async (userData) => {
    try {
      await api.post('/api/auth/register', userData);
    } catch (error) {
      throw error.response?.data || "Registration failed";
    }
  };

  const logout = async () => {
    try {
      await api.post('/api/auth/logout');
    } finally {
      localStorage.removeItem('token');
      setUser(null);
    }
  };

  const checkAuth = async () => {
    try {
      const response = await api.get('/api/auth/me');
      console.log("Authenticated user:", response.data);
      setUser(response.data);
    } catch (error) {
      console.warn("User not authenticated");
      setUser(null);
    }
    setLoading(false);
  };

  useEffect(() => {
    checkAuth(); // Fetch user authentication state on page load
  }, []);

  return (
    <AuthContext.Provider value={{ user, loading, login, register, logout, checkAuth }}>
      {!loading && children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
