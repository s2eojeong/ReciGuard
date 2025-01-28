import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./RecipeList.css";
import 스크랩전 from "../../assets/allscrap.png";
import 스크랩후 from "../../assets/allscraps.png";
import 인분 from "../../assets/allService.png";

const RecipeCard = ({ recipe }) => {
  const [scrapped, setScrapped] = useState(recipe.scrapped); // 초기값은 props로 설정
  const navigate = useNavigate();

  const handleScrap = async () => {
    const token = localStorage.getItem("jwtToken");

    try {
      const response = await fetch(
        `http://localhost:8080/api/recipes/scrap/${recipe.recipeId}`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
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

      alert(data.message || data); // 성공 메시지 출력
      setScrapped((prev) => !prev); // 상태 토글
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
        <button
          className="recipe-btn"
          onClick={() => navigate(`/recipes/${recipe.recipeId}`)}
        >
          레시피 보기
        </button>
      </div>
    </div>
  );
};

const RecipeList = () => {
  const [recipes, setRecipes] = useState([]); // 레시피 목록
  const [loading, setLoading] = useState(true); // 로딩 상태
  const [error, setError] = useState(null); // 에러 메시지
  const [filterEnabled, setFilterEnabled] = useState(false); // 필터 상태
  const [selectedCuisine, setSelectedCuisine] = useState("전체"); // 선택된 카테고리
  const [currentPage, setCurrentPage] = useState(1); // 현재 페이지
  const [currentGroup, setCurrentGroup] = useState(1); // 현재 페이지 그룹
  const itemsPerPage = 20; // 한 페이지에 표시할 레시피 수
  const pagesPerGroup = 10; // 한 그룹에 표시할 페이지 버튼 수

  const cuisineOptions = ["전체", "한식", "중식", "양식", "일식", "아시안"];

  // API 호출 함수
  const fetchRecipes = async () => {
    setLoading(true);
    setError(null);

    try {
      const token = localStorage.getItem("jwtToken"); // 로그인 시 저장된 JWT 토큰
      let url =
        selectedCuisine === "전체"
          ? `http://localhost:8080/api/recipes/all?filter=${filterEnabled}`
          : `http://localhost:8080/api/recipes?cuisine=${selectedCuisine}&filter=${filterEnabled}`;

      const response = await fetch(url, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error("레시피 데이터를 불러오지 못했습니다.");
      }

      const data = await response.json();
      setRecipes(data); // 레시피 데이터 설정
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchRecipes();
  }, [selectedCuisine, filterEnabled]);

  // 현재 페이지에 표시할 레시피 계산
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentRecipes = recipes.slice(indexOfFirstItem, indexOfLastItem);

  // 총 페이지 수 계산
  const totalPages = Math.ceil(recipes.length / itemsPerPage);
  const totalGroups = Math.ceil(totalPages / pagesPerGroup); // 총 그룹 수 계산

  // 현재 그룹의 페이지 번호 계산
  const startPage = (currentGroup - 1) * pagesPerGroup + 1;
  const endPage = Math.min(currentGroup * pagesPerGroup, totalPages);

  // 페이지를 변경할 때 스크롤을 위로 이동
  const handlePageChange = (page) => {
    setCurrentPage(page);
    window.scrollTo(0, 0); // 페이지 상단으로 스크롤
  };

  // 카테고리 변경 시 페이지 초기화
  const handleCuisineChange = (cuisine) => {
    setSelectedCuisine(cuisine);
    setCurrentPage(1); // 페이지를 1로 초기화
    setCurrentGroup(1); // 그룹도 초기화
  };

  // 페이지네이션 버튼 렌더링
  const renderPagination = () => {
    const pageNumbers = [];
    for (let i = startPage; i <= endPage; i++) {
      pageNumbers.push(i);
    }

    return (
      <div className="pagination">
        {/* 이전 그룹 버튼 */}
        {currentGroup > 1 && (
          <button
            className="group-btn"
            onClick={() => {
              setCurrentGroup((prev) => prev - 1);
              setCurrentPage((currentGroup - 2) * pagesPerGroup + 1); // 그룹 이동 시 첫 페이지로 이동
              window.scrollTo(0, 0); // 페이지 상단으로 이동
            }}
          >
            &lt;
          </button>
        )}
        {/* 페이지 번호 버튼 */}
        {pageNumbers.map((page) => (
          <button
            key={page}
            className={`page-btn ${currentPage === page ? "active" : ""}`}
            onClick={() => handlePageChange(page)}
          >
            {page}
          </button>
        ))}
        {/* 다음 그룹 버튼 */}
        {currentGroup < totalGroups && (
          <button
            className="group-btn"
            onClick={() => {
              setCurrentGroup((prev) => prev + 1);
              setCurrentPage(currentGroup * pagesPerGroup + 1); // 그룹 이동 시 첫 페이지로 이동
              window.scrollTo(0, 0); // 페이지 상단으로 이동
            }}
          >
            &gt;
          </button>
        )}
      </div>
    );
  };

  return (
    <div className="recipe-list-container">
      <h2 className="allrecipes-header">오늘 뭐 먹지?</h2>
      <p className="allrecipes-p">알레르기를 가진 당신을 위한 안전한 레시피</p>

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

      <div className="cuisine-tabs">
        {cuisineOptions.map((cuisine) => (
          <button
            key={cuisine}
            className={`tab-btn ${selectedCuisine === cuisine ? "active" : ""}`}
            onClick={() => handleCuisineChange(cuisine)} // 카테고리 변경
          >
            {cuisine}
          </button>
        ))}
      </div>

      <div className="recipe-list">
        {loading ? (
          <p>레시피를 불러오는 중...</p>
        ) : error ? (
          <p className="error-text">{error}</p>
        ) : currentRecipes.length > 0 ? (
          currentRecipes.map((recipe, index) => (
            <RecipeCard key={index} recipe={recipe} />
          ))
        ) : (
          <p>조건에 맞는 레시피가 없습니다.</p>
        )}
      </div>

      {/* 페이지네이션 */}
      {!loading && !error && recipes.length > 0 && renderPagination()}
    </div>
  );
};

export default RecipeList;
