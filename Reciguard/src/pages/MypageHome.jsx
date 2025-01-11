import React from "react";
import Header2 from "../components/Header2";
import Footer from "../components/Footer";
import Sidebar1 from "../components/Mypage/Sidebar1";

function MypageHome() {
  return (
    <div>
      <div style={{ borderBottom: "2px solid #ddd" }}>
        <Header2 />
      </div>
      <Sidebar1 />
      <Footer />
    </div>
  );
}

export default MypageHome;
