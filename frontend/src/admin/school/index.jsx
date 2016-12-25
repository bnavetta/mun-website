// @flow

import React from 'react';
import ReactDOM from 'react-dom';

function renderSchoolDisplay(elem: HTMLElement) {
    System.import('./list').then((module) => {
        const SchoolDisplay = module.default;
        ReactDOM.render(<SchoolDisplay />, elem);
    });
}

document.addEventListener('DOMContentLoaded', () => {
    const schoolDisplay = document.getElementById('school-display');
    if (schoolDisplay) {
        renderSchoolDisplay(schoolDisplay);
        if (module.hot) {
            module.hot.accept('./list', () => renderSchoolDisplay(schoolDisplay));
        }
    }
});
