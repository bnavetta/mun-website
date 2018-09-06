import React from "react";
import ReactDOM from "react-dom";

import configureStore from "./state";
import StaffDashboard from "./StaffDashboard";

import "noty/lib/noty.css";
import "noty/lib/themes/mint.css";

import "./staff.css";

const store = configureStore();

document.addEventListener("DOMContentLoaded", () => {
    const root = document.querySelector(".staff-dashboard");
    ReactDOM.render(<StaffDashboard store={store} />, root);
});
