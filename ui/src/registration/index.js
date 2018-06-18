import React from "react";
import ReactDOM from "react-dom";

import RegistrationForm from "./RegistrationForm";
import "./registration.css";

document.addEventListener("DOMContentLoaded", () => {
    const root = document.querySelector(".registration-root");

    ReactDOM.render(<RegistrationForm />, root);
});
