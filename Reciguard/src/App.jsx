import './App.css';
import { Routes, Route } from "react-router-dom";
import Landing from "./pages/Landing";
import Login from "./pages/Login";
import Signup from "./pages/Signup"
import Form from './pages/Form';

function App() {
    return (
        <>
            <Routes>
                <Route path="/" element={<Landing />}/>
                <Route path="/Login" element={<Login />}/>
                <Route path="/Signup" element={<Signup />}/>
                <Route path="/Form" element={<Form />}/>
            </Routes>
        </>
        );
}

export default App;
