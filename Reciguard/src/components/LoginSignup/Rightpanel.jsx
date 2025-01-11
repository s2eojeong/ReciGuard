import React, { useState } from "react";
import "./Rightpanel.css";
import { Link, useNavigate } from "react-router-dom";

function Rightpanel() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState(""); // 에러 메시지 상태
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault(); // 기본 폼 동작 방지

    try {
      const response = await fetch("http://localhost:8080/api/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          email: email,
          password: password,
        }),
      });

      const data = await response.json();

      if (response.ok) {
        // 성공적으로 로그인한 경우
        console.log("로그인 성공:", data.message);

        // JWT 토큰 저장 (예: Local Storage)
        const token = data.token; // 백엔드에서 반환하는 토큰 키 이름에 따라 수정
        localStorage.setItem("jwtToken", token);

        // 메인 페이지로 이동
        navigate("/recipes");
      } else {
        // 로그인 실패 시
        console.error("로그인 실패:", data.message);
        setErrorMessage(data.message); // 에러 메시지 표시
      }
    } catch (error) {
      console.error("서버 요청 중 오류 발생:", error);
      setErrorMessage("이메일 또는 비밀번호를 다시 확인해 주세요.");
    }
  };

  return (
    <div className="right-panel">
      <h2>Welcome!</h2>
      <p className="meet">Meet the good taste today</p>
      <form className="login-form" onSubmit={handleLogin}>
        <div className="form-group">
          <label htmlFor="email">E-mail</label>
          <input
            type="email"
            id="email"
            placeholder="Type your e-mail"
            required
            value={email}
            onChange={(e) => setEmail(e.target.value)} // 상태 업데이트
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
              value={password}
              onChange={(e) => setPassword(e.target.value)} // 상태 업데이트
            />
          </div>
        </div>
        <button type="submit" className="log-in-btn">
          Log In
        </button>
      </form>
      {errorMessage && <p className="error-message">{errorMessage}</p>}{" "}
      {/* 에러 메시지 표시 */}
      <div>
        <p className="sign-up">
          Don’t have an account?{" "}
          <Link to="/auth/register">
            <a href="#">Sign Up</a>
          </Link>
        </p>
      </div>
    </div>
  );
}

export default Rightpanel;
