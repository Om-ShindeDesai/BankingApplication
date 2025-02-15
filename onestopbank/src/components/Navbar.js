import React from "react";
import { FaBars } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import "../styles/Navbar.css";

const Navbar = ({ toggleSidebar, isOpen }) => {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem("authToken"); // Clear authentication token
    localStorage.removeItem("userEmail"); // Clear session email
    navigate("/"); // Redirect to Home page
  };

  return (
    <nav className="navbar">
      <div className="navbar-content">
        <button className={`toggle-btn ${isOpen ? "move-right" : ""}`} onClick={toggleSidebar}>
          <FaBars />
        </button>
        <div className="nav-links">
          <a href="/dashboard" className="nav-link">Home</a> {/* Redirects to Dashboard */}
          <a href="/contact" className="nav-link">Contact</a>
          <a href="/about" className="nav-link">About Us</a>
          <button className="logout-btn" onClick={handleLogout}>Logout</button>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
