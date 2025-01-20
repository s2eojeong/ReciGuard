import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./Rightpanel2.css";
import "./Form1.css";

function Rightpanel2() {
  const [isPopupOpen, setIsPopupOpen] = useState(false); // 팝업 상태 관리
  const [preferences, setPreferences] = useState({}); // Form1의 상태
  const [isLoading, setIsLoading] = useState(false); // 로딩 상태 관리
  const [error, setError] = useState(""); // 오류 메시지 관리

  const navigate = useNavigate();

  // 팝업 열기 및 닫기 핸들러
  const handlePopupOpen = (e) => {
    e.preventDefault();
    setIsPopupOpen(true);
  };

  const handlePopupClose = () => {
    setIsPopupOpen(false);
  };

  // 다중 선택용 체크박스 핸들러
  const handleMultiCheckboxChange = (e) => {
    const { name, value } = e.target;

    // 알레르기 항목 처리 (ingredients 배열에 저장)
    if (["유제품", "견과류", "곡물류", "콩류", "육류", "해산물", "과일류"].some((group) => options[group]?.includes(value))) {
      setPreferences((prev) => {
        const selectedAllergies = prev.ingredients || [];
        if (selectedAllergies.includes(value)) {
          // 이미 선택된 항목 제거
          return { ...prev, ingredients: selectedAllergies.filter((item) => item !== value) };
        } else {
          // 새로운 항목 추가
          return { ...prev, ingredients: [...selectedAllergies, value] };
        }
      });
    } else {
      // 일반적인 선호도 옵션 처리
      setPreferences((prev) => {
        const selectedItems = prev[name] || [];
        return selectedItems.includes(value)
            ? { ...prev, [name]: selectedItems.filter((item) => item !== value) }
            : { ...prev, [name]: [...selectedItems, value] };
      });
    }
  };

  // 단순 입력값 변경 핸들러 (텍스트, 숫자 등 단일 입력)
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setPreferences((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleGenderChange = (e) => {
    setPreferences((prev) => ({
      ...prev,
      gender: e.target.value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true); // 로딩 시작
    setError(""); // 이전 오류 메시지 초기화

    const requestData = {
      username: preferences.name || "",
      gender: preferences.gender || "",
      age: Number(preferences.age) || 0,
      weight: Number(preferences.weight) || 0,
      email: preferences.email || "",
      password: preferences.password || "",
      ingredients: preferences.ingredients || [], // 알레르기 리스트 배열
      userCookingStyle: preferences.cookingStyle || [],
      userCuisine: preferences.country || [],
      userFoodType: preferences.mealType || [],
    };

    console.log("전송 데이터:", requestData);

    try {
      const response = await fetch("http://localhost:8080/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(requestData),
      });

      const responseText = await response.text(); // 응답을 문자열로 받음

      if (response.ok) {
        alert(responseText); // 성공 메시지 표시
        setIsPopupOpen(false); // 팝업 닫기
        setPreferences({}); // 입력값 초기화
        navigate("/auth/login");
      } else {
        console.error("오류 응답:", responseText);
        alert(responseText || "서버 오류가 발생했습니다.");
      }
    } catch (err) {
      console.error("회원가입 요청 중 오류 발생:", err);
      alert("네트워크 오류가 발생했습니다. 다시 시도해주세요.");
    } finally {
      setIsLoading(false); // 로딩 종료
    }
  };

  // 옵션 데이터
  const options = {
    country: ["한식", "중식", "일식", "양식", "아시안"],
    mealType: ["밥", "면", "떡", "죽", "반찬"],
    cookingStyle: ["찌기", "끓이기", "굽기", "볶기", "튀기기", "기타"],
    유제품: ["우유"],
    견과류: ["호두", "아몬드", "땅콩", "참깨"],
    곡물류: ["밀가루", "귀리", "호밀가루"],
    콩류: ["대두"],
    육류: ["돼지고기", "닭고기", "소고기", "계란"],
    해산물: ["고등어", "연어", "참치", "꽃게", "새우", "오징어", "굴", "전복", "홍합", "조개"],
    과일류: ["복숭아", "키위"],
    해당없음: ["해당없음"],
  };

  // 체크박스 렌더링 함수
  const renderCheckboxes = (name, items, useSpecialClass = false) =>
      items.map((item) => (
          <label
              key={item}
              className={useSpecialClass ? "category-label1" : "category-label"}
          >
            <input
                type="checkbox"
                name={name}
                value={item}
                onChange={handleMultiCheckboxChange} // 다중 선택 핸들러 사용
            />{" "}
            {item}
          </label>
      ));

  return (
      <div className="right-panel">
        <h2>Create your account</h2>
        <p className="easy">It's easy and free</p>
        <form className="login-form" onSubmit={handlePopupOpen}>
          <div className="form-group">
            <label htmlFor="name">이름</label>
            <input
                type="text"
                id="name"
                name="name"
                placeholder="Enter your name"
                required
                onChange={handleInputChange}
            />
          </div>
          <div className="form-group">
            <label htmlFor="email">E-mail</label>
            <input
                type="email"
                id="email"
                name="email"
                placeholder="Type your e-mail"
                required
                onChange={handleInputChange}
            />
          </div>
          <div className="form-group">
            <label htmlFor="password">Password</label>
            <div className="input-container">
              <input
                  type="password"
                  id="password"
                  name="password"
                  placeholder="Type your password"
                  required
                  onChange={handleInputChange}
              />
            </div>
          </div>
          <div className="checkbox-group">
            <input type="checkbox" className="checkbox-input" required />
            <label className="checkbox-label">
              가입 시 개인정보 수집 및 이용에 동의합니다.
            </label>
          </div>
          <button type="submit" className="sign-up-btn">
            Tell us more!
          </button>
        </form>
        <div>
          <p className="log-in">
            Already have an account? <a href="/auth/login">Log in</a>
          </p>
        </div>

        {isPopupOpen && (
            <div className="popup-overlay">
              <div className="popup-content">
                <button className="close-popup" onClick={handlePopupClose}>
                  x
                </button>
                <div className="form-page">
                  <form className="reciguard-form" onSubmit={handleSubmit}>
                    <h1>ReciGuard</h1>
                    {error && <p className="error">{error}</p>}

                    <div className="group">
                      <label>성별</label>
                      <div className="gender-options">
                        <label>
                          <input
                              type="radio"
                              name="gender"
                              value="남성"
                              onChange={handleGenderChange}
                          />{" "}
                          남성
                        </label>
                        <label>
                          <input
                              type="radio"
                              name="gender"
                              value="여성"
                              onChange={handleGenderChange}
                          />{" "}
                          여성
                        </label>
                      </div>
                    </div>

                    <div className="group">
                      <label>나이</label>
                      <div className="year">
                        <select
                            id="age"
                            name="age"
                            onChange={handleInputChange}
                            defaultValue=""
                        >
                          <option value="" disabled></option>
                          {Array.from({ length: 116 }, (_, i) => 2025 - i).map(
                              (year) => (
                                  <option key={year} value={year}>
                                    {year}
                                  </option>
                              )
                          )}
                        </select>
                        년생
                      </div>
                    </div>

                    <div className="group">
                      <label>체중</label>
                      <div className="kg">
                        <input
                            className="weight"
                            type="number"
                            name="weight"
                            step="0.1"
                            onChange={handleInputChange}
                        />{" "}
                        kg
                      </div>
                    </div>

                    <fieldset className="preferences-section">
                      <legend>
                        <span className="pre">선호도</span> 좋아하는 종류를
                        선택해주세요. (복수 선택 가능)
                      </legend>
                      <div className="preferences-grid">
                        <div>
                          <label className="type">국가별</label>
                          <div className="country">
                            {renderCheckboxes("country", options.country)}
                          </div>
                        </div>
                        <div>
                          <label className="type">식사 유형</label>
                          <div>{renderCheckboxes("mealType", options.mealType)}</div>
                        </div>
                        <div>
                          <label className="type">조리 방식</label>
                          <div>{renderCheckboxes("cookingStyle", options.cookingStyle)}</div>
                        </div>
                      </div>
                    </fieldset>

                    <fieldset className="allergy-section">
                      <legend>
                        <span className="alle">알레르기</span> 해당되는 항목을
                        선택해주세요. (복수 선택 가능)
                      </legend>
                      <div className="allergy-grid">
                        {Object.entries(options).map(
                            ([key, values], index) =>
                                key !== "country" &&
                                key !== "mealType" &&
                                key !== "cookingStyle" && (
                                    <div key={index} className="allergy-div">
                                      <label className="type">{key}</label>
                                      <div className="category">
                                        {renderCheckboxes(key, values, true)}
                                      </div>
                                    </div>
                                )
                        )}
                      </div>
                    </fieldset>

                    <button className="signup-finish" disabled={isLoading}>
                      {isLoading ? "가입 중..." : "가입완료"}
                    </button>
                  </form>
                </div>
              </div>
            </div>
        )}
      </div>
  );
}

export default Rightpanel2;

