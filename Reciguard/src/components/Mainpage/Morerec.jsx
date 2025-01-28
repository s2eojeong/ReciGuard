import React from "react";
import {useNavigate} from "react-router-dom";
import 화살표 from "../../assets/Arrow.png"
import 레시피 from "../../assets/morerecips.png"
import 스크랩 from "../../assets/scrapfunc.png"
import 필터링 from "../../assets/warnalle.png"
import 다른유저 from "../../assets/peoples.png"
import 정보수정 from "../../assets/upadateuser.png"
import 스크랩관리 from "../../assets/managescrap.png"
import 레시피관리 from "../../assets/managemy.png"
import "./Morerec.css";

const Morerec = () => {
  const navigate = useNavigate();

  return (
      <div className="mypage-container">
        <h2 className="more-title" onClick={() => navigate("/recipes/all")}>
          더 많은 레시피를 보려면 클릭하세요!
        </h2>

        {/* 마이페이지 섹션 */}
      </div>
  );
};

export default Morerec;

