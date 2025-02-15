import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "../styles/Deposit.css";

const Deposit = ({ mainBalance, onDeposit }) => {
  const [amount, setAmount] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleDeposit = async () => {
    const depositAmount = parseFloat(amount);

    if (!depositAmount || depositAmount <= 0) {
      setError("Please enter a valid deposit amount.");
      return;
    }

    try {
      const token = localStorage.getItem("authToken"); // ✅ Get auth token
      const accountNumber = localStorage.getItem("accountInfo"); // ✅ Get account number

      const response = await axios.post(
        "http://localhost:8080/api/user/creditAccount",
        {
          accountNumber,
          amount: depositAmount,
        },
        {
          headers: { Authorization: `Bearer ${token}` }, // ✅ Pass token in header
        }
      );

      console.log("Deposit Response:", response.data);

      if (response.status === 200) {
        alert("Deposit successful!");
        onDeposit(depositAmount); // ✅ Update UI balance
        setAmount("");
        navigate("/dashboard"); // ✅ Redirect to Dashboard
      }
    } catch (error) {
      console.error("Deposit Error:", error);
      setError(error.response?.data?.message || "Deposit failed! Please try again.");
    }
  };

  return (
    <div className="deposit-container">
      <h2>Deposit Funds</h2>
      {error && <p className="error">{error}</p>}
      <input
        type="number"
        placeholder="Enter deposit amount"
        value={amount}
        onChange={(e) => setAmount(e.target.value)}
      />
      <button onClick={handleDeposit}>Deposit</button>
    </div>
  );
};

export default Deposit;
