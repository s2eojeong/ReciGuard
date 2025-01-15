import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./RecipeList.css";
import 스크랩전 from "../../assets/allscrap.png";
import 인분 from "../../assets/all인분.png";

const RecipeCard = ({ recipe }) => {
    const [isScrapped, setIsScrapped] = useState(false); // 스크랩 상태 관리
    const navigate = useNavigate(); // 페이지 이동 훅

    // 스크랩 API 호출 함수
    const handleScrap = async () => {
        const token = localStorage.getItem("jwtToken"); // JWT 토큰 가져오기

        try {
            const response = await fetch(`http://localhost:8080/api/recipes/scrap/${recipe.recipeId}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`,
                },
                body: JSON.stringify({
                    recipeId: recipe.recipeId,
                }),
            });

            if (!response.ok) {
                throw new Error("스크랩 요청 실패");
            }

            const data = await response.json();
            alert(data.message); // API 응답 메시지 출력
            setIsScrapped((prev) => !prev); // 스크랩 상태 토글
        } catch (error) {
            console.error("스크랩 요청 중 오류:", error);
        }
    };

    // 레시피 상세 페이지로 이동
    const handleViewDetail = () => {
        console.log("레시피 ID:", recipe.recipeId); // 로그로 확인
        if (recipe.recipeId) {
            navigate(`/recipes/${recipe.recipeId}`); // URL에 recipeId 전달
        } else {
            console.error("레시피 ID가 없습니다!");
        }
    };

    return (
        <div className="recipe-card">
            <div className="recipe-header">
                <h3>{recipe.recipeName}</h3>
                <button className="scrap-btn" onClick={handleScrap}>
                    <img
                        src={isScrapped ? "../../assets/allscrap.png" : 스크랩전}
                        alt={isScrapped ? "스크랩됨" : "스크랩하기"}
                        className="scrap-icon"
                    />
                </button>
            </div>
            <div className="recipe-image-div">
                <img
                    src={recipe.imagePath}
                    alt={recipe.recipeName}
                    className="recipe-image-real"
                />
            </div>
            <div className="recipe-info">
                <p>
                    <img className="recipe-info-person" src={인분} alt="인분 아이콘" />
                    {recipe.serving}인분
                </p>
                <button className="recipe-btn" onClick={handleViewDetail}>
                    레시피 보기
                </button>
            </div>
        </div>
    );
};

function RecipeList() {
    const [recipes, setRecipes] = useState([]); // 레시피 목록
    const [loading, setLoading] = useState(true); // 로딩 상태
    const [error, setError] = useState(null); // 에러 메시지
    const [filterEnabled, setFilterEnabled] = useState(false); // 필터 상태
    const [selectedCuisine, setSelectedCuisine] = useState("전체"); // 선택된 카테고리

    const cuisineOptions = ["전체", "한식", "중식", "양식", "일식", "아시안"];

    // API 호출 함수
    const fetchRecipes = async () => {
        setLoading(true);
        setError(null);

        try {
            const token = localStorage.getItem("jwtToken"); // 로그인 시 저장된 JWT 토큰
            let url = "";

            if (selectedCuisine === "전체") {
                // 전체 레시피 API
                url = `http://localhost:8080/api/recipes/all?filter=${filterEnabled}`;
            } else {
                // 카테고리별 레시피 API
                url = `http://localhost:8080/api/recipes?cuisine=${selectedCuisine}&filter=${filterEnabled}`;
            }

            const response = await fetch(url, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`,
                },
                credentials: "include",
            });

            if (!response.ok) {
                throw new Error("레시피 데이터를 불러오지 못했습니다.");
            }

            const data = await response.json();
            console.log("API 응답 데이터:", data);
            console.log("filterEnabled:", filterEnabled);
            setRecipes(data);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    // 선택된 카테고리 또는 필터 상태 변경 시 API 호출
    useEffect(() => {
        fetchRecipes();
    }, [selectedCuisine, filterEnabled]);

    return (
        <div className="recipe-list-container">
            <h2 className="allrecipes-header">오늘 뭐 먹지?</h2>
            <p className="allrecipes-p">알레르기를 가진 당신을 위한 안전한 레시피</p>

            {/* 필터 스위치 버튼 */}
            <div className="filter-buttons">
                <div className="filter-header">알레르기 유발 음식 필터링</div>
                <div className="filter-btn-group">
                    <button
                        className={`filter-btn ${!filterEnabled ? "active" : ""}`}
                        onClick={() => setFilterEnabled(false)}
                    >
                        OFF
                    </button>
                    <button
                        className={`filter-btn ${filterEnabled ? "active" : ""}`}
                        onClick={() => setFilterEnabled(true)}
                    >
                        ON
                    </button>
                </div>
            </div>

            {/* 카테고리 탭 */}
            <div className="cuisine-tabs">
                {cuisineOptions.map((cuisine) => (
                    <button
                        key={cuisine}
                        className={`tab-btn ${selectedCuisine === cuisine ? "active" : ""}`}
                        onClick={() => setSelectedCuisine(cuisine)}
                    >
                        {cuisine}
                    </button>
                ))}
            </div>

            {/* 레시피 리스트 */}
            <div className="recipe-list">
                {loading ? (
                    <p>레시피를 불러오는 중...</p>
                ) : error ? (
                    <p className="error-text">{error}</p>
                ) : recipes.length > 0 ? (
                    recipes.map((recipe, index) => (
                        <RecipeCard key={index} recipe={recipe} />
                    ))
                ) : (
                    <p>조건에 맞는 레시피가 없습니다.</p>
                )}
            </div>
        </div>
    );
}

export default RecipeList;
