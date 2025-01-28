import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import 스크랩 from "../../assets/allscraps.png"; // 스크랩 상태
import 스크랩전 from "../../assets/allscrap.png"; // 스크랩 전 상태
import "./MypageMyRecipe.css";

const MypageMyRecipe = () => {
  const [myRecipes, setMyRecipes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const navigate = useNavigate();

  // 나만의 레시피 불러오기
  useEffect(() => {
    const fetchMyRecipes = async () => {
      try {
        const token = localStorage.getItem("jwtToken");
        const response = await fetch(
            "http://localhost:8080/api/recipes/myrecipes",
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
        setMyRecipes(data);
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
          `http://localhost:8080/api/recipes/scrap/${recipeId}`,
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

      // 스크랩 상태에 따른 메시지
      const message = currentScrapped
          ? "레시피가 스크랩 취소 되었습니다."
          : "레시피가 성공적으로 스크랩되었습니다.";

      alert(message);

      // 상태 업데이트
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


  // 레시피 삭제
  const handleDeleteRecipe = async (recipeId) => {
    try {
      // 삭제 확인 팝업
      const confirmed = window.confirm("정말로 삭제하시겠습니까?");
      if (!confirmed) {
        return; // 사용자가 '취소'를 클릭하면 함수 종료
      }

      const token = localStorage.getItem("jwtToken");
      const response = await fetch(
          `http://localhost:8080/api/recipes/myrecipe/${recipeId}/delete`,
          {
            method: "DELETE",
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
        throw new Error(data.message || "레시피 삭제 실패");
      }

      alert(data.message || "레시피가 삭제되었습니다.");

      // 삭제된 레시피를 상태에서 제거
      setMyRecipes((prevRecipes) =>
          prevRecipes.filter((recipe) => recipe.recipeId !== recipeId)
      );
    } catch (error) {
      console.error("레시피 삭제 중 오류:", error);
      alert(error.message || "레시피 삭제 중 문제가 발생했습니다.");
    }
  };

  const handleViewRecipe = (recipeId) => {
    navigate(`/users/myrecipes/edit/${recipeId}`);
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

  // 레시피 상세 보기로 이동
  const handleViewRecipeDetail = (recipeId) => {
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
                  <h3 className="my-recipe-title"
                      onClick={() => handleViewRecipeDetail(recipe.recipeId)}
                  >{recipe.recipeName}</h3>
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
                <div className="my-recipe-actions">
                  <button
                      className="view-recipe-button"
                      onClick={() => handleViewRecipe(recipe.recipeId)}
                  >
                    수정
                  </button>
                  <button
                      className="delete-recipe-button"
                      onClick={() => handleDeleteRecipe(recipe.recipeId)}
                  >
                    삭제
                  </button>
                </div>
              </div>
          ))}
        </div>
      </div>
  );
};

export default MypageMyRecipe;

