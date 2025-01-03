import React from 'react';
import './Myrec.css';
import { Link } from 'react-router-dom';

const Myrec = () => {
    return (
        <div className="myrec-container">
            <div className="myrec-box">
                <p className="myrec-subtitle">“ 오늘은 내가 요리사 ”</p>
                <h2 className="myrec-title">나만의 레시피, 나만의 꿀팁을 공유해주세요!</h2>
                <Link to="/Register">
                <button className="myrec-button">등록하러 가기 &gt;</button>
                </Link>
            </div>
        </div>
    );
};

export default Myrec;
