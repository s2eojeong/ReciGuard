import React, { useEffect, useRef } from "react";
import { useNavigate } from "react-router-dom";
import "./Landcom2.css";

const Landcom2 = () => {
    const containerRef = useRef(null); // cta-container 참조
    const navigate = useNavigate();

    useEffect(() => {
        const observerOptions = {
            threshold: 0.2, // 요소가 20% 이상 화면에 나타날 때 트리거
        };

        // Intersection Observer 콜백
        const observer = new IntersectionObserver((entries) => {
            entries.forEach((entry) => {
                if (entry.isIntersecting) {
                    entry.target.classList.add("visible");
                }
            });
        }, observerOptions);

        // containerRef 내의 hidden 클래스를 가진 요소에 옵저버 적용
        const elements = containerRef.current.querySelectorAll(".hidden");
        elements.forEach((el) => observer.observe(el));

        return () => observer.disconnect(); // 옵저버 해제
    }, []);

    const handleClick = () => {
        navigate("/register"); // /register 경로로 이동
    };

    return (
        <div className="cta-container" ref={containerRef}>
            <h2 className="cta-heading hidden">
                안전한 레시피를 얻고 싶은 당신, <br />
                지금 바로 시작하세요!
            </h2>
            <button className="cta-button hidden" onClick={handleClick}>무료로 시작하기</button>
        </div>
    );
};

export default Landcom2;
