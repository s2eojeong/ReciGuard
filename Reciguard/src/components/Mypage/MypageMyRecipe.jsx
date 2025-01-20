import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom"; // useNavigate 추가
import 스크랩 from "../../assets/allscraps.png"; // 스크랩 상태
import 스크랩전 from "../../assets/allscrap.png"; // 스크랩 전 상태
import "./MypageMyRecipe.css";

const MypageMyRecipe = () => {
  const [myRecipes, setMyRecipes] = useState([]); // 나만의 레시피 상태
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const navigate = useNavigate(); // useNavigate 사용

  // 나만의 레시피 불러오기
  useEffect(() => {
    const fetchMyRecipes = async () => {
      try {
        const token = localStorage.getItem("jwtToken");
        const response = await fetch(
          "https://reciguard.com/api/recipes/myrecipes",
          {
            method: "GET",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
          }
        );

        if (!response.ok) {
          throw new Error("나만의 레시피를 불러오는 데 실패했습니다.");
        }

        const data = await response.json();
        setMyRecipes(data); // 나만의 레시피 데이터 설정
      } catch (error) {
        setError(error.message);
      } finally {
        setLoading(false);
      }
    };

    fetchMyRecipes();
  }, []);

  // 스크랩 상태 토글
  const handleScrapToggle = async (recipeId, currentScrapped) => {
    try {
      const token = localStorage.getItem("jwtToken");
      const response = await fetch(
        `https://reciguard.com/api/recipes/scrap/${recipeId}`,
        {
          method: "POST",
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      const contentType = response.headers.get("content-type");
      const data =
        contentType && contentType.includes("application/json")
          ? await response.json()
          : await response.text();

      if (!response.ok) {
        throw new Error(data.message || "스크랩 요청 실패");
      }

      alert(data.message || "스크랩 상태가 변경되었습니다.");

      // 상태 업데이트: 해당 레시피의 스크랩 상태 변경
      setMyRecipes((prevRecipes) =>
        prevRecipes.map((recipe) =>
          recipe.recipeId === recipeId
            ? { ...recipe, scrapped: !currentScrapped }
            : recipe
        )
      );
    } catch (error) {
      console.error("스크랩 요청 중 오류:", error);
      alert(error.message || "스크랩 요청 중 문제가 발생했습니다.");
    }
  };

  const handleViewRecipe = (recipeId) => {
    // 레시피 상세 페이지로 이동
    navigate(`/recipes/${recipeId}`);
  };

  if (loading) {
    return <p className="loading-text"></p>;
  }

  if (error) {
    return <p className="error-text">{error}</p>;
  }

  if (myRecipes.length === 0) {
    return <p className="no-recipes-text">나만의 레시피가 없습니다.</p>;
  }

  return (
    <div className="my-recipe-container">
      <h2 className="my-recipe-header">나만의 레시피</h2>
      <div className="my-recipe-list">
        {myRecipes.map((recipe, index) => (
          <div key={index} className="my-recipe-card">
            <div className="my-recipe-info">
              <h3 className="my-recipe-title">{recipe.recipeName}</h3>
              <img
                src={recipe.scrapped ? 스크랩 : 스크랩전}
                alt={recipe.scrapped ? "스크랩됨" : "스크랩하기"}
                className="scrap-heart-icon"
                onClick={() =>
                  handleScrapToggle(recipe.recipeId, recipe.scrapped)
                }
              />
            </div>
            <img
              src={recipe.imagePath}
              alt={recipe.recipeName}
              className="my-recipe-image"
            />
            <button
              className="view-recipe-button" // CSS 클래스
              onClick={() => handleViewRecipe(recipe.recipeId)}
            >
              레시피
            </button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default MypageMyRecipe;
