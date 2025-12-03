// components/Navbar.js
import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useTheme } from '../contexts/ThemeContext';
import { useAuth } from '../contexts/AuthContext';
import './Navbar.css';

function Navbar() {
  const { isDark, toggleTheme } = useTheme();
  const { user, logout } = useAuth();
  console.debug('Navbar user.role =', user?.role);
  const navigate = useNavigate();
  const [showMenu, setShowMenu] = useState(false);

  const handleLogout = () => {
    logout();
    navigate('/');
    setShowMenu(false);
  };

  const handleProfileClick = () => {
    setShowMenu(!showMenu);
  };

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

        {/* ORG USER EXCLUSIVE */}
        {user && user.role === 'ORG_USER' && (
          <li>
            <Link to="/add-pet"><button>Add Pet</button></Link>
          </li>
        )}

        {/* ADMIN EXCLUSIVE */}
        {user && user.role === 'ADMIN' && (
          <li>
            <Link to="/admin"><button>Admin Panel</button></Link>
          </li>
        )}

        {/* THEME TOGGLE */}
        <li>
          <button onClick={toggleTheme}>
            {isDark ? '🌙' : '☀️'}
          </button>
        </li>

        {/* PROFILE MENU */}
        {user ? (
          <li className="profile-menu-container">
            <button onClick={handleProfileClick} className="profile-btn">
              👤 Profile
            </button>

            {showMenu && (
              <div className="profile-menu">
                <div className="profile-info">
                  <p>Logged in as: {user.email}</p>
                  <p>Role: {user.role}</p>
                </div>

                <hr />

                {/* PUBLIC USER OPTIONS */}
                {user.role === "PUBLIC_USER" && (
                  <>
                    <Link to="/my-requests" onClick={() => setShowMenu(false)}>
                      My Adoption Requests
                    </Link>

                    <Link to="/my-adopted" onClick={() => setShowMenu(false)}>
                      My Adopted Pets
                    </Link>
                  </>
                )}

                {/* ORG USER OPTIONS */}
                {user.role === "ORG_USER" && (
                  <>
                    <Link to="/my-pets" onClick={() => setShowMenu(false)}>
                      My Pets
                    </Link>
                    <Link to="/my-requests" onClick={() => setShowMenu(false)}>
                      Adoption Requests (My Shelter)
                    </Link>
                  </>
                )}

                <hr />

                <button onClick={handleLogout} className="logout-btn">
                  Logout
                </button>
              </div>
            )}
          </li>
        ) : (
          <>
            <li>
              <Link to="/login"><button>Login</button></Link>
            </li>
            <li>
              <Link to="/register"><button>Register</button></Link>
            </li>
          </>
        )}
      </ul>
    </nav>
  );
}

export default Navbar;
