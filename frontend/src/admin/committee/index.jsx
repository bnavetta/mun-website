import React from 'react';
import ReactDOM from 'react-dom';

import './positions-page.scss';

function renderAddPositions(elem) {
	System.import('./add-positions').then(module => {
		const AddPositions = module.default;
		ReactDOM.render(<AddPositions/>, elem);
	});
}

document.addEventListener('DOMContentLoaded', () => {
	const addPositions = document.getElementById('add-positions');
	if (addPositions) {
		renderAddPositions(addPositions);

		module.hot.accept('./add-positions', () => renderAddPositions(addPositions));
	}
});
