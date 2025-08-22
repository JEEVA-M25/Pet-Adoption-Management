import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import * as api from '../utils/api';
import './AddPetForm.css';
import petImage from '../assets/surroundform.jpg'; // Assuming you have an image for the background

function AddPetForm() {
  const [form, setForm] = useState({
    name: '',
    species: '',
    breed: '',
    age: '',
    description: '',
    imageUrl: '',
    adoptionStatus: 'Available',
  });

  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.addPet(form);
      navigate('/pets'); // Navigate to listing page
    } catch (err) {
      alert('Error creating pet: ' + err.message);
    }
  };

  return (
  <div
  className="add-pet-page"
  style={{ backgroundImage: `url(${petImage})` }}
>
    <div className="add-pet-form-container">
      <h2>Add New Pet</h2>
      <form onSubmit={handleSubmit}>
        <input name="name" placeholder="Name" value={form.name} onChange={handleChange} required />
        <input name="species" placeholder="Species" value={form.species} onChange={handleChange} required />
        <input name="breed" placeholder="Breed" value={form.breed} onChange={handleChange} required />
        <input name="age" type="number" placeholder="Age" value={form.age} onChange={handleChange} required />
        <textarea name="description" placeholder="Description" value={form.description} onChange={handleChange} />
        <input name="imageUrl" placeholder="Image URL" value={form.imageUrl} onChange={handleChange} />
        <select name="adoptionStatus" value={form.adoptionStatus} onChange={handleChange}>
          <option value="Available">Available</option>
          <option value="Pending">Pending</option>
          <option value="Adopted">Adopted</option>
        </select>
        <button type="submit">Add Pet</button>
      </form>
    </div>
  </div>
  );
}
export default AddPetForm;
