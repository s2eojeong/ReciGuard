import React, { useState, useEffect } from "react";
import axios from "axios";

const PasswordUpdate = () => {
    const [formData, setFormData] = useState({
        username: "",
        userid: "",
        currentPassword: "",
        newPassword: "",
    });
    const [message, setMessage] = useState(null);
    const [error, setError] = useState(null);

    // JWT 토큰에서 사용자 정보 추출
    const decodeToken = (token) => {
        try {
            const payload = JSON.parse(atob(token.split(".")[1])); // JWT Payload 디코딩
            return { userid: payload.userid }; // username과 userid 추출
        } catch (err) {
            console.error("토큰 디코딩 실패:", err);
            return null;
        }
    };

    // 컴포넌트 마운트 시 username과 userid 설정
    useEffect(() => {
        const token = localStorage.getItem("jwtToken");
        if (!token) {
            setError("로그인이 필요합니다.");
            return;
        }

        const userInfo = decodeToken(token);
        if (userInfo) {
            setFormData((prevData) => ({
                ...prevData,
                username: userInfo.username,
                userid: userInfo.userid,
            }));
        } else {
            setError("유효하지 않은 토큰입니다. 다시 로그인해주세요.");
        }
    }, []);

    // 입력 값 변경 핸들러
    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    // 비밀번호 수정 요청 핸들러
    const handleSubmit = async (e) => {
        e.preventDefault(); // 기본 폼 동작 방지
        setMessage(null);
        setError(null);

        const token = localStorage.getItem("jwtToken"); // JWT 토큰 가져오기
        if (!token) {
            setError("로그인이 필요합니다.");
            return;
        }

        try {
            const response = await axios.post(
                "http://localhost:8080/api/users/password",
                {
                    username: formData.username,
                    currentPassword: formData.currentPassword,
                    newPassword: formData.newPassword,
                },
                {
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: `Bearer ${token}`,
                    },
                }
            );
            if (response.status === 200) {
                setMessage(response.data.message); // 성공 메시지 설정
            }
        } catch (err) {
            console.error("비밀번호 변경 오류:", err);
            setError(err.response?.data?.message || "비밀번호 변경 중 오류가 발생했습니다.");
        }
    };

    return (
        <div>
            <h1>비밀번호 변경</h1>
            {error && <p style={{ color: "red" }}>{error}</p>}
            {message && <p style={{ color: "green" }}>{message}</p>}
            {!error && (
                <form onSubmit={handleSubmit}>
                    <div>
                        <label htmlFor="username">사용자 이름</label>
                        <input
                            type="text"
                            id="username"
                            name="username"
                            value={formData.username}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <div>
                        <label htmlFor="currentPassword">현재 비밀번호</label>
                        <input
                            type="password"
                            id="currentPassword"
                            name="currentPassword"
                            value={formData.currentPassword}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <div>
                        <label htmlFor="newPassword">새 비밀번호</label>
                        <input
                            type="password"
                            id="newPassword"
                            name="newPassword"
                            value={formData.newPassword}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <button type="submit">변경하기</button>
                </form>
            )}
        </div>
    );
};

export default PasswordUpdate;
