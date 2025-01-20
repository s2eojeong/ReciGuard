import React, { useState } from "react";
import "./Regis1.css";
import image from "../../assets/imageupload.png";

const Regis1 = ({ onDataUpdate = () => {} }) => {
  const [recipeName, setRecipeName] = useState("");
  const [serving, setServing] = useState("");
  const [cuisine, setCuisine] = useState("");
  const [foodType, setFoodType] = useState("");
  const [cookingStyle, setCookingStyle] = useState("");
  const [uploadedImage, setUploadedImage] = useState(image); // 기본 이미지 경로

  const handleImageChange = (event) => {
    const file = event.target.files[0];
    if (file) {
      setUploadedImage(URL.createObjectURL(file)); // 미리보기 설정
      onDataUpdate({ recipeImage: file }); // File 객체로 상위 전달
      console.log("이미지 파일 업로드:", file); // 디버깅 로그 추가
    }
  };

  const handleChange = (field, value) => {
    onDataUpdate({ [field]: value }); // 다른 필드 업데이트
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
                value={recipeName}
                className="regis-input"
                placeholder="예) 돼지고기 김치구이"
                onChange={(e) => {
                  setRecipeName(e.target.value);
                  handleChange("recipeName", e.target.value);
                }}
              />
            </div>
            <div className="regis-group">
              <label htmlFor="category" className="regis-label">
                카테고리
              </label>
              <select
                id="cuisine"
                className="regis-select"
                value={cuisine}
                onChange={(e) => {
                  setCuisine(e.target.value);
                  handleChange("cuisine", e.target.value);
                }}
              >
                <option value="" disabled>
                  국가별
                </option>
                <option value="한식">한식</option>
                <option value="중식">중식</option>
                <option value="일식">일식</option>
                <option value="양식">양식</option>
                <option value="아시안">아시안</option>
              </select>
              <select
                id="foodType"
                className="regis-select"
                value={foodType}
                onChange={(e) => {
                  setFoodType(e.target.value);
                  handleChange("foodType", e.target.value);
                }}
              >
                <option value="" disabled>
                  식사 유형
                </option>
                <option value="밥">밥</option>
                <option value="면">면</option>
                <option value="떡">떡</option>
                <option value="죽">죽</option>
                <option value="반찬">반찬</option>
              </select>
              <select
                id="cookingStyle"
                className="regis-select"
                value={cookingStyle}
                onChange={(e) => {
                  setCookingStyle(e.target.value);
                  handleChange("cookingStyle", e.target.value);
                }}
              >
                <option value="" disabled>
                  조리 방식
                </option>
                <option value="찌기">찌기</option>
                <option value="끓이기">끓이기</option>
                <option value="굽기">굽기</option>
                <option value="볶기">볶기</option>
                <option value="튀기기">튀기기</option>
                <option value="기타">기타</option>
              </select>
            </div>
            <div className="regis-group">
              <label className="regis-label">요리정보</label>
              <div className="regis-info">
                <select
                  className="regis-select small"
                  value={serving}
                  onChange={(e) => {
                    setServing(e.target.value);
                    handleChange("serving", e.target.value);
                  }}
                >
                  <option value="" disabled>
                    인분
                  </option>
                  <option value="1">1인분</option>
                  <option value="2">2인분</option>
                  <option value="3">3인분</option>
                  <option value="4">4인분</option>
                  <option value="5">5인분</option>
                  <option value="6">6인분</option>
                </select>
              </div>
            </div>
          </div>
          <div className="regis-right">
            <div
              className="regis-image-upload"
              onClick={() => document.getElementById("imageInput").click()}
            >
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

export default Regis1;
