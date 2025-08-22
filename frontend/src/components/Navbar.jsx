// Navbar.js
import React from 'react';
import { Link } from 'react-router-dom';
import { useTheme } from '../contexts/ThemeContext';
import './Navbar.css';

function Navbar() {
  const { isDark, toggleTheme } = useTheme();
  
  return (
    <nav className="navbar">
      <div className="navbar-brand">ShelterBuddy</div>
      <ul className="navbar-links">
        <li>
          <Link to="/"><button>Home</button></Link>
        </li>
        <li>
          <Link to="/pets"><button>Browse Pets</button></Link>
        </li>
        <li>
          <Link to="/add-pet"><button>Add Pet</button></Link>
        </li>
        <li>
          <button onClick={toggleTheme}>
            {isDark ? '🌙' : '☀️'}
          </button>
        </li>
      </ul>
    </nav>
  );
}

export default Navbar;