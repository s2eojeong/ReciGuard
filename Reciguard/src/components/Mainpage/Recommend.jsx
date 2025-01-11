import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import fork from "../../assets/fork.png";
import chicken from "../../assets/닭도리탕.jpeg";
import "./Recommend.css";

const Recommend = () => {
  const [showTitle, setShowTitle] = useState(false);
  const [showCard, setShowCard] = useState(false);

  useEffect(() => {
    // 제목이 0.5초 후 나타나고 카드가 1초 후 나타남
    const titleTimeout = setTimeout(() => setShowTitle(true), 300);
    const cardTimeout = setTimeout(() => setShowCard(true), 300);

    return () => {
      clearTimeout(titleTimeout);
      clearTimeout(cardTimeout);
    };
  }, []);

  return (
    <div className="recommend-container">
      <div className={`recommend-tit ${showTitle ? "show" : ""}`}>
        <img className="fork" src={fork} alt="fork" />
        <h2 className="recommend-title">오늘의 추천 레시피</h2>
      </div>
      <div className={`recommend-card ${showCard ? "show" : ""}`}>
        <img src={chicken} className="recommend-image" alt="닭도리탕" />
        <div className="recommend-content">
          <h3 className="recommend-dish-name">‘매콤 칼칼한 닭도리탕’</h3>
          <Link to="/recipes/detail">
            <button className="recommend-button">레시피 보기</button>
          </Link>
        </div>
      </div>
    </div>
  );
};

export default Recommend;
