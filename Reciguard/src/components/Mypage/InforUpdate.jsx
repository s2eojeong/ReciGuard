import React, { useEffect, useState } from 'react';
import {useNavigate} from "react-router-dom";
import axios from 'axios';

const InforUpdate = () => {

    const navigate=useNavigate();
    const passwordUp = () => {
        navigate("/users/password")
    }

    const [userData, setUserData] = useState({
        username: '',
        gender: '',
        age: '',
        weight: '',
        email: '',
        userCookingStyle: [],
        userCuisine: [],
        userFoodType: [],
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
        const token = localStorage.getItem('jwtToken'); // 로컬 스토리지에서 JWT 토큰 가져오기
        if (!token) {
            setError('로그인이 필요합니다.');
            setLoading(false);
            return;
        }

        const userInfo = getUserInfoFromToken(token);

        if (userInfo) {
            const { userid } = userInfo;

            axios
                .get(`http://localhost:8080/api/users/info/${userid}`, {
                    headers: {
                        Authorization: `Bearer ${token}`, // 이 헤더가 포함되어야 합니다.
                    },
                })
                .then((response) => {
                    console.log('서버 응답 데이터:', response.data);
                    setUserData(response.data);
                    setLoading(false);
                })
                .catch((err) => {
                    console.error('GET 요청 에러:', err.response || err.message);
                    setError('사용자 정보를 가져오지 못했습니다.');
                    setLoading(false);
                });

        } else {
            setError('잘못된 토큰입니다.');
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

            axios
                .put(
                    `http://localhost:8080/api/users/info/${userid}`,
                    userData, // 업데이트할 데이터
                    {
                        headers: {
                            Authorization: `Bearer ${token}`, // 올바른 위치에 헤더 추가
                            'Content-Type': 'application/json', // Content-Type 명시
                        },
                    }
                )
                .then((response) => {
                    alert('사용자 정보가 성공적으로 업데이트되었습니다.');
                    setUserData(response.data); // 서버에서 반환된 데이터로 업데이트
                })
                .catch((err) => {
                    console.error('에러:', err);
                    setError('사용자 정보를 업데이트하지 못했습니다.');
                });
        }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setUserData((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    };

    if (loading) return <p>로딩 중...</p>;
    if (error) return <p>{error}</p>;

    return (
        <div>
            <h1>나의 계정</h1>
            <div>
                <label>이름:</label>
                <input
                    type="text"
                    name="username"
                    value={userData.username}
                    readOnly
                />
            </div>
            <div onClick={passwordUp}>
                비밀번호:******** <button>수정</button>
            </div>
            <div>
                <label>성별:</label>
                <input
                    type="text"
                    name="gender"
                    value={userData.gender}
                    onChange={handleChange}
                />
            </div>
            <div>
                <label>나이:</label>
                <input
                    type="number"
                    name="age"
                    value={userData.age}
                    onChange={handleChange}
                />
            </div>
            <div>
                <label>체중:</label>
                <input
                    type="number"
                    name="weight"
                    value={userData.weight}
                    onChange={handleChange}
                />
            </div>
            <div>
                <label>이메일:</label>
                <input
                    type="email"
                    name="email"
                    value={userData.email}
                    readOnly
                />
            </div>
            <div>
                <label>국가별:</label>
                <input
                    type="text"
                    name="userCookingStyle"
                    value={userData.userCookingStyle.join(", ")} // 배열 데이터를 문자열로 변환
                    onChange={(e) => {
                        const value = e.target.value.split(",").map((item) => item.trim()); // 쉼표로 구분된 문자열을 배열로 변환
                        setUserData((prevData) => ({
                            ...prevData,
                            userCookingStyle: value,
                        }));
                    }}
                />
            </div>
            <div>
                <label>식사 유형:</label>
                <input
                    type="text"
                    name="userCuisine"
                    value={userData.userCuisine.join(", ")} // 배열 데이터를 문자열로 변환
                    onChange={(e) => {
                        const value = e.target.value.split(",").map((item) => item.trim()); // 쉼표로 구분된 문자열을 배열로 변환
                        setUserData((prevData) => ({
                            ...prevData,
                            userCuisine: value,
                        }));
                    }}
                />
            </div>
            <div>
                <label>조리 방식:</label>
                <input
                    type="text"
                    name="userFoodType"
                    value={userData.userFoodType.join(", ")} // 배열 데이터를 문자열로 변환
                    onChange={(e) => {
                        const value = e.target.value.split(",").map((item) => item.trim()); // 쉼표로 구분된 문자열을 배열로 변환
                        setUserData((prevData) => ({
                            ...prevData,
                            userFoodType: value,
                        }));
                    }}
                />
            </div>
            <button onClick={handleUpdate}>정보 업데이트</button>
        </div>
    );
};

export default InforUpdate;
