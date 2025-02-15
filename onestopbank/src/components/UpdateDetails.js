import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "../styles/UpdateDetails.css"; // Updated file name for uniqueness

const UpdateDetails = () => {
  const [formData, setFormData] = useState({
    accountNumber: "",
    firstName: "",
    lastName: "",
    otherName: "",
    gender: "",
    address: "",
    stateOfOrigin: "",
    phoneNumber: "",
    alternativePhoneNumber: "",
    password: "",
  });

  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleUpdate = async () => {
    setError("");
    setSuccess("");

    const token = localStorage.getItem("authToken");

    if (!formData.accountNumber) {
      setError("Account number is required!");
      return;
    }

    try {
      const response = await axios.put(
        `http://localhost:8080/api/user/update/${formData.accountNumber}`,
        formData,
        { headers: { Authorization: `Bearer ${token}` } }
      );

      if (response.status === 200) {
        setSuccess("Details updated successfully!");
        setTimeout(() => navigate("/dashboard"), 2000);
      }
    } catch (error) {
      console.error("Update Error:", error);
      setError(error.response?.data?.message || "Update failed! Please try again.");
    }
  };

  return (
    <div className="unique-update-container">
      <h2 className="unique-update-title">Update Details</h2>
      {error && <p className="unique-error-message">{error}</p>}
      {success && <p className="unique-success-message">{success}</p>}
      <div className="unique-input-group">
        {Object.keys(formData).map((key) => (
          <input
            key={key}
            type={key === "password" ? "password" : "text"}
            name={key}
            placeholder={`Enter ${key.replace(/([A-Z])/g, " $1")}`}
            value={formData[key]}
            onChange={handleChange}
            required
            className="unique-input-field"
          />
        ))}
      </div>
      <button className="unique-update-button" onClick={handleUpdate}>
        Update
      </button>
    </div>
  );
};

export default UpdateDetails;
