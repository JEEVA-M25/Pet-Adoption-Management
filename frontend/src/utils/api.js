// utils/api.js
import { API_BASE_URL } from './constants';

// Helper function to get auth headers
const getAuthHeaders = () => {
  const token = localStorage.getItem('token');
  return {
    'Content-Type': 'application/json',
    'Authorization': token ? `Bearer ${token}` : ''
  };
};

// Auth API calls
export const login = (email, password) => {
  return fetch(`${API_BASE_URL}/api/auth/login`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ email, password }),
  })
  .then(res => {
    if (!res.ok) throw new Error('Login failed');
    return res.json();
  });
};

export const register = (userData) => {
  return fetch(`${API_BASE_URL}/api/users`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(userData),
  })
  .then(res => {
    if (!res.ok) throw new Error('Registration failed');
    return res.json();
  });
};

// GET all pets
export const getPets = () => {
  return fetch(`${API_BASE_URL}/api/pets`, {
    headers: getAuthHeaders()
  })
  .then(res => {
    if (!res.ok) throw new Error('Failed to fetch pets');
    return res.json();
  });
};

// GET single pet by ID
export const getPetById = (id) => {
  return fetch(`${API_BASE_URL}/api/pets/${id}`, {
    headers: getAuthHeaders()
  })
  .then(res => {
    if (!res.ok) throw new Error('Failed to fetch pet');
    return res.json();
  });
};

// POST create new pet
export const addPet = (petData) => {
  return fetch(`${API_BASE_URL}/api/pets`, {
    method: 'POST',
    headers: getAuthHeaders(),
    body: JSON.stringify(petData),
  }).then(res => {
    if (!res.ok) throw new Error('Failed to add pet');
    return res.json();
  });
};

// PUT update existing pet
export const updatePet = (id, petData) => {
  return fetch(`${API_BASE_URL}/api/pets/${id}`, {
    method: 'PUT',
    headers: getAuthHeaders(),
    body: JSON.stringify(petData),
  }).then(res => {
    if (!res.ok) throw new Error('Failed to update pet');
    return res.json();
  });
};

// DELETE a pet
export const deletePet = (id) => {
  return fetch(`${API_BASE_URL}/api/pets/${id}`, {
    method: 'DELETE',
    headers: getAuthHeaders(),
  }).then(res => {
    if (!res.ok) throw new Error('Failed to delete pet');
    return res.text();
  });
};

// Adoption requests
export const getAdoptionRequests = () => {
  return fetch(`${API_BASE_URL}/api/adoption-requests`, {
    headers: getAuthHeaders()
  })
  .then(res => {
    if (!res.ok) throw new Error('Failed to fetch adoption requests');
    return res.json();
  });
};

export const getMyAdoptionRequests = () => {
  return fetch(`${API_BASE_URL}/api/adoption-requests/my-requests`, {
    headers: getAuthHeaders()
  })
  .then(res => {
    if (!res.ok) throw new Error('Failed to fetch my adoption requests');
    return res.json();
  });
};

export const createAdoptionRequest = (requestData) => {
  return fetch(`${API_BASE_URL}/api/adoption-requests`, {
    method: 'POST',
    headers: getAuthHeaders(),
    body: JSON.stringify(requestData),
  }).then(res => {
    if (!res.ok) throw new Error('Failed to create adoption request');
    return res.json();
  });
};

// Shelter management (admin only)
export const createShelter = (shelterData) => {
  return fetch(`${API_BASE_URL}/api/shelters`, {
    method: 'POST',
    headers: getAuthHeaders(),
    body: JSON.stringify(shelterData),
  }).then(res => {
    if (!res.ok) throw new Error('Failed to create shelter');
    return res.json();
  });
};

export const getShelters = () => {
  return fetch(`${API_BASE_URL}/api/shelters`, {
    headers: getAuthHeaders()
  })
  .then(res => {
    if (!res.ok) throw new Error('Failed to fetch shelters');
    return res.json();
  });
};

// User profile
export const getCurrentUser = () => {
  const token = localStorage.getItem('token');
  if (!token) return Promise.resolve(null);
  
  // Extract user info from token (simplified - in real app, you might want an API endpoint)
  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    return Promise.resolve({
      email: payload.sub,
      role: payload.role || 'PUBLIC_USER'
    });
  } catch (e) {
    return Promise.resolve(null);
  }
};

// Logout
export const logout = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('userRole');
};