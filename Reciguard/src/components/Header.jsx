import React from "react";
import "./Header.css";

const Header = () => {
    return (
    <nav className="navbar">
        <div className="navbar-logo">ReciGuard</div>
        <ul className="navbar-menu">
        <li>분류</li>
        <li>랭킹</li>
        <li>레시피 등록</li>
        </ul>
        <div className="navbar-actions">
        <input type="text" placeholder="Search..." className="navbar-search" />
        <button className="signup-btn">Sign Up</button>
        <button className="login-btn">Log In</button>
        </div>
    </nav>
    );
};

export default Header;
