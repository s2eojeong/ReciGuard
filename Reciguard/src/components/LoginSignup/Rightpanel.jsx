import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "./Rightpanel.css";

function Rightpanel() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch("http://localhost:8080/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          username: username,
          password: password,
        }),
      });

      if (!response.ok) {
        const errorData = await response.json();
        setErrorMessage(errorData.error || "로그인 실패");
        return;
      }

      // JWT 토큰 가져오기
      const token = response.headers.get("Authorization")?.split(" ")[1];
      if (token) {
        localStorage.setItem("jwtToken", token); // 토큰 저장
        console.log("JWT 토큰 저장:", token);
        console.log(localStorage.getItem("jwtToken")); // 저장된 JWT 토큰 출력
        navigate("/recipes"); // 로그인 후 보호된 페이지로 이동
      } else {
        setErrorMessage("토큰을 가져오지 못했습니다.");
      }
    } catch (error) {
      console.error("서버 요청 중 오류 발생:", error);
      setErrorMessage("서버에 문제가 발생했습니다. 다시 시도해 주세요.");
    }
  };

  return (
      <div className="right-panel">
        <h2>Welcome!</h2>
        <p className="meet">Meet the good taste today</p>
        <form className="login-form" onSubmit={handleLogin}>
          <div className="form-group">
            <label htmlFor="username">이름</label>
            <input
                type="text"
                id="username"
                placeholder="Type your name"
                required
                value={username}
                onChange={(e) => setUsername(e.target.value)}
            />
          </div>
          <div className="form-group">
            <label htmlFor="password">비밀번호</label>
            <input
                type="password"
                id="password"
                placeholder="Type your password"
                required
                value={password}
                onChange={(e) => setPassword(e.target.value)}
            />
          </div>
          <button type="submit" className="log-in-btn">
            Log In
          </button>
        </form>
        {errorMessage && <p className="error-message">{errorMessage}</p>}
        <div>
          <p className="sign-up">
            Don’t have an account? <Link to="/auth/register">Sign Up</Link>
          </p>
        </div>
      </div>
  );
}

export default Rightpanel;
