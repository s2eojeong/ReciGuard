import React from "react";
import "./Hero.css";

const Hero = () => {
return (
    <div className="hero-container">
        <h1 className="hero-title">ReciGuard</h1>
        <p className="hero-subtitle">알레르기를 가진 당신에게 안전한 레시피를 제공합니다</p>
        <button className="hero-button">Get Started</button>
    </div>
    );
};

export default Hero;
