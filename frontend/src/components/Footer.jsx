// Footer.js
import React from 'react';
import { Link } from 'react-router-dom';
import './Footer.css';

function Footer() {
  return (
    <footer className="footer">
      <div className="footer-content">
        <div className="footer-section">
          <div className="footer-brand">
            <h3>ShelterBuddy</h3>
            <p>Connecting loving pets with caring families since 2024</p>
          </div>
        </div>

        <div className="footer-section">
          <h4>Quick Links</h4>
          <ul className="footer-links">
            <li><Link to="/">Home</Link></li>
            <li><Link to="/pets">Browse Pets</Link></li>
            <li><Link to="/add-pet">Add Pet</Link></li>
          </ul>
        </div>

        <div className="footer-section">
          <h4>About</h4>
          <ul className="footer-links">
            <li><a href="#about">About Us</a></li>
            <li><a href="#contact">Contact</a></li>
            <li><a href="#help">Help Center</a></li>
          </ul>
        </div>

        <div className="footer-section">
          <h4>Connect</h4>
          <div className="social-links">
            <a href="#facebook" className="social-link">📘</a>
            <a href="#twitter" className="social-link">🐦</a>
            <a href="#instagram" className="social-link">📷</a>
            <a href="#email" className="social-link">📧</a>
          </div>
          <p className="footer-email">help@shelterbuddy.com</p>
        </div>
      </div>

      <div className="footer-bottom">
        <div className="footer-bottom-content">
          <p>&copy; 2024 ShelterBuddy. All rights reserved.</p>
          <div className="footer-bottom-links">
            <a href="#privacy">Privacy Policy</a>
            <a href="#terms">Terms of Service</a>
          </div>
        </div>
      </div>
    </footer>
  );
}

export default Footer;