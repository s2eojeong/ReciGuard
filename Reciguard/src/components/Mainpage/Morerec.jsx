import React from "react";
import {useNavigate} from "react-router-dom";
import 화살표 from "../../assets/Arrow.png"
import 레시피 from "../../assets/morerecips.png"
import 스크랩 from "../../assets/scrapfunc.png"
import 필터링 from "../../assets/warnalle.png"
import 다른유저 from "../../assets/peoples.png"
import 정보수정 from "../../assets/upadateuser.png"
import 스크랩관리 from "../../assets/managescrap.png"
import 레시피관리 from "../../assets/managemy.png"
import "./Morerec.css";

const Morerec = () => {
  const navigate = useNavigate();

  return (
      <div className="mypage-container">
        <h2 className="more-title" onClick={() => navigate("/recipes/all")}>
          더 많은 레시피를 보려면 클릭하세요!
        </h2>
        <div className="mypage-navigation animated-title">
          <div className="track">
            <div className="nav-item">
              <img src={레시피} alt="Recipes" className="nav-icon"/>
              <span>1200여개의 레시피</span>
            </div>
            <div className="nav-item">
              <img src={스크랩} alt="Scrap" className="nav-icon"/>
              <span>스크랩 기능</span>
            </div>
            <div className="nav-item">
              <img src={필터링} alt="Allergy Filter" className="nav-icon"/>
              <span>알레르기 필터링</span>
            </div>
            <div className="nav-item">
              <img src={다른유저} alt="User Tips" className="nav-icon"/>
              <span>다른 유저의 팁</span>
            </div>

            {/* 반복 요소를 추가하여 무한 스크롤처럼 보이게 만듭니다 */}
            <div className="nav-item">
              <img src={레시피} alt="Recipes" className="nav-icon"/>
              <span>1200여개의 레시피</span>
            </div>
            <div className="nav-item">
              <img src={스크랩} alt="Scrap" className="nav-icon"/>
              <span>스크랩 기능</span>
            </div>
            <div className="nav-item">
              <img src={필터링} alt="Allergy Filter" className="nav-icon"/>
              <span>알레르기 필터링</span>
            </div>
            <div className="nav-item">
              <img src={다른유저} alt="User Tips" className="nav-icon"/>
              <span>다른 유저의 팁</span>
            </div>
            <div className="nav-item">
              <img src={레시피} alt="Recipes" className="nav-icon"/>
              <span>1200여개의 레시피</span>
            </div>
            <div className="nav-item">
              <img src={스크랩} alt="Scrap" className="nav-icon"/>
              <span>스크랩 기능</span>
            </div>
            <div className="nav-item">
              <img src={필터링} alt="Allergy Filter" className="nav-icon"/>
              <span>알레르기 필터링</span>
            </div>
            <div className="nav-item">
              <img src={다른유저} alt="User Tips" className="nav-icon"/>
              <span>다른 유저의 팁</span>
            </div>
          </div>
        </div>

        {/* 마이페이지 섹션 */}
        <div className="mypage-section">
          <h2 className="mypage-title">마이페이지</h2>
          <p className="mypage-subtitle">Manage your Account</p>
          <div className="mypage-options">
            <div className="option-card" onClick={() => navigate("/users/:userid")}>
              <img src={정보수정} alt="Edit Info" className="option-icon"/>
              <span>회원 정보 수정</span>
            </div>
            <div className="option-card" onClick={() => navigate("/users/scraps")} >
              <img src={스크랩관리} alt="Scrap Manage" className="option-icon"/>
              <span>스크랩 레시피 관리</span>
            </div>
            <div className="option-card" onClick={() => navigate("/users/myrecipes")}>
              <img src={레시피관리} alt="My Recipe" className="option-icon"/>
              <span>나만의 레시피 관리</span>
            </div>
          </div>
        </div>
      </div>
  );
};

export default Morerec;
