import React, { useState } from "react";
import { Link } from "react-router-dom";
import "./HomeSide.css";
import 나의계정 from "../../assets/나의계정.png";
import 회원정보수정 from "../../assets/회원정보수정.png";
import 알레르기정보수정 from "../../assets/알레르기정보수정.png";

const HomeSide3 = () => {
  return (
    <div className="home-side-container">
      <div className="menu-group11">
        <Link to="/users/{userid}" className="menu-link1">
          <div className="menu-header11">
            <img src={나의계정} alt="나의 계정" className="menu-header-icon" />
            <span>나의 계정</span>
          </div>
        </Link>
        <Link to="/users/info" className="menu-link1">
          <div className="menu-header11">
            <img
              src={회원정보수정}
              alt="회원정보 수정"
              className="menu-header-icon"
            />
            <span>회원정보 수정</span>
          </div>
        </Link>
        <Link to="/users/allergy" className="menu-link1">
          <div className="menu-header11-hover">
            <img
              src={알레르기정보수정}
              alt="알레르기정보 수정"
              className="menu-header-icon"
            />
            <span>알레르기 정보 수정</span>
          </div>
        </Link>
      </div>
    </div>
  );
};

export default HomeSide3;
