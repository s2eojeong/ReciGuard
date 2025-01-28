import React from "react";
import "./Hero.css";
import { Link } from "react-router-dom";

const Hero = () => {
  return (
    <div className="hero-container">
      <h1 className="hero-title">ReciGuard</h1>
      <p className="hero-subtitle">
        알레르기를 가진 당신에게 안전한 레시피를 제공합니다
      </p>
      <Link to="/login">
        <button className="hero-button">Get Started</button>
      </Link>
    </div>
  );
};

export default Hero;
