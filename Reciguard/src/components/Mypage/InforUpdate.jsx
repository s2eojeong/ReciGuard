import React, { useEffect, useState } from 'react';
import { useNavigate } from "react-router-dom";
import axios from 'axios';
import "./InforUpdate.css";

const InforUpdate = () => {
    const navigate = useNavigate();
    const passwordUp = () => {
        navigate("/users/password");
    };

    const [userData, setUserData] = useState({
        username: '',
        gender: '',
        age: '',
        weight: '',
        email: '',
        userCuisine: [],
        userFoodType: [],
        userCookingStyle: [],
    });
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
                })
                .catch((err) => {
                    console.error('GET 요청 에러:', err.response || err.message);
                    setError('사용자 정보를 가져오지 못했습니다.');
                });
        } else {
            setError('잘못된 토큰입니다.');
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

    // 에러가 있을 경우 에러 메시지 출력
    if (error) return <p>{error}</p>;

    // 데이터가 로드되지 않았을 경우 기본 메시지 출력
    if (!userData.username) {
        return  <div className="userinfo-container"></div>;
    }

    return (
        <div className="inforupdate-container">
            <h1 className="inforupdate-h1">회원정보 수정</h1>
            <div className="userinfo-container1">
                <div className="inforupdate-item">
                    <label>아이디</label>
                    <span>{userData.username}</span>
                </div>
                <div className="inforupdate-gender">
                    <label>성별</label>
                    <label className="inforupdate-radio">
                        <input
                            type="radio"
                            name="gender"
                            value="남성"
                            checked={userData.gender === '남성'}
                            onChange={handleChange}
                        />
                        남성
                    </label>
                    <label className="inforupdate-radio">
                        <input
                            type="radio"
                            name="gender"
                            value="여성"
                            checked={userData.gender === '여성'}
                            onChange={handleChange}
                        />
                        여성
                    </label>
                </div>
            </div>
            <div className="userinfo-container1">
                <div className="inforupdate-item">
                    <label>이메일</label>
                    <span>{userData.email}</span>
                </div>
                <div className="inforupdate-item3">
                    <label>나이</label>
                    <input
                        type="number"
                        name="age"
                        value={userData.age}
                        onChange={handleChange}
                    />
                    년생
                </div>
            </div>
            <div className="userinfo-container1">
                <div className="inforupdate-item2">
                    <label>비밀번호</label>
                    <span>********
                        <button onClick={passwordUp}>수정</button>
                    </span>
                </div>
                <div className="inforupdate-item3">
                    <label>체중</label>
                    <input
                        type="number"
                        name="weight"
                        value={userData.weight}
                        onChange={handleChange}
                    />
                    kg
                </div>
            </div>

            <fieldset className="preferences-container">
                <legend>
                    <span className="preferences-container-write">선호도</span>
                </legend>
                <div className="preference-group">
                    <label className="preference-group-head">국가별</label>
                    <div className="checkbox-group2-country">
                        {["한식", "중식", "일식", "양식", "아시안"].map((style) => (
                            <label key={style}>
                                <input
                                    type="checkbox"
                                    value={style}
                                    checked={userData.userCuisine.includes(style.trim())}
                                    onChange={(e) => {
                                        const value = e.target.value;
                                        const updatedCuisine = e.target.checked
                                            ? [...userData.userCuisine, value]
                                            : userData.userCuisine.filter((item) => item !== value);
                                        setUserData((prevData) => ({
                                            ...prevData,
                                            userCuisine: updatedCuisine,
                                        }));
                                    }}
                                />
                                {style}
                            </label>
                        ))}
                    </div>
                </div>
                <div className="preference-group">
                    <label className="preference-group-head">식사 유형</label>
                    <div className="checkbox-group2">
                        {["밥", "면", "떡", "죽", "반찬"].map((type) => (
                            <label key={type}>
                                <input
                                    type="checkbox"
                                    value={type}
                                    checked={userData.userFoodType.includes(type.trim())}
                                    onChange={(e) => {
                                        const value = e.target.value;
                                        const updatedFoodTypes = e.target.checked
                                            ? [...userData.userFoodType, value]
                                            : userData.userFoodType.filter((item) => item !== value);
                                        setUserData((prevData) => ({
                                            ...prevData,
                                            userFoodType: updatedFoodTypes,
                                        }));
                                    }}
                                />
                                {type}
                            </label>
                        ))}
                    </div>
                </div>
                <div className="preference-group">
                    <label className="preference-group-head">조리 방식</label>
                    <div className="checkbox-group2">
                        {["찌기", "끓이기", "굽기", "볶기", "튀기기", "기타"].map((method) => (
                            <label key={method}>
                                <input
                                    type="checkbox"
                                    value={method}
                                    checked={userData.userCookingStyle.includes(method.trim())}
                                    onChange={(e) => {
                                        const value = e.target.value;
                                        const updatedMethods = e.target.checked
                                            ? [...userData.userCookingStyle, value]
                                            : userData.userCookingStyle.filter((item) => item !== value);
                                        setUserData((prevData) => ({
                                            ...prevData,
                                            userCookingStyle: updatedMethods,
                                        }));
                                    }}
                                />
                                {method}
                            </label>
                        ))}
                    </div>
                </div>
            </fieldset>
            <button onClick={handleUpdate} className="inforupdate-update">수정하기</button>
        </div>
    );
};

export default InforUpdate;
