import React from 'react';
import { Link } from 'react-router-dom';
import './Login.css';

function Login() {
  return (
    <>
      <section className='Login flex'>
        <div className="loginpage">
          <h2>Login</h2>
          <input type="text" placeholder='user name, mobile or e-mail' />
          <input type="password" placeholder='Password' />
          <button>Submit</button>
          <p className='pass'>
            Reset Password?
            <a href="#" className="forget-password">Forget Password?</a>
          </p>
          <p className='mid'>
            Don't have an account? 
            <Link to="/signup" className="register">Register</Link>
          </p>
        </div>
      </section>
    </>
  );
}

export default Login;
