import React from "react";
import "./Login.css";
import Leftpanel from '../components/Leftpanel';
import Rightpanel from '../components/Rightpanel';
import Footer from '../components/footer'

function Login() {
    return (
    <div className="login-container">
        <Leftpanel />
        <Rightpanel />
    </div>
    );
}

export default Login;
