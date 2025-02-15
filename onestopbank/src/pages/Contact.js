import React, { useState } from "react";
import emailjs from "@emailjs/browser"; // Import EmailJS
import "../styles/Contact.css";

const Contact = () => {
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    message: "",
  });

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    // EmailJS Configuration
    const serviceID = "service_xb6ttsi"; // Replace with your EmailJS service ID
    const templateID = "template_vbueyta"; // Replace with your EmailJS template ID
    const publicKey = "YR_qWrlQf_H4xt27z"; // Replace with your EmailJS public key

    const templateParams = {
      from_name: formData.name,
      from_email: formData.email,
      message: formData.message,
      to_email: "omshindedesai@gmail.com", // Default email to receive messages
    };

    emailjs.send(serviceID, templateID, templateParams, publicKey)
      .then((response) => {
        alert("✅ Message sent successfully!");
        console.log("SUCCESS!", response);
        setFormData({ name: "", email: "", message: "" }); // Clear form fields
      })
      .catch((error) => {
        alert("❌ Error sending message. Please try again.");
        console.error("FAILED...", error);
      });
  };

  return (
    <div className="contact-container">
      <h2>Contact Us</h2>
      <p>Have any questions? Reach out to us!</p>
      <form onSubmit={handleSubmit}>
        <label>Name:</label>
        <input
          type="text"
          name="name"
          placeholder="Enter your name"
          value={formData.name}
          onChange={handleChange}
          required
        />

        <label>Email:</label>
        <input
          type="email"
          name="email"
          placeholder="Enter your email"
          value={formData.email}
          onChange={handleChange}
          required
        />

        <label>Message:</label>
        <textarea
          name="message"
          placeholder="Write your message here"
          value={formData.message}
          onChange={handleChange}
          required
        ></textarea>

        <button type="submit">Send Message</button>
      </form>
    </div>
  );
};

export default Contact;
