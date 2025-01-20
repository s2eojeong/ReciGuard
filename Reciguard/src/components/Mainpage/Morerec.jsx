import React from "react";
import { useNavigate } from "react-router-dom";
import "./Morerec.css";
import 한식 from "../../assets/한식 대표 사진.jpg";
import 중식 from "../../assets/중식 대표 사진.png";
import 일식 from "../../assets/일식 대표 사진.png";
import 양식 from "../../assets/pasta.png";
import 아시안 from "../../assets/아시안 대표 사진.jpg";

const Morerec = () => {
  const navigate = useNavigate();

  const categories = [
    { name: "한식", image: 한식 },
    { name: "중식", image: 중식 },
    { name: "일식", image: 일식 },
    { name: "양식", image: 양식 },
    { name: "아시안", image: 아시안 },
  ];

  const handleCategoryClick = (category) => {
    // 쿼리 파라미터로 카테고리 전달
    navigate(`/recipes/all`);
  };

  return (
    <div className="more-recipes">
      <h2 className="more-recipes-title">더 많은 레시피</h2>
      <div className="recipe-category-container">
        {categories.map((category, index) => (
          <div
            className="recipe-category"
            key={index}
            onClick={() => handleCategoryClick(category.name)} // 클릭 이벤트 추가
          >
            <img
              src={category.image}
              alt={category.name}
              className="recipe-image"
            />
            <p className="recipe-name">{category.name}</p>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Morerec;
