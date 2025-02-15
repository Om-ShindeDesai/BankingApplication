import React from "react";
import "../styles/Transactions.css"

const Transactions = () => {
  const transactions = [
    { id: 31, amount: "₹200.00", type: "Credited", date: "2023-07-25", account: 116797 },
    { id: 28, amount: "₹20.00", type: "Credited", date: "2023-07-25", account: 116797 },
    { id: 27, amount: "₹10.00", type: "Withdraw", date: "2023-07-25", account: 131019 },
    { id: 26, amount: "₹100.00", type: "Transfer", date: "2023-07-25", account: 131019 },
    { id: 25, amount: "₹10,000.00", type: "Deposited", date: "2023-07-25", account: 131019 }
  ];

  return (
    <div className="mt-6">
      <h2 className="text-xl font-bold">Transaction History</h2>
      <table className="transaction-table">
        <thead>
          <tr>
            <th>Transaction ID</th>
            <th>Amount</th>
            <th>Transaction Type</th>
            <th>Transaction Date</th>
            <th>Source Account</th>
          </tr>
        </thead>
        <tbody>
          {transactions.map((t) => (
            <tr key={t.id}>
              <td>{t.id}</td>
              <td>{t.amount}</td>
              <td className={
                t.type === "Credited" ? "transaction-credit" :
                t.type === "Withdraw" ? "transaction-debit" :
                "transaction-transfer"
              }>
                {t.type}
              </td>
              <td>{t.date}</td>
              <td>{t.account}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default Transactions;
