import React from "react";
import "./Signup.css";
import Leftpanel from "../components/LoginSignup/Leftpanel";
import Rightpanel2 from "../components/LoginSignup/Rightpanel2";

function Signup() {
  return (
    <div className="login-container">
      <Leftpanel />
      <Rightpanel2 />
    </div>
  );
}

export default Signup;
