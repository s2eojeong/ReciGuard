import "./App.css";
import { Routes, Route } from "react-router-dom";
import ScrollToTop from "./scrolltoTop";
import Landing from "./pages/Landing";
import Login from "./pages/Login";
import Signup from "./pages/Signup";
import Form from "./pages/Form";
import Realmain from "./pages/Realmain";
import Register from "./pages/Register";
import Recipedetail from "./pages/Recipedetail";

function App() {
  return (
    <>
      <ScrollToTop />
      <Routes>
        <Route path="/" element={<Landing />} />
        <Route path="/auth/login" element={<Login />} />
        <Route path="/auth/register" element={<Signup />} />
        <Route path="/Form" element={<Form />} />
        <Route path="/recipes" element={<Realmain />} />
        <Route path="/users/recipe-form" element={<Register />} />
        <Route path="/recipes/detail" element={<Recipedetail />} />
      </Routes>
    </>
  );
}

export default App;
