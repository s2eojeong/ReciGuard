import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "./Sidebar1.css";
import 회원정보 from "../../assets/회원정보.png";
import 회원정보호버 from "../../assets/회원정보호버.png";
import 스크랩 from "../../assets/스크랩.png";
import 스크랩호버 from "../../assets/스크랩호버.png";
import 마이레시피 from "../../assets/마이레시피.png";
import 마이레시피호버 from "../../assets/마이레시피호버.png";
import 로그아웃 from "../../assets/로그아웃.png";
import 회원탈퇴 from "../../assets/회원탈퇴.png";

const Sidebar3 = () => {
  const [hoveredItem, setHoveredItem] = useState(null);
  const navigate = useNavigate();

  // JWT 토큰에서 사용자 정보를 추출하는 함수
  const getUserInfoFromToken = (token) => {
    try {
      const payload = JSON.parse(atob(token.split(".")[1])); // JWT payload 디코딩
      return { username: payload.username, userid: payload.userid };
    } catch (err) {
      console.error("Invalid token", err);
      return null;
    }
  };

  // 로그아웃 처리
  const handleLogout = () => {
    localStorage.removeItem("jwtToken"); // JWT 토큰 삭제
    navigate("/"); // 메인 페이지로 이동
  };

  // 회원탈퇴 처리
  const handleDeleteAccount = async () => {
    const token = localStorage.getItem("jwtToken"); // JWT 토큰 가져오기
    if (!token) {
      alert("로그인이 필요합니다.");
      navigate("/login");
      return;
    }

    const userInfo = getUserInfoFromToken(token); // 토큰에서 사용자 정보 추출
    if (!userInfo) {
      alert("유효하지 않은 토큰입니다.");
      navigate("/login");
      return;
    }

    const { userid } = userInfo; // 사용자 ID 추출
    const confirmed = window.confirm("정말로 회원 탈퇴하시겠습니까?");
    if (!confirmed) return;

    try {
      const response = await fetch(
        `https://reciguard.com/api/users/${userid}`,
        {
          method: "DELETE",
          headers: {
            Authorization: `Bearer ${token}`, // 인증 헤더 추가
            "Content-Type": "application/json",
          },
        }
      );

      if (response.ok) {
        alert("회원 탈퇴가 완료되었습니다.");
        localStorage.removeItem("jwtToken"); // 탈퇴 후 JWT 토큰 삭제
        navigate("/"); // 메인 페이지로 이동
      } else {
        alert("회원 탈퇴에 실패했습니다. 다시 시도해주세요.");
      }
    } catch (error) {
      console.error("Error during account deletion:", error);
      alert("오류가 발생했습니다. 다시 시도해주세요.");
    }
  };

  const token = localStorage.getItem("jwtToken"); // JWT 토큰 가져오기
  const userInfo = token ? getUserInfoFromToken(token) : null;
  const userId = userInfo ? userInfo.userid : "unknown"; // 유효한 토큰에서 사용자 ID 추출

  return (
    <div className="sidebar-container">
      <div className="menu-group">
        <div
          className="menu-item"
          onMouseEnter={() => setHoveredItem("회원정보")}
          onMouseLeave={() => setHoveredItem(null)}
        >
          <Link to={`/users/${userId}`} className="menu-link">
            {" "}
            {/* 사용자 ID 반영 */}
            <img
              src={hoveredItem === "회원정보" ? 회원정보호버 : 회원정보}
              alt="회원정보"
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
        <div className="menu-item-hover">
          <Link to="/users/myrecipes" className="menu-link">
            <img
              src={마이레시피호버} // 항상 호버 이미지를 사용
              alt="마이레시피"
              className="menu-icon"
            />
            <span>My 레시피</span>
          </Link>
        </div>
      </div>

      <div className="menu-footer">
        <div className="menu-item2" onClick={handleLogout}>
          <img src={로그아웃} alt="로그아웃" className="menu-icon2" />
          <span>로그아웃</span>
        </div>
        <div className="menu-item2" onClick={handleDeleteAccount}>
          <img src={회원탈퇴} alt="회원탈퇴" className="menu-icon2" />
          <span>회원탈퇴</span>
        </div>
      </div>
    </div>
  );
};

export default Sidebar3;
