import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import "./RecipeList.css";
import 스크랩전 from "../../assets/allscrap.png";
import 스크랩후 from "../../assets/allscraps.png";
import 인분 from "../../assets/all인분.png";

const RecipeCard = ({ recipe }) => {
    const [scrapped, setScrapped] = useState(recipe.scrapped);
    const navigate = useNavigate();

    const handleScrap = async () => {
        const token = localStorage.getItem("jwtToken");

        try {
            const response = await fetch(`http://localhost:8080/api/recipes/scrap/${recipe.recipeId}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
            });

            const contentType = response.headers.get("content-type");
            const data = contentType && contentType.includes("application/json")
                ? await response.json()
                : await response.text();

            if (!response.ok) {
                throw new Error(data.message || "스크랩 요청 실패");
            }

            alert(data.message || data);
            setScrapped((prev) => !prev);
        } catch (error) {
            console.error("스크랩 요청 중 오류:", error);
            alert(error.message || "스크랩 요청 중 문제가 발생했습니다.");
        }
    };

    return (
        <div className="recipe-card">
            <div className="recipe-header">
                <h3>{recipe.recipeName}</h3>
                <button className="scrap-btn" onClick={handleScrap}>
                    <img
                        src={scrapped ? 스크랩후 : 스크랩전}
                        alt={scrapped ? "스크랩됨" : "스크랩하기"}
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
                <button className="recipe-btn" onClick={() => navigate(`/recipes/${recipe.recipeId}`)}>
                    레시피 보기
                </button>
            </div>
        </div>
    );
};

const Search = () => {
    const [results, setResults] = useState([]);
    const [loading, setLoading] = useState(true);
    const [query, setQuery] = useState(""); // 검색어 상태 추가
    const [filterEnabled, setFilterEnabled] = useState(false); // 필터 상태
    const [noResults, setNoResults] = useState(false); // 결과 없음 상태 추가

    const location = useLocation();

    useEffect(() => {
        const fetchSearchResults = async () => {
            const queryParams = new URLSearchParams(location.search);
            const searchQuery = queryParams.get("query") || ""; // 검색어 추출
            setQuery(searchQuery);

            const token = localStorage.getItem("jwtToken");

            try {
                const response = await fetch(
                    `http://localhost:8080/api/recipes/search?query=${searchQuery}&filter=${filterEnabled}`,
                    {
                        method: "GET",
                        headers: {
                            "Content-Type": "application/json",
                            Authorization: `Bearer ${token}`,
                        },
                    }
                );

                if (!response.ok || response.status === 204) { // HTTP 204 No Content 대응
                    setNoResults(true);
                    setResults([]);
                    return;
                }

                const data = await response.json();
                if (data.length === 0) {
                    setNoResults(true);
                } else {
                    setNoResults(false);
                }
                setResults(data);
            } catch (err) {
                console.error("검색 요청 중 오류:", err);
            } finally {
                setLoading(false);
            }
        };

        fetchSearchResults();
    }, [location.search, filterEnabled]);

    if (loading) {
        return <p>검색 결과를 불러오는 중입니다...</p>;
    }

    return (
        <div className="recipe-list-container">
            <h2 className="allrecipes-header">
                {query ? `"${query}"의 검색 결과입니다.` : "검색 결과"}
            </h2>

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

            <div className="recipe-list">
                {noResults ? (
                    <p className="no-results-text">검색 결과가 없습니다.</p>
                ) : results.length > 0 ? (
                    results.map((recipe, index) => (
                        <RecipeCard key={index} recipe={recipe} />
                    ))
                ) : (
                    <p className="no-results-text">검색 결과가 없습니다.</p>
                )}
            </div>
        </div>
    );
};

export default Search;
