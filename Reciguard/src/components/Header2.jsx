import React, { useState } from "react";
import "./Header2.css";
import mypage from "../assets/mypage.png";
import search from "../assets/search.png"
import { Link, useNavigate } from "react-router-dom";

const Header2 = () => {
  const [searchQuery, setSearchQuery] = useState(""); // 검색어 상태
  const navigate = useNavigate();

  const handleSearch = (e) => {
    e.preventDefault();
    if (searchQuery.trim()) {
      // 검색어가 존재하면 /search 경로로 이동
      navigate(`/search?query=${encodeURIComponent(searchQuery)}`);
    } else {
      alert("검색어를 입력해주세요.");
    }
  };

  return (
    <nav className="navbar2">
      <Link className="logolink" to="/recipes">
        <div className="navbar-logo2">ReciGuard</div>
      </Link>
      <ul className="navbar-menu2">
        <Link to="/recipes/all" style={{ textDecoration: "none" }}>
          <li>분류</li>
        </Link>
        <Link to="/users/recipe-form" style={{ textDecoration: "none" }}>
          <li>레시피 등록</li>
        </Link>
      </ul>
      <div className="rightnav2">
          <form onSubmit={handleSearch} style={{display: "flex", alignItems: "center"}}>
            <div className="search-container" style={{position: "relative", flex: 1}}>
              <img
                  src={search}
                  alt="search icon"
                  className="search-icon"
              />
              <input
                  className="navbar-search2"
                  type="text"
                  placeholder="Search..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
              />
            </div>
            <button type="submit" style={{display: "none"}}>
              Search
            </button>
          </form>
        <img className="mypage-image2" src={mypage} alt="마이페이지"/>
        <Link to="/users/{userid}" style={{textDecoration: "none"}}>
          <h1 className="mypage-letter">마이페이지</h1>
        </Link>
      </div>
    </nav>
  );
};

export default Header2;
