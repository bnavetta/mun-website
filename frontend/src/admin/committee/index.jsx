// @flow

import React from 'react';
import ReactDOM from 'react-dom';

import './positions-page.scss';

function renderAddPositions(elem: HTMLElement) {
    System.import('./add-positions').then((module) => {
        const AddPositions = module.default;
        ReactDOM.render(<AddPositions />, elem);
    });
}

document.addEventListener('DOMContentLoaded', () => {
    const addPositions = document.getElementById('add-positions');
    if (addPositions) {
        renderAddPositions(addPositions);

        if (module.hot) {
            module.hot.accept('./add-positions', () => renderAddPositions(addPositions));
        }
    }
});
