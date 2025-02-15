import React, { useState, useEffect, useRef } from "react";
import { useNavigate } from "react-router-dom";
import { FaTachometerAlt, FaMoneyBill, FaExchangeAlt, FaKey, FaHistory } from "react-icons/fa";
import "../styles/Sidebar.css";

const Sidebar = ({ isOpen, toggleSidebar }) => {
  const [active, setActive] = useState("Dashboard");
  const navigate = useNavigate();
  const sidebarRef = useRef(null); // Ref for sidebar

  // Handle navigation and auto-close sidebar
  const handleNavigation = (route, label) => {
    setActive(label);
    navigate(route);
    toggleSidebar(false); // Close sidebar on navigation
  };

  // Detect click outside sidebar to auto-close
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (sidebarRef.current && !sidebarRef.current.contains(event.target)) {
        toggleSidebar(false); // Close sidebar
      }
    };

    if (isOpen) {
      document.addEventListener("mousedown", handleClickOutside);
    }

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [isOpen]);

  return (
    <div ref={sidebarRef} className={`sidebar ${isOpen ? "open" : ""}`}>
      <ul>
        <li className={active === "Dashboard" ? "active" : ""} onClick={() => handleNavigation("/dashboard", "Dashboard")}>
          <FaTachometerAlt /> Dashboard
        </li>
        <li className={active === "Deposit" ? "active" : ""} onClick={() => handleNavigation("/deposit", "Deposit")}>
          <FaMoneyBill /> Deposit
        </li>
        <li className={active === "Withdraw" ? "active" : ""} onClick={() => handleNavigation("/withdraw", "Withdraw")}>
          <FaMoneyBill /> Withdraw
        </li>
        <li className={active === "Fund Transfer" ? "active" : ""} onClick={() => handleNavigation("/fund-transfer", "Fund Transfer")}>
          <FaExchangeAlt /> Fund Transfer
        </li>
        <li className={active === "Update Details" ? "active" : ""} onClick={() => handleNavigation("/update-details", "Update Details")}>
          <FaKey /> Update Details
        </li>
        <li className={active === "Transaction History" ? "active" : ""} onClick={() => handleNavigation("/transaction-history", "Transaction History")}>
          <FaHistory /> Transaction History
        </li>
      </ul>
    </div>
  );
};

export default Sidebar;
