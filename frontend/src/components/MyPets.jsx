// components/MyPets.js
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import * as api from '../utils/api';
import './MyPets.css';

function MyPets() {
  const [pets, setPets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const { user } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    const fetchMyPets = async () => {
      try {
        const allPets = await api.getPets();
        
        // Filter pets to show only those posted by the current user
        // This assumes your backend returns pets with postedBy information
        const userPets = allPets.filter(pet => 
          pet.postedBy && pet.postedBy.email === user.email
        );
        
        setPets(userPets);
      } catch (err) {
        setError('Failed to load your pets. Please try again later.');
        console.error('Error fetching pets:', err);
      } finally {
        setLoading(false);
      }
    };

    if (user) {
      fetchMyPets();
    } else {
      setLoading(false);
    }
  }, [user]);

  const handleEditPet = (petId, e) => {
    e.stopPropagation();
    navigate(`/pets/${petId}`);
  };

  const handleDeletePet = async (petId, e) => {
    e.stopPropagation();
    
    if (window.confirm('Are you sure you want to delete this pet?')) {
      try {
        await api.deletePet(petId);
        setPets(pets.filter(pet => pet.id !== petId));
      } catch (err) {
        alert('Failed to delete pet: ' + err.message);
      }
    }
  };

  if (loading) {
    return (
      <div className="loading">
        <div className="loading-spinner"></div>
        <p>Loading your pets...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="error">
        <h3>⚠️ Error</h3>
        <p>{error}</p>
        <button className="retry-btn" onClick={() => window.location.reload()}>
          Try Again
        </button>
      </div>
    );
  }

  if (!user) {
    return (
      <div className="not-logged-in">
        <h2>Please Log In</h2>
        <p>You need to be logged in to view your pets.</p>
        <button 
          className="login-btn"
          onClick={() => navigate('/login')}
        >
          Go to Login
        </button>
      </div>
    );
  }

  return (
    <div className="my-pets">
      <div className="my-pets-header">
        <h2>My Pets</h2>
        <p className="pets-count">
          {pets.length} pet{pets.length !== 1 ? 's' : ''} in your care
        </p>
      </div>
      
      {pets.length === 0 ? (
        <div className="no-pets">
          <div className="no-pets-illustration">🐾</div>
          <h3>No Pets Yet</h3>
          <p>You haven't added any pets to your profile yet.</p>
          <button 
            className="cta-button"
            onClick={() => navigate('/add-pet')}
          >
            Add Your First Pet
          </button>
        </div>
      ) : (
        <div className="pets-grid">
          {pets.map(pet => (
            <div
              key={pet.id}
              className="pet-card"
              onClick={() => navigate(`/pets/${pet.id}`)}
            >
              <div className="pet-image-container">
                <img 
                  src={pet.imageUrl || 'https://via.placeholder.com/300x200/667eea/white?text=Pet+Photo'} 
                  alt={pet.name}
                  onError={(e) => {
                    e.target.src = 'https://via.placeholder.com/300x200/667eea/white?text=Pet+Photo';
                  }}
                />
                <div className={`status-badge ${pet.adoptionStatus.toLowerCase()}`}>
                  {pet.adoptionStatus}
                </div>
              </div>
              
              <div className="pet-card-content">
                <h3>{pet.name || 'Unnamed Pet'}</h3>
                <div className="pet-details">
                  <p className="species-breed">
                    <span className="label">Type:</span> 
                    {pet.species} {pet.breed && `- ${pet.breed}`}
                  </p>
                  <p className="age">
                    <span className="label">Age:</span> 
                    {pet.age} month{pet.age !== 1 ? 's' : ''}
                  </p>
                  {pet.description && (
                    <p className="description">
                      {pet.description.length > 100 
                        ? `${pet.description.substring(0, 100)}...` 
                        : pet.description
                      }
                    </p>
                  )}
                </div>
                
                <div className="pet-actions">
                  <button 
                    className="edit-btn"
                    onClick={(e) => handleEditPet(pet.id, e)}
                  >
                    ✏️ Edit
                  </button>
                  <button 
                    className="delete-btn"
                    onClick={(e) => handleDeletePet(pet.id, e)}
                  >
                    🗑️ Delete
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default MyPets;