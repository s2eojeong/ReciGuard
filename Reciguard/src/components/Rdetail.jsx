import React, { useState } from "react";
import "./Rdetail.css";
import chicken from "../assets/닭도리탕.jpeg";
import 몇인분 from "../assets/몇인분.png";
import 국가별 from "../assets/국가별.png";
import 조회수 from "../assets/조회수.png";
import filledHeartImg from "../assets/heart1.png";
import emptyHeartImg from "../assets/heart.png";
import 해당순서 from "../assets/해당순서.png";

const Rdetail = () => {
  const [isScrapped, setIsScrapped] = useState(false); // 스크랩 여부 상태
  const [scrapCount, setScrapCount] = useState(168); // 초기 스크랩 수

  const handleScrapClick = () => {
    if (isScrapped) {
      setScrapCount(scrapCount - 1); // 스크랩 해제 시 1 감소
    } else {
      setScrapCount(scrapCount + 1); // 스크랩 시 1 증가
    }
    setIsScrapped(!isScrapped); // 상태 반전
  };
  return (
    /* 레시피 제목 컨테이너 */
    <div className="rdetail-container">
      <div className="rdetail-header">
        <img
          src={chicken}
          alt="매콤 칼칼한 닭도리탕"
          className="rdetail-image"
        />
        <h2 className="rdetail-title">매콤 칼칼한 닭도리탕</h2>
        <hr class="divider-line" />
        <div className="rdetail-icons">
          <div className="icon-container">
            <div className="icon-item">
              <img src={몇인분} alt="인원수" className="icon-image" />
              <span className="icon-text">3인분</span>
            </div>
            <div className="icon-item">
              <img src={국가별} alt="국가별" className="icon-image" />
              <span className="icon-text">한식</span>
            </div>
            <div className="icon-item">
              <img src={조회수} alt="조회수" className="icon-image" />
              <span className="icon-text">1,618</span>
            </div>
          </div>
          <div className="favorite-container">
            <button className="favorite-button" onClick={handleScrapClick}>
              <img
                src={isScrapped ? filledHeartImg : emptyHeartImg}
                alt="스크랩 버튼"
                className="heart-image"
              />
            </button>
            <span className="scrap-text">스크랩 {scrapCount}</span>
          </div>
        </div>
      </div>

      <div className="ingredient-stpes">
        <h3>재료</h3>
        <div className="ingredient-container">
          <section className="ingredient-section">
            <div className="ingredients-list">
              <div className="ingredient1-row">
                <div className="ingredient-texts">
                  <span className="ingredient-name">닭</span>
                  <span className="ingredient-quantity">1마리</span>
                </div>
                <hr />
              </div>
              <div className="ingredient1-row">
                <div className="ingredient-texts">
                  <span className="ingredient-name">당근</span>
                  <span className="ingredient-quantity">1/3개</span>
                </div>
                <hr />
              </div>
              <div className="ingredient1-row">
                <div className="ingredient-texts">
                  <span className="ingredient-name">양파</span>
                  <span className="ingredient-quantity">1/2개</span>
                </div>
                <hr />
              </div>
              <div className="ingredient1-row">
                <div className="ingredient-texts">
                  <span className="ingredient-name">감자</span>
                  <span className="ingredient-quantity">3개</span>
                </div>
                <hr />
              </div>
              <div className="ingredient1-row">
                <div className="ingredient-texts">
                  <span className="ingredient-name">대파</span>
                  <span className="ingredient-quantity">4개</span>
                </div>
                <hr />
              </div>
              <div className="ingredient1-row">
                <div className="ingredient-texts">
                  <span className="ingredient-name">청양고추</span>
                  <span className="ingredient-quantity">4개</span>
                </div>
                <hr />
              </div>
              <div className="ingredient1-row">
                <div className="ingredient-texts">
                  <span className="ingredient-name">고추장</span>
                  <span className="ingredient-quantity">2큰술</span>
                </div>
                <hr />
              </div>
              <div className="ingredient1-row">
                <div className="ingredient-texts">
                  <span className="ingredient-name">고추가루</span>
                  <span className="ingredient-quantity">7큰술</span>
                </div>
                <hr />
              </div>
            </div>
          </section>
        </div>

        <h3>요리순서</h3>
        <div className="recipe-container">
          <section className="recipe-steps-section">
            <div className="step-container">
              <div className="stepss">
                <div className="step-label">Step 1</div>
                <div className="step-content">
                  <p>양파는 반을 자른 후 사각으로 썰어주세요</p>
                </div>
                <div className="instruction-image-box">
                  <img
                    src={해당순서}
                    alt="해당 순서 사진"
                    className="instruction-image"
                  />
                </div>
              </div>
              <div className="stepss">
                <div className="step-label">Step 2</div>
                <div className="step-content">
                  <p>
                    고추장2큰술 고추가루7큰술 설탕2큰술 국간장5큰술 마늘1큰술
                    넣고 소주잔으로 물1컵 넣고 섞어 주세요.
                  </p>
                </div>
                <div className="instruction-image-box">
                  <img
                    src={해당순서}
                    alt="해당 순서 사진"
                    className="instruction-image"
                  />
                </div>
              </div>
            </div>
          </section>
        </div>
      </div>
      <div className="nutrition-stpes">
        <h3>영양 성분</h3>
        <div className="nutrition-container">
          <section className="nutrition-section">
            <div className="nutritions-list">
              <div className="nutrition1-row">
                <div className="nutrition-texts">
                  <span className="nutrition-name">서빙 사이즈</span>
                  <span className="nutrition-quantity">100g</span>
                </div>
                <hr />
              </div>
              <div className="nutrition1-row">
                <div className="nutrition-texts">
                  <span className="nutrition-name">열량</span>
                  <span className="nutrition-quantity">197kcal</span>
                </div>
                <hr />
              </div>
              <div className="nutrition1-row">
                <div className="nutrition-texts">
                  <span className="nutrition-name">탄수화물</span>
                  <span className="nutrition-quantity">1.73g</span>
                </div>
                <hr />
              </div>
              <div className="nutrition1-row">
                <div className="nutrition-texts">
                  <span className="nutrition-name">단백질</span>
                  <span className="nutrition-quantity">10.24g</span>
                </div>
                <hr />
              </div>
              <div className="nutrition1-row">
                <div className="nutrition-texts">
                  <span className="nutrition-name">지방</span>
                  <span className="nutrition-quantity">15.18g</span>
                </div>
                <hr />
              </div>
            </div>
          </section>
        </div>
      </div>
    </div>
  );
};

export default Rdetail;
