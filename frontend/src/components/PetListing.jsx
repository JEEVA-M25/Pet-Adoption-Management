import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import * as api from '../utils/api';
import './PetListing.css';

function PetListing() {
  const [pets, setPets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [speciesFilter, setSpeciesFilter] = useState('All');
  const [statusFilter, setStatusFilter] = useState('All');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchPets = async () => {
      try {
        const data = await api.getPets();
        setPets(data);
      } catch (err) {
        setError('⚠️ Failed to load pets. Please try again later.');
      } finally {
        setLoading(false);
      }
    };
    fetchPets();
  }, []);

  const filteredPets = pets.filter((pet) => {
    const matchesSpecies = speciesFilter === 'All' || pet.species === speciesFilter;
    const matchesStatus = statusFilter === 'All' || pet.adoptionStatus === statusFilter;
    return matchesSpecies && matchesStatus;
  });

  const handleImageError = (e) => {
    e.target.src = 'https://via.placeholder.com/300x250/667eea/white?text=Pet+Photo';
  };

  const formatPetInfo = (species, breed) => {
    if (!breed || breed.toLowerCase() === 'unknown' || breed.toLowerCase() === 'mixed') {
      return species;
    }
    return `${species} - ${breed}`;
  };

  return (
    <div className="pet-listing">
      <div className="content-container">
        <h2>🐾 Find Your Perfect Companion</h2>

        <div className="filters">
          <label>
            Filter by Species:
            <select data-testid="species-filter" value={speciesFilter} onChange={(e) => setSpeciesFilter(e.target.value)}>
              <option value="All">All Species</option>
              <option value="Dog">🐕 Dogs</option>
              <option value="Cat">🐱 Cats</option>
              <option value="Bird">🐦 Birds</option>
              <option value="Rabbit">🐰 Rabbits</option>
              <option value="Other">🐾 Other</option>
            </select>
          </label>

          <label>
            Filter by Status:
            <select data-testid="status-filter" value={statusFilter} onChange={(e) => setStatusFilter(e.target.value)}>
              <option value="All">All Status</option>
              <option value="Available">✅ Available</option>
              <option value="Adopted">🏠 Adopted</option>
              <option value="Pending">⏳ Pending</option>
            </select>
          </label>
        </div>

        {loading && <div className="loading">Loading adorable pets...</div>}

        {error && (
          <div className="error">
            {error}
            <button onClick={() => window.location.reload()}>Retry</button>
          </div>
        )}

        {!loading && !error && filteredPets.length === 0 && (
          <div className="no-results">
            <div style={{ fontSize: '3rem', marginBottom: '1rem' }}>🔍</div>
            <div>No pets found with the selected filters</div>
          </div>
        )}

        {!loading && !error && filteredPets.length > 0 && (
          <div className="pet-grid" data-testid="pet-grid">
            {filteredPets.map((pet, index) => (
              <div
                key={pet.id}
                className="pet-card"
                onClick={() => navigate(`/pets/${pet.id}`)}
                style={{ animationDelay: `${index * 0.1}s` }}
                role="button"
                tabIndex={0}
                onKeyDown={(e) => { if (e.key === 'Enter' || e.key === ' ') navigate(`/pets/${pet.id}`); }}
              >
                <img
                  src={pet.imageUrl || 'https://via.placeholder.com/300x250/667eea/white?text=Pet+Photo'}
                  alt={pet.name || 'Adorable pet'}
                  onError={handleImageError}
                />
                <h3>{pet.name || 'Sweet Pet'}</h3>
                <p>{formatPetInfo(pet.species, pet.breed)}</p>
                <p className={`status ${pet.adoptionStatus?.toLowerCase()}`}>
                  {pet.adoptionStatus === 'Available' && '✅ '}
                  {pet.adoptionStatus === 'Adopted' && '🏠 '}
                  {pet.adoptionStatus === 'Pending' && '⏳ '}
                  {pet.adoptionStatus || 'Unknown'}
                </p>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

export default PetListing;
