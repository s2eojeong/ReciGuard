import React, { useEffect, useState } from 'react';
import axios from 'axios';

const InfoUpdate = () => {
    const [userInfo, setUserInfo] = useState({
        username: '',
        gender: '',
        age: '',
        weight: '',
        email: '',
        password: '',
        userCookingStyle: [],
        userCuisine: [],
        userFoodType: [],
    });
    const [message, setMessage] = useState('');
    const [error, setError] = useState('');

    // 사용자 정보 GET 요청
    useEffect(() => {
        const token = localStorage.getItem('jwtToken');
        if (!token) {
            setError('로그인이 필요합니다.');
            return;
        }

        axios
            .get('http://localhost:8080/api/users/info', {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            })
            .then((response) => {
                setUserInfo(response.data); // 사용자 정보를 상태에 저장
                console.log('회원정보 로드 성공:', response.data);
            })
            .catch((err) => {
                setError('회원 정보를 불러오지 못했습니다.');
                console.error(err);
            });
    }, []);

    // 사용자 정보 PUT 요청 (수정)
    const handleUpdate = async (e) => {
        e.preventDefault();
        const token = localStorage.getItem('jwtToken');
        if (!token) {
            setError('로그인이 필요합니다.');
            return;
        }

        try {
            const response = await axios.put(
                'http://localhost:8080/api/users/info',
                userInfo, // 수정할 정보를 요청 바디로 전달
                {
                    headers: {
                        'Content-Type': 'application/json',
                        Authorization: `Bearer ${token}`,
                    },
                }
            );

            setMessage(response.data); // 성공 메시지
            console.log('회원정보 수정 성공:', response.data);
        } catch (err) {
            setError('회원정보를 수정하지 못했습니다.');
            console.error(err);
        }
    };

    // 입력 변경 핸들러
    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setUserInfo((prev) => ({
            ...prev,
            [name]: value,
        }));
    };

    // 다중 선택 항목 변경 핸들러 (배열 필드용)
    const handleMultiSelectChange = (e) => {
        const { name, value } = e.target;
        setUserInfo((prev) => ({
            ...prev,
            [name]: prev[name].includes(value)
                ? prev[name].filter((item) => item !== value) // 이미 선택된 항목이면 제거
                : [...prev[name], value], // 새 항목 추가
        }));
    };

    return (
        <div>
            <h2>회원 정보 수정</h2>
            {error && <p className="error-message">{error}</p>}
            {message && <p className="success-message">{message}</p>}
            <form onSubmit={handleUpdate}>
                <div className="form-group">
                    <label htmlFor="username">이름</label>
                    <input
                        type="text"
                        id="username"
                        name="username"
                        value={userInfo.username}
                        onChange={handleInputChange}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="gender">성별</label>
                    <select
                        id="gender"
                        name="gender"
                        value={userInfo.gender}
                        onChange={handleInputChange}
                        required
                    >
                        <option value="" disabled>
                            선택해주세요
                        </option>
                        <option value="남성">남성</option>
                        <option value="여성">여성</option>
                    </select>
                </div>
                <div className="form-group">
                    <label htmlFor="age">나이</label>
                    <input
                        type="number"
                        id="age"
                        name="age"
                        value={userInfo.age}
                        onChange={handleInputChange}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="weight">체중</label>
                    <input
                        type="number"
                        id="weight"
                        name="weight"
                        value={userInfo.weight}
                        onChange={handleInputChange}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="email">이메일</label>
                    <input
                        type="email"
                        id="email"
                        name="email"
                        value={userInfo.email}
                        onChange={handleInputChange}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="password">비밀번호</label>
                    <input
                        type="password"
                        id="password"
                        name="password"
                        placeholder="비밀번호를 입력해주세요"
                        onChange={handleInputChange}
                    />
                </div>

                <div className="form-group">
                    <label>조리 방식</label>
                    {['볶기', '찌기', '굽기'].map((style) => (
                        <label key={style}>
                            <input
                                type="checkbox"
                                name="userCookingStyle"
                                value={style}
                                checked={userInfo.userCookingStyle.includes(style)}
                                onChange={handleMultiSelectChange}
                            />
                            {style}
                        </label>
                    ))}
                </div>

                <div className="form-group">
                    <label>국가별</label>
                    {['한식', '양식', '일식'].map((cuisine) => (
                        <label key={cuisine}>
                            <input
                                type="checkbox"
                                name="userCuisine"
                                value={cuisine}
                                checked={userInfo.userCuisine.includes(cuisine)}
                                onChange={handleMultiSelectChange}
                            />
                            {cuisine}
                        </label>
                    ))}
                </div>

                <button type="submit">회원정보 수정</button>
            </form>
        </div>
    );
};

export default InfoUpdate;
