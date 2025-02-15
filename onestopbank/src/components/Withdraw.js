import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "../styles/Withdraw.css";

const Withdraw = () => {
  const [formData, setFormData] = useState({ accountNumber: "", amount: "" });
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");

    const token = localStorage.getItem("authToken"); // Retrieve token

    if (!token) {
      setError("Unauthorized! Please login first.");
      return;
    }

    try {
      const response = await axios.post(
        "http://localhost:8080/api/user/debitAccount",
        formData,
        {
          headers: {
            Authorization: `Bearer ${token}`, // Attach token in header
            "Content-Type": "application/json",
          },
        }
      );

      if (response.status === 200 || response.status === 201) {
        setSuccess(response.data.message || "Withdrawal successful! Redirecting...");
        
        setTimeout(() => navigate("/dashboard"), 2000);
      }
    } catch (error) {
      setError(error.response?.data?.message || "Withdrawal failed! Please try again.");
    }
  };

  return (
    <div className="withdraw-page">
      <div className="withdraw-container">
        <h2 className="withdraw-title">Withdraw Funds</h2>
        {error && <p className="error-message">{error}</p>}
        {success && <p className="success-message">{success}</p>}

        <form onSubmit={handleSubmit}>
          <input type="text" name="accountNumber" placeholder="Enter Account Number" value={formData.accountNumber} onChange={handleChange} required />
          <input type="number" name="amount" placeholder="Enter Amount" value={formData.amount} onChange={handleChange} required />
          <button type="submit" className="submit-button">Withdraw</button>
        </form>
      </div>
    </div>
  );
};

export default Withdraw;
