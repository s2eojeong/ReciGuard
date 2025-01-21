import "./App.css";
import { Routes, Route } from "react-router-dom";
import ScrollToTop from "./scrolltoTop";
import Landing from "./pages/Landing";
import Login from "./pages/Login";
import Signup from "./pages/Signup";
import Realmain from "./pages/Realmain";
import Register from "./pages/Register";
import Recipedetail from "./pages/Recipedetail";
import MypageHome from "./pages/Mypage/MypageHome";
import MypageRecipe from "./pages/Mypage/MypageRecipe";
import MypageScrap from "./pages/Mypage/MypageScrap";
import InfoUpdate from "./pages/Mypage/InfoUpdate";
import AllergyUpdate from "./pages/Mypage/AllergyUpdate";
import PasswordUpdate from "./pages/Mypage/PasswordUpdate.jsx";
import AllRecipes from "./pages/AllRecipes";
import SearchPage from "./pages/SearchPage";
import RegisterUpdate from "./pages/RegisterUpdate.jsx";

function App() {
  return (
    <>
      <ScrollToTop />
      <Routes>
        <Route path="/" element={<Landing />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Signup />} />
        <Route path="/recipes" element={<Realmain />} />
        <Route path="/users/recipe-form" element={<Register />} />
        <Route path="/recipes/edit/:recipeId" element={<RegisterUpdate />} />
        <Route path="/recipes/:recipeId" element={<Recipedetail />} />
        <Route path="/users/:userid" element={<MypageHome />} />
        <Route path="/users/info" element={<InfoUpdate />} />
        <Route path="/password" element={<PasswordUpdate />} />
        <Route path="/users/allergy" element={<AllergyUpdate />} />
        <Route path="/users/myrecipes" element={<MypageRecipe />} />
        <Route path="/users/scraps" element={<MypageScrap />} />
        <Route path="/recipes/all" element={<AllRecipes />} />
        <Route path="/search" element={<SearchPage />} />
      </Routes>
    </>
  );
}

export default App;
