import React from "react";
import "./Login.css";
import Leftpanel from "../components/LoginSignup/Leftpanel";
import Rightpanel from "../components/LoginSignup/Rightpanel";

function Login() {
  return (
    <div className="login-container">
      <Leftpanel />
      <Rightpanel />
    </div>
  );
}

export default Login;
