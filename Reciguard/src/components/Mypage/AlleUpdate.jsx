import React, { useEffect, useState } from 'react';
import axios from 'axios';
import "./AlleUpdate.css"

const AlleUpdate = () => {
    const [userData, setUserData] = useState({
        ingredients: "",
    });
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // JWT 토큰에서 username과 userid를 추출하는 함수
    const getUserInfoFromToken = (token) => {
        try {
            const payload = JSON.parse(atob(token.split('.')[1])); // JWT payload 디코딩
            console.log('디코딩된 토큰:', payload); // 디버깅 로그
            if (!payload.username || !payload.userid) {
                throw new Error('유효하지 않은 토큰입니다.');
            }
            return { username: payload.username, userid: payload.userid };
        } catch (err) {
            console.error('Invalid token:', err);
            return null;
        }
    };

    useEffect(() => {
        const token = localStorage.getItem("jwtToken"); // 로컬 스토리지에서 JWT 토큰 가져오기
        console.log("사용 중인 토큰:", token);

        if (!token) {
            setError("로그인이 필요합니다.");
            setLoading(false);
            return;
        }

        const userInfo = getUserInfoFromToken(token);

        if (userInfo) {
            const { userid } = userInfo;

            axios
                .get(`http://localhost:8080/api/users/allergy/${userid}`, {
                    headers: {
                        Authorization: `Bearer ${token}`, // 이 헤더가 포함되어야 합니다.
                    },
                })
                .then((response) => {
                    console.log("서버 응답 데이터:", response.data);

                    // ingredientName만 추출하여 문자열로 변환
                    const ingredients = response.data
                        .map((item) => item.ingredientName.trim()) // ingredientName만 추출
                        .join(", "); // 문자열로 변환

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
        const token = localStorage.getItem('jwtToken'); // JWT 토큰 가져오기
        if (!token) {
            setError('로그인이 필요합니다.');
            return;
        }

        const userInfo = getUserInfoFromToken(token); // 토큰에서 사용자 정보 추출

        if (userInfo) {
            const { userid } = userInfo;

            const userIngredientListDTO = {
                ingredients: userData.ingredients.split(",").map((item) => item.trim()), // 쉼표로 구분된 문자열을 배열로 변환
            };

            axios
                .post(
                    `http://localhost:8080/api/users/allergy/${userid}`,
                    userIngredientListDTO,
                    {
                        headers: {
                            Authorization: `Bearer ${token}`, // 올바른 위치에 헤더 추가
                            'Content-Type': 'application/json', // Content-Type 명시
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

    const handleChange = (e) => {
        const { name, value } = e.target;
        setUserData((prevData) => ({
            ...prevData,
            [name]: value, // 문자열 그대로 설정
        }));
    };

    if (loading) return <p></p>;
    if (error) return <p>{error}</p>;

    return (
        <div className="alleupdate-container">
            <h1 className="alleupdate-h1">알레르기 정보 수정</h1>
            <div>
                <label>알레르기</label>
                <input
                    type="text"
                    name="ingredients"
                    value={userData.ingredients}
                    onChange={handleChange}
                />
            </div>
            <button onClick={handleUpdate}>정보 업데이트</button>
        </div>
    );
};

export default AlleUpdate;
