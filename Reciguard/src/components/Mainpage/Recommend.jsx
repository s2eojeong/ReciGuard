import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import fork from "../../assets/fork.png";
import "./Recommend.css";

const Recommend = () => {
  const [recommendData, setRecommendData] = useState(null); // 추천 레시피 데이터
  const [loading, setLoading] = useState(true); // 로딩 상태
  const [error, setError] = useState(null); // 에러 상태

  useEffect(() => {
    const token = localStorage.getItem("jwtToken");
    const fetchRecommendRecipe = async () => {
      try {
        const response = await fetch(`http://localhost:8080/api/recipes/recommend`, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`,
          },
        });
        if (!response.ok) {
          throw new Error(`HTTP 에러 상태: ${response.status}`);
        }
        const data = await response.json();
        setRecommendData(data); // 추천 데이터 설정
      } catch (error) {
        setError(error.message); // 에러 상태 설정
      } finally {
        setLoading(false); // 로딩 상태 해제
      }
    };

    fetchRecommendRecipe();
  }, []);

  if (loading) {
    return <div className="recommend-container">로딩 중...</div>;
  }

  if (error) {
    return <div className="recommend-container">에러 발생: {error}</div>;
  }

  if (!recommendData) {
    return <div className="recommend-container">추천 레시피가 없습니다.</div>;
  }

  const { imagePath, recipeName, serving } = recommendData;

  return (
      <div className="recommend-container">
        <div className="recommend-tit show">
          <img className="fork" src={fork} alt="fork" />
          <h2 className="recommend-title">오늘의 추천 레시피</h2>
        </div>
        <div className="recommend-card show">
          <img
              src={imagePath}
              className="recommend-image"
              alt={recipeName}
          />
          <div className="recommend-content">
            <h3 className="recommend-dish-name">{recipeName}</h3>
            <p className="recommend-serving">인분: {serving}인분</p>
            <Link to="/recipes/detail">
              <button className="recommend-button">레시피 보기</button>
            </Link>
          </div>
        </div>
      </div>
  );
};

export default Recommend;
