import React from "react";
import "./Rightpanel.css";
import { Link } from 'react-router-dom';

function Rightpanel() {
return (
    <div className="right-panel">
        <h2>Welcome!</h2>
        <p className='meet'>Meet the good taste today</p>
        <form className="login-form">
        <div className="form-group">
            <label htmlFor="email">E-mail</label>
            <input
            type="email"
            id="email"
            placeholder="Type your e-mail"
            required/>
        </div>
        <div className="form-group">
            <label htmlFor="password">Password</label>
            <div className="input-container">
            <input
            type="password"
            id="password"
            placeholder="Type your password"
            required/>
            <a href="#" className="forgot-password">비밀번호 찾기</a>
            </div>
        </div>
        <button type="submit" className="log-in-btn">Log In</button>
        </form>
        <div>
        <p className="sign-up">
        Don’t have an account? 
        <Link  to="/Signup">
        <a href="#">Sign Up</a>
        </Link>
        </p>
        </div>
    </div>
);
}

export default Rightpanel;
