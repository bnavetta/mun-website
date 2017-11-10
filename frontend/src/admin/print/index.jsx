import React from 'react';
import ReactDOM from 'react-dom';
import { AppContainer } from 'react-hot-loader';

import PrintPage from './PrintPage';

const render = (Component) => {
    const staffEmail = document.querySelector('meta[name=staffEmail]').getAttribute('content');
    console.log(`Using staff email ${staffEmail}`);

    ReactDOM.render(
        <AppContainer>
            <Component staffEmail={staffEmail}/>
        </AppContainer>,
        document.getElementById('react-print')
    );
};

document.addEventListener('DOMContentLoaded', () => render(PrintPage));

if (module.hot) {
    module.hot.accept('./PrintPage', () => render(PrintPage));
}
