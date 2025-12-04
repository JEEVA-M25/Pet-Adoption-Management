// components/AdminPanel.js
import React, { useState, useEffect } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { Navigate } from "react-router-dom";
import './AdminPanel.css';
import { toast } from "react-toastify";

import { 
  createShelter, 
  getShelters, 
  createOrgUserForShelter 
} from "../utils/api";

function AdminPanel() {
  const [activeTab, setActiveTab] = useState('shelters');

  const [shelterForm, setShelterForm] = useState({
    name: '',
    address: '',
    phone: ''
  });

  // For creating ORG USER
  const [orgUserForm, setOrgUserForm] = useState({
    shelterId: "",
    name: "",
    email: "",
    password: "",
    phone: ""
  });

  // Store list of shelters for dropdown
  const [shelters, setShelters] = useState([]);

  const { user } = useAuth();

  // 🚨 BLOCK NON-ADMINS
  if (!user || user.role !== "ADMIN") {
    return <Navigate to="/" replace />;
  }

  // 🔄 Load shelters when USERS tab opens
  useEffect(() => {
    if (activeTab === "users") {
      getShelters()
        .then((data) => setShelters(data.content || data))
        .catch(() => toast.error("Failed to load shelters"));
    }
  }, [activeTab]);

  // 🏠 Create shelter submit
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

  // 👥 Create ORG USER Submit
  const handleOrgUserSubmit = async (e) => {
    e.preventDefault();

    try {
      await createOrgUserForShelter(orgUserForm.shelterId, orgUserForm);

      toast.success("ORG user created successfully!");

      setOrgUserForm({
        shelterId: "",
        name: "",
        email: "",
        password: "",
        phone: ""
      });

    } catch (err) {
      toast.error(err.message || "Failed to create ORG user");
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

      </div>

      <div className="admin-content">

        {/* 🏠 SHELTER MANAGEMENT */}
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

        {/* 👥 USER MANAGEMENT — CREATE ORG USER */}
        {activeTab === 'users' && (
          <div className="user-management">
            <h3>Create ORG User for Shelter</h3>

            <form onSubmit={handleOrgUserSubmit} className="shelter-form">

              {/* Select Shelter */}
              <div className="form-group">
                <label>Select Shelter *</label>
                <select
                  value={orgUserForm.shelterId}
                  required
                  onChange={(e) =>
                    setOrgUserForm({
                      ...orgUserForm,
                      shelterId: e.target.value
                    })
                  }
                >
                  <option value="">-- Choose Shelter --</option>
                  {shelters.map((s) => (
                    <option key={s.id} value={s.id}>
                      {s.name}
                    </option>
                  ))}
                </select>
              </div>

              {/* User Name */}
              <div className="form-group">
                <label>Name *</label>
                <input
                  type="text"
                  required
                  value={orgUserForm.name}
                  onChange={(e) =>
                    setOrgUserForm({ ...orgUserForm, name: e.target.value })
                  }
                />
              </div>

              {/* Email */}
              <div className="form-group">
                <label>Email *</label>
                <input
                  type="email"
                  required
                  value={orgUserForm.email}
                  onChange={(e) =>
                    setOrgUserForm({ ...orgUserForm, email: e.target.value })
                  }
                />
              </div>

              {/* Password */}
              <div className="form-group">
                <label>Password *</label>
                <input
                  type="password"
                  required
                  value={orgUserForm.password}
                  onChange={(e) =>
                    setOrgUserForm({ ...orgUserForm, password: e.target.value })
                  }
                />
              </div>

              {/* Phone */}
              <div className="form-group">
                <label>Phone *</label>
                <input
                  type="tel"
                  required
                  value={orgUserForm.phone}
                  onChange={(e) =>
                    setOrgUserForm({ ...orgUserForm, phone: e.target.value })
                  }
                />
              </div>

              <button type="submit" className="create-btn">
                Create ORG User
              </button>
            </form>
          </div>
        )}

        

      </div>
    </div>
  );
}

export default AdminPanel;
