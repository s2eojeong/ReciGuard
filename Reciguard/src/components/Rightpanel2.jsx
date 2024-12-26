import React from "react";
import "./Rightpanel2.css";

function Rightpanel2() {

return (
    <div className="right-panel">
        <h2>Create your account</h2>
        <p className='easy'>It's easy and free</p>
        <form className="login-form">
        <div className="form-group">
            <label htmlFor="name">이름</label>
            <input
            type="name"
            id="name"
            placeholder="Enter your name"
            required/>
        </div>
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
        </div>       
        </div>
        <div className="checkbox-group">
        <input
            type="checkbox"
            className="checkbox-input"
            required/>
        <label className="checkbox-label">
        가입 시 개인정보 수집 및 이용에 동의합니다.
        </label>
        </div>
        <button type="submit" className="sign-up-btn">Tell us more!</button>
        </form>
        <div>
        <p className="sign-in">
        Already have an account? <a href="#">Sign in</a>
        </p>
        </div>
    </div>
);
}

export default Rightpanel2;
