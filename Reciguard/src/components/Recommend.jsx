import React from "react";
import fork from "../assets/fork.png";
import chicken from "../assets/닭도리탕.jpeg";
import "./Recommend.css";

const Recommend = () => {
  return (
    <div className="recommend-container">
      <div className="recommend-tit">
        <img className="fork" src={fork} />
        <h2 className="recommend-title">오늘의 추천 레시피</h2>
      </div>
      <div className="recommend-card">
        <img src={chicken} className="recommend-image" />
        <div className="recommend-content">
          <h3 className="recommend-dish-name">‘매콤 칼칼한 닭도리탕’</h3>
          <button className="recommend-button">레시피 보기</button>
        </div>
      </div>
    </div>
  );
};

export default Recommend;
