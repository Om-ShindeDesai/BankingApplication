import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import axios from "axios";
import "../styles/Login.css";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false); // ✅ Track password visibility
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError(""); // Clear previous errors

    try {
      const response = await axios.post("http://localhost:8080/api/user/login", {
        email,
        password,
      });

      if (response.status === 200) {
        const token = response.data.responseMessage;
        localStorage.setItem("authToken", token);
        localStorage.setItem("userEmail", email);
        localStorage.setItem("accountInfo", response.data.accountInfo.accountNumber);
        navigate("/dashboard");
      }
    } catch (error) {
      setError(error.response?.data?.message || "Invalid email or password!");
    }
  };

  return (
    <div className="login-page">
      <div className="login-container">
        <h2>Login</h2>
        {error && <p className="error">{error}</p>}
        <form onSubmit={handleLogin}>
          <div className="input-container">
            <input
              type="email"
              placeholder="Enter your email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
            <div className="password-container"> {/* ✅ Added container for styling */}
              <input
                type={showPassword ? "text" : "password"} // ✅ Toggle password visibility
                placeholder="Enter your password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
              <span className="eye-icon" onClick={() => setShowPassword(!showPassword)}>
                {showPassword ? "🚫" : "👁️"} {/* ✅ Show/Hide icon */}
              </span>
            </div>
          </div>
          <button className="submit-button" type="submit">Login</button>
        </form>
        <p className="forgot-password-text">
          <Link to="/forgot-password" className="forgot-password-link">Reset Password?</Link>
        </p>
        <p className="create-account-text">Don't have an account?</p>
        <Link to="/create-account" className="create-account-link">Create one</Link>
      </div>
    </div>
  );
};

export default Login;
