import React from "react";
import Header2 from "../../components/Header2";
import Footer from "../../components/Footer";
import Sidebar2 from "../../components/Mypage/Sidebar2";

function MypageScrap() {
  return (
    <div>
      <div style={{ borderBottom: "2px solid #ddd" }}>
        <Header2 />
      </div>
      <Sidebar2 />
      <Footer />
    </div>
  );
}

export default MypageScrap;
