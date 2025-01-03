import React from "react";
import "./Header2.css";
import mypage from '../assets/마이페이지.png'
import { Link } from 'react-router-dom';

const Header2 = () => {
    return (
    <nav className="navbar2">
        <Link className="logolink" to="/Realmain">
        <div className="navbar-logo2">ReciGuard</div>
        </Link>
        <ul className="navbar-menu2">
        <li>분류</li>
        <li>랭킹</li>
        <Link to="/Register" style={{ textDecoration: "none" }}>
        <li>레시피 등록</li>
        </Link>
        </ul>
        <div className="rightnav2">
        <input className="navbar-search2" type="text" placeholder="Search..." />
        <img className="mypage-image2" src={mypage} />
        <h1 className='mypage-letter'>마이페이지</h1>       
        </div>
    </nav>
    );
};

export default Header2;
