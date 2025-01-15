import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom"; // URL 파라미터 사용을 위해 import
import "./Rdetail.css";
import 몇인분 from "../assets/몇인분.png"; // 유지된 프론트 이미지
import 국가별 from "../assets/국가별.png"; // 유지된 프론트 이미지
import 조회수 from "../assets/조회수.png"; // 유지된 프론트 이미지
import filledHeartImg from "../assets/heart1.png"; // 유지된 프론트 이미지
import emptyHeartImg from "../assets/heart.png"; // 유지된 프론트 이미지

const Rdetail = () => {
  const { recipeId } = useParams(); // URL에서 recipeId 가져오기
  const [recipe, setRecipe] = useState(null); // API 데이터를 저장할 상태
  const [isScrapped, setIsScrapped] = useState(false); // 스크랩 여부 상태
  const [scrapCount, setScrapCount] = useState(0); // 초기 스크랩 수

  // API 호출
  useEffect(() => {
    const fetchRecipeDetail = async () => {
      try {
        const token = localStorage.getItem("jwtToken"); // 로컬 스토리지에서 토큰 가져오기
        console.log("JWT 토큰:", token);
        console.log("레시피 ID:", recipeId);
        const response = await fetch(`http://localhost:8080/api/recipes/${recipeId}`, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`, // 토큰 추가
          },
        });

        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const data = await response.json();
        console.log("레시피 데이터:", data);
        setRecipe(data); // 레시피 데이터 설정
        setScrapCount(data.scrapCount); // 스크랩 수 설정
      } catch (error) {
        console.error("레시피 데이터를 가져오는 중 오류 발생:", error);
      }
    };

    fetchRecipeDetail();
  }, [recipeId]);

  // 스크랩 버튼 클릭 시 처리
  const handleScrapClick = async () => {
    try {
      const token = localStorage.getItem("jwtToken");
      const response = await fetch(`http://localhost:8080/api/recipes/scrap/${recipeId}`, {
        method: isScrapped ? "DELETE" : "POST",
        headers: {
          "Authorization": `Bearer ${token}`,
        },
      });

      if (!response.ok) throw new Error("스크랩 요청 실패");

      setScrapCount((prev) => (isScrapped ? prev - 1 : prev + 1));
      setIsScrapped((prev) => !prev);
    } catch (error) {
      console.error("스크랩 요청 중 오류:", error);
    }
  };

  if (!recipe) {
    return <div className="loading-message">레시피 정보를 불러오는 중입니다...</div>; // 로딩 상태
  }

  return (
      <div className="rdetail-container">
        <div className="rdetail-header">
          <img
              src={recipe.imagePath}
              alt={recipe.recipeName}
              className="rdetail-image"
          />
          <h2 className="rdetail-title">{recipe.recipeName}</h2>
          <hr className="divider-line" />
          <div className="rdetail-icons">
            <div className="icon-container">
              <div className="icon-item">
                <img src={몇인분} alt="인원수 아이콘" className="icon-image" />
                <span className="icon-text">{recipe.serving}인분</span>
              </div>
              <div className="icon-item">
                <img src={국가별} alt="국가별 아이콘" className="icon-image" />
                <span className="icon-text">{recipe.cuisine}</span>
              </div>
              <div className="icon-item">
                <img src={조회수} alt="조회수 아이콘" className="icon-image" />
                <span className="icon-text">{recipe.viewCount}</span>
              </div>
            </div>
            <div className="favorite-container">
              <button className="favorite-button" onClick={handleScrapClick}>
                <img
                    src={isScrapped ? filledHeartImg : emptyHeartImg}
                    alt="스크랩 버튼"
                    className="heart-image"
                />
              </button>
              <span className="scrap-text">스크랩 {scrapCount}</span>
            </div>
          </div>
        </div>

        <div className="ingredient-stpes">
          <h3>재료</h3>
          <div className="ingredient-container">
            <section className="ingredient-section">
              <div className="ingredients-list">
                {recipe.ingredients?.map((ingredient, index) => (
                    <div className="ingredient1-row" key={index}>
                      <div className="ingredient-texts">
                        <span className="ingredient-name">{ingredient.ingredient}</span>
                        <span className="ingredient-quantity">{ingredient.quantity}</span>
                      </div>
                      <hr />
                    </div>
                ))}
              </div>
            </section>
          </div>

          <h3>요리순서</h3>
          <div className="recipe-container">
            <section className="recipe-steps-section">
              <div className="step-container">
                {recipe.instructions?.map((instruction, index) => (
                    <div className="stepss" key={index}>
                      <div className="step-label">Step {index + 1}</div>
                      <div className="step-content">
                        <p>{instruction.instruction}</p>
                      </div>
                      <div className="instruction-image-box">
                        {instruction.instructionImage ? (
                            <img
                                src={instruction.instructionImage}
                                alt={`Step ${index + 1} 이미지`}
                                className="instruction-image"
                            />
                        ) : (
                            <p className="no-image-text">이미지가 없습니다.</p>
                        )}
                      </div>
                    </div>
                ))}
              </div>
            </section>
          </div>
        </div>

        <div className="nutrition-stpes">
          <h3>영양 성분</h3>
          <div className="nutrition-container">
            <section className="nutrition-section">
              <div className="nutritions-list">
                <div className="nutrition1-row">
                  <div className="nutrition-texts">
                    <span className="nutrition-name">칼로리</span>
                    <span className="nutrition-quantity">{recipe.calories} kcal</span>
                  </div>
                  <hr />
                </div>
                <div className="nutrition1-row">
                  <div className="nutrition-texts">
                    <span className="nutrition-name">탄수화물</span>
                    <span className="nutrition-quantity">{recipe.carbohydrate}g</span>
                  </div>
                  <hr />
                </div>
                <div className="nutrition1-row">
                  <div className="nutrition-texts">
                    <span className="nutrition-name">단백질</span>
                    <span className="nutrition-quantity">{recipe.protein}g</span>
                  </div>
                  <hr />
                </div>
                <div className="nutrition1-row">
                  <div className="nutrition-texts">
                    <span className="nutrition-name">지방</span>
                    <span className="nutrition-quantity">{recipe.fat}g</span>
                  </div>
                  <hr />
                </div>
                <div className="nutrition1-row">
                  <div className="nutrition-texts">
                    <span className="nutrition-name">나트륨</span>
                    <span className="nutrition-quantity">{recipe.sodium}mg</span>
                  </div>
                  <hr />
                </div>
              </div>
            </section>
          </div>
        </div>
      </div>
  );
};

export default Rdetail;
