import React from "react";
import Header2 from "../../components/Header2";
import Footer from "../../components/Footer";
import Sidebar3 from "../../components/Mypage/Sidebar3";
import MypageMyRecipe from "../../components/Mypage/MypageMyRecipe.jsx";

function MypageRecipe() {
  return (
      <div>
          <div style={{borderBottom: "2px solid #ddd"}}>
              <Header2/>
          </div>
          <div className="MypageHome-Container">
              <Sidebar3/>
              <MypageMyRecipe/>
          </div>
          <Footer/>
      </div>
  );
}

export default MypageRecipe;
