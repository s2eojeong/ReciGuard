import './App.css';
import { Routes, Route } from "react-router-dom";
import Landing from "./pages/Landing";
import Login from "./pages/Login";
import Signup from "./pages/Signup"

function App() {
    return (
        <>
            <Routes>
                <Route path="/" element={<Landing />}/>
                <Route path="/login" element={<Login />}/>
                <Route path="/Signup" element={<Signup />}/>
            </Routes>
        </>
        );
}

export default App;
