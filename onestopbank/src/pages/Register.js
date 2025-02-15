import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import axios from "axios";
import { FaEye, FaEyeSlash } from "react-icons/fa";
import "../styles/Register.css"; // Updated file name for uniqueness

const Register = () => {
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    otherName: "",
    gender: "",
    address: "",
    stateOfOrigin: "",
    email: "",
    password: "",
    phoneNumber: "",
    alternativePhoneNumber: "",
  });

  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const navigate = useNavigate();

  // Handle input changes
  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");

    try {
      const response = await axios.post("http://localhost:8080/api/auth/register", formData);

      if (response.status === 201 || response.status === 200) {
        setSuccess(response.data.message || "Registration successful! Redirecting...");

        // Redirect to login after 2 seconds
        setTimeout(() => navigate("/login"), 2000);
      }
    } catch (error) {
      setError(error.response?.data?.message || "Registration failed! Please try again.");
    }
  };

  return (
    <div className="unique-register-page">
      <div className="unique-register-container">
        <h2 className="unique-register-title">Register</h2>
        {error && <p className="unique-error-message">{error}</p>}
        {success && <p className="unique-success-message">{success}</p>}

        <form onSubmit={handleSubmit}>
          <div className="unique-input-group">
            <input type="text" name="firstName" placeholder="First Name" value={formData.firstName} onChange={handleChange} required />
            <input type="text" name="lastName" placeholder="Last Name" value={formData.lastName} onChange={handleChange} required />
          </div>

          <div className="unique-input-group">
            <input type="text" name="otherName" placeholder="Other Name" value={formData.otherName} onChange={handleChange} />
            <select name="gender" value={formData.gender} onChange={handleChange} required>
              <option value="">Select Gender</option>
              <option value="Male">Male</option>
              <option value="Female">Female</option>
            </select>
          </div>

          <div className="unique-input-group">
            <input type="text" name="address" placeholder="Address" value={formData.address} onChange={handleChange} required />
            <input type="text" name="stateOfOrigin" placeholder="State of Origin" value={formData.stateOfOrigin} onChange={handleChange} required />
          </div>

          <div className="unique-input-group">
            <input type="email" name="email" placeholder="Email" value={formData.email} onChange={handleChange} required />
            <div className="unique-password-container">
              <input
                type={showPassword ? "text" : "password"}
                name="password"
                placeholder="Password"
                value={formData.password}
                onChange={handleChange}
                required
              />
              <span className="unique-eye-icon" onClick={() => setShowPassword(!showPassword)}>
                {showPassword ? <FaEyeSlash /> : <FaEye />}
              </span>
            </div>
          </div>

          <div className="unique-input-group">
            <input type="tel" name="phoneNumber" placeholder="Phone Number" value={formData.phoneNumber} onChange={handleChange} required />
            <input type="tel" name="alternativePhoneNumber" placeholder="Alternative Phone Number" value={formData.alternativePhoneNumber} onChange={handleChange} />
          </div>

          <button type="submit" className="unique-submit-button">Register</button>
        </form>

        <p className="unique-login-text">Already have an account?</p>
        <Link to="/login" className="unique-login-link">Login Here</Link>
      </div>
    </div>
  );
};

export default Register;
