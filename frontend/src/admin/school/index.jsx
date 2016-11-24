import React from 'react';
import ReactDOM from 'react-dom';

import SchoolDisplay from './list';

document.addEventListener('DOMContentLoaded', () => {
	const schoolDisplay = document.getElementById('school-display');
	if (!!schoolDisplay) {
		ReactDOM.render(<SchoolDisplay/>, schoolDisplay);
	}
});
