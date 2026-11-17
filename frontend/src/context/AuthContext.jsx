import React, { createContext, useContext, useState, useEffect } from 'react';
import api from '../api/client';

const AuthContext = createContext(null);
const TOKEN_STORAGE_KEY = 'placify_token';
const USER_STORAGE_KEY = 'placify_user';
const LEGACY_TOKEN_STORAGE_KEY = 'preptrack_token';
const LEGACY_USER_STORAGE_KEY = 'preptrack_user';

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const savedToken = localStorage.getItem(TOKEN_STORAGE_KEY)
      || localStorage.getItem(LEGACY_TOKEN_STORAGE_KEY);
    const savedUser = localStorage.getItem(USER_STORAGE_KEY)
      || localStorage.getItem(LEGACY_USER_STORAGE_KEY);

    if (savedToken && savedUser) {
      let userData;
      try {
        userData = JSON.parse(savedUser);
      } catch {
        localStorage.removeItem(TOKEN_STORAGE_KEY);
        localStorage.removeItem(USER_STORAGE_KEY);
        localStorage.removeItem(LEGACY_TOKEN_STORAGE_KEY);
        localStorage.removeItem(LEGACY_USER_STORAGE_KEY);
        setLoading(false);
        return;
      }

      localStorage.setItem(TOKEN_STORAGE_KEY, savedToken);
      localStorage.setItem(USER_STORAGE_KEY, JSON.stringify(userData));
      localStorage.removeItem(LEGACY_TOKEN_STORAGE_KEY);
      localStorage.removeItem(LEGACY_USER_STORAGE_KEY);
      setToken(savedToken);
      setUser(userData);
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

      localStorage.setItem(TOKEN_STORAGE_KEY, authToken);
      localStorage.setItem(USER_STORAGE_KEY, JSON.stringify(userData));
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

      localStorage.setItem(TOKEN_STORAGE_KEY, authToken);
      localStorage.setItem(USER_STORAGE_KEY, JSON.stringify(userData));
      setToken(authToken);
      setUser(userData);
      return userData;
    }
    throw new Error(res.data.message || 'Registration failed');
  };

  const logout = () => {
    localStorage.removeItem(TOKEN_STORAGE_KEY);
    localStorage.removeItem(USER_STORAGE_KEY);
    localStorage.removeItem(LEGACY_TOKEN_STORAGE_KEY);
    localStorage.removeItem(LEGACY_USER_STORAGE_KEY);
    setToken(null);
    setUser(null);
  };

  const updateUser = (updatedUser) => {
    setUser(updatedUser);
    localStorage.setItem(USER_STORAGE_KEY, JSON.stringify(updatedUser));
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
