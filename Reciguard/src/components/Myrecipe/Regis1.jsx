import React from "react";
import "./Regis1.css";
import { useState, useRef } from "react";
import image from "../../assets/이미지업로드.png";

const Regis = () => {
  const [uploadedImage, setuploadedImage] = useState(image); // 기본 이미지 경로

  const handleImageChange = (event) => {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = () => {
        setuploadedImage(reader.result); // 업로드된 이미지 미리보기
      };
      reader.readAsDataURL(file);
    }
  };

  const handleImageClick = () => {
    document.getElementById("imageInput").click(); // 숨겨진 input 태그 클릭
  };

  return (
    <div className="regis-wrapper">
      <div className="regis-container">
        <div className="regis-header">레시피 소개</div>
        <form className="regis-form">
          <div className="regis-left">
            <div className="regis-group">
              <label htmlFor="recipe-title" className="regis-label">
                레시피명
              </label>
              <input
                type="text"
                id="recipe-title"
                className="regis-input"
                placeholder="예) 돼지고기 김치구이"
              />
            </div>
            <div className="regis-group">
              <label htmlFor="category" className="regis-label">
                카테고리
              </label>
              <select id="category" className="regis-select">
                <option value="" disabled selected>
                  국가별
                </option>
                <option value="korean">한식</option>
                <option value="chinese">중식</option>
                <option value="japanese">일식</option>
                <option value="western">양식</option>
                <option value="asian">아시안</option>
              </select>
              <select id="category" className="regis-select">
                <option value="" disabled selected>
                  식사 유형
                </option>
                <option value="korean">밥</option>
                <option value="chinese">면</option>
                <option value="japanese">떡</option>
                <option value="western">죽</option>
                <option value="asian">반찬</option>
              </select>
              <select id="category" className="regis-select">
                <option value="" disabled selected>
                  조리 방식
                </option>
                <option value="korean">찌기</option>
                <option value="chinese">끓이기</option>
                <option value="japanese">굽기</option>
                <option value="western">볶기</option>
                <option value="asian">튀기기</option>
                <option value="asian">기타</option>
              </select>
            </div>
            <div className="regis-group">
              <label className="regis-label">요리정보</label>
              <div className="regis-info">
                <select className="regis-select small">
                  <option value="" disabled selected>
                    인분
                  </option>
                  <option value="1">1인분</option>
                  <option value="2">2인분</option>
                  <option value="3">3인분</option>
                  <option value="3">4인분</option>
                  <option value="3">5인분</option>
                  <option value="3">6인분</option>
                </select>
              </div>
            </div>
          </div>
          <div className="regis-right">
            <div className="regis-image-upload" onClick={handleImageClick}>
              <img className="image-upload" src={uploadedImage} alt="Upload" />
              <input
                id="imageInput"
                type="file"
                accept="image/*"
                style={{ display: "none" }}
                onChange={handleImageChange}
              />
            </div>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Regis;
