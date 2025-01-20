import React, { useEffect, useState } from "react";
import axios from "axios";
import "./AlleUpdate.css";

const AlleUpdate = () => {
  const [userData, setUserData] = useState({
    ingredients: [], // 배열로 초기화
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // JWT 토큰에서 username과 userid를 추출하는 함수
  const getUserInfoFromToken = (token) => {
    try {
      const payload = JSON.parse(atob(token.split(".")[1])); // JWT payload 디코딩
      console.log("디코딩된 토큰:", payload); // 디버깅 로그
      if (!payload.username || !payload.userid) {
        throw new Error("유효하지 않은 토큰입니다.");
      }
      return { username: payload.username, userid: payload.userid };
    } catch (err) {
      console.error("Invalid token:", err);
      return null;
    }
  };

  useEffect(() => {
    const token = localStorage.getItem("jwtToken"); // 로컬 스토리지에서 JWT 토큰 가져오기

    if (!token) {
      setError("로그인이 필요합니다.");
      setLoading(false);
      return;
    }

    const userInfo = getUserInfoFromToken(token);

    if (userInfo) {
      const { userid } = userInfo;

      axios
        .get(
          `https://reciguard.comhttps://reciguard.com/users/allergy/${userid}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        )
        .then((response) => {
          console.log("서버 응답 데이터:", response.data);

          const ingredients = response.data.map((item) =>
            item.ingredientName.trim()
          ); // 배열로 변환
          setUserData({ ingredients });
          setLoading(false);
        })
        .catch((err) => {
          console.error("GET 요청 에러:", err.response || err.message);
          setError("사용자 정보를 가져오지 못했습니다.");
          setLoading(false);
        });
    } else {
      setError("잘못된 토큰입니다.");
      setLoading(false);
    }
  }, []);

  const handleUpdate = () => {
    const token = localStorage.getItem("jwtToken"); // JWT 토큰 가져오기
    if (!token) {
      setError("로그인이 필요합니다.");
      return;
    }

    const userInfo = getUserInfoFromToken(token); // 토큰에서 사용자 정보 추출

    if (userInfo) {
      const { userid } = userInfo;

      const userIngredientListDTO = {
        ingredients: userData.ingredients, // 이미 배열이므로 변환 불필요
      };

      axios
        .post(
          `https://reciguard.comhttps://reciguard.com/users/allergy/${userid}`,
          userIngredientListDTO,
          {
            headers: {
              Authorization: `Bearer ${token}`, // 올바른 위치에 헤더 추가
              "Content-Type": "application/json", // Content-Type 명시
            },
          }
        )
        .then((response) => {
          console.log("서버 응답 데이터:", response.data);
          alert("사용자 정보가 성공적으로 업데이트되었습니다.");
        })
        .catch((err) => {
          console.error("에러:", err);
          setError("사용자 정보를 업데이트하지 못했습니다.");
        });
    }
  };

  if (error) return <p>{error}</p>;

  return (
    <div className="alleupdate-container">
      <h1 className="alleupdate-h1">알레르기 정보 수정</h1>
      <fieldset className="allergy-container">
        <legend>알레르기</legend>
        {[
          { category: "유제품", items: ["우유"] },
          { category: "곡물류", items: ["밀가루", "귀리", "호밀가루"] },
          { category: "견과류", items: ["호두", "아몬드", "땅콩", "참깨"] },
          { category: "콩류", items: ["대두"] },
          { category: "육류", items: ["돼지고기", "닭고기", "소고기", "계란"] },
          {
            category: "해산물",
            items: [
              "고등어",
              "연어",
              "참치",
              "꽃게",
              "새우",
              "오징어",
              "굴",
              "전복",
              "홍합",
              "조개",
            ],
          },
          { category: "과일류", items: ["복숭아", "키위"] },
          { category: "해당없음", items: ["해당없음"] },
        ].map((group) => (
          <div key={group.category} className="allergy-group">
            <label className="allergy-category">{group.category}</label>
            <div className="checkbox-group">
              {group.items.map((item) => (
                <label key={item}>
                  <input
                    type="checkbox"
                    value={item}
                    checked={userData.ingredients.includes(item)}
                    onChange={(e) => {
                      const value = e.target.value;
                      const updatedIngredients = e.target.checked
                        ? [...userData.ingredients, value]
                        : userData.ingredients.filter(
                            (ingredient) => ingredient !== value
                          );
                      setUserData((prevData) => ({
                        ...prevData,
                        ingredients: updatedIngredients,
                      }));
                    }}
                  />
                  {item}
                </label>
              ))}
            </div>
          </div>
        ))}
      </fieldset>
      <button onClick={handleUpdate} className="update-button">
        수정하기
      </button>
    </div>
  );
};

export default AlleUpdate;
