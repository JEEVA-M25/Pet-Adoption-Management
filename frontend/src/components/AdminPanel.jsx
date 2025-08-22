// components/AdminPanel.js
import React, { useState } from 'react';
import { useAuth } from '../contexts/AuthContext';
import './AdminPanel.css';

function AdminPanel() {
  const [activeTab, setActiveTab] = useState('shelters');
  const [shelterForm, setShelterForm] = useState({
    name: '',
    address: '',
    phone: ''
  });
  const { user } = useAuth();

  if (!user || user.role !== 'ADMIN') {
    return (
      <div className="admin-panel">
        <div className="access-denied">
          <h2>Access Denied</h2>
          <p>You must be an administrator to access this page.</p>
        </div>
      </div>
    );
  }

  const handleShelterSubmit = (e) => {
    e.preventDefault();
    // Implement shelter creation
    console.log('Create shelter:', shelterForm);
    // Reset form
    setShelterForm({ name: '', address: '', phone: '' });
  };

  return (
    <div className="admin-panel">
      <h2>Admin Dashboard</h2>
      
      <div className="admin-tabs">
        <button 
          className={activeTab === 'shelters' ? 'active' : ''}
          onClick={() => setActiveTab('shelters')}
        >
          Manage Shelters
        </button>
        <button 
          className={activeTab === 'users' ? 'active' : ''}
          onClick={() => setActiveTab('users')}
        >
          Manage Users
        </button>
        <button 
          className={activeTab === 'requests' ? 'active' : ''}
          onClick={() => setActiveTab('requests')}
        >
          Adoption Requests
        </button>
      </div>

      <div className="admin-content">
        {activeTab === 'shelters' && (
          <div className="shelter-management">
            <h3>Create New Shelter</h3>
            <form onSubmit={handleShelterSubmit} className="shelter-form">
              <div className="form-group">
                <label>Shelter Name</label>
                <input
                  type="text"
                  value={shelterForm.name}
                  onChange={(e) => setShelterForm({...shelterForm, name: e.target.value})}
                  required
                />
              </div>
              <div className="form-group">
                <label>Address</label>
                <input
                  type="text"
                  value={shelterForm.address}
                  onChange={(e) => setShelterForm({...shelterForm, address: e.target.value})}
                  required
                />
              </div>
              <div className="form-group">
                <label>Phone</label>
                <input
                  type="tel"
                  value={shelterForm.phone}
                  onChange={(e) => setShelterForm({...shelterForm, phone: e.target.value})}
                  required
                />
              </div>
              <button type="submit">Create Shelter</button>
            </form>
          </div>
        )}

        {activeTab === 'users' && (
          <div className="user-management">
            <h3>User Management</h3>
            <p>User management functionality coming soon...</p>
          </div>
        )}

        {activeTab === 'requests' && (
          <div className="request-management">
            <h3>Adoption Requests</h3>
            <p>Adoption request management coming soon...</p>
          </div>
        )}
      </div>
    </div>
  );
}

export default AdminPanel;