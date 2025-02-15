import axios from "axios";
import { useEffect, useState } from "react";
import "../styles/Dashboard.css"; // Import CSS

const Dashboard = () => {
  const [accountName, setAccountName] = useState("");
  const [loading, setLoading] = useState(true);
  const [balance,setBalance] = useState("XXXX.XX");
  const accountNumber = localStorage.getItem("accountInfo");
  const token = localStorage.getItem("authToken");

  const nameEnquiry = async () => {
    try {
      const response = await axios.post("http://localhost:8080/api/user/nameEnquiry", 
        { 'accountNumber': accountNumber },
        { headers: { Authorization: `Bearer ${token}` } }
      );
      console.log(response)
      setAccountName(response.data);
    } catch (error) {
      console.error("Error fetching account name:", error);
    } finally {
      setLoading(false);
    }
  };
  const balanceEnquiry = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/api/user/balanceEnquiry/${accountNumber}`,
        { headers: { Authorization: `Bearer ${token}` } }
      );
      console.log(response.data)
      setBalance(response.data.accountInfo.accountBalance);
    } catch (error) {
      console.error("Error fetching account name:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    nameEnquiry();
    balanceEnquiry();
    if (!accountNumber || !token) {
      console.error("Missing account number or token!");
      setLoading(false);
    }
  }, []);

  return (
    <div className="dashboard-container">
      <div className="dashboard-content">
        <h1 className="dashboard-title">Welcome To Apna-Bank</h1>

        <div className="card-container">
          {/* Account Info Card */}
          <div className="card">
            <h2>Account Details</h2>
            {loading ? (
              <p>Loading...</p>
            ) : accountName ? (
              <p className="account-name">Account Name: {accountName}</p>
            ) : (
              <p>No account name found.</p>
            )}
          </div>

          {/* Placeholder - Balance */}
          <div className="card">
            <h2>Current Balance</h2>
            <p>₹{balance}</p>
          </div>

        </div>
      </div>
    </div>
  );
};

export default Dashboard;
