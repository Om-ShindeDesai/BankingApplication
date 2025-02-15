import React from "react";
import { Link } from "react-router-dom";
import "../styles/Home.css";

const Home = () => {
  return (
    <div className="home-container">
      <div className="home-content">
        <h1>Welcome to Apna Bank</h1>
        <p>Your trusted partner for secure and seamless banking.</p>
        <div className="button-group">
          <Link to="/login">
            <button className="login-btn">Login</button>
          </Link>
          <Link to="/create-account">
            <button className="register-btn">Register</button>
          </Link>
        </div>
      </div>
    </div>
  );
};

export default Home;
