import React, { useState } from "react";
import "./Regis2.css";

const Regis2 = () => {
    const [ingredients, setIngredients] = useState([
        { id: 1, name: "예) 돼지고기 앞다리살", quantity: "예) 300g", isExample: true },
        { id: 2, name: "예) 양파", quantity: "예) 1/2개", isExample: true },
        { id: 3, name: "예) 신김치", quantity: "예) 1/4개", isExample: true },
    ]);

    const addIngredient = () => {
        setIngredients([
            ...ingredients, 
            { id: Date.now(), name: "", quantity: "", isExample: false } // 새 항목은 예시가 아님
        ]);
    };

    const handleInputChange = (id, field, value) => {
        setIngredients((prevIngredients) =>
            prevIngredients.map((ingredient) =>
                ingredient.id === id ? { ...ingredient, [field]: value } : ingredient
            )
        );
    };

    const handleInputFocus = (id, field) => {
        setIngredients((prevIngredients) =>
            prevIngredients.map((ingredient) =>
                ingredient.id === id && ingredient.isExample
                    ? { ...ingredient, [field]: "" } // 예시 항목은 포커스 시 초기화
                    : ingredient
            )
        );
    };

    const removeIngredient = (id) => {
        setIngredients((prevIngredients) => 
            prevIngredients.filter((ingredient) => ingredient.id !== id)
        );
    };

    return (
        <div className='regis2-wrapper'>
            <div className="regis2-container">
                <div className='regis2-header'>재료 정보</div>
                <div className='regis2-note'>재료가 남거나 부족하지 않도록 정확한 계량정보를 적어주세요.</div>
                <div className='regis2-form'>
                    {ingredients.map((ingredient) => (
                        <div key={ingredient.id} className='ingredient-row'>
                            <input
                                type="text"
                                placeholder="재료 이름"
                                value={ingredient.name}
                                onFocus={() => handleInputFocus(ingredient.id, "name")}
                                onChange={(e) => handleInputChange(ingredient.id, "name", e.target.value)}
                            />
                            <input
                                type="text"
                                placeholder="계량"
                                value={ingredient.quantity}
                                onFocus={() => handleInputFocus(ingredient.id, "quantity")}
                                onChange={(e) => handleInputChange(ingredient.id, "quantity", e.target.value)}
                            />
                            <button 
                                className='ingredient-delete' 
                                onClick={() => removeIngredient(ingredient.id)}
                            >
                                x
                            </button>
                        </div>
                    ))}
                </div>
                <button className="ingredient-add" onClick={addIngredient}>
                    추가
                </button>
            </div>
        </div>
    );
};

export default Regis2;