import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import './Signup.css';

function Signup() {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [number, setNumber] = useState('');
  const [password, setPassword] = useState('');
  const [confirm, setConfirm] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);

  const handleSubmit = async (event) => {
    event.preventDefault();
    if (password !== confirm) {
      setError("Passwords do not match");
      return;
    }
    try {
      const response = await fetch("http://localhost:1111/usersave", {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          name,
          email,
          number,
          password,
          confirm
        }),
      });
      if (!response.ok) {
        throw new Error(response.statusText);
      }
      const result = await response.json();
      console.log(result);
      console.log("Started Server");
      setSuccess(true);
      setError('');
    } catch (error) {
      setError(error.message);
      setSuccess(false);
    }
  };

  return (
    <div className="signup-container">
      <div className="signup-box">
        <h2>Sign Up</h2>
        <form onSubmit={handleSubmit}>
          <div className="input-group">
            <label>Full Name</label>
            <input type="text" placeholder="Enter Name" value={name} onChange={(event) => setName(event.target.value)} />
          </div>
          <div className="input-group">
            <label>Email</label>
            <input type="email" placeholder="e-mail" value={email} onChange={(event) => setEmail(event.target.value)} />
          </div>
          <div className="input-group">
            <label>Mobile</label>
            <input type="number" placeholder="mobile no." value={number} onChange={(event) => setNumber(event.target.value)} />
          </div>
          <div className="input-group">
            <label>Password</label>
            <input type="password" placeholder="Password" value={password} onChange={(event) => setPassword(event.target.value)} />
          </div>
          <div className="input-group">
            <label>Confirm Password</label>
            <input type="password" placeholder="Confirm Password" value={confirm} onChange={(event) => setConfirm(event.target.value)} />
          </div>
          {error && <p style={{ color: 'red' }}>{error}</p>}
          {success && <p style={{ color: 'green' }}>Registration successful!</p>}
          <button type="submit">Sign Up</button>
          <p className="already"> Already have an account? <Link to="/login">Login</Link> </p>
        </form>
      </div>
    </div>
  );
}

export default Signup;


