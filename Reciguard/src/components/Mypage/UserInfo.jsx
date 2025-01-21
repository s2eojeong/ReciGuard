import React, { useEffect, useState } from "react";
import axios from "axios";
import "./UserInfo.css";

const UserInfo = () => {
  const [userData, setUserData] = useState(null); // 초기 상태 null
  const [error, setError] = useState(null);

  // JWT 토큰에서 username과 userid를 추출하는 함수
  const getUserInfoFromToken = (token) => {
    try {
      const payload = JSON.parse(atob(token.split(".")[1])); // JWT payload 디코딩
      return { username: payload.username, userid: payload.userid };
    } catch (err) {
      console.error("Invalid token", err);
      return null;
    }
  };

  useEffect(() => {
    const token = localStorage.getItem("jwtToken"); // 로컬 스토리지에서 JWT 토큰 가져오기
    if (!token) {
      setError("로그인이 필요합니다.");
      return;
    }

    const userInfo = getUserInfoFromToken(token);

    if (userInfo) {
      const { userid } = userInfo;
      axios
        .get(`https://reciguard.com/api/users/${userid}`, {
          headers: {
            Authorization: `Bearer ${token}`, // 인증 헤더에 토큰 포함
          },
        })
        .then((response) => {
          console.log("응답 데이터:", response.data);
          setUserData(response.data); // 데이터 설정
        })
        .catch((err) => {
          setError("사용자 정보를 불러오지 못했습니다.");
        });
    } else {
      setError("잘못된 토큰입니다.");
    }
  }, []);

  if (error) return <p>{error}</p>;

  // 데이터가 로드되지 않았을 경우 기본 UI 처리
  if (!userData) {
    return <div className="userinfo-container"></div>;
  }

  return (
    <div className="userinfo-container">
      <h1 className="userinfo-h1">나의 계정</h1>
      <div className="userinfo-container1">
        <div className="userinfo-item">
          <label>아이디</label>
          <span>{userData.username}</span>
        </div>
        <div className="userinfo-item2">
          <label>성별</label>
          <span>{userData.gender}</span>
        </div>
      </div>
      <div className="userinfo-container1">
        <div className="userinfo-item">
          <label>이메일</label>
          <span>{userData.email}</span>
        </div>
        <div className="userinfo-item2">
          <label>나이</label>
          <span>{userData.age}년생</span>
        </div>
      </div>
      <div className="userinfo-container1">
        <div className="userinfo-item">
          <label>비밀번호</label>
          <span>********</span>
        </div>
        <div className="userinfo-item2">
          <label>체중</label>
          <span>{userData.weight}kg</span>
        </div>
      </div>
    </div>
  );
};

export default UserInfo;
