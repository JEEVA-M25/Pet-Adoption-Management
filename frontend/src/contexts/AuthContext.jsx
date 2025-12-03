// AuthContext.jsx
import React, { createContext, useContext, useState, useEffect } from 'react';
import * as api from '../utils/api';

const AuthContext = createContext();
export const useAuth = () => useContext(AuthContext);

// Helper -> normalize backend role string to canonical form
const normalizeRole = (raw) => {
  if (!raw) return null;
  // raw might be "ROLE_ADMIN" or "ADMIN" or "ROLE_PUBLIC_USER"
  const clean = raw.toString().toUpperCase().replace(/^ROLE_/, '');
  // possible mapping if your backend uses slightly different names:
  if (clean === 'PUBLIC_USER') return 'PUBLIC_USER';
  if (clean === 'ORG_USER' || clean === 'ORG') return 'ORG_USER';
  if (clean === 'ADMIN' || clean === 'ROLE_ADMIN') return 'ADMIN';
  // fallback: return clean
  return clean;
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  // init: read localStorage and normalize role
  useEffect(() => {
    const token = localStorage.getItem('token');
    const role = localStorage.getItem('userRole');
    const email = localStorage.getItem('userEmail');

    if (token && role && email) {
      setUser({ email, role: normalizeRole(role) });
    }
    setLoading(false);
  }, []);

  const login = async (email, password) => {
    try {
      const res = await api.login(email, password);

      // backend returns res.role maybe "ROLE_ADMIN"
      const canonicalRole = normalizeRole(res.role);

      localStorage.setItem('token', res.token);
      localStorage.setItem('userRole', canonicalRole);
      localStorage.setItem('userEmail', email);

      setUser({ email, role: canonicalRole });
      return { success: true, role: canonicalRole };
    } catch (err) {
      return { success: false, error: err.message };
    }
  };

  const register = async (userData) => {
    try {
      const res = await api.register(userData);
      return { success: true };
    } catch (error) {
      let message = "Registration failed";
      if (error.response?.data?.message) message = error.response.data.message;
      else if (error.message) message = error.message;
      return { success: false, error: message };
    }
  };

  const logout = () => {
    localStorage.clear();
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, login, logout, register, loading }}>
      {children}
    </AuthContext.Provider>
  );
};
