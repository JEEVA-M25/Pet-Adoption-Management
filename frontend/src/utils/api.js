// utils/api.js
import { API_BASE_URL } from './constants';

// GET all pets
export const getPets = () => {
  return fetch(`${API_BASE_URL}/api/pets`)
    .then(res => {
      if (!res.ok) throw new Error('Failed to fetch pets');
      return res.json();
    });
};

// GET single pet by ID
export const getPetById = (id) => {
  return fetch(`${API_BASE_URL}/api/pets/${id}`)
    .then(res => {
      if (!res.ok) throw new Error('Failed to fetch pet');
      return res.json();
    });
};

// POST create new pet
export const addPet = (petData) => {
  return fetch(`${API_BASE_URL}/api/pets`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
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
    headers: {
      'Content-Type': 'application/json',
    },
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
  }).then(res => {
    if (!res.ok) throw new Error('Failed to delete pet');
    return res.text(); // or .json() if backend returns JSON
  });
};
