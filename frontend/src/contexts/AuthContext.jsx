import { createContext, useContext, useState } from 'react';

const AuthContext = createContext();
const API_URL = process.env.REACT_APP_API_URL || 'https://localhost:8443';

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);

  const login = async ({ username, password }) => {
    const res = await fetch(`${API_URL}/api/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password }),
    });
    if (!res.ok) throw new Error('Invalid credentials');

    // Expect { token, user } from the backend
    const { token, user: userFromServer } = await res.json();

    // store JWT for later calls
    localStorage.setItem('jwt', token);

    // set the full user object (with id, username, roles, etc.)
    setUser(userFromServer);

    return userFromServer;
  };

  const logout = () => {
    localStorage.removeItem('jwt');
    setUser(null);
  };

  return (
      <AuthContext.Provider value={{ user, login, logout }}>
        {children}
      </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
