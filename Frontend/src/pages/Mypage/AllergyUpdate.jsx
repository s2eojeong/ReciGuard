import React from "react";
import "./MypageHome.css";
import Header2 from "../../components/Header2";
import Footer from "../../components/Footer";
import Sidebar1 from "../../components/Mypage/Sidebar1";
import HomeSide3 from "../../components/Mypage/HomeSide3";
import AlleUpdate from "../../components/Mypage/AlleUpdate.jsx";

function AllergyUpdate() {
  return (
    <div>
      <div style={{ borderBottom: "2px solid #ddd" }}>
        <Header2 />
      </div>
      <div className="MypageHome-Container">
        <Sidebar1 />
        <HomeSide3 />
         <AlleUpdate/>
      </div>
      <Footer />
    </div>
  );
}

export default AllergyUpdate;
