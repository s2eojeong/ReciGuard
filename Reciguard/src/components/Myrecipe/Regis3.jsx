import React, { useState } from "react";
import image2 from "../../assets/요리순서업로드.png";
import "./Regis3.css";

const Regis3 = ({ onDataUpdate = () => {} }) => {
  const [steps, setSteps] = useState([
    {
      id: 1,
      description:
          "예) 닭은 껍데기에 지방이 붙어있어 지방 섭취가 싫으신 분들은 미리 닭껍질을 손질해서 준비해주세요.",
      image: image2,
      imageRemoved: false,
      isExample: true,
    },
  ]);

  const addStep = () => {
    const maxId = steps.length > 0 ? Math.max(...steps.map((step) => step.id)) : 0;
    const updatedSteps = [
      ...steps,
      {
        id: maxId + 1, // 새로운 ID 생성
        description: "",
        image: image2, // 기본 이미지 경로 설정
        imageRemoved: false,
        isExample: false,
      },
    ];
    setSteps(updatedSteps);
    sendDataToParent(updatedSteps); // 부모로 데이터 전달
  };

  const handleInputChange = (id, field, value) => {
    const updatedSteps = steps.map((step) =>
        step.id === id ? { ...step, [field]: value } : step
    );
    setSteps(updatedSteps);
    sendDataToParent(updatedSteps); // 부모로 데이터 전달
  };

  const handleInputFocus = (id, field) => {
    const updatedSteps = steps.map((step) =>
        step.id === id && step.isExample ? { ...step, [field]: "" } : step
    );
    setSteps(updatedSteps);
    sendDataToParent(updatedSteps); // 부모로 데이터 전달
  };

  const handleImageClick = (id) => {
    document.getElementById(`file-upload-${id}`).click();
  };

  const handleImageUpload = (id, file) => {
    const updatedSteps = steps.map((step) => {
      if (step.id === id) {
        // 기존 이미지 URL 해제 (File일 경우)
        if (step.image && step.image instanceof File) {
          URL.revokeObjectURL(step.image.preview);
        }

        // 새 이미지 설정
        const newImage = file;
        newImage.preview = URL.createObjectURL(file); // 미리보기 URL 생성

        return { ...step, image: newImage, imageRemoved: false };
      }
      return step;
    });

    setSteps(updatedSteps);
    sendDataToParent(updatedSteps); // 부모로 데이터 전달
  };

  const renderImage = (step) => {
    if (step.image instanceof File) {
      // 업로드된 이미지 미리보기 URL 반환
      return step.image.preview;
    }
    if (step.image) {
      // 기본 이미지 경로 반환
      return step.image;
    }
    // 이미지가 없을 경우 null 반환
    return null;
  };

  const removeStep = (id) => {
    const updatedSteps = steps.filter((step) => {
      if (step.id === id && step.image instanceof File) {
        // 이미지 URL 해제
        URL.revokeObjectURL(step.image.preview);
      }
      return step.id !== id;
    });

    setSteps(updatedSteps);
    sendDataToParent(updatedSteps); // 부모로 데이터 전달
  };

  const sendDataToParent = (updatedSteps) => {
    const instructions = updatedSteps.map((step) => ({
      instructionId: step.id,
      instruction: step.description,
      image: step.image instanceof File ? step.image : null, // File 객체만 전달
      imageRemoved: step.imageRemoved, // 삭제 여부
    }));
    onDataUpdate({ instructions });
  };

  return (
      <div className="regis3-wrapper">
        <div className="regis3-container">
          <div className="regis3-header">요리순서</div>
          <div className="regis3-note">
            요리 맛이 좌우될 수 있는 중요한 부분을 빠짐없이 적어주세요.
          </div>
          <div className="regis3-form">
            {steps.map((step, index) => (
                <div key={step.id} className="step-row">
                  <div className="step-number">Step {index + 1}</div>
                  <textarea
                      className="step-description"
                      placeholder="순서 설명을 작성해주세요."
                      value={step.description}
                      onFocus={() => handleInputFocus(step.id, "description")}
                      onChange={(e) =>
                          handleInputChange(step.id, "description", e.target.value)
                      }
                  />
                  <div className="step-image-upload" onClick={() => handleImageClick(step.id)}>
                    <img
                        src={renderImage(step)}
                        alt="Uploaded"
                        className="step-image"
                    />
                    <input
                        id={`file-upload-${step.id}`}
                        type="file"
                        accept="image/*"
                        style={{display: "none"}}
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
      </div>
  );
};

export default Regis3;
