//AuthContext.jsx
import React, { createContext, useContext, useState, useEffect } from 'react';
import * as api from '../utils/api';

const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem('token');
    const role = localStorage.getItem('userRole');
    const email = localStorage.getItem('userEmail');

    if (token && role && email) {
      setUser({ email, role });
    }
    setLoading(false);
  }, []);

 const login = async (email, password) => {
  try {
    const res = await api.login(email, password);

    localStorage.setItem('token', res.token);
    localStorage.setItem('userRole', res.role);
    localStorage.setItem('userEmail', email);

    setUser({ email, role: res.role });
    return { success: true, role: res.role };

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

    // Read backend error message (Spring Boot sends JSON: {"message": "Email already exists"})
    if (error.response?.data?.message) {
      message = error.response.data.message;
    } else if (error.message) {
      message = error.message;
    }

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
