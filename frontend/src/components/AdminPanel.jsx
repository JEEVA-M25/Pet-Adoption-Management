// components/AdminPanel.js
import React, { useState } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { Navigate } from "react-router-dom";
import './AdminPanel.css';
import { createShelter } from "../utils/api";
import { toast } from "react-toastify";

function AdminPanel() {
  const [activeTab, setActiveTab] = useState('shelters');

  const [shelterForm, setShelterForm] = useState({
    name: '',
    address: '',
    phone: ''
  });

  const { user } = useAuth();

  // 🚨 BLOCK NON-ADMINS FROM ACCESSING ADMIN PANEL
  if (!user || user.role !== "ADMIN") {
    return <Navigate to="/" replace />;
  }

  // 🏠 Create Shelter Submit
  const handleShelterSubmit = async (e) => {
    e.preventDefault();

    try {
      await createShelter(shelterForm);

      toast.success("Shelter created successfully!");

      setShelterForm({
        name: '',
        address: '',
        phone: ''
      });

    } catch (err) {
      toast.error(err.message || "Failed to create shelter");
    }
  };


  return (
    <div className="admin-panel">
      <h2>Admin Dashboard</h2>

      {/* TABS */}
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
          📄 Adoption Requests
        </button>
      </div>

      {/* TAB CONTENT */}
      <div className="admin-content">

        {/* 🏠 MANAGE SHELTERS */}
        {activeTab === 'shelters' && (
          <div className="shelter-management">
            <h3>Create New Shelter</h3>

            <form onSubmit={handleShelterSubmit} className="shelter-form">
              
              <div className="form-group">
                <label>Shelter Name *</label>
                <input
                  type="text"
                  value={shelterForm.name}
                  onChange={(e) =>
                    setShelterForm({ ...shelterForm, name: e.target.value })
                  }
                  required
                />
              </div>

              <div className="form-group">
                <label>Address *</label>
                <input
                  type="text"
                  value={shelterForm.address}
                  onChange={(e) =>
                    setShelterForm({ ...shelterForm, address: e.target.value })
                  }
                  required
                />
              </div>

              <div className="form-group">
                <label>Phone *</label>
                <input
                  type="tel"
                  value={shelterForm.phone}
                  onChange={(e) =>
                    setShelterForm({ ...shelterForm, phone: e.target.value })
                  }
                  required
                />
              </div>

              <button type="submit" className="create-btn">
                Create Shelter
              </button>
            </form>
          </div>
        )}

        {/* 👥 MANAGE USERS TAB */}
        {activeTab === 'users' && (
          <div className="user-management">
            <h3>Manage Users</h3>
            <p>User management interface coming soon...</p>
          </div>
        )}

        {/* 📄 ADOPTION REQUEST TAB */}
        {activeTab === 'requests' && (
          <div className="request-management">
            <h3>Adoption Requests</h3>
            <p>Admin-wide adoption request management coming soon...</p>
          </div>
        )}

      </div>
    </div>
  );
}

export default AdminPanel;
