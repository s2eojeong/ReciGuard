import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom"; // 페이지 이동을 위한 useNavigate 추가
import 스크랩 from "../../assets/allscraps.png"; // 스크랩 상태
import 스크랩전 from "../../assets/allscrap.png"; // 스크랩전 상태
import "./MyScrap.css";

const MyScrap = () => {
  const [scrapRecipes, setScrapRecipes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate(); // useNavigate 훅 초기화

  useEffect(() => {
    const fetchScrapRecipes = async () => {
      try {
        const token = localStorage.getItem("jwtToken");
        const response = await fetch(
          "https://reciguard.comhttps://reciguard.com/users/scraps",
          {
            method: "GET",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
          }
        );

        if (!response.ok) {
          throw new Error("스크랩한 레시피를 불러오는 데 실패했습니다.");
        }

        const data = await response.json();
        console.log(data); // 데이터를 확인하여 imagePath가 포함되어 있는지 체크
        setScrapRecipes(data);
      } catch (error) {
        setError(error.message);
      } finally {
        setLoading(false);
      }
    };

    fetchScrapRecipes();
  }, []);

  const handleScrapToggle = async (recipeId, currentScrapped) => {
    try {
      const token = localStorage.getItem("jwtToken");
      const response = await fetch(
        `https://reciguard.comhttps://reciguard.com/recipes/scrap/${recipeId}`,
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

      // 상태 업데이트
      if (!currentScrapped) {
        // 스크랩 추가 시 상태만 변경
        setScrapRecipes((prevRecipes) =>
          prevRecipes.map((recipe) =>
            recipe.recipeId === recipeId
              ? { ...recipe, scrapped: !currentScrapped }
              : recipe
          )
        );
      } else {
        // 스크랩 취소 시 리스트에서 제거
        setScrapRecipes((prevRecipes) =>
          prevRecipes.filter((recipe) => recipe.recipeId !== recipeId)
        );
      }
    } catch (error) {
      console.error("스크랩 요청 중 오류:", error);
      alert(error.message || "스크랩 요청 중 문제가 발생했습니다.");
    }
  };

  const goToRecipeDetail = (recipeId) => {
    navigate(`/recipes/${recipeId}`); // 상세 페이지 경로로 이동
  };

  if (loading) {
    return <p className="loading-text"></p>;
  }

  if (error) {
    return <p className="error-text">{error}</p>;
  }

  if (scrapRecipes.length === 0) {
    return <p className="no-scrap-text">스크랩한 레시피가 없습니다.</p>;
  }

  return (
    <div className="my-scrap-container">
      <h2 className="my-scrap-header">스크랩한 레시피</h2>
      <div className="scrap-recipe-list">
        {scrapRecipes.map((recipe, index) => (
          <div key={index} className="scrap-recipe-card">
            <div className="scrap-recipe-info">
              <h3 className="scrap-recipe-title">{recipe.recipeName}</h3>
              <img
                src={recipe.scrapped ? 스크랩 : 스크랩전}
                alt={recipe.scrapped ? "스크랩됨" : "스크랩하기"}
                className="scrap-heart-icon"
                onClick={() =>
                  handleScrapToggle(recipe.recipeId, recipe.scrapped)
                }
              />
            </div>
            <div className="scrap-recipe-cuisine">{recipe.cuisine}</div>
            <img
              src={recipe.imagePath}
              alt={recipe.recipeName}
              className="scrap-recipe-image"
            />
            {/* 상세 페이지 이동 버튼 추가 */}
            <button
              className="detail-button"
              onClick={() => goToRecipeDetail(recipe.recipeId)}
            >
              레시피
            </button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default MyScrap;
