import React, { createContext, useContext, useState, useEffect } from 'react';
import api from '../api/client';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const savedToken = localStorage.getItem('preptrack_token');
    const savedUser = localStorage.getItem('preptrack_user');

    if (savedToken && savedUser) {
      try {
        setToken(savedToken);
        setUser(JSON.parse(savedUser));
      } catch (e) {
        localStorage.removeItem('preptrack_token');
        localStorage.removeItem('preptrack_user');
      }
    }
    setLoading(false);
  }, []);

  const login = async (usernameOrEmail, password) => {
    const res = await api.post('/auth/login', { usernameOrEmail, password });
    if (res.data.success && res.data.data) {
      const data = res.data.data;
      const authToken = data.token;
      const userData = {
        id: data.id,
        username: data.username,
        email: data.email,
        fullName: data.fullName,
        targetCompany: data.targetCompany,
        targetRole: data.targetRole,
        graduationYear: data.graduationYear,
        role: data.role,
      };

      localStorage.setItem('preptrack_token', authToken);
      localStorage.setItem('preptrack_user', JSON.stringify(userData));
      setToken(authToken);
      setUser(userData);
      return userData;
    }
    throw new Error(res.data.message || 'Login failed');
  };

  const register = async (registerData) => {
    const res = await api.post('/auth/register', registerData);
    if (res.data.success && res.data.data) {
      const data = res.data.data;
      const authToken = data.token;
      const userData = {
        id: data.id,
        username: data.username,
        email: data.email,
        fullName: data.fullName,
        targetCompany: data.targetCompany,
        targetRole: data.targetRole,
        graduationYear: data.graduationYear,
        role: data.role,
      };

      localStorage.setItem('preptrack_token', authToken);
      localStorage.setItem('preptrack_user', JSON.stringify(userData));
      setToken(authToken);
      setUser(userData);
      return userData;
    }
    throw new Error(res.data.message || 'Registration failed');
  };

  const logout = () => {
    localStorage.removeItem('preptrack_token');
    localStorage.removeItem('preptrack_user');
    setToken(null);
    setUser(null);
  };

  const updateUser = (updatedUser) => {
    setUser(updatedUser);
    localStorage.setItem('preptrack_user', JSON.stringify(updatedUser));
  };

  return (
    <AuthContext.Provider value={{ user, token, loading, login, register, logout, updateUser }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
