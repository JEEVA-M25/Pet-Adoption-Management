// App.jsx
import React from "react";
import { Routes, Route } from "react-router-dom";
import { ThemeProvider } from "./contexts/ThemeContext";
import { AuthProvider, useAuth } from "./contexts/AuthContext";

import Navbar from "./components/Navbar";
import Footer from "./components/Footer";
import PetListing from "./components/PetListing";
import PetDetails from "./components/PetDetails";
import LandingPage from "./components/LandingPage";
import AddPetForm from "./components/AddPetForm";
import Login from "./components/Login";
import Register from "./components/Register";
import MyRequests from "./components/MyRequests";
import MyPets from "./components/MyPets";
import AdminPanel from "./components/AdminPanel";
import MyAdopted from "./components/MyAdopted";
import OrgShelterRequests from "./components/OrgShelterRequests";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "./App.css";


// 🔒 UNIVERSAL ROLE CHECK WRAPPER
function ProtectedRoute({ element, roles }) {
  const { user } = useAuth();

  if (!user) {
    return <Login />;
  }

  if (roles && !roles.includes(user.role)) {
    return (
      <div style={{ padding: "4rem", textAlign: "center" }}>
        <h2>⛔ Access Denied</h2>
        <p>You do not have permission to view this page.</p>
      </div>
    );
  }

  return element;
}


function App() {
  return (
    <ThemeProvider>
      <AuthProvider>
        <div className="app">
          <ToastContainer position="top-right" autoClose={2500} />
          <Navbar />

          <main className="main-content">
            <Routes>

              {/* PUBLIC ROUTES */}
              <Route path="/" element={<LandingPage />} />
              <Route path="/pets" element={<PetListing />} />
              <Route path="/pets/:id" element={<PetDetails />} />
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />

              {/* PUBLIC_USER ROUTES */}
              <Route
                path="/my-adopted"
                element={
                  <ProtectedRoute
                    element={<MyAdopted />}
                    roles={["PUBLIC_USER"]}
                  />
                }
              />

              <Route
                path="/my-requests"
                element={
                  <ProtectedRoute
                    element={<MyRequests />}
                    roles={["PUBLIC_USER", "ORG_USER"]}
                  />
                }
              />

                          {/* ORG_USER ROUTES */}
              <Route
                path="/add-pet"
                element={
                  <ProtectedRoute
                    element={<AddPetForm />}
                    roles={["ORG_USER"]}
                  />
                }
              />

              <Route
                path="/my-pets"
                element={
                  <ProtectedRoute
                    element={<MyPets />}
                    roles={["ORG_USER"]}
                  />
                }
              />

              <Route
                path="/shelter-requests"
                element={
                  <ProtectedRoute
                    element={<OrgShelterRequests />}
                    roles={["ORG_USER"]}
                  />
                }
              />


              {/* ADMIN ROUTE */}
              <Route
                path="/admin"
                element={
                  <ProtectedRoute
                    element={<AdminPanel />}
                    roles={["ADMIN"]}
                  />
                }
              />

              {/* 404 PAGE */}
              <Route
                path="*"
                element={
                  <div
                    style={{
                      textAlign: "center",
                      padding: "4rem 2rem",
                      color: "var(--text-secondary, #4a5568)",
                    }}
                  >
                    <h2>🐾 Page Not Found</h2>
                    <p>The page you're looking for doesn't exist.</p>
                  </div>
                }
              />
            </Routes>
          </main>

          <Footer />
        </div>
      </AuthProvider>
    </ThemeProvider>
  );
}

export default App;
