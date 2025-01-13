import React, { useEffect, useState } from "react";

const RecipeCard = ({ recipe }) => (
    <div className="recipe-card">
        <h3>{recipe.recipeName}</h3>
        <img
            src={recipe.imagePath}
            alt={recipe.recipeName}
            className="recipe-image"
        />
        <div className="recipe-info">
            <p>👥 {recipe.serving}인분</p>
            <button className="recipe-btn">레시피 보기</button>
        </div>
    </div>
);

function RecipeList() {
    const [recipes, setRecipes] = useState([]); // 레시피 목록
    const [loading, setLoading] = useState(true); // 로딩 상태
    const [error, setError] = useState(null); // 에러 메시지
    const [filterEnabled, setFilterEnabled] = useState(false); // 필터 상태

    // API 호출 함수
    const fetchRecipes = async () => {
        setLoading(true);
        setError(null);
        try {
            const filterQuery = filterEnabled ? "true" : "false"; // 필터링 여부에 따라 URL 변경

            // JWT 토큰 가져오기 (예: localStorage 또는 sessionStorage)
            const token = localStorage.getItem("jwtToken"); // jwtToken은 로그인 시 저장된 토큰

            const response = await fetch(
                `http://localhost:8080/api/recipes/all?filter=${filterQuery}`,
                {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": `Bearer ${token}`, // JWT 토큰 추가
                    },
                    credentials: "include", // 인증 정보 포함
                }
            );

            if (!response.ok) {
                throw new Error("레시피 데이터를 불러오지 못했습니다.");
            }

            const data = await response.json();
            setRecipes(data);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    // 컴포넌트 마운트 시 레시피 데이터 가져오기
    useEffect(() => {
        fetchRecipes();
    }, [filterEnabled]); // 필터 상태가 변경될 때 API 호출

    return (
        <div className="recipe-list-container">
            <h2>오늘 뭐 먹지?</h2>
            <p>알레르기를 가진 당신을 위한 안전한 레시피</p>

            {/* 필터링 버튼 */}
            <div className="filter-buttons">
                <button
                    className={`filter-btn ${!filterEnabled ? "active" : ""}`}
                    onClick={() => setFilterEnabled(false)}
                >
                    전체 레시피 보기
                </button>
                <button
                    className={`filter-btn ${filterEnabled ? "active" : ""}`}
                    onClick={() => setFilterEnabled(true)}
                >
                    필터링된 레시피 보기
                </button>
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


