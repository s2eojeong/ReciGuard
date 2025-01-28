import React from "react";
import Header2 from "../components/Header2";
import Footer from "../components/Footer";
import Recommend from "../components/Mainpage/Recommend";
import Morerec from "../components/Mainpage/Morerec";
import Myrec from "../components/Mainpage/Myrec";

function Realmain() {
  return (
    <div>
      <Header2 />
      <Recommend />
      <Morerec />
      <Myrec />
      <Footer />
    </div>
  );
}

export default Realmain;
