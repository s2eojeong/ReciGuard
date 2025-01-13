import React, { useEffect, useState } from "react";

function UserInfo() {
    const [userInfo, setUserInfo] = useState(null);
    const [error, setError] = useState("");

    useEffect(() => {
        const fetchUserInfo = async () => {
            try {
                // LocalStorage에서 JWT 토큰 가져오기
                const token = localStorage.getItem("jwtToken");
                if (!token) {
                    setError("로그인이 필요합니다.");
                    return;
                }

                const response = await fetch("http://localhost:8080/api/users/${userId}", {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": `Bearer ${token}`, // JWT 토큰 추가
                    },
                });

                if (response.ok) {
                    const data = await response.json();
                    setUserInfo(data); // 유저 정보 저장
                } else {
                    setError("사용자 정보를 가져오는데 실패했습니다.");
                }
            } catch (error) {
                setError("서버 오류가 발생했습니다.");
            }
        };

        fetchUserInfo();
    }, []);

    if (error) return <div>{error}</div>;
    if (!userInfo) return <div>로딩 중...</div>;

    return (
        <div>
            <h2>My Page</h2>
            <p>이름: {userInfo.username}</p>
            <p>이메일: {userInfo.email}</p>
            <p>역할: {userInfo.role}</p>
        </div>
    );
}

export default UserInfo;
