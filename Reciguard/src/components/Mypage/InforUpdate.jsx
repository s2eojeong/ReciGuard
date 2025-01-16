import React, { useEffect, useState } from 'react';
import axios from 'axios';

const InforUpdate = () => {
    const [userData, setUserData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // JWT 토큰에서 username과 userid를 추출하는 함수
    const getUserInfoFromToken = (token) => {
        try {
            const payload = JSON.parse(atob(token.split('.')[1])); // JWT payload 디코딩
            return { username: payload.username, userid: payload.userid};
        } catch (err) {
            console.error('Invalid token', err);
            return null;
        }
    };

    useEffect(() => {
        const token = localStorage.getItem('jwtToken'); // 로컬 스토리지에서 JWT 토큰 가져오기
        console.log('JWT 토큰:', token);
        if (!token) {
            setError('로그인이 필요합니다.');
            setLoading(false);
            return;
        }

        const userInfo = getUserInfoFromToken(token);

        if (userInfo) {
            const { userid } = userInfo;
            axios
                .post(
                    `http://localhost:8080/api/users/info/${userid}`,
                    {}, // POST 요청 본문 (데이터를 보내지 않는 경우 빈 객체)
                    {
                        headers: {
                            Authorization: `Bearer ${token}`, // 인증 헤더에 JWT 토큰 포함
                        },
                    }
                )
                .then((response) => {
                    console.log("응답 데이터:", response.data);
                    setUserData(response.data);
                    setLoading(false);
                })
                .catch((err) => {
                    console.error("에러:", err);
                    setError('사용자 정보를 불러오지 못했습니다.');
                    setLoading(false);
                });

        } else {
            setError('잘못된 토큰입니다.');
            setLoading(false);
        }
    }, []);

    if (loading) return <p>로딩 중...</p>;
    if (error) return <p>{error}</p>;

    return (
        <div>
            <h1>나의 계정</h1>
            <p><strong>이름:</strong> {userData.username}</p>
            <p><strong>성별:</strong> {userData.gender}</p>
            <p><strong>나이:</strong> {userData.age}년생</p>
            <p><strong>체중:</strong> {userData.weight}kg</p>
            <p><strong>이메일:</strong> {userData.email}</p>
        </div>
    );
};

export default InforUpdate;
