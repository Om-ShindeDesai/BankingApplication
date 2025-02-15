import React, { useState } from "react";
import { BrowserRouter as Router, Routes, Route, useNavigate } from "react-router-dom";
import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Dashboard from "./components/Dashboard";
import Transactions from "./components/Transaction";
import TransactionHistory from "./components/TransactionHistory"; // ✅ Imported Transaction History
import Navbar from "./components/Navbar";
import Sidebar from "./components/Sidebar";
import Contact from "./pages/Contact";
import About from "./pages/About";
import Deposit from "./components/Deposit";
import Withdraw from "./components/Withdraw";
import TransferFunds from "./components/TransferFunds";
import UpdateDetails from "./components/UpdateDetails"; // ✅ Imported UpdateDetails component
import "./App.css";
import ResetPassword from "./components/ResetPassword";

function LayoutWithSidebar({ children }) {
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const [mainBalance, setMainBalance] = useState(5000);
  const navigate = useNavigate();

  const toggleSidebar = (state) => {
    setIsSidebarOpen(state !== undefined ? state : !isSidebarOpen);
  };

  const handleDeposit = (amount) => {
    setMainBalance((prevBalance) => prevBalance + amount);
    navigate("/dashboard");
  };

  const handleWithdraw = (amount) => {
    setMainBalance((prevBalance) =>
      prevBalance >= amount ? prevBalance - amount : prevBalance
    );
    navigate("/dashboard");
  };

  return (
    <>
      <Navbar toggleSidebar={toggleSidebar} />
      <Sidebar isOpen={isSidebarOpen} toggleSidebar={toggleSidebar} />
      <div
        className={`sidebar-main-content ${isSidebarOpen ? "shifted" : ""}`}
        onClick={() => toggleSidebar(false)}
      >
        {React.cloneElement(children, {
          mainBalance,
          onDeposit: handleDeposit,
          onWithdraw: handleWithdraw,
        })}
      </div>
    </>
  );
}

function AppRoutes() {
  return (
    <Routes>
      {/* Public Pages */}
      <Route path="/" element={<Home />} />
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route path="/create-account" element={<Register />} />
      <Route path="/contact" element={<Contact />} />
      <Route path="/about" element={<About />} />
      <Route path="/forgot-password" element={<ResetPassword />} />

      {/* Dashboard & Authenticated Pages Wrapped with Sidebar Layout */}
      <Route path="/dashboard" element={<LayoutWithSidebar><Dashboard /></LayoutWithSidebar>} />
      <Route path="/transactions" element={<LayoutWithSidebar><Transactions /></LayoutWithSidebar>} />
      <Route path="/transaction-history" element={<LayoutWithSidebar><TransactionHistory /></LayoutWithSidebar>} /> {/* ✅ Added Transaction History Route */}
      <Route path="/deposit" element={<LayoutWithSidebar><Deposit /></LayoutWithSidebar>} />
      <Route path="/withdraw" element={<LayoutWithSidebar><Withdraw /></LayoutWithSidebar>} />
      <Route path="/fund-transfer" element={<LayoutWithSidebar><TransferFunds /></LayoutWithSidebar>} />
      <Route path="/update-details" element={<LayoutWithSidebar><UpdateDetails /></LayoutWithSidebar>} />
    </Routes>
  );
}

export default function AppWrapper() {
  return (
    <Router>
      <AppRoutes />
    </Router>
  );
}
