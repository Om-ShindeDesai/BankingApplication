import React, { useState } from "react";
import axios from "axios";
import "../styles/TransactionHistory.css";

const TransactionHistory = () => {
  const [accountNumber, setAccountNumber] = useState("");
  const [transactions, setTransactions] = useState([]);
  const [error, setError] = useState("");

  const [showPdfForm, setShowPdfForm] = useState(false);
  const [pdfForm, setPdfForm] = useState({
    accountNumber: "",
    startDate: "",
    endDate: "",
  });

  const handleChange = (e) => {
    setAccountNumber(e.target.value);
  };

  const handlePdfInputChange = (e) => {
    setPdfForm({ ...pdfForm, [e.target.name]: e.target.value });
  };

  const fetchTransactions = async () => {
    setError("");
    setTransactions([]);

    if (!accountNumber) {
      setError("Please enter an account number.");
      return;
    }

    try {
      const response = await axios.get(
        `http://localhost:8080/api/user/transactionHistory/${accountNumber}`,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("authToken")}`,
          },
        }
      );

      console.log(response.data);
      if (response.data.length === 0) {
        setError("No transactions found for this account.");
      } else {
        setTransactions(response.data);
      }
    } catch (err) {
      console.error("Transaction Fetch Error:", err);
      setError("Failed to fetch transactions. Please try again.");
    }
  };

  const generatePdf = async () => {
    if (!pdfForm.accountNumber || !pdfForm.startDate || !pdfForm.endDate) {
      setError("Please fill all fields before generating the PDF.");
      return;
    }

    try {
      await axios.get("http://localhost:8080/api/bank/statement", {
        params: pdfForm,
        headers: {
          Authorization: `Bearer ${localStorage.getItem("authToken")}`,
          "Content-Type": "application/json",
        },
      });

      alert("PDF request sent successfully.");
    } catch (err) {
      console.error("PDF Generation Error:", err);
      setError("Failed to generate PDF. Please try again.");
    }
  };

  return (
    <div className="transaction-history-container">
      <h2>Transaction History</h2>

      <div className="input-group">
        <input
          type="text"
          placeholder="Enter Account Number"
          value={accountNumber}
          onChange={handleChange}
          required
        />
        <button onClick={fetchTransactions}>View History</button>
      </div>

      {error && <p className="error">{error}</p>}

      {transactions.length > 0 && (
        <>
          <div className="transaction-table-container">
            <table className="transaction-table">
              <thead>
                <tr>
                  <th>Type</th>
                  <th>Amount</th>
                  <th>Account No.</th>
                  <th>Status</th>
                  <th>Date</th>
                </tr>
              </thead>
              <tbody>
                {transactions.map((tx, index) => (
                  <tr key={index}>
                    <td>{tx.transactionType}</td>
                    <td>${tx.transactionAmount}</td>
                    <td>{tx.accountNumber}</td>
                    <td>{tx.status}</td>
                    <td>{new Date(tx.transactionDate).toLocaleString()}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <button
            className="generate-pdf-button"
            onClick={() => setShowPdfForm(true)}
          >
            Generate PDF
          </button>

          {showPdfForm && (
            <div className="pdf-form">
              <h3>Generate Account Statement</h3>
              <input
                type="text"
                name="accountNumber"
                placeholder="Enter Account Number"
                value={pdfForm.accountNumber}
                onChange={handlePdfInputChange}
                required
              />
              <input
                type="date"
                name="startDate"
                value={pdfForm.startDate}
                onChange={handlePdfInputChange}
                required
              />
              <input
                type="date"
                name="endDate"
                value={pdfForm.endDate}
                onChange={handlePdfInputChange}
                required
              />
              <button onClick={generatePdf}>Get PDF</button>
            </div>
          )}
        </>
      )}
    </div>
  );
};

export default TransactionHistory;
