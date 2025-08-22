// src/components/LandingPage.js
import React from "react";
import { Link } from "react-router-dom";
import "./LandingPage.css";
import banimg from "../assets/banner.jpg"; 
const LandingPage = () => {
  return (
    <div className="landing-page">
      {/* Banner Section */}
      <div className="landing-banner">
        <img
          src={banimg}
          alt="Adopt a Pet"
          className="banner-img"
        />
        <div className="banner-text">
          <h1>Find Your New Best Friend</h1>
          <p>Adopt a loving pet and give them a forever home</p>
          <Link to="/pets" className="cta-button">
            Browse Pets
          </Link>
        </div>
      </div>

      {/* Issue Awareness Section */}
      <div className="landing-issues">
        <h2>Stray Dogs Need Us — Now More Than Ever</h2>
        <p className="issue-intro">
          With recent <span className="supreme">Supreme court orders</span> and rising challenges, thousands of dogs face
          uncertain futures. <strong>Your adoption can make a difference.</strong>
        </p>

        <div className="issue-cards">
          <div className="issue-card">
            <img src="https://media.4-paws.org/6/3/1/f/631f8b7e6e4772c6350b660a9b871be32b51212f/VIER%20PFOTEN_2016-09-18_081-1927x1333-1920x1328.webp" alt="Overcrowded Shelters" />
            <h3>Overcrowded Shelters</h3>
            <p>
              Shelters are overwhelmed after the Supreme Court’s order.
              Adopting even one dog reduces the burden and saves a life.
            </p>
          </div>

          <div className="issue-card">
            <img src="https://images.pexels.com/photos/32687834/pexels-photo-32687834.jpeg" alt="Stray Dog on Street" />
            <h3>Strays in Danger</h3>
            <p>
              Thousands of community dogs face relocation. By adopting, you give
              them safety, love, and a real home.
            </p>
          </div>

          <div className="issue-card">
            <img src="https://images.pexels.com/photos/28558640/pexels-photo-28558640.jpeg" alt="Happy Adopted Dog" />
            <h3>Be Their Hope</h3>
            <p>
              Every adoption is a stand against cruelty and abandonment.
              Together, we can reduce stray suffering across our cities.
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LandingPage;
