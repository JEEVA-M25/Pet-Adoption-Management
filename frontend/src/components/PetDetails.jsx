// PetDetails.jsx
import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import * as api from '../utils/api';
import './PetDetails.css';

function PetDetails() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();   // ✅ GET LOGGED-IN USER

  const [pet, setPet] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState({});

  useEffect(() => {
    const fetchPet = async () => {
      try {
        const data = await api.getPetById(id);
        if (!data) setError('Pet not found');
        else {
          setPet(data);
          setFormData(data);
        }
      } catch (err) {
        setError('Could not fetch pet details');
      } finally {
        setLoading(false);
      }
    };
    fetchPet();
  }, [id]);

  const isOrgUser = user?.role === "ORG_USER";    // ✅ only org users can edit/delete

  const handleDelete = async () => {
    if (window.confirm(`Are you sure you want to delete ${pet.name}?`)) {
      try {
        await api.deletePet(id);
        navigate('/pets');
      } catch (err) {
        setError('Failed to delete pet.');
      }
    }
  };

  const handleEditToggle = () => setIsEditing(!isEditing);
  const handleChange = (e) => setFormData({ ...formData, [e.target.name]: e.target.value });

  const handleUpdate = async (e) => {
    e.preventDefault();
    try {
      const updated = await api.updatePet(id, formData);
      setPet(updated);
      setIsEditing(false);
    } catch (err) {
      setError('Failed to update pet.');
    }
  };

  const handleImageError = (e) => {
    e.target.src =
      'https://via.placeholder.com/400x400/667eea/white?text=Pet+Photo';
  };

  if (loading) return (
    <div className="pet-details">
      <div className="loading-state">
        <div className="loading-spinner"></div>
        <p>Loading pet details...</p>
      </div>
    </div>
  );

  if (error) return (
    <div className="pet-details">
      <div className="error-state">
        <h2>⚠️ Error</h2>
        <p>{error}</p>
        <button onClick={() => navigate('/pets')} className="back-button">
          ⬅️ Back to Pets
        </button>
      </div>
    </div>
  );

  return (
    <div className={`pet-details ${isEditing ? 'editing' : ''}`}>

      {/* ------- VIEW MODE ------- */}
      {!isEditing ? (
        <>
          <div className="pet-image-section">
            <img
              src={
                pet.imageUrl ||
                'https://via.placeholder.com/400x400/667eea/white?text=Pet+Photo'
              }
              alt={pet.name || 'Pet'}
              onError={handleImageError}
            />
          </div>

          <div className="pet-info-section">
            <h2>{pet.name || 'Unnamed Pet'}</h2>

            <div className="pet-details-grid">
              {pet.species && (
                <div className="detail-item">
                  <span className="detail-label">Species:</span>
                  <span className="detail-value">{pet.species}</span>
                </div>
              )}

              {pet.breed && (
                <div className="detail-item">
                  <span className="detail-label">Breed:</span>
                  <span className="detail-value">{pet.breed}</span>
                </div>
              )}

              {pet.age !== undefined && (
                <div className="detail-item">
                  <span className="detail-label">Age:</span>
                  <span className="detail-value">{pet.age} months</span>
                </div>
              )}

              {pet.adoptionStatus && (
                <div className="detail-item status-item">
                  <span className="detail-label">Status:</span>
                  <span
                    className={`detail-value status-${pet.adoptionStatus.toLowerCase()}`}
                  >
                    {pet.adoptionStatus}
                  </span>
                </div>
              )}
            </div>

            {pet.description && (
              <div className="description-section">
                <h3>About {pet.name}</h3>
                <p>{pet.description}</p>
              </div>
            )}
          </div>

          {/* -------- ACTION BUTTONS -------- */}
          <div className="actions">

            {/* Always show Back button */}
            <button
              onClick={() => navigate('/pets')}
              className="back-btn"
            >
              ⬅️ Back
            </button>

            {/* Only Org Users see Edit/Delete */}
            {isOrgUser && (
              <>
                <button onClick={handleEditToggle} className="edit-btn">
                  ✏️ Edit
                </button>

                <button onClick={handleDelete} className="delete-btn">
                  🗑️ Delete
                </button>
              </>
            )}

          </div>
        </>
      ) : (
        /* ------- EDIT MODE ------- */
        <form onSubmit={handleUpdate} className="edit-form">
          <h2>Edit {pet.name}</h2>

          <div className="form-group">
            <label>Pet Name</label>
            <input
              type="text"
              name="name"
              value={formData.name || ""}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-row">
            <div className="form-group">
              <label>Species</label>
              <select
                name="species"
                value={formData.species || ""}
                onChange={handleChange}
                required
              >
                <option value="">Select Species</option>
                <option value="Dog">Dog</option>
                <option value="Cat">Cat</option>
                <option value="Bird">Bird</option>
                <option value="Rabbit">Rabbit</option>
                <option value="Other">Other</option>
              </select>
            </div>

            <div className="form-group">
              <label>Breed</label>
              <input
                type="text"
                name="breed"
                value={formData.breed || ""}
                onChange={handleChange}
              />
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label>Age (months)</label>
              <input
                type="number"
                name="age"
                value={formData.age || ""}
                onChange={handleChange}
                min="0"
              />
            </div>

            <div className="form-group">
              <label>Adoption Status</label>
              <select
                name="adoptionStatus"
                value={formData.adoptionStatus || "Available"}
                onChange={handleChange}
              >
                <option value="Available">Available</option>
                <option value="Adopted">Adopted</option>
                <option value="Pending">Pending</option>
              </select>
            </div>
          </div>

          <div className="form-group">
            <label>Description</label>
            <textarea
              name="description"
              value={formData.description || ""}
              onChange={handleChange}
              rows="4"
            />
          </div>

          <div className="form-group">
            <label>Image URL</label>
            <input
              type="url"
              name="imageUrl"
              value={formData.imageUrl || ""}
              onChange={handleChange}
            />
          </div>

          <div className="actions">
            <button type="submit" className="save-btn">
              💾 Save Changes
            </button>

            <button
              type="button"
              onClick={handleEditToggle}
              className="cancel-btn"
            >
              ❌ Cancel
            </button>
          </div>
        </form>
      )}
    </div>
  );
}

export default PetDetails;
