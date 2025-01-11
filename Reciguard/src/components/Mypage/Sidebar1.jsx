import React, { useState } from "react";
import { Link } from "react-router-dom";
import "./Sidebar1.css";
import 회원정보 from "../../assets/회원정보.png";
import 회원정보호버 from "../../assets/회원정보호버.png";
import 스크랩 from "../../assets/스크랩.png";
import 스크랩호버 from "../../assets/스크랩호버.png";
import 마이레시피 from "../../assets/마이레시피.png";
import 마이레시피호버 from "../../assets/마이레시피호버.png";
import 로그아웃 from "../../assets/로그아웃.png";
import 회원탈퇴 from "../../assets/회원탈퇴.png";

const Sidebar1 = () => {
  const [hoveredItem, setHoveredItem] = useState(null);

  return (
    <div className="sidebar-container">
      <div className="menu-group">
        <div className="menu-item-hover">
          <Link to="/users/{userid}" className="menu-link">
            <img
              src={회원정보호버} // 항상 호버 이미지를 사용
              alt="회원 정보"
              className="menu-icon"
            />
            <span>회원 정보</span>
          </Link>
        </div>
        <div
          className="menu-item"
          onMouseEnter={() => setHoveredItem("스크랩")}
          onMouseLeave={() => setHoveredItem(null)}
        >
          <Link to="/users/scraps" className="menu-link">
            <img
              src={hoveredItem === "스크랩" ? 스크랩호버 : 스크랩}
              alt="스크랩"
              className="menu-icon"
            />
            <span>스크랩 레시피</span>
          </Link>
        </div>
        <div
          className="menu-item"
          onMouseEnter={() => setHoveredItem("마이레시피")}
          onMouseLeave={() => setHoveredItem(null)}
        >
          <Link to="/users/myrecipes" className="menu-link">
            <img
              src={hoveredItem === "마이레시피" ? 마이레시피호버 : 마이레시피}
              alt="My 레시피"
              className="menu-icon"
            />
            <span>My 레시피</span>
          </Link>
        </div>
      </div>

      <div className="menu-footer">
        <div className="menu-item2">
          <img src={로그아웃} alt="로그아웃" className="menu-icon2" />
          <span>로그아웃</span>
        </div>
        <div className="menu-item2">
          <img src={회원탈퇴} alt="회원탈퇴" className="menu-icon2" />
          <span>회원탈퇴</span>
        </div>
      </div>
    </div>
  );
};

export default Sidebar1;
