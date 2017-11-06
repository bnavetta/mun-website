import React from 'react';
import ReactDOM from 'react-dom';
import { AppContainer } from 'react-hot-loader';

import AttendancePage from './AttendancePage';

const render = (Component) => {
    ReactDOM.render(
        <AppContainer>
            <Component/>
        </AppContainer>,
        document.getElementById('react-attendance')
    );
};

document.addEventListener('DOMContentLoaded', () => render(AttendancePage));

if (module.hot) {
    module.hot.accept('./AttendancePage', () => render(AttendancePage));
}
