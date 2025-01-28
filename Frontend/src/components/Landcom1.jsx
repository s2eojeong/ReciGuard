import React from "react";
import {useEffect} from "react";
import "./Landcom1.css";
import landing1 from "../assets/landing1.png";
import landing2 from "../assets/landing2.png";
import landing3 from "../assets/landing3.png";

const Landcom1 = () => {
    useEffect(() => {
      const observerOptions = {
        threshold: 0.2, // 요소가 20% 이상 화면에 나타날 때 트리거
      };

      const observer = new IntersectionObserver((entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            entry.target.classList.add("visible");
          }
        });
      }, observerOptions);

      const elements = document.querySelectorAll(".hidden"); // hidden 클래스를 가진 모든 요소 선택
      elements.forEach((el) => observer.observe(el)); // 각 요소에 옵저버 적용

      return () => observer.disconnect(); // 컴포넌트가 언마운트될 때 옵저버 해제
    }, []);

    return (
    <div className="landing-page">
      <header className="landing-header">
        <h1 className="main-heading hidden">가장 효과적인 식품 알레르기 대처 방안</h1>
        <h2 className="sub-heading hidden">“알레르기 발생 식품 피하기”</h2>
      </header>
      <section className="main-content">
        <div className="reciguard-info">
          <h1 className="content-title">ReciGuard</h1>
        </div>
          <div className="features hidden">
            <div className="feature-item">
              <img src={landing1} alt="경고 아이콘" className="feature-icon"/>
            </div>
            <p className="features-p">
              <strong className="features-strong">알레르기 유발 식품 경고</strong>
              <br/>
              개인 맞춤형 필터링
            </p>
          </div>

          <div className="features hidden">
            <p className="features-p">
              <strong className="features-strong">회원가입 설문지 기반</strong>
              <br/>
              추천 레시피 제공
            </p>
            <div className="feature-item">
              <img src={landing2} alt="추천 아이콘" className="feature-icon"/>
            </div>
          </div>

          <div className="features hidden">
            <div className="feature-item">
              <img src={landing3} alt="공유 아이콘" className="feature-icon"/>
            </div>
            <p className="features-p">
              <strong className="features-strong">나만의 레시피 공유</strong>
              <br/>
              다른 유저의 레시피 참고
            </p>
          </div>
      </section>
    </div>
  );
};

export default Landcom1;
