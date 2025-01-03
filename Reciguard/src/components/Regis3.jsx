import React, { useState } from "react";
import image2 from "../assets/요리순서업로드.png";
import "./Regis3.css";

const Regis3 = () => {
    const [steps, setSteps] = useState([
        { id: 1, description: "예) 닭은 껍데기에 지방이 붙어있어 지방 섭취가 싫으신 분들은 미리 닭껍질을 손질해서 준비해주세요.", image: image2, isExample: true }
    ]);

    const addStep = () => {
        setSteps([
            ...steps, 
            { id: Date.now(), description: "", image: image2, isExample: false }
        ]);
    };

    const handleInputChange = (id, field, value) => {
        setSteps((prevSteps) =>
            prevSteps.map((step) =>
                step.id === id ? { ...step, [field]: value } : step
            )
        );
    };

    const handleInputFocus = (id, field) => {
        setSteps((prevSteps) =>
            prevSteps.map((step) =>
                step.id === id && step.isExample
                    ? { ...step, [field]: "" }
                    : step
            )
        );
    };

    const handleImageClick = (id) => {
        document.getElementById(`file-upload-${id}`).click();
    };

    const handleImageUpload = (id, file) => {
        setSteps((prevSteps) =>
            prevSteps.map((step) =>
                step.id === id ? { ...step, image: URL.createObjectURL(file) } : step
            )
        );
    };

    const removeStep = (id) => {
        setSteps((prevSteps) => 
            prevSteps.filter((step) => step.id !== id)
        );
    };

    return (
        <div className="regis3-wrapper">
            <div className='regis3-container'>
            <div className="regis3-header">요리순서</div>
            <div className="regis3-note">요리 맛이 좌우될 수 있는 중요한 부분을 빠짐없이 적어주세요.</div>
            <div className="regis3-form">
                {steps.map((step, index) => (
                    <div key={step.id} className="step-row">
                        <div className="step-number">Step{index + 1}</div>
                        <textarea
                            className="step-description"
                            placeholder="순서 설명을 작성해주세요."
                            value={step.description}
                            onFocus={() => handleInputFocus(step.id, "description")}
                            onChange={(e) => handleInputChange(step.id, "description", e.target.value)}
                        />
                        <div className="step-image-upload" onClick={() => handleImageClick(step.id)}>
                            {step.image ? (
                                <img
                                    src={step.image}
                                    alt="Uploaded"
                                    className="step-image"
                                />
                            ) : (
                                <div className="step-image-placeholder">
                                    + 사진 추가
                                </div>
                            )}
                            <input
                                id={`file-upload-${step.id}`}
                                type="file"
                                accept="image/*"
                                style={{ display: "none" }}
                                onChange={(e) => handleImageUpload(step.id, e.target.files[0])}
                            />
                        </div>
                        <button
                            className="step-delete"
                            onClick={() => removeStep(step.id)}
                        >
                            X
                        </button>
                    </div>
                ))}
            </div>
            <button className="step-add" onClick={addStep}>
                추가
            </button>
            </div>
            <button className='register-button'>등록하기</button>
        </div>
    );
};

export default Regis3;
