import React, { useState } from "react";
import "./Rightpanel2.css";
import Form1 from "./Form1"; // Form1 컴포넌트 import

function Rightpanel2() {
  const [isPopupOpen, setIsPopupOpen] = useState(false); // 팝업 상태 관리

  const handlePopupOpen = (e) => {
    e.preventDefault(); // 기본 동작 방지
    setIsPopupOpen(true); // 팝업 열기
  };

  const handlePopupClose = () => {
    setIsPopupOpen(false); // 팝업 닫기
  };

  return (
    <div className="right-panel">
      <h2>Create your account</h2>
      <p className="easy">It's easy and free</p>
      <form className="login-form">
        <div className="form-group">
          <label htmlFor="name">이름</label>
          <input type="name" id="name" placeholder="Enter your name" required />
        </div>
        <div className="form-group">
          <label htmlFor="email">E-mail</label>
          <input
            type="email"
            id="email"
            placeholder="Type your e-mail"
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="password">Password</label>
          <div className="input-container">
            <input
              type="password"
              id="password"
              placeholder="Type your password"
              required
            />
          </div>
        </div>
        <div className="checkbox-group">
          <input type="checkbox" className="checkbox-input" required />
          <label className="checkbox-label">
            가입 시 개인정보 수집 및 이용에 동의합니다.
          </label>
        </div>
        {/* 팝업 열기 버튼 */}
        <button type="button" className="sign-up-btn" onClick={handlePopupOpen}>
          Tell us more!
        </button>
      </form>
      <div>
        <p className="log-in">
          Already have an account?
          <a href="/auth/login">Log in</a>
        </p>
      </div>

      {/* 팝업 창 */}
      {isPopupOpen && (
        <div className="popup-overlay">
          <div className="popup-content">
            <button className="close-popup" onClick={handlePopupClose}>
              x
            </button>
            <Form1 /> {/* Form1 컴포넌트 렌더링 */}
          </div>
        </div>
      )}
    </div>
  );
}

export default Rightpanel2;
