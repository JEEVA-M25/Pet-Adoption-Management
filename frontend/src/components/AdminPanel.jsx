// components/AdminPanel.jsx
import React, { useState, useEffect } from "react";
import { useAuth } from "../contexts/AuthContext";
import { Navigate } from "react-router-dom";
import "./AdminPanel.css";
import { toast } from "react-toastify";

import {
  createShelter,
  getShelters,
  createOrgUserForShelter,
  updateShelter,
  deleteShelter,
  getAllUsers,
  deleteUser
} from "../utils/api";

function AdminPanel() {

  const { user } = useAuth();
  if (!user || user.role !== "ADMIN") return <Navigate to="/" replace />;

  const [activeTab, setActiveTab] = useState("shelters");

  // ------------------------------
  // FORM STATES
  // ------------------------------
  const [shelterForm, setShelterForm] = useState({
    name: "",
    address: "",
    phone: ""
  });

  const [orgUserForm, setOrgUserForm] = useState({
    shelterId: "",
    name: "",
    email: "",
    password: "",
    phone: ""
  });

  const [shelters, setShelters] = useState([]);
  const [users, setUsers] = useState([]);

  const [editShelter, setEditShelter] = useState(null);

  // ------------------------------
  // FETCH FUNCTIONS
  // ------------------------------
  const loadShelters = () => {
    getShelters()
      .then(res => setShelters(res.content || res))
      .catch(() => toast.error("Failed to load shelters"));
  };

  const loadUsers = () => {
    getAllUsers()
      .then(setUsers)
      .catch(() => toast.error("Failed to load users"));
  };

  useEffect(() => {
    loadShelters();
    loadUsers();
  }, []);

  // ------------------------------
  // CREATE SHELTER
  // ------------------------------
  const handleCreateShelter = async (e) => {
    e.preventDefault();
    try {
      await createShelter(shelterForm);
      toast.success("Shelter created!");
      setShelterForm({ name: "", address: "", phone: "" });
      loadShelters();
    } catch (err) {
      toast.error(err.message);
    }
  };

  // ------------------------------
  // CREATE ORG USER
  // ------------------------------
  const handleCreateOrgUser = async (e) => {
    e.preventDefault();
    try {
      await createOrgUserForShelter(orgUserForm.shelterId, orgUserForm);
      toast.success("ORG user created!");
      setOrgUserForm({
        shelterId: "",
        name: "",
        email: "",
        password: "",
        phone: ""
      });
      loadUsers();
    } catch (err) {
      toast.error(err.message);
    }
  };

  // ------------------------------
  // UPDATE SHELTER
  // ------------------------------
  const handleUpdateShelter = async () => {
    try {
      await updateShelter(editShelter.id, editShelter);
      toast.success("Shelter updated!");
      setEditShelter(null);
      loadShelters();
    } catch {
      toast.error("Failed to update");
    }
  };

  // ------------------------------
  // DELETE SHELTER
  // ------------------------------
  const handleDeleteShelter = async (id) => {
    if (!window.confirm("Delete shelter?")) return;
    try {
      await deleteShelter(id);
      toast.success("Shelter deleted");
      loadShelters();
    } catch {
      toast.error("Cannot delete shelter");
    }
  };

  // ------------------------------
  // DELETE USER
  // ------------------------------
  const handleDeleteUser = async (id) => {
    if (!window.confirm("Delete this user?")) return;
    try {
      await deleteUser(id);
      toast.success("User deleted");
      loadUsers();
    } catch {
      toast.error("Failed to delete user");
    }
  };

  // ------------------------------
  // DASHBOARD COUNTS
  // ------------------------------
  const totalShelters = shelters.length;
  const totalOrgUsers = users.filter(u => u.role === "ORG_USER").length;
  const totalPublicUsers = users.filter(u => u.role === "PUBLIC_USER").length;

  return (
    <div className="admin-panel">
      <h2>Admin Dashboard</h2>

      {/* SUMMARY CARDS */}
      <div className="stats-overview">
        <div className="stat-card">
          <div className="stat-number">{totalShelters}</div>
          <div className="stat-label">Shelters</div>
        </div>
        <div className="stat-card">
          <div className="stat-number">{totalOrgUsers}</div>
          <div className="stat-label">Org Users</div>
        </div>
        <div className="stat-card">
          <div className="stat-number">{totalPublicUsers}</div>
          <div className="stat-label">Public Users</div>
        </div>
      </div>

      {/* NAV TABS */}
      <div className="admin-tabs">
        <button className={activeTab === "shelters" ? "active" : ""} onClick={() => setActiveTab("shelters")}>
          🏠 Manage Shelters
        </button>
        <button className={activeTab === "users" ? "active" : ""} onClick={() => setActiveTab("users")}>
          👥 Manage Users
        </button>
        <button className={activeTab === "createUser" ? "active" : ""} onClick={() => setActiveTab("createUser")}>
          ➕ Create ORG User
        </button>
        <button className={activeTab === "createShelter" ? "active" : ""} onClick={() => setActiveTab("createShelter")}>
          🏗️ Create Shelter
        </button>
      </div>

      <div className="admin-content">

        {/* -------------------------- MANAGE SHELTERS -------------------------- */}
        {activeTab === "shelters" && (
          <>
            <h3>All Shelters</h3>

            {shelters.map(s => (
              <div key={s.id} className="admin-list-card">
                <div>
                  <strong>{s.name}</strong><br />
                  {s.address}<br />
                  {s.phone}
                </div>

                <div className="actions">
                  <button onClick={() => setEditShelter(s)}>Edit</button>
                  <button className="delete" onClick={() => handleDeleteShelter(s.id)}>Delete</button>
                </div>
              </div>
            ))}

            {/* EDIT SHELTER MODAL */}
            {editShelter && (
              <div className="modal">
                <div className="modal-content">
                  <h3>Edit Shelter</h3>

                  <input
                    value={editShelter.name}
                    onChange={(e) => setEditShelter({ ...editShelter, name: e.target.value })}
                  />
                  <input
                    value={editShelter.address}
                    onChange={(e) => setEditShelter({ ...editShelter, address: e.target.value })}
                  />
                  <input
                    value={editShelter.phone}
                    onChange={(e) => setEditShelter({ ...editShelter, phone: e.target.value })}
                  />

                  <button onClick={handleUpdateShelter}>Save</button>
                  <button className="delete" onClick={() => setEditShelter(null)}>Cancel</button>
                </div>
              </div>
            )}
          </>
        )}

        {/* -------------------------- MANAGE USERS -------------------------- */}
        {activeTab === "users" && (
          <>
            <h3>All Users</h3>

            {users.map(u => (
              <div key={u.id} className="admin-list-card">
                <div>
                  <strong>{u.name}</strong> ({u.role})<br />
                  {u.email}
                </div>

                <div className="actions">
                  <button className="delete" onClick={() => handleDeleteUser(u.id)}>Delete</button>
                </div>
              </div>
            ))}
          </>
        )}

        {/* -------------------------- CREATE ORG USER -------------------------- */}
        {activeTab === "createUser" && (
          <>
            <h3>Create ORG User for Shelter</h3>

            <form onSubmit={handleCreateOrgUser} className="shelter-form">

              <div className="form-group">
                <label>Select Shelter *</label>
                <select
                  required
                  value={orgUserForm.shelterId}
                  onChange={(e) => setOrgUserForm({ ...orgUserForm, shelterId: e.target.value })}
                >
                  <option value="">-- Select --</option>
                  {shelters.map(s => (
                    <option key={s.id} value={s.id}>{s.name}</option>
                  ))}
                </select>
              </div>

              <input
                placeholder="Name"
                required
                value={orgUserForm.name}
                onChange={(e) => setOrgUserForm({ ...orgUserForm, name: e.target.value })}
              />

              <input
                placeholder="Email"
                type="email"
                required
                value={orgUserForm.email}
                onChange={(e) => setOrgUserForm({ ...orgUserForm, email: e.target.value })}
              />

              <input
                placeholder="Password"
                type="password"
                required
                value={orgUserForm.password}
                onChange={(e) => setOrgUserForm({ ...orgUserForm, password: e.target.value })}
              />

              <input
                placeholder="Phone"
                required
                value={orgUserForm.phone}
                onChange={(e) => setOrgUserForm({ ...orgUserForm, phone: e.target.value })}
              />

              <button className="create-btn">Create ORG User</button>
            </form>
          </>
        )}

        {/* -------------------------- CREATE SHELTER -------------------------- */}
        {activeTab === "createShelter" && (
          <>
            <h3>Create New Shelter</h3>

            <form onSubmit={handleCreateShelter} className="shelter-form">
              <div className="form-group">
                <label>Name *</label>
                <input
                  required
                  value={shelterForm.name}
                  onChange={(e) => setShelterForm({ ...shelterForm, name: e.target.value })}
                />
              </div>

              <div className="form-group">
                <label>Address *</label>
                <input
                  required
                  value={shelterForm.address}
                  onChange={(e) => setShelterForm({ ...shelterForm, address: e.target.value })}
                />
              </div>

              <div className="form-group">
                <label>Phone *</label>
                <input
                  required
                  value={shelterForm.phone}
                  onChange={(e) => setShelterForm({ ...shelterForm, phone: e.target.value })}
                />
              </div>

              <button className="create-btn">Create Shelter</button>
            </form>
          </>
        )}

      </div>
    </div>
  );
}

export default AdminPanel;
