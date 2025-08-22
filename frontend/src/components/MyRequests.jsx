// components/MyRequests.js
import React, { useState, useEffect } from 'react';
import { useAuth } from '../contexts/AuthContext';
import * as api from '../utils/api';
import './MyRequests.css';

function MyRequests() {
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const { user } = useAuth();

  useEffect(() => {
    const fetchRequests = async () => {
      try {
        const data = await api.getMyAdoptionRequests();
        setRequests(data);
      } catch (err) {
        setError('Failed to load adoption requests');
      } finally {
        setLoading(false);
      }
    };

    fetchRequests();
  }, []);

  if (loading) return <div className="loading">Loading your requests...</div>;
  if (error) return <div className="error">{error}</div>;

  return (
    <div className="my-requests">
      <h2>My Adoption Requests</h2>
      
      {requests.length === 0 ? (
        <div className="no-requests">
          <p>You haven't made any adoption requests yet.</p>
        </div>
      ) : (
        <div className="requests-grid">
          {requests.map(request => (
            <div key={request.id} className="request-card">
              <h3>{request.pet?.name || 'Unknown Pet'}</h3>
              <p><strong>Status:</strong> 
                <span className={`status ${request.status.toLowerCase()}`}>
                  {request.status}
                </span>
              </p>
              <p><strong>Submitted:</strong> {new Date(request.submissionDate).toLocaleDateString()}</p>
              <p><strong>Pet Species:</strong> {request.pet?.species || 'Unknown'}</p>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default MyRequests;