import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "../styles/TransferFunds.css";

const TransferFunds = () => {
  const [sourceAccount, setSourceAccount] = useState("");
  const [destinationAccount, setDestinationAccount] = useState("");
  const [amount, setAmount] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const navigate = useNavigate();

  const handleTransfer = async () => {
    setError("");
    setSuccess("");

    const transferAmount = parseFloat(amount);
    if (!sourceAccount || !destinationAccount || transferAmount <= 0) {
      setError("Please enter valid account details and amount.");
      return;
    }

    try {
      const token = localStorage.getItem("authToken"); // Get stored auth token

      const response = await axios.post(
        "http://localhost:8080/api/user/transfer",
        {
          sourceAccountNumber: sourceAccount,
          destinationAccountNumber: destinationAccount,
          amount: transferAmount,
        },
        {
          headers: { Authorization: `Bearer ${token}` }, // Pass token in header
        }
      );

      if (response.status === 200) {
        setSuccess("Transfer successful!");
        setAmount("");
        setSourceAccount("");
        setDestinationAccount("");
        setTimeout(() => navigate("/dashboard"), 2000); // Redirect after success
      }
    } catch (error) {
      console.error("Transfer Error:", error);
      setError(error.response?.data?.message || "Transfer failed! Please try again.");
    }
  };

  return (
    <div className="transfer-container">
      <h2>Fund Transfer</h2>
      {error && <p className="error">{error}</p>}
      {success && <p className="success">{success}</p>}
      <div className="input-group">
        <input
          type="text"
          placeholder="Enter Source Account Number"
          value={sourceAccount}
          onChange={(e) => setSourceAccount(e.target.value)}
          required
        />
        <input
          type="text"
          placeholder="Enter Destination Account Number"
          value={destinationAccount}
          onChange={(e) => setDestinationAccount(e.target.value)}
          required
        />
        <input
          type="number"
          placeholder="Enter Amount"
          value={amount}
          onChange={(e) => setAmount(e.target.value)}
          required
        />
      </div>
      <button className="transfer-button" onClick={handleTransfer}>
        Transfer
      </button>
    </div>
  );
};

export default TransferFunds;
