import React from 'react';
import { Link } from 'react-router-dom';
import './Nav.css';

function Nav() {
  return (
    <>
      <div className="nav-container">
        <div className="nav flex">
          <div className="logo-part">
            <img
              src="https://img.weddingbazaar.com/shaadisaga_production/static/vendor_categories/WeddingBazaarLogo.svg"
              alt="Logo"
            />
          </div>
          <div className="search-part">
            <button className="search-btn flex">
              <input type="text" placeholder="Search for event..." />
              <i className="fa-solid fa-magnifying-glass"></i>
            </button>
          </div>
          <div className="nav-links-part">
            <ul className="nav-links flex">
              <Link to="/Home">
                <li>Home</li>
              </Link>
              <Link to="/login">
                <li>Login</li>
              </Link>
              <Link to="/blog">
                <li>Blog</li>
              </Link>
              <Link to="/vendor">
                <li>Are You Vendor?..</li>
              </Link>
            </ul>
          </div>
        </div>
      </div>
    </>
  );
}

export default Nav;