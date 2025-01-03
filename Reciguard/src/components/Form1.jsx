import React, { useState } from "react";
import "./Form1.css";

const Form1 = () => {
  const [preferences, setPreferences] = useState({});

  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target;
    setPreferences((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const handleGenderChange = (e) => {
    setPreferences((prev) => ({
      ...prev,
      gender: e.target.value,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log(preferences);
  };

  return (
    <div className="form-page">
      <form className="reciguard-form" onSubmit={handleSubmit}>
        <h1>ReciGaurd</h1>

        {/* Gender */}
        <div className="group">
          <label>성별</label>
          <div className="gender-options">
            <label>
              <input
                type="radio"
                name="gender"
                value="male"
                onChange={handleGenderChange}
              />{" "}
              남자
            </label>
            <label>
              <input
                type="radio"
                name="gender"
                value="female"
                onChange={handleGenderChange}
              />{" "}
              여자
            </label>
          </div>
        </div>

        {/* Age */}
        <div className="group">
          <label>나이</label>
          <div className="year">
            <select id="age" onChange={handleInputChange} defaultValue="">
              <option value="" disabled></option>
              {Array.from({ length: 116 }, (_, i) => 1910 + i).map((year) => (
                <option key={year} value={year}>
                  {year}
                </option>
              ))}
            </select>
            년생
          </div>
        </div>

        {/* Weight */}
        <div className="group">
          <label>체중</label>
          <div className="kg">
            <input
              className="weight"
              type="number"
              id="weight"
              step="0.1"
              onChange={handleInputChange}
            />{" "}
            kg
          </div>
        </div>

        {/* Preferences */}
        <fieldset className="preferences-section">
          <legend>
            <span className="pre">선호도</span>좋아하는 종류를 선택해주세요.
            (복수 선택 가능)
          </legend>
          <div className="preferences-grid">
            <div>
              <label className="type">국가별</label>
              <div className="country">
                <label className="category-label">
                  <input
                    type="checkbox"
                    name="country"
                    value="한식"
                    onChange={handleInputChange}
                  />{" "}
                  한식
                </label>
                <label className="category-label">
                  <input
                    type="checkbox"
                    name="country"
                    value="중식"
                    onChange={handleInputChange}
                  />{" "}
                  중식
                </label>
                <label className="category-label">
                  <input
                    type="checkbox"
                    name="country"
                    value="일식"
                    onChange={handleInputChange}
                  />{" "}
                  일식
                </label>
                <label className="category-label">
                  <input
                    type="checkbox"
                    name="country"
                    value="양식"
                    onChange={handleInputChange}
                  />{" "}
                  양식
                </label>
                <label className="category-label">
                  <input
                    type="checkbox"
                    name="country"
                    value="아시안"
                    onChange={handleInputChange}
                  />{" "}
                  아시안
                </label>
              </div>
            </div>
            <div>
              <label className="type">식사 유형</label>
              <div>
                <label className="category-label">
                  <input
                    type="checkbox"
                    name="mealType"
                    value="밥"
                    onChange={handleInputChange}
                  />{" "}
                  밥
                </label>
                <label className="category-label">
                  <input
                    type="checkbox"
                    name="mealType"
                    value="면"
                    onChange={handleInputChange}
                  />{" "}
                  면
                </label>
                <label className="category-label">
                  <input
                    type="checkbox"
                    name="mealType"
                    value="떡"
                    onChange={handleInputChange}
                  />{" "}
                  떡
                </label>
                <label className="category-label">
                  <input
                    type="checkbox"
                    name="mealType"
                    value="죽"
                    onChange={handleInputChange}
                  />{" "}
                  죽
                </label>
                <label className="category-label">
                  <input
                    type="checkbox"
                    name="mealType"
                    value="반찬"
                    onChange={handleInputChange}
                  />{" "}
                  반찬
                </label>
              </div>
            </div>
            <div>
              <label className="type">조리 방식</label>
              <div>
                <label className="category-label">
                  <input
                    type="checkbox"
                    name="cookingStyle"
                    value="구이"
                    onChange={handleInputChange}
                  />{" "}
                  구이
                </label>
                <label className="category-label">
                  <input
                    type="checkbox"
                    name="cookingStyle"
                    value="볶음"
                    onChange={handleInputChange}
                  />{" "}
                  볶음
                </label>
                <label className="category-label">
                  <input
                    type="checkbox"
                    name="cookingStyle"
                    value="튀김"
                    onChange={handleInputChange}
                  />{" "}
                  튀김
                </label>
                <label className="category-label">
                  <input
                    type="checkbox"
                    name="cookingStyle"
                    value="찜"
                    onChange={handleInputChange}
                  />{" "}
                  찜
                </label>
                <label className="category-label">
                  <input
                    type="checkbox"
                    name="cookingStyle"
                    value="국"
                    onChange={handleInputChange}
                  />{" "}
                  국
                </label>
                <label className="category-label">
                  <input
                    type="checkbox"
                    name="cookingStyle"
                    value="찌개"
                    onChange={handleInputChange}
                  />{" "}
                  찌개
                </label>
              </div>
            </div>
          </div>
        </fieldset>

        {/* allergy */}
        <fieldset className="allergy-section">
          <legend>
            <span className="alle">알레르기</span>해당되는 항목을 선택해주세요.
            (복수 선택 가능)
          </legend>
          <div className="allergy-grid">
            <div className="allergy-div">
              <label className="type">유제품 및 난류</label>
              <div className="category">
                <label className="category-label1">
                  <input
                    type="checkbox"
                    name="dairyEgg"
                    value="우유"
                    onChange={handleInputChange}
                  />{" "}
                  우유
                </label>
                <label className="category-label1">
                  <input
                    type="checkbox"
                    name="dairyEgg"
                    value="계란"
                    onChange={handleInputChange}
                  />{" "}
                  계란
                </label>
              </div>
            </div>
            <div className="allergy-div">
              <label className="type">곡류 및 견과류</label>
              <div className="category">
                <label className="category-label1">
                  <input
                    type="checkbox"
                    name="grainNuts"
                    value="밀"
                    onChange={handleInputChange}
                  />{" "}
                  밀
                </label>
                <label className="category-label1">
                  <input
                    type="checkbox"
                    name="grainNuts"
                    value="대두"
                    onChange={handleInputChange}
                  />{" "}
                  대두
                </label>
                <label className="category-label1">
                  <input
                    type="checkbox"
                    name="grainNuts"
                    value="땅콩"
                    onChange={handleInputChange}
                  />{" "}
                  땅콩
                </label>
                <label className="category-label1">
                  <input
                    type="checkbox"
                    name="grainNuts"
                    value="호두"
                    onChange={handleInputChange}
                  />{" "}
                  호두
                </label>
                <label className="category-label1">
                  <input
                    type="checkbox"
                    name="grainNuts"
                    value="메밀"
                    onChange={handleInputChange}
                  />{" "}
                  메밀
                </label>
                <label className="category-label1">
                  <input
                    type="checkbox"
                    name="grainNuts"
                    value="잣"
                    onChange={handleInputChange}
                  />{" "}
                  잣
                </label>
              </div>
            </div>
            <div className="allergy-div">
              <label className="type">육류</label>
              <div className="category">
                <label className="category-label1">
                  <input
                    type="checkbox"
                    name="meat"
                    value="돼지고기"
                    onChange={handleInputChange}
                  />{" "}
                  돼지고기
                </label>
                <label className="category-label1">
                  <input
                    type="checkbox"
                    name="meat"
                    value="닭고기"
                    onChange={handleInputChange}
                  />{" "}
                  닭고기
                </label>
                <label className="category-label1">
                  <input
                    type="checkbox"
                    name="meat"
                    value="쇠고기"
                    onChange={handleInputChange}
                  />{" "}
                  쇠고기
                </label>
              </div>
            </div>
            <div className="allergy-div">
              <label className="type">해산물</label>
              <div className="category">
                <label className="category-label1">
                  <input
                    type="checkbox"
                    name="seafood"
                    value="새우"
                    onChange={handleInputChange}
                  />{" "}
                  새우
                </label>
                <label className="category-label1">
                  <input
                    type="checkbox"
                    name="seafood"
                    value="게"
                    onChange={handleInputChange}
                  />{" "}
                  게
                </label>
                <label className="category-label1">
                  <input
                    type="checkbox"
                    name="seafood"
                    value="고등어"
                    onChange={handleInputChange}
                  />{" "}
                  고등어
                </label>
                <label className="category-label1">
                  <input
                    type="checkbox"
                    name="seafood"
                    value="오징어"
                    onChange={handleInputChange}
                  />{" "}
                  오징어
                </label>
              </div>
            </div>

            <div className="allergy-div">
              <label className="type">조개류</label>
              <div className="category">
                <label className="category-label1">
                  <input
                    type="checkbox"
                    name="clam"
                    value="굴"
                    onChange={handleInputChange}
                  />{" "}
                  굴
                </label>
                <label className="category-label1">
                  <input
                    type="checkbox"
                    name="clam"
                    value="전복"
                    onChange={handleInputChange}
                  />{" "}
                  전복
                </label>
                <label className="category-label1">
                  <input
                    type="checkbox"
                    name="clam"
                    value="홍합"
                    onChange={handleInputChange}
                  />{" "}
                  홍합
                </label>
                <label className="category-label1">
                  <input
                    type="checkbox"
                    name="clam"
                    value="기타조개"
                    onChange={handleInputChange}
                  />{" "}
                  기타조개
                </label>
              </div>
            </div>

            <div className="allergy-div">
              <label className="type">과일</label>
              <div className="category">
                <label className="category-label1">
                  <input
                    type="checkbox"
                    name="fruit"
                    value="복숭아"
                    onChange={handleInputChange}
                  />{" "}
                  복숭아
                </label>
                <label className="category-label1">
                  <input
                    type="checkbox"
                    name="fruit"
                    value="토마토"
                    onChange={handleInputChange}
                  />{" "}
                  토마토
                </label>
              </div>
            </div>
            <div className="allergy-div">
              <label className="type">기타</label>
              <div className="category">
                <label className="category-label1">
                  <input
                    type="checkbox"
                    name="others"
                    value="아황산염"
                    onChange={handleInputChange}
                  />{" "}
                  아황산염
                </label>
              </div>
            </div>
            <div>
              <label className="type">해당없음</label>
              <div className="category">
                <label className="category-label1">
                  <input
                    type="checkbox"
                    name="noAllergy"
                    value="해당없음"
                    onChange={handleInputChange}
                  />{" "}
                  해당없음
                </label>
              </div>
            </div>
          </div>
        </fieldset>
        <button className="signup-finish">가입완료</button>
      </form>
    </div>
  );
};

export default Form1;
