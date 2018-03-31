import React from 'react';
import ReactDOM from 'react-dom';

import AdvisorDashboard from "./AdvisorDashboard";

document.addEventListener('DOMContentLoaded', () => {
    const root = document.querySelector('.advisor-dashboard');

    ReactDOM.render(<AdvisorDashboard/>, root);
});