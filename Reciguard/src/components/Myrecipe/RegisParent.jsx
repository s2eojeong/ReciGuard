import React, { useState, useEffect } from "react";
import "./RegisParent.css";
import Regis1 from "./Regis1";
import Regis2 from "./Regis2";
import Regis3 from "./Regis3";

const RegisParent = () => {
  useEffect(() => {
    console.log("로컬 스토리지 토큰:", localStorage.getItem("jwtToken"));
  }, []);

  const [formData, setFormData] = useState({
    recipeForm: {
      recipeName: "",
      cuisine: "",
      foodType: "",
      cookingStyle: "",
      serving: "",
      ingredients: [],
      instructions: [],
    },
    recipeImage: null, // 메인 레시피 이미지 파일
  });

  // Regis1, Regis2, Regis3에서 전달받은 데이터 업데이트
  const handleDataUpdate = (data) => {
    setFormData((prev) => ({
      ...prev,
      recipeForm: { ...prev.recipeForm, ...data.recipeForm },
      recipeImage: data.recipeImage || prev.recipeImage,
    }));
  };

  const isTokenExpired = (token) => {
    try {
      const [, payload] = token.split(".");
      const decodedPayload = JSON.parse(atob(payload));
      const now = Math.floor(Date.now() / 1000); // 현재 시간 (초 단위)
      return decodedPayload.exp < now; // 만료 시간(exp)과 비교
    } catch (error) {
      console.error("토큰 파싱 중 오류 발생:", error.message);
      return true; // 오류가 발생하면 만료된 것으로 간주
    }
  };

  const handleSubmit = async () => {
    try {
      const token = localStorage.getItem("jwtToken");
      if (!token) {
        alert("로그인이 필요합니다.");
        return;
      }

      if (isTokenExpired(token)) {
        alert("로그인 세션이 만료되었습니다. 다시 로그인해주세요.");
        return;
      }

      const { recipeForm, recipeImage } = formData;

      // ingredients 데이터 변환
      const formattedIngredients = recipeForm.ingredients.map((item) => ({
        ingredient: item.selectedIngredient?.label || "", // 선택된 재료명
        quantity: item.quantity || "", // 수량
      }));

      // instructions 데이터 변환
      const formattedInstructions = recipeForm.instructions.map(
        (instruction) => ({
          instructionId: instruction.instructionId,
          instruction: instruction.instruction,
          imageRemoved: instruction.imageRemoved || false,
        })
      );

      // recipeForm 데이터 가공
      const formattedRecipeForm = {
        recipeName: recipeForm.recipeName,
        serving: parseInt(recipeForm.serving, 10), // 정수 변환
        cuisine: recipeForm.cuisine,
        foodType: recipeForm.foodType,
        cookingStyle: recipeForm.cookingStyle,
        ingredients: formattedIngredients,
        instructions: formattedInstructions,
      };

      // FormData 생성
      const payloadForm = new FormData();
      payloadForm.append("recipeForm", JSON.stringify(formattedRecipeForm));

      // recipeImage 추가
      if (recipeImage instanceof File) {
        payloadForm.append("recipeImage", recipeImage);
      }

      // instructionImageFiles 추가
      recipeForm.instructions.forEach((instruction, index) => {
        if (instruction.image instanceof File) {
          payloadForm.append(
            `instructionImageFiles[${index + 1}]`,
            instruction.image
          );
        }
      });

      // FormData 디버깅
      console.log("FormData 내용 확인:");
      payloadForm.forEach((value, key) => {
        if (value instanceof File) {
          console.log(`${key}: [파일] ${value.name}`);
        } else {
          console.log(`${key}:`, value);
        }
      });

      // API 요청
      const response = await fetch(
        "https://reciguard.com/api/recipes/myrecipe/save",
        {
          method: "POST",
          headers: {
            Authorization: `Bearer ${token.replace("Bearer ", "")}`,
          },
          body: payloadForm,
        }
      );

      // 응답 처리
      const contentType = response.headers.get("Content-Type");

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(
          `API 요청 실패 (HTTP ${response.status}): ${errorText}`
        );
      }

      if (contentType && contentType.includes("application/json")) {
        const result = await response.json();
        alert(result.message || "레시피가 성공적으로 등록되었습니다.");
      } else {
        const resultText = await response.text();
        alert(resultText); // 텍스트 응답 처리
      }
    } catch (error) {
      console.error("에러 발생:", error.message);
      alert(`요청 중 오류가 발생했습니다: ${error.message}`);
    }
  };

  return (
    <div>
      <Regis1
        onDataUpdate={(data) =>
          handleDataUpdate({
            recipeForm: { ...formData.recipeForm, ...data },
            recipeImage: data.recipeImage || formData.recipeImage,
          })
        }
      />
      <Regis2
        onDataUpdate={(data) =>
          handleDataUpdate({
            recipeForm: {
              ...formData.recipeForm,
              ingredients: data.ingredients,
            },
          })
        }
      />
      <Regis3
        onDataUpdate={(data) =>
          handleDataUpdate({
            recipeForm: {
              ...formData.recipeForm,
              instructions: data.instructions,
            },
          })
        }
      />
      <div className="myrecipe-register-div">
        <button onClick={handleSubmit} className="myrecipe-register">
          등록하기
        </button>
      </div>
    </div>
  );
};

export default RegisParent;
