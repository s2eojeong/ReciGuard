import React, { useState } from "react";
import Select from "react-select";
import "./Regis2.css";

const Regis2 = () => {
    const availableIngredients = [
        { value: "돼지고기 앞다리살", label: "돼지고기 앞다리살" },
        { value: "양파", label: "양파" },
        { value: "신김치", label: "신김치" },
        { value: "두부", label: "두부" },
        { value: "소고기", label: "소고기" },
        { value: "대파", label: "대파" },
        { value: "마늘", label: "마늘" },
        { value: "고춧가루", label: "고춧가루" },
    ];

    const [ingredients, setIngredients] = useState([
        { id: 1, selectedIngredient: null, quantity: "", isExample: true },
    ]);

    const addIngredient = () => {
        setIngredients([
            ...ingredients,
            { id: Date.now(), selectedIngredient: null, quantity: "", isExample: false },
        ]);
    };

    const handleSelectChange = (id, selectedOption) => {
        setIngredients((prevIngredients) =>
            prevIngredients.map((ingredient) =>
                ingredient.id === id
                    ? { ...ingredient, selectedIngredient: selectedOption }
                    : ingredient
            )
        );
    };

    const handleInputChange = (id, field, value) => {
        setIngredients((prevIngredients) =>
            prevIngredients.map((ingredient) =>
                ingredient.id === id ? { ...ingredient, [field]: value } : ingredient
            )
        );
    };

    const removeIngredient = (id) => {
        setIngredients((prevIngredients) =>
            prevIngredients.filter((ingredient) => ingredient.id !== id)
        );
    };

    return (
        <div className="regis2-wrapper">
            <div className="regis2-container">
                <div className="regis2-header">재료 정보</div>
                <div className="regis2-note">
                    재료가 남거나 부족하지 않도록 정확한 계량정보를 적어주세요.
                </div>
                <div className="regis2-form">
                    {ingredients.map((ingredient) => (
                        <div key={ingredient.id} className="ingredient-row">
                            <Select
                                options={availableIngredients}
                                placeholder="재료 검색"
                                value={ingredient.selectedIngredient}
                                onChange={(selectedOption) =>
                                    handleSelectChange(ingredient.id, selectedOption)
                                }
                                className="ingredient-select"
                            />
                            <input
                                type="text"
                                placeholder="계량"
                                value={ingredient.quantity}
                                onChange={(e) =>
                                    handleInputChange(ingredient.id, "quantity", e.target.value)
                                }
                            />
                            <button
                                className="ingredient-delete"
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
