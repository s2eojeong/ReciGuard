import React from "react";
import fork from '../assets/fork.png'
import chicken from '../assets/닭도리탕.jpeg'
import "./Recommend.css";

const Recommend = () => {
    return (
        <div className="recipe-hero">
        <div className="recipe-header">
        <div className='green-section'>
        <div className="recipe-title-container">
            <img className='fork' src={fork}/>
            <h1 className="recipe-title">오늘의 추천 레시피</h1>
            <img className="chicken" id='imagePath' src={chicken} />
        </div>
        <h2 className="recipe-subtitle" id='recipeName'>‘매콤 칼칼한 닭도리탕’</h2>
        <button className="recipe-button">레시피 보기</button>
        </div>
        </div>
        <div className="gray-section">
        <div className="recipe-ingredients">
        <h3 className="ingredients-header">재료</h3>
        <p className="ingredients-content" id='ingredients'>
            닭 1마리, 당근 1/3개, 양파 2/1개, 감자 3개, 대파 4개, 청양고추 4개, 고추장 2큰술, 고춧가루 7큰술, 설탕 2큰술, 국간장 5큰술, 마늘 1큰술, 물 1소주잔
        </p>
        </div>
        </div>
    </div>
    );
};

export default Recommend;