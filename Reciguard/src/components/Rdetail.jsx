import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import "./Rdetail.css";
import 몇인분 from "../assets/몇인분.png";
import 국가별 from "../assets/국가별.png";
import 조회수 from "../assets/조회수.png";
import filledHeartImg from "../assets/heart1.png";
import emptyHeartImg from "../assets/heart.png";

const Rdetail = () => {
  const { recipeId } = useParams();
  const [recipe, setRecipe] = useState(null);
  const [isScrapped, setIsScrapped] = useState(false);
  const [scrapCount, setScrapCount] = useState(0);

  // 레시피 상세 정보 및 초기 스크랩 상태 로드
  useEffect(() => {
    const fetchRecipeDetail = async () => {
      try {
        const token = localStorage.getItem("jwtToken");
        const response = await fetch(`http://localhost:8080/api/recipes/${recipeId}`, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`,
          },
        });

        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const data = await response.json();
        setRecipe(data);
        setScrapCount(data.scrapCount); // 백엔드에서 받은 스크랩 수
        setIsScrapped(data.isScrapped); // 백엔드에서 받은 스크랩 상태
      } catch (error) {
        console.error("레시피 데이터를 가져오는 중 오류 발생:", error);
      }
    };

    fetchRecipeDetail();
  }, [recipeId]);

  // 스크랩 버튼 클릭 처리
  // 스크랩 버튼 클릭 시 처리
  const handleScrapClick = async () => {
    try {
      const token = localStorage.getItem("jwtToken");
      const response = await fetch(`http://localhost:8080/api/recipes/scrap/${recipeId}`, {
        method: "POST",
        headers: {
          "Authorization": `Bearer ${token}`,
        },
      });

      const contentType = response.headers.get("content-type");
      const data = contentType && contentType.includes("application/json")
          ? await response.json()
          : await response.text();

      if (!response.ok) {
        throw new Error(data.message || "스크랩 요청 실패");
      }

      alert(data.message || data); // 성공 메시지 출력

      // 백엔드에서 최신 상태 가져오기
      setIsScrapped(data.isScrapped); // 백엔드에서 반환된 값을 적용
      setScrapCount(data.scrapCount); // 스크랩 수 업데이트
    } catch (error) {
      console.error("스크랩 요청 중 오류:", error);
      alert(error.message || "스크랩 요청 중 문제가 발생했습니다.");
    }
  };

  if (!recipe) {
    return <div className="loading-message">레시피 정보를 불러오는 중입니다...</div>;
  }

  return (
      <div className="rdetail-container">
        <div className="rdetail-header">
          <img src={recipe.imagePath} alt={recipe.recipeName} className="rdetail-image" />
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
                    alt={isScrapped ? "스크랩됨" : "스크랩하기"}
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
                {["칼로리", "탄수화물", "단백질", "지방", "나트륨"].map((nutrient, index) => (
                    <div className="nutrition1-row" key={index}>
                      <div className="nutrition-texts">
                        <span className="nutrition-name">{nutrient}</span>
                        <span className="nutrition-quantity">{recipe[nutrient.toLowerCase()]}{nutrient === "칼로리" ? " kcal" : nutrient === "나트륨" ? "mg" : "g"}</span>
                      </div>
                      <hr />
                    </div>
                ))}
              </div>
            </section>
          </div>
        </div>
      </div>
  );
};

export default Rdetail;
