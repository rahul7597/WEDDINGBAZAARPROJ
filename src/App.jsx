import { useState } from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import './App.css';
import Login from './Component/Login/Login';
import Nav from './Component/NavBar/Nav';
import Signup from './Component/Signup/Signup';
import Header from './Component/Main/Header/Header';
import Vendors from './Component/Vendors Category/Vendors';
import Home from './Component/Home/Home';
import VendorForm from './Component/VendorForm/VendorForm';

function App() {
  const [count, setCount] = useState(0)

  return (
    <>

      {/* <i class="fa-solid fa-magnifying-glass"></i>
      <i class="fa-solid fa-house"></i>
      <i class="fa-solid fa-user"></i>
      <i class="fa-brands fa-facebook"></i>
      <i class="fa-brands fa-twitter"></i>
      <i class="fa-brands fa-instagram"></i>
      <i class="fa-solid fa-envelope"></i>
      <i class="fa-solid fa-star"></i>
      <i class="fa-solid fa-cart-shopping"></i>
      <i class="fa-solid fa-chevron-down"></i>
      <i class="fa-solid fa-heart"></i> */}
      {/* <Nav/> */}
    
      <BrowserRouter>
      <Nav />
      <Routes>
        {/* <Route path="/" element={<Card />} /> */}  
        <Route path="/Home" element={<Home/>} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/vendor" element={<VendorForm />} />
        {/* <Route path="/card" element={<Card />} /> */}
        {/* <Route path="*" element={<div>Page Not Found</div>} /> */}
      </Routes>
    </BrowserRouter>
      
    </>
  )
}

export default App
