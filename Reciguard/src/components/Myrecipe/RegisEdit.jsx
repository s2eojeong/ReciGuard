import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import "./RegisEdit.css";
import image2 from "../../assets/foodupload.png"

const RegisEdit = () => {
    const { recipeId } = useParams();
    const navigate = useNavigate();

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [formData, setFormData] = useState({
        recipeForm: {
            recipeName: "",
            serving: "",
            cuisine: "",
            foodType: "",
            cookingStyle: "",
            ingredients: [],
            instructions: [],
        },
        recipeImage: null, // 메인 이미지 파일
        imagePath: "", // API에서 불러온 메인 이미지 경로
        instructionImageFiles: [],
    });

    useEffect(() => {
        const fetchRecipeData = async () => {
            try {
                const token = localStorage.getItem("jwtToken");
                if (!token) {
                    alert("로그인이 필요합니다.");
                    navigate("/login");
                    return;
                }

                const response = await fetch(
                    `http://localhost:8080/api/recipes/myrecipe/${recipeId}/edit`,
                    {
                        method: "GET",
                        headers: {
                            Authorization: `Bearer ${token.replace("Bearer ", "")}`,
                        },
                    }
                );

                if (!response.ok) {
                    const errorText = await response.text();
                    throw new Error(
                        `API 요청 실패 (HTTP ${response.status}): ${errorText}`
                    );
                }

                const data = await response.json();
                setFormData({
                    recipeForm: {
                        recipeName: data.recipeName,
                        serving: data.serving,
                        cuisine: data.cuisine,
                        foodType: data.foodType,
                        cookingStyle: data.cookingStyle,
                        ingredients: data.ingredients,
                        instructions: data.instructions.map((instruction) => ({
                            ...instruction,
                            instructionImage: instruction.instructionImage || "", // 조리 방법 이미지 경로
                        })),
                    },
                    recipeImage: null, // 새로 업로드된 파일 처리
                    imagePath: data.imagePath || "",
                    instructionImageFiles: [],
                });
            } catch (err) {
                console.error("데이터를 불러오는 중 에러 발생:", err.message);
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        fetchRecipeData();
    }, [recipeId, navigate]);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => ({
            ...prev,
            recipeForm: {
                ...prev.recipeForm,
                [name]: value,
            },
        }));
    };

    const handleIngredientChange = (index, field, value) => {
        setFormData((prev) => {
            const updatedIngredients = [...prev.recipeForm.ingredients];
            updatedIngredients[index][field] = value;
            return {
                ...prev,
                recipeForm: {
                    ...prev.recipeForm,
                    ingredients: updatedIngredients,
                },
            };
        });
    };

    const handleIngredientDelete = (index) => {
        setFormData((prev) => {
            const updatedIngredients = prev.recipeForm.ingredients.filter(
                (_, i) => i !== index
            );
            return {
                ...prev,
                recipeForm: {
                    ...prev.recipeForm,
                    ingredients: updatedIngredients,
                },
            };
        });
    };

    const handleIngredientAdd = () => {
        setFormData((prev) => ({
            ...prev,
            recipeForm: {
                ...prev.recipeForm,
                ingredients: [
                    ...prev.recipeForm.ingredients,
                    { ingredient: "", quantity: "" }, // 새로운 빈 재료 추가
                ],
            },
        }));
    };

    const handleInstructionAdd = () => {
        setFormData((prev) => ({
            ...prev,
            recipeForm: {
                ...prev.recipeForm,
                instructions: [
                    ...prev.recipeForm.instructions,
                    { instruction: "", instructionImage: image2 },
                ],
            },
            instructionImageFiles: [...prev.instructionImageFiles, null], // 새로운 파일 추가
        }));
    };

    const handleFileChange = (e) => {
        const file = e.target.files[0];
        const { name } = e.target;

        if (file) {
            const reader = new FileReader();
            reader.onload = () => {
                if (name === "recipeImage") {
                    setFormData((prev) => ({
                        ...prev,
                        recipeImage: file,
                        imagePath: reader.result, // 미리보기 이미지 설정
                    }));
                } else {
                    const index = parseInt(name.replace("instructionImageFiles[", "").replace("]", ""), 10);
                    setFormData((prev) => {
                        const updatedFiles = [...prev.instructionImageFiles];
                        updatedFiles[index] = file;

                        const updatedInstructions = [...prev.recipeForm.instructions];
                        if (updatedInstructions[index]) {
                            updatedInstructions[index].instructionImage = reader.result; // 미리보기 이미지 설정
                        }

                        return {
                            ...prev,
                            instructionImageFiles: updatedFiles,
                            recipeForm: {
                                ...prev.recipeForm,
                                instructions: updatedInstructions,
                            },
                        };
                    });
                }
            };
            reader.readAsDataURL(file);
        }
    }

    const handleSubmit = async () => {
        try {
            const token = localStorage.getItem("jwtToken");
            if (!token) {
                alert("로그인이 필요합니다.");
                return;
            }

            const { recipeForm, recipeImage, instructionImageFiles } = formData;

            const formattedInstructions = recipeForm.instructions.map((instruction, index) => ({
                instructionId: instruction.instructionId || index + 1,
                instruction: instruction.instruction,
                imageRemoved: !instruction.instructionImage, // 이미지가 없는 경우 제거 처리
            }));

            const payloadForm = new FormData();
            payloadForm.append(
                "recipeForm",
                JSON.stringify({
                    ...recipeForm,
                    instructions: formattedInstructions,
                })
            );
            if (recipeImage) payloadForm.append("recipeImage", recipeImage);
            instructionImageFiles.forEach((file, index) => {
                if (file) payloadForm.append(`instructionImageFiles[${index + 1}]`, file);
            });

            const response = await fetch(
                `http://localhost:8080/api/recipes/myrecipe/${recipeId}/edit`,
                {
                    method: "POST",
                    headers: {
                        Authorization: `Bearer ${token.replace("Bearer ", "")}`,
                    },
                    body: payloadForm,
                }
            );

            const contentType = response.headers.get("content-type");
            let result;

            if (contentType && contentType.includes("application/json")) {
                result = await response.json();
            } else {
                result = await response.text(); // JSON이 아닌 경우 텍스트로 처리
            }

            if (!response.ok) {
                throw new Error(result.message || `API 요청 실패 (HTTP ${response.status})`);
            }

            alert(result.message || result || "레시피가 성공적으로 수정되었습니다.");
            navigate(`/users/myrecipes`);
        } catch (err) {
            console.error("레시피 수정 중 에러 발생:", err.message);
            alert(`수정 중 오류가 발생했습니다: ${err.message}`);
        }
    };

    return (
        <div className="recipe-edit-container">
            <form className="recipe-edit-form" onSubmit={(e) => {
                e.preventDefault();
                handleSubmit();
            }}>
                <div className="regis-container">
                    <div className="regis-header">레시피 소개</div>
                    <div className="regis-form">
                        <div className="regis-left">
                            <div className="regis-group">
                                <label className="regis-label-1">
                                    레시피명
                                    <input
                                        type="text"
                                        name="recipeName"
                                        className="regis-input-1"
                                        value={formData.recipeForm.recipeName}
                                        onChange={handleInputChange}
                                    />
                                </label>
                            </div>
                            <div className="regis-group">
                                <label htmlFor="category" className="regis-label">
                                    카테고리
                                </label>
                                <select
                                    name="cuisine"
                                    className="regis-select"
                                    value={formData.recipeForm.cuisine}
                                    onChange={handleInputChange}
                                >
                                    <option value="">선택하세요</option>
                                    {["한식", "중식", "양식", "아시안", "일식"].map((value) => (
                                        <option key={value} value={value}>{value}</option>
                                    ))}
                                </select>
                                <select
                                    name="foodType"
                                    className="regis-select"
                                    value={formData.recipeForm.foodType}
                                    onChange={handleInputChange}
                                >
                                    <option value="">선택하세요</option>
                                    {["밥", "면", "떡", "죽", "반찬"].map((value) => (
                                        <option key={value} value={value}>{value}</option>
                                    ))}
                                </select>
                                <select
                                    name="cookingStyle"
                                    className="regis-select"
                                    value={formData.recipeForm.cookingStyle}
                                    onChange={handleInputChange}
                                >
                                    <option value="">선택하세요</option>
                                    {["찌기", "끓이기", "굽기", "볶기", "튀기기", "기타"].map((value) => (
                                        <option key={value} value={value}>{value}</option>
                                    ))}
                                </select>
                            </div>
                            <div className="regis-group">
                                <label className="regis-label">요리정보</label>
                                <div className="regis-info">
                                    <select
                                        name="serving"
                                        className="regis-select small"
                                        value={formData.recipeForm.serving}
                                        onChange={handleInputChange}
                                    >
                                        <option value="">선택하세요</option>
                                        {[1, 2, 3, 4, 5, 6].map((value) => (
                                            <option key={value} value={value}>{value}인분</option>
                                        ))}
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div className="regis-right">
                            <label className="form-label">
                                {formData.imagePath && (
                                    <img
                                        src={formData.imagePath}
                                        alt="메인 이미지"
                                        className="recipe-main-image"
                                    />
                                )}
                                <input
                                    type="file"
                                    name="recipeImage"
                                    style={{display: "none"}}
                                    onChange={handleFileChange}
                                />
                            </label>
                        </div>
                    </div>
                </div>
                <div className="regis-container-1">
                    <div className="regis-header">재료 정보</div>
                    <div className="regis2-note">
                        재료가 남거나 부족하지 않도록 정확한 계량정보를 적어주세요.
                    </div>
                    <div className="regis2-form">
                        {formData.recipeForm.ingredients.map((ingredient, index) => (
                            <div key={index} className="ingredient-row">
                                <input
                                    type="text"
                                    placeholder="재료 이름"
                                    value={ingredient.ingredient}
                                    onChange={(e) => handleIngredientChange(index, "ingredient", e.target.value)}
                                />
                                <input
                                    type="text"
                                    placeholder="수량"
                                    value={ingredient.quantity}
                                    onChange={(e) => handleIngredientChange(index, "quantity", e.target.value)}
                                />
                                <button
                                    type="button"
                                    className="ingredient-delete"
                                    onClick={() => handleIngredientDelete(index)}
                                >
                                    X
                                </button>
                            </div>
                        ))}
                        <button
                            type="button"
                            className="ingredient-add-1"
                            onClick={handleIngredientAdd}
                        >
                            추가
                        </button>
                    </div>
                </div>
                <div className="regis-container-1">
                    <div className="regis3-header">요리순서</div>
                    <div className="regis3-note">
                        요리 맛이 좌우될 수 있는 중요한 부분을 빠짐없이 적어주세요.
                    </div>
                    <div className="regis3-form">
                        {formData.recipeForm.instructions.map((instruction, index) => (
                            <div key={index} className="instruction-section">
                                <div className="step-number">Step {index + 1}</div>
                                <textarea
                                    className="step-description"
                                    name={`instructionText[${index}]`}
                                    value={instruction.instruction}
                                    onChange={(e) => {
                                        const updatedInstructions = [...formData.recipeForm.instructions];
                                        updatedInstructions[index].instruction = e.target.value;
                                        setFormData((prev) => ({
                                            ...prev,
                                            recipeForm: {
                                                ...prev.recipeForm,
                                                instructions: updatedInstructions,
                                            },
                                        }));
                                    }}
                                />
                                <label className="form-label">
                                    {instruction.instructionImage && (
                                        <img
                                            src={instruction.instructionImage}
                                            alt="조리 이미지"
                                            className="instruction-image"
                                        />
                                    )}
                                    <input
                                        type="file"
                                        name={`instructionImageFiles[${index}]`}
                                        style={{display: "none"}}
                                        onChange={handleFileChange}
                                    />
                                </label>
                            </div>
                        ))}
                        <button
                            type="button"
                            className="add-step-button"
                            onClick={handleInstructionAdd}
                        >
                            추가
                        </button>
                    </div>
                </div>
                <button type="submit" className="myrecipe-edit">수정하기</button>
            </form>
        </div>
    );
};

export default RegisEdit;




