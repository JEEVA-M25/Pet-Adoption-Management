// components/MyAdopted.jsx
import React, { useEffect, useState } from "react";
import { getMyAdoptedPets } from "../utils/api";
import "./MyAdopted.css";

function MyAdopted() {
  const [pets, setPets] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getMyAdoptedPets()
      .then((data) => {
        setPets(data);
        setLoading(false);
      })
      .catch(() => {
        setPets([]);
        setLoading(false);
      });
  }, []);

  return (
    <div className="adopted-container">
      <h2 className="adopted-title">🐾 My Adopted Pets</h2>

      {loading ? (
        <p className="loading-text">Loading your adopted pets...</p>
      ) : pets.length === 0 ? (
        <div className="empty-state">
          <p>You haven’t adopted any pets yet.</p>
        </div>
      ) : (
        <div className="adopted-grid">
          {pets.map((pet) => (
            <div className="adopted-card" key={pet.id}>
              <img src={pet.imageUrl} alt={pet.name} className="adopted-img" />

              <div className="adopted-info">
                <h3>{pet.name}</h3>
                <p>
                  <strong>Species:</strong> {pet.species}
                </p>
                <p>
                  <strong>Breed:</strong> {pet.breed}
                </p>
                <p>
                  <strong>Age:</strong> {pet.age} years
                </p>
                <p className="status-tag">
                  ✅ Adopted
                </p>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default MyAdopted;
