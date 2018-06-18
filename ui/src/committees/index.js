import React from "react";
import ReactDOM from "react-dom";

import Committees from "./Committees";
import "./committees.css";

document.addEventListener("DOMContentLoaded", () => {
    const root = document.querySelector(".committees");

    ReactDOM.render(<Committees />, root);
});
