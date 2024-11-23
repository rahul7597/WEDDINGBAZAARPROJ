import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import './Login.css';

function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch('http://localhost:1111/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password }),
      });
      const data = await response.json();
      if (data.success) {
        // Login successful, redirect to dashboard
        setSuccess('Login successful!');
        setTimeout(() => {
          window.location.href = '/dashboard';
        }, 2000);
        // Token ko LocalStorage mein store karein
      localStorage.setItem('token', data.token);
      // Token ko Header mein add karein
      const token = localStorage.getItem('token');
      const headers = {
        'Authorization': `Bearer ${token}`,
      };
      const response = await fetch('http://localhost:1111/AuthorizedUserInfo', {
        method: 'GET',
        headers: headers,
      });
      const data = await response.json();


      } else {
        setError(data.message);
      }
    } catch (error) {
      setError('An error occurred while logging in.');
    }
  };

  return (
    <>
      <section className='Login flex'>
        <div className="loginpage">
          <h2>Login</h2>
          <form onSubmit={handleSubmit}>
            <input
              type="text" placeholder='user name, mobile or e-mail' value={username} onChange={(e) => setUsername(e.target.value)}/>
            <input type="password" placeholder='Password' value={password} onChange={(e) => setPassword(e.target.value)} />
            <button type="submit">Submit</button>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            {success && <p style={{ color: 'green' }}>{success}</p>}
          </form>

          <p className='pass'> Reset Password? <a href="#" className="forget-password">Forget Password?</a> </p>
          <p className='mid'> Don't have an account? <Link to="/signup" className="register">Register</Link> </p>

        </div>
      </section>
    </>
  );
}

export default Login;

