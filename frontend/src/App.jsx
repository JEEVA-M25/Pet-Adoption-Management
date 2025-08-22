// App.js
import React from 'react';
import { Routes, Route } from 'react-router-dom';
import { ThemeProvider } from './contexts/ThemeContext';
import Navbar from './components/Navbar';
import Footer from './components/Footer';
import PetListing from './components/PetListing';
import PetDetails from './components/PetDetails';
import LandingPage from './components/LandingPage';
import AddPetForm from './components/AddPetForm';
import './App.css';

function App() {
  return (
    <ThemeProvider>
      <div className="app">
        <Navbar />
        <main className="main-content">
          <Routes>
            <Route path="/" element={<LandingPage />} />
            <Route path="/add-pet" element={<AddPetForm />} />
            <Route path="/pets" element={<PetListing />} />
            <Route path="/pets/:id" element={<PetDetails />} />
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
    </ThemeProvider>
  );
}

export default App;