import React from "react";
import ReactDOM from "react-dom";

import { displayError } from "../lib/util";

import { fetchUserInfo } from "./api";
import configureStore from "./state";
import { setUserInfo } from "./state/user";
import StaffDashboard from "./StaffDashboard";

import "noty/lib/noty.css";
import "noty/lib/themes/mint.css";

import "./staff.css";

const store = configureStore();

document.addEventListener("DOMContentLoaded", () => {
    fetchUserInfo()
        .then(userInfo => {
            store.dispatch(setUserInfo(userInfo));
            const root = document.querySelector(".staff-dashboard");
            ReactDOM.render(<StaffDashboard store={store} />, root);
        })
        .catch(error => {
            console.error("Unable to load user info", error);
            displayError("Unable to load user info");
        });
});
