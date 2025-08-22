// App.js
import React from 'react';
import { Routes, Route } from 'react-router-dom';
import { ThemeProvider } from './contexts/ThemeContext';
import { AuthProvider } from './contexts/AuthContext';
import Navbar from './components/Navbar';
import Footer from './components/Footer';
import PetListing from './components/PetListing';
import PetDetails from './components/PetDetails';
import LandingPage from './components/LandingPage';
import AddPetForm from './components/AddPetForm';
import Login from './components/Login';
import Register from './components/Register';
import MyRequests from './components/MyRequests';
import MyPets from './components/MyPets';
import AdminPanel from './components/AdminPanel';
import './App.css';

function App() {
  return (
    <ThemeProvider>
      <AuthProvider>
        <div className="app">
          <Navbar />
          <main className="main-content">
            <Routes>
              <Route path="/" element={<LandingPage />} />
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              <Route path="/add-pet" element={<AddPetForm />} />
              <Route path="/pets" element={<PetListing />} />
              <Route path="/pets/:id" element={<PetDetails />} />
              <Route path="/my-requests" element={<MyRequests />} />
              <Route path="/my-pets" element={<MyPets />} />
              <Route path="/admin" element={<AdminPanel />} />
              <Route path="*" element={
                <div style={{
                  textAlign: 'center',
                  padding: '4rem 2rem',
                  color: 'var(--text-secondary, #4a5568)'
                }}>
                  <h2>🐾 Page Not Found</h2>
                  <p>The page you're looking for doesn't exist.</p>
                </div>
              } />
            </Routes>
          </main>
          <Footer />
        </div>
      </AuthProvider>
    </ThemeProvider>
  );
}

export default App;