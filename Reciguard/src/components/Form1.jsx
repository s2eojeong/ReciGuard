import React, { useState } from 'react';
import './Form1.css';

const Form1 = () => {

    const [preferences, setPreferences] = useState({});

    const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target;
    setPreferences((prev) => ({
    ...prev,
    [name]: type === "checkbox" ? checked : value,
    }));
};

    const handleGenderChange = (e) => {
    setPreferences((prev) => ({
    ...prev,
    gender: e.target.value,
    }));
};

    const handleSubmit = (e) => {
    e.preventDefault();
    console.log(preferences);
};

return (
    <div className='form-page'>
    <form className="reciguard-form" onSubmit={handleSubmit}>
        <h1>ReciGaurd</h1>

        {/* Gender */}
        <div className="group">
        <label>성별</label>
        <div className="gender-options">
        <label>
        <input type="radio" name="gender" value="male" onChange={handleGenderChange} /> 남자
        </label>
        <label>
        <input type="radio" name="gender" value="female" onChange={handleGenderChange} /> 여자
        </label>
        </div>
        </div>

        {/* Age */}
        <div className="group">
        <label>나이</label>
        <div className='year'>
        <select id="age" onChange={handleInputChange} defaultValue="">
        <option value="" disabled>
        
        </option>
            {Array.from({ length: 116 }, (_, i) => 1910 + i).map((year) => (
            <option key={year} value={year}>
            {year}
            </option>
        ))}
        </select>
        년생
        </div>
        </div>

        {/* Weight */}
        <div className="group">
        <label>체중</label>
        <div className='kg'>
        <input className='weight' type="number" id="weight" step="0.1" onChange={handleInputChange} /> kg
        </div>
        </div>

        {/* Preferences */}
        <fieldset className="preferences-section">
        <legend>
            <span className='pre'>선호도</span>좋아하는 종류를 선택해주세요. (복수 선택 가능)
            </legend>
        <div className="preferences-grid">
            <div>
            <label className='type'>국가별</label>
            <div className='country'>
                <label className="category-label"><input type="checkbox" name="country" value="한식" onChange={handleInputChange} /> 한식</label>
                <label className="category-label"><input type="checkbox" name="country" value="중식" onChange={handleInputChange} /> 중식</label>
                <label className="category-label"><input type="checkbox" name="country" value="일식" onChange={handleInputChange} /> 일식</label>
                <label className="category-label"><input type="checkbox" name="country" value="양식" onChange={handleInputChange} /> 양식</label>
                <label className="category-label"><input type="checkbox" name="country" value="아시안" onChange={handleInputChange} /> 아시안</label>
            </div>
            </div>
            <div>
            <label className='type'>식사 유형</label>
            <div>
                <label className="category-label"><input type="checkbox" name="mealType" value="밥" onChange={handleInputChange} /> 밥</label>
                <label className="category-label"><input type="checkbox" name="mealType" value="면" onChange={handleInputChange} /> 면</label>
                <label className="category-label"><input type="checkbox" name="mealType" value="떡" onChange={handleInputChange} /> 떡</label>
                <label className="category-label"><input type="checkbox" name="mealType" value="죽" onChange={handleInputChange} /> 죽</label>
                <label className="category-label"><input type="checkbox" name="mealType" value="반찬" onChange={handleInputChange} /> 반찬</label>
            </div>
            </div>
            <div>
            <label className='type'>조리 방식</label>
            <div>
                <label className="category-label"><input type="checkbox" name="cookingStyle" value="구이" onChange={handleInputChange} /> 구이</label>
                <label className="category-label"><input type="checkbox" name="cookingStyle" value="볶음" onChange={handleInputChange} /> 볶음</label>
                <label className="category-label"><input type="checkbox" name="cookingStyle" value="튀김" onChange={handleInputChange} /> 튀김</label>
                <label className="category-label"><input type="checkbox" name="cookingStyle" value="찜" onChange={handleInputChange} /> 찜</label>
                <label className="category-label"><input type="checkbox" name="cookingStyle" value="국" onChange={handleInputChange} /> 국</label>
                <label className="category-label"><input type="checkbox" name="cookingStyle" value="찌개" onChange={handleInputChange} /> 찌개</label>
            </div>
            </div>
        </div>
        </fieldset>

        <div className="group">
            <label>알레르기</label>
        </div>

    </form>
    </div>   
    );
};

export default Form1;
