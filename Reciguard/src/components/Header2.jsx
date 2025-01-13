import React from "react";
import "./Header2.css";
import mypage from "../assets/마이페이지.png";
import { Link } from "react-router-dom";

const Header2 = () => {
  return (
    <nav className="navbar2">
      <Link className="logolink" to="/recipes">
        <div className="navbar-logo2">ReciGuard</div>
      </Link>
      <ul className="navbar-menu2">
        <Link to="/recipes/all" style={{ textDecoration: "none" }}>
          <li>분류</li>
        </Link>
        <li>랭킹</li>
        <Link to="/users/recipe-form" style={{ textDecoration: "none" }}>
          <li>레시피 등록</li>
        </Link>
      </ul>
      <div className="rightnav2">
        <input className="navbar-search2" type="text" placeholder="Search..." />
        <img className="mypage-image2" src={mypage} />
        <Link to="/users/{userid}" style={{ textDecoration: "none" }}>
          <h1 className="mypage-letter">마이페이지</h1>
        </Link>
      </div>
    </nav>
  );
};

export default Header2;
