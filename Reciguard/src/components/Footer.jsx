import React from "react";
import "./Footer.css";

const Footer = () => {
    return (
        <footer className="footer">
        <div className="footer-left">
        <p>&copy; 2024 Tave team7. All Rights Reserved.</p>
        </div>
        <div className="footer-center">
        <span className="footer-logo">
            <span style={{ color: "#EDBCBE", fontWeight: "bold" }}>Reci</span>
            <span style={{ color: "#3E5944", fontWeight: "bold" }}>Guard</span>
        </span>
        </div>
        <div className="footer-right">
        <a href="mailto:contact@reciguard.com" className="footer-link">
            contact us
        </a>
        <span className="footer-icon">
            <i className="fa fa-instagram" /> tave_wave
        </span>
        </div>
    </footer>
    );
};

export default Footer;
