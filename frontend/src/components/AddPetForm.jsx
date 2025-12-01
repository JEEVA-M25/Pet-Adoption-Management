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
  const [message, setMessage] = useState('');
  const { user } = useAuth();

  // if (!user || user.role !== 'ADMIN') {
  //   return (
  //     <div className="admin-panel">
  //       <div className="access-denied">
  //         <h2>Access Denied</h2>
  //         <p>You must be an administrator to access this page.</p>
  //       </div>
  //     </div>
  //   );
  // }

  const handleShelterSubmit = async (e) => {
    e.preventDefault();
    try {
      // Simulate API call
      setMessage('Creating shelter...');
      await new Promise(resolve => setTimeout(resolve, 1000));
      
      // In a real app, you would call: await api.createShelter(shelterForm);
      console.log('Create shelter:', shelterForm);
      
      setMessage('Shelter created successfully!');
      setShelterForm({ name: '', address: '', phone: '' });
      
      // Clear message after 3 seconds
      setTimeout(() => setMessage(''), 3000);
    } catch (error) {
      setMessage('Error creating shelter: ' + error.message);
    }
  };

  return (
    <div className="admin-panel">
      <h2>Admin Dashboard</h2>
      
      {/* Stats Overview (placeholder) */}
      <div className="stats-overview">
        <div className="stat-card">
          <div className="stat-number">42</div>
          <div className="stat-label">Total Users</div>
        </div>
        <div className="stat-card">
          <div className="stat-number">18</div>
          <div className="stat-label">Active Shelters</div>
        </div>
        <div className="stat-card">
          <div className="stat-number">156</div>
          <div className="stat-label">Pets Available</div>
        </div>
        <div className="stat-card">
          <div className="stat-number">89</div>
          <div className="stat-label">Adoption Requests</div>
        </div>
      </div>
      
      <div className="admin-tabs">
        <button 
          className={activeTab === 'shelters' ? 'active' : ''}
          onClick={() => setActiveTab('shelters')}
        >
          🏠 Manage Shelters
        </button>
        <button 
          className={activeTab === 'users' ? 'active' : ''}
          onClick={() => setActiveTab('users')}
        >
          👥 Manage Users
        </button>
        <button 
          className={activeTab === 'requests' ? 'active' : ''}
          onClick={() => setActiveTab('requests')}
        >
          📋 Adoption Requests
        </button>
      </div>

      {message && (
        <div className={`message ${message.includes('Error') ? 'error' : 'success'}`}>
          {message}
        </div>
      )}

      <div className="admin-content">
        {activeTab === 'shelters' && (
          <div className="shelter-management">
            <h3>Create New Shelter</h3>
            <form onSubmit={handleShelterSubmit} className="shelter-form">
              <div className="form-group">
                <label>Shelter Name *</label>
                <input
                  type="text"
                  value={shelterForm.name}
                  onChange={(e) => setShelterForm({...shelterForm, name: e.target.value})}
                  required
                  placeholder="Enter shelter name"
                />
              </div>
              <div className="form-group">
                <label>Address *</label>
                <input
                  type="text"
                  value={shelterForm.address}
                  onChange={(e) => setShelterForm({...shelterForm, address: e.target.value})}
                  required
                  placeholder="Full address"
                />
              </div>
              <div className="form-group">
                <label>Phone Number *</label>
                <input
                  type="tel"
                  value={shelterForm.phone}
                  onChange={(e) => setShelterForm({...shelterForm, phone: e.target.value})}
                  required
                  placeholder="+1 (555) 123-4567"
                  pattern="[+]{0,1}[0-9\s\-\(\)]{10,}"
                />
              </div>
              <button type="submit">Create Shelter</button>
            </form>
          </div>
        )}

        {activeTab === 'users' && (
          <div className="user-management">
            <h3>User Management</h3>
            <div className="coming-soon">
              <div className="coming-soon-icon">👥</div>
              <p>User management dashboard coming soon!</p>
              <p>This section will allow you to view, edit, and manage all users in the system.</p>
            </div>
          </div>
        )}

        {activeTab === 'requests' && (
          <div className="request-management">
            <h3>Adoption Requests</h3>
            <div className="coming-soon">
              <div className="coming-soon-icon">📋</div>
              <p>Adoption request management coming soon!</p>
              <p>This section will allow you to review and process all adoption applications.</p>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

export default AdminPanel;