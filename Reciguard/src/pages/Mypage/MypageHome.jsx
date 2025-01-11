import React from "react";
import "./MypageHome.css";
import Header2 from "../../components/Header2";
import Footer from "../../components/Footer";
import Sidebar1 from "../../components/Mypage/Sidebar1";
import HomeSide1 from "../../components/Mypage/HomeSide1";

function MypageHome() {
  return (
    <div>
      <div style={{ borderBottom: "2px solid #ddd" }}>
        <Header2 />
      </div>
      <div className="MypageHome-Container">
        <Sidebar1 />
        <HomeSide1 />
      </div>
      <Footer />
    </div>
  );
}

export default MypageHome;
