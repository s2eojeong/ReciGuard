import React from "react";
import Header2 from "../../components/Header2";
import Footer from "../../components/Footer";
import MyScrap from "../../components/Mypage/MyScrap.jsx";
import Sidebar2 from "../../components/Mypage/Sidebar2.jsx";


function MypageScrap() {
  return (
    <div>
      <div style={{ borderBottom: "2px solid #ddd" }}>
        <Header2 />
      </div>
        <div className="MypageHome-Container">
            <Sidebar2/>
            <MyScrap />
        </div>
        <Footer/>
    </div>
  );
}

export default MypageScrap;
