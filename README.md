#  **🥨 ReciGuard**
### **식품 알레르기 기반 레시피 추천 서비스**

### **📅 개발 기간**

> 프로젝트 개발 기간: 2024.12.14 - 2025.01.21

### 🔗 **배포 링크**
[www.reciguard.com](https://reciguard.com)

<img width="513" alt="Image" src="https://github.com/user-attachments/assets/ce1c1940-ce77-4cef-b2ce-7bb06269e382" />

### 📹 **시연 영상**
[![Video Label](http://img.youtube.com/vi/8BFe5Di_Nco/0.jpg)](https://youtu.be/8BFe5Di_Nco)


## <span id="1">🍽️ 1. 프로젝트 소개</span>

> Recipe(레시피)와 Guard(지키다, 보호하다)를 결합한 이름으로 알레르기를 가진 사람들에게 안전한 레시피를 제공하고 보호한다는 의미

### 주요 제공 서비스

🍚 1. 다양한 레시피 제공

- 1,253개의 기존 레시피 데이터 확보

- 나만의 레시피 등록을 통한 추가 레시피 데이터 확보

⚠️ 2. 알레르기 성분 탐지

- 1차 필터링: "우유"를 기준으로 레시피 재료에서 정확히 일치하거나 해당 단어가 포함된 재료를 탐지 (예: "저지방우유")

- 2차 필터링: "우유"가 포함되지 않지만, 잠재적으로 관련이 있는 재료를 추가로 탐지 (예: "생크림", "버터", "치즈", "밀크" 등)

🤖 3. AI 기반 레시피 추천 알고리즘

- 사용자 프로필 기반 선호에 따른 맞춤형 레시피 추천

- 최신 트렌드에 따른 맞춤형 레시피 추천

### 📒 **ReciGuard 프로젝트 노션**
<a href="https://silk-desk-56e.notion.site/ReciGuard-Dash-board-1426a1f0a45f80868fa6e679de8522d3">
    <img src="https://upload.wikimedia.org/wikipedia/commons/e/e9/Notion-logo.svg" alt="Notion Logo" width="100" />
</a>

📌 **노션에서 개발 일정, API 명세서, 회의록 등을 확인할 수 있습니다.**

## <span id="2">🏃 2. 팀원 소개</span>

| 팀장 | 팀원 | 팀원 |
| :-----------------------------------------------------------------: | :--------------------------------------------------------------: | :------------------------------------------------------: |
| <img src="https://github.com/user-attachments/assets/e4c85b47-43bb-44e8-97e5-75ef7845d509" width="120px;" alt=""/> |      <img src="https://github.com/user-attachments/assets/379f4d65-e4ed-4d5e-a6af-ee21c287d3cd" width="120px;" alt=""/>      |  <img src="https://github.com/user-attachments/assets/e73dc212-db23-40f3-b993-2050097539a8" width="120px;" alt=""/>  |
|        [BE 조서정](https://github.com/s2eojeong)         |           [BE 한동범](https://github.com/Handongbeom)              |          [FE 김민주](https://github.com/juzzing)           |


## <span id="3">🛠️ 3. 기술 스택</span>
### ✔️Frond-end
<img src="https://img.shields.io/badge/react-61DAFB?style=for-the-badge&logo=react&logoColor=black"><img src="https://img.shields.io/badge/css-1572B6?style=for-the-badge&logo=css3&logoColor=white"><img src="https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white"> <img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black"><img src="https://img.shields.io/badge/vercel-000000?style=for-the-badge&logo=vercel&logoColor=white">
### ✔️Back-end
<img src="https://img.shields.io/badge/spring boot-6DB33F?style=for-the-badge&logo=spring boot&logoColor=white"><img src="https://img.shields.io/badge/spring security-6DB33F?style=for-the-badge&logo=spring security&logoColor=white"><img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"><img src="https://img.shields.io/badge/amazon s3-569A31?style=for-the-badge&logo=amazon s3&logoColor=white"><img src="https://img.shields.io/badge/amazon RDS-527FFF?style=for-the-badge&logo=amazon RDS&logoColor=white"><img src="https://img.shields.io/badge/amazon EC2-FF9900?style=for-the-badge&logo=amazon EC2&logoColor=white">

## <span id="4">🖇️ 4. 프로젝트 구조도</span>
### ✔️Frond-end
```
📦src
 ┣ 📂assets
 ┃ ┣ 📜Arrow.png
 ┃ ┣ 📜allService.png
 ┃ ┣ 📜allscrap.png
 ┃ ┣ 📜allscraps.png
 ┃ ┣ 📜allupdate.png
 ┃ ┣ 📜asian.jpg
 ┃ ┣ 📜china.png
 ┃ ┣ 📜count.png
 ┃ ┣ 📜cuisine.png
 ┃ ┣ 📜delete.png
 ┃ ┣ 📜foodupload.png
 ┃ ┣ 📜fork.png
 ┃ ┣ 📜heart.png
 ┃ ┣ 📜heart1.png
 ┃ ┣ 📜imageupload.png
 ┃ ┣ 📜japan.png
 ┃ ┣ 📜korea.jpg
 ┃ ┣ 📜landing1.png
 ┃ ┣ 📜landing2.png
 ┃ ┣ 📜landing3.png
 ┃ ┣ 📜logo2.svg
 ┃ ┣ 📜logout.png
 ┃ ┣ 📜managemy.png
 ┃ ┣ 📜managescrap.png
 ┃ ┣ 📜morerecips.png
 ┃ ┣ 📜myaccount.png
 ┃ ┣ 📜mypage.png
 ┃ ┣ 📜myrecipe.png
 ┃ ┣ 📜myrecipehover.png
 ┃ ┣ 📜pasta.png
 ┃ ┣ 📜peoples.png
 ┃ ┣ 📜scrap.png
 ┃ ┣ 📜scrapfunc.png
 ┃ ┣ 📜scraphover.png
 ┃ ┣ 📜search.png
 ┃ ┣ 📜serving.png
 ┃ ┣ 📜upadateuser.png
 ┃ ┣ 📜userinfo.png
 ┃ ┣ 📜userinfohover.png
 ┃ ┣ 📜userupdate.png
 ┃ ┣ 📜warnalle.png
 ┃ ┗ 📜warning.png
 ┣ 📂components
 ┃ ┣ 📂AllRecipe
 ┃ ┃ ┣ 📜RecipeList.css
 ┃ ┃ ┣ 📜RecipeList.jsx
 ┃ ┃ ┗ 📜Search.jsx
 ┃ ┣ 📂LoginSignup
 ┃ ┃ ┣ 📜Form1.css
 ┃ ┃ ┣ 📜Leftpanel.css
 ┃ ┃ ┣ 📜Leftpanel.jsx
 ┃ ┃ ┣ 📜Rightpanel.css
 ┃ ┃ ┣ 📜Rightpanel.jsx
 ┃ ┃ ┣ 📜Rightpanel2.css
 ┃ ┃ ┗ 📜Rightpanel2.jsx
 ┃ ┣ 📂Mainpage
 ┃ ┃ ┣ 📜Morerec.css
 ┃ ┃ ┣ 📜Morerec.jsx
 ┃ ┃ ┣ 📜Myrec.css
 ┃ ┃ ┣ 📜Myrec.jsx
 ┃ ┃ ┣ 📜Recommend.css
 ┃ ┃ ┗ 📜Recommend.jsx
 ┃ ┣ 📂Mypage
 ┃ ┃ ┣ 📜AlleUpdate.css
 ┃ ┃ ┣ 📜AlleUpdate.jsx
 ┃ ┃ ┣ 📜HomeSide.css
 ┃ ┃ ┣ 📜HomeSide1.jsx
 ┃ ┃ ┣ 📜HomeSide2.jsx
 ┃ ┃ ┣ 📜HomeSide3.jsx
 ┃ ┃ ┣ 📜InforUpdate.css
 ┃ ┃ ┣ 📜InforUpdate.jsx
 ┃ ┃ ┣ 📜MyScrap.css
 ┃ ┃ ┣ 📜MyScrap.jsx
 ┃ ┃ ┣ 📜MypageMyRecipe.css
 ┃ ┃ ┣ 📜MypageMyRecipe.jsx
 ┃ ┃ ┣ 📜PassUpadate.css
 ┃ ┃ ┣ 📜PassUpdate.jsx
 ┃ ┃ ┣ 📜Sidebar1.css
 ┃ ┃ ┣ 📜Sidebar1.jsx
 ┃ ┃ ┣ 📜Sidebar2.jsx
 ┃ ┃ ┣ 📜Sidebar3.jsx
 ┃ ┃ ┣ 📜UserInfo.css
 ┃ ┃ ┗ 📜UserInfo.jsx
 ┃ ┣ 📂Myrecipe
 ┃ ┃ ┣ 📜Regis1.css
 ┃ ┃ ┣ 📜Regis1.jsx
 ┃ ┃ ┣ 📜Regis2.css
 ┃ ┃ ┣ 📜Regis2.jsx
 ┃ ┃ ┣ 📜Regis3.css
 ┃ ┃ ┣ 📜Regis3.jsx
 ┃ ┃ ┣ 📜RegisEdit.css
 ┃ ┃ ┣ 📜RegisEdit.jsx
 ┃ ┃ ┣ 📜RegisParent.css
 ┃ ┃ ┗ 📜RegisParent.jsx
 ┃ ┣ 📜Footer.css
 ┃ ┣ 📜Footer.jsx
 ┃ ┣ 📜Header.css
 ┃ ┣ 📜Header.jsx
 ┃ ┣ 📜Header2.css
 ┃ ┣ 📜Header2.jsx
 ┃ ┣ 📜Hero.css
 ┃ ┣ 📜Hero.jsx
 ┃ ┣ 📜Landcom1.css
 ┃ ┣ 📜Landcom1.jsx
 ┃ ┣ 📜Landcom2.css
 ┃ ┣ 📜Landcom2.jsx
 ┃ ┣ 📜Rdetail.css
 ┃ ┗ 📜Rdetail.jsx
 ┣ 📂pages
 ┃ ┣ 📂Mypage
 ┃ ┃ ┣ 📜AllergyUpdate.jsx
 ┃ ┃ ┣ 📜InfoUpdate.jsx
 ┃ ┃ ┣ 📜MypageHome.css
 ┃ ┃ ┣ 📜MypageHome.jsx
 ┃ ┃ ┣ 📜MypageRecipe.jsx
 ┃ ┃ ┣ 📜MypageScrap.jsx
 ┃ ┃ ┗ 📜PasswordUpdate.jsx
 ┃ ┣ 📜AllRecipes.jsx
 ┃ ┣ 📜Landing.jsx
 ┃ ┣ 📜Login.css
 ┃ ┣ 📜Login.jsx
 ┃ ┣ 📜Realmain.jsx
 ┃ ┣ 📜Recipedetail.jsx
 ┃ ┣ 📜Register.jsx
 ┃ ┣ 📜RegisterUpdate.jsx
 ┃ ┣ 📜SearchPage.jsx
 ┃ ┣ 📜Signup.css
 ┃ ┗ 📜Signup.jsx
 ┣ 📜App.css
 ┣ 📜App.jsx
 ┣ 📜index.css
 ┣ 📜main.jsx
 ┗ 📜scrolltoTop.jsx
```
### ✔️Back-end
```
📦main
 ┣ 📂java
 ┃ ┗ 📂com
 ┃ ┃ ┗ 📂ReciGuard
 ┃ ┃ ┃ ┣ 📂JWT
 ┃ ┃ ┃ ┃ ┣ 📜JWTFilter.java
 ┃ ┃ ┃ ┃ ┣ 📜JWTUtil.java
 ┃ ┃ ┃ ┃ ┗ 📜LoginFilter.java
 ┃ ┃ ┃ ┣ 📂RestTemplateConfig
 ┃ ┃ ┃ ┃ ┗ 📜RestTemplateConfig.java
 ┃ ┃ ┃ ┣ 📂S3Config
 ┃ ┃ ┃ ┃ ┗ 📜S3Config.java
 ┃ ┃ ┃ ┣ 📂SecurityConfig
 ┃ ┃ ┃ ┃ ┣ 📜SecurityConfig.java
 ┃ ┃ ┃ ┃ ┗ 📜UserPrincipal.java
 ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┣ 📜AuthController.java
 ┃ ┃ ┃ ┃ ┣ 📜RecipeController.java
 ┃ ┃ ┃ ┃ ┗ 📜UserController.java
 ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┣ 📜CustomUserDetails.java
 ┃ ┃ ┃ ┃ ┣ 📜IngredientRequestDTO.java
 ┃ ┃ ┃ ┃ ┣ 📜IngredientResponseDTO.java
 ┃ ┃ ┃ ┃ ┣ 📜InstructionRequestDTO.java
 ┃ ┃ ┃ ┃ ┣ 📜InstructionResponseDTO.java
 ┃ ┃ ┃ ┃ ┣ 📜MyRecipeForm.java
 ┃ ┃ ┃ ┃ ┣ 📜MyRecipeFormEdit.java
 ┃ ┃ ┃ ┃ ┣ 📜RecipeDetailResponseDTO.java
 ┃ ┃ ┃ ┃ ┣ 📜RecipeListResponseDTO.java
 ┃ ┃ ┃ ┃ ┣ 📜RecipeRecommendResponseDTO.java
 ┃ ┃ ┃ ┃ ┣ 📜ScrapRecipeDTO.java
 ┃ ┃ ┃ ┃ ┣ 📜SimilarAllergyIngredientDTO.java
 ┃ ┃ ┃ ┃ ┣ 📜UserIngredientDTO.java
 ┃ ┃ ┃ ┃ ┣ 📜UserIngredientListDTO.java
 ┃ ┃ ┃ ┃ ┣ 📜UserPasswordDTO.java
 ┃ ┃ ┃ ┃ ┣ 📜UserResponseDTO.java
 ┃ ┃ ┃ ┃ ┗ 📜UserUpdateDTO.java
 ┃ ┃ ┃ ┣ 📂entity
 ┃ ┃ ┃ ┃ ┣ 📜Ingredient.java
 ┃ ┃ ┃ ┃ ┣ 📜Instruction.java
 ┃ ┃ ┃ ┃ ┣ 📜Nutrition.java
 ┃ ┃ ┃ ┃ ┣ 📜Recipe.java
 ┃ ┃ ┃ ┃ ┣ 📜RecipeIngredient.java
 ┃ ┃ ┃ ┃ ┣ 📜RecipeStats.java
 ┃ ┃ ┃ ┃ ┣ 📜User.java
 ┃ ┃ ┃ ┃ ┣ 📜UserCookingStyle.java
 ┃ ┃ ┃ ┃ ┣ 📜UserCuisine.java
 ┃ ┃ ┃ ┃ ┣ 📜UserFoodType.java
 ┃ ┃ ┃ ┃ ┣ 📜UserIngredient.java
 ┃ ┃ ┃ ┃ ┗ 📜UserScrap.java
 ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┣ 📜IngredientRepository.java
 ┃ ┃ ┃ ┃ ┣ 📜InstructionRepository.java
 ┃ ┃ ┃ ┃ ┣ 📜RecipeIngredientRepository.java
 ┃ ┃ ┃ ┃ ┣ 📜RecipeRepository.java
 ┃ ┃ ┃ ┃ ┣ 📜RecipeStatsRepository.java
 ┃ ┃ ┃ ┃ ┣ 📜UserCookingStyleRepository.java
 ┃ ┃ ┃ ┃ ┣ 📜UserCuisineRepository.java
 ┃ ┃ ┃ ┃ ┣ 📜UserFoodTypeRepository.java
 ┃ ┃ ┃ ┃ ┣ 📜UserIngredientRepository.java
 ┃ ┃ ┃ ┃ ┣ 📜UserRepository.java
 ┃ ┃ ┃ ┃ ┗ 📜UserScrapRepository.java
 ┃ ┃ ┃ ┣ 📂service
 ┃ ┃ ┃ ┃ ┣ 📜CustomUserDetailService.java
 ┃ ┃ ┃ ┃ ┣ 📜RecipeService.java
 ┃ ┃ ┃ ┃ ┣ 📜RecipeStatsService.java
 ┃ ┃ ┃ ┃ ┣ 📜S3Uploader.java
 ┃ ┃ ┃ ┃ ┣ 📜UserIngredientService.java
 ┃ ┃ ┃ ┃ ┣ 📜UserScrapService.java
 ┃ ┃ ┃ ┃ ┗ 📜UserService.java
 ┃ ┃ ┃ ┣ 📂webconfig
 ┃ ┃ ┃ ┃ ┗ 📜Webconfig.java
 ┃ ┃ ┃ ┗ 📜ReciGuardApplication.java
 ┗ 📂resources
 ┃ ┣ 📂db
 ┃ ┃ ┗ 📜data.sql
 ┃ ┗ 📜application.yml
```
