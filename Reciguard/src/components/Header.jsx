import React from "react";
import "./Header.css";
import { Link } from "react-router-dom";

const Header = () => {
  return (
    <nav className="navbar">
      <div className="navbar-logo">ReciGuard</div>
      <ul className="navbar-menu">
        <li>분류</li>
        <li>랭킹</li>
        <li>레시피 등록</li>
      </ul>
      <div className="rightnav">
        <input className="navbar-search" type="text" placeholder="Search..." />
        <Link to="/auth/register ">
          <button className="signup-btn">Sign Up</button>
        </Link>
        <Link to="/auth/login ">
          <button className="login-btn">Log In</button>
        </Link>
      </div>
    </nav>
  );
};

export default Header;
