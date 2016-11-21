import React from 'react';
import ReactDOM from 'react-dom';

import AddPositions from './add-positions';

document.addEventListener('DOMContentLoaded', () => {
	const addPositions = document.getElementById('add-positions');
	if (!!addPositions) {
		console.log('Mounting AddPositions');
		ReactDOM.render(<AddPositions/>, addPositions);
	}
});
