import "./App.css";
import { Routes, Route } from "react-router-dom";
import ScrollToTop from "./scrolltoTop";
import Landing from "./pages/Landing";
import Login from "./pages/Login";
import Signup from "./pages/Signup";
import Realmain from "./pages/Realmain";
import Register from "./pages/Register";
import Recipedetail from "./pages/Recipedetail";
import MypageHome from "./pages/MypageHome";
import MypageRecipe from "./pages/MypageRecipe";
import MypageScrap from "./pages/MypageScrap";

function App() {
  return (
    <>
      <ScrollToTop />
      <Routes>
        <Route path="/" element={<Landing />} />
        <Route path="/auth/login" element={<Login />} />
        <Route path="/auth/register" element={<Signup />} />
        <Route path="/recipes" element={<Realmain />} />
        <Route path="/users/recipe-form" element={<Register />} />
        <Route path="/recipes/detail" element={<Recipedetail />} />
        <Route path="/users/{userid}" element={<MypageHome />} />
        <Route path="/users/myrecipes" element={<MypageRecipe />} />
        <Route path="/users/scraps" element={<MypageScrap />} />
      </Routes>
    </>
  );
}

export default App;
