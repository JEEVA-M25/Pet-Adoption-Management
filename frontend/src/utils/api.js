// utils/api.js
import { API_BASE_URL } from './constants.js';

// Helper function to get auth headers
const getAuthHeaders = () => {
  const token = localStorage.getItem('token');
  return {
    'Content-Type': 'application/json',
    'Authorization': token ? `Bearer ${token}` : ''
  };
};

// Auth API calls
export const login = async (email, password) => {
  const res = await fetch(`${API_BASE_URL}/api/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, password }),
  });

  if (!res.ok) {
    const errorData = await res.json().catch(() => null);
    throw new Error(errorData?.message || "Invalid credentials");
  }

  return res.json();
};


export const register = (userData) => {
  return fetch(`${API_BASE_URL}/api/users`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(userData),
  })
  .then(async res => {
    if (!res.ok) {
      const errorData = await res.json().catch(() => null);
      throw new Error(errorData?.message || "Registration failed");
    }
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
  const role = localStorage.getItem('userRole');
  const email = localStorage.getItem('userEmail');

  if (!token || !role || !email) return null;

  return { email, role };
};


// Logout
export const logout = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('userRole');
};